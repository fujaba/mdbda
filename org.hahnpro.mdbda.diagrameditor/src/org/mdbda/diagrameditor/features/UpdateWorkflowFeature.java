package org.mdbda.diagrameditor.features;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.BoxRelativeAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.mdbda.diagrameditor.features.AbstactMDBDAAddFeature;
import org.mdbda.diagrameditor.utils.DiagramUtils;
import org.mdbda.diagrameditor.utils.LiveStatusShapeHelper;
import org.mdbda.diagrameditor.utils.NameShapeHelper;
import org.mdbda.model.MDBDADiagram;
import org.mdbda.model.RemoteWorkflow;
import org.mdbda.model.Resource;
import org.mdbda.model.Task;
import org.mdbda.model.Workflow;

public class UpdateWorkflowFeature extends AbstractUpdateFeature {

	public UpdateWorkflowFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getPictogramElement());
		return bo instanceof  Workflow || bo instanceof  RemoteWorkflow;
	}

	private MDBDADiagram getRemoteMDBDADiagram(String name){
		List<Diagram> diaList = DiagramUtils.getDiagrams(getDiagram()).stream().filter(rd -> rd.getName().equals(name)).collect(Collectors.toList()) ;
		
//		if(diaList.size() > 1 ){
//			throw new RuntimeException("Ups remote diagram name (" + name + ") is ambiguous");
//		}else 
			if(diaList.size() == 0 ){
			throw new RuntimeException("Ups remote diagram with name '" + name + "' was not found " );
		}else{
			return (MDBDADiagram) getBusinessObjectForPictogramElement( diaList.get(0) );
		}
	}
	
	@Override
	public IReason updateNeeded(IUpdateContext context) {
		Resource bo = (Resource) getBusinessObjectForPictogramElement( context.getPictogramElement() );
		ContainerShape rootContainerShape = (ContainerShape) context.getPictogramElement();
		// AbstactMDBDAAddFeature.getRootContainerShapeForResourceElement(bo, getFeatureProvider());
		
		NameShapeHelper nameHelper = new NameShapeHelper(bo,rootContainerShape,getFeatureProvider());
		if( nameHelper.hasChanged()){
			return Reason.createTrueReason("The name is out of date");
		}
		
		if(bo instanceof RemoteWorkflow){
			//check remote in and outputs
			RemoteWorkflow rwf = (RemoteWorkflow) bo;
			
			//size == 1
			MDBDADiagram remoteDiagram = getRemoteMDBDADiagram(rwf.getName());		
			EList<Resource> remoteResources = remoteDiagram.getResources();
			if(remoteResources.size() != rootContainerShape.getAnchors().size()){
				return Reason.createTrueReason();
			}
			List<Resource> remoteInputs = filterInputs(remoteResources);
			List<Resource> remoteOutputs = filterOutputs(remoteResources);
			
			for(Anchor a : rootContainerShape.getAnchors()){
				if(a instanceof BoxRelativeAnchor){
					BoxRelativeAnchor ba = (BoxRelativeAnchor) a;
					Resource anchorResource = (Resource) getBusinessObjectForPictogramElement(ba);
					if(ba.isUseAnchorLocationAsConnectionEndpoint()){
						//input
						if( ! remoteInputs.contains(anchorResource) ) {
							return Reason.createTrueReason();
						}
					}else{
						//output
						if( ! remoteOutputs.contains(anchorResource) ) {
							return Reason.createTrueReason();
						}
					}
				} 				
			}
		}			
		
		
		return Reason.createFalseReason();
	}

	@Override
	public boolean update(IUpdateContext context) {
		Resource bo = (Resource) getBusinessObjectForPictogramElement( context.getPictogramElement() );
		ContainerShape rootContainerShape = (ContainerShape) context.getPictogramElement();//AbstactMDBDAAddFeature.getRootContainerShapeForResourceElement(bo, getFeatureProvider());
		
		boolean somethingchanged = false;
		NameShapeHelper nameHelper = new NameShapeHelper(bo,rootContainerShape,getFeatureProvider());
		if( nameHelper.hasChanged()){
			somethingchanged = true;
			nameHelper.update();			
		}
		if(bo instanceof RemoteWorkflow){

			RemoteWorkflow rwf = (RemoteWorkflow) bo;
			MDBDADiagram remoteDiagram = getRemoteMDBDADiagram(rwf.getName());

			EList<Resource> remoteResources = remoteDiagram.getResources();
			List<Resource> remoteInputs     = filterInputs(remoteResources);
			List<Resource> remoteOutputs    = filterOutputs(remoteResources);
			
			HashSet<Anchor> anchorsToRemove = new HashSet<Anchor>();
			for(Anchor a : rootContainerShape.getAnchors()){
				if(a instanceof BoxRelativeAnchor){
					BoxRelativeAnchor ba = (BoxRelativeAnchor) a;
					Resource anchorResource = (Resource) getBusinessObjectForPictogramElement(ba);
					if(ba.isUseAnchorLocationAsConnectionEndpoint()){
						//input
						if( ! remoteInputs.contains(anchorResource) ) {
							anchorsToRemove.add(ba);
							//rootContainerShape.getAnchors().remove(ba);
						}else{
							//remove resources that are okay
							remoteInputs.remove(anchorResource);
						}						
					}else{
						//output
						if( ! remoteOutputs.contains(anchorResource) ) {
							anchorsToRemove.add(ba);
							//rootContainerShape.getAnchors().remove(ba);
						}else{
							//remove resources that are okay
							remoteOutputs.remove(anchorResource);
						}
					}
				} 				
			}
			rootContainerShape.getAnchors().removeAll(anchorsToRemove);
			
			//add new anchors
			for(Resource rr : remoteInputs){
				AbstactMDBDAAddFeature.addInputAnchor(rr, rootContainerShape, 1, getDiagram(), getFeatureProvider());
			}
			for(Resource rr : remoteOutputs){
				AbstactMDBDAAddFeature.addOutputAnchor(rr, rootContainerShape, 1, getDiagram(), getFeatureProvider());
			}
			//relayout anchors
			
			remoteInputs  = filterInputs(remoteResources);
			remoteOutputs = filterOutputs(remoteResources);

			int maxIn = remoteInputs.size();
			int maxOut = remoteOutputs.size();
			double faktorIn = 1.0 / (maxIn + 1.0);
			double faktorOut = 1.0 / (maxOut + 1.0);
			
			int countIn = 1;
			int countOut = 1;
			
			for(Anchor a : rootContainerShape.getAnchors()){
				if(a instanceof BoxRelativeAnchor){
					BoxRelativeAnchor ba = (BoxRelativeAnchor) a;
					if(ba.isUseAnchorLocationAsConnectionEndpoint()){
						//input
						ba.setRelativeHeight(faktorIn * countIn);
						countIn++;
					}else{
						//output

						ba.setRelativeHeight(faktorOut * countOut);
						countOut++;
					}
				}
			}
			
			
		}
		return somethingchanged;
	}

	private List<Resource> filterOutputs(EList<Resource> remoteResources) {
		return remoteResources.stream().filter(rr -> rr.getOutputResources().size() == 0 && rr.getInputResources().size() != 0).collect(Collectors.toList());
	}

	private List<Resource> filterInputs(EList<Resource> remoteResources) {
		return remoteResources.stream().filter(rr -> rr.getInputResources().size() == 0 && rr.getOutputResources().size() != 0).collect(Collectors.toList());
	}

}
