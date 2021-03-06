package org.mdbda.diagrameditor.features;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.BoxRelativeAnchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.mdbda.diagrameditor.features.AbstactIOMDBDAAddFeature;
import org.mdbda.diagrameditor.utils.DiagramUtils;
import org.mdbda.diagrameditor.utils.NameShapeHelper;
import org.mdbda.model.MDBDAModelRoot;
import org.mdbda.model.RemoteWorkflow;
import org.mdbda.model.Resource;
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

	private MDBDAModelRoot getRemoteMDBDAModelRoot(String name){
		List<Diagram> diaList = DiagramUtils.getDiagrams(
				getDiagram()).stream()
				.filter(
						rd -> rd.getName().equals(name))
						.collect(Collectors.toList()) ;
		
//		if(diaList.size() > 1 ){
//			throw new RuntimeException("Ups remote diagram name (" + name + ") is ambiguous");
//		}else 
		if(diaList.size() == 0 ){
			throw new RuntimeException("Ups remote diagram with name '" + name + "' was not found " );
		}else if(diaList.size() == 1){
			return (MDBDAModelRoot) getBusinessObjectForPictogramElement( diaList.get(0) );
		}else{
			// there is often a copy at the bin folder
			if(diaList.size() == 2){
				MDBDAModelRoot mr0 = (MDBDAModelRoot) getBusinessObjectForPictogramElement( diaList.get(0) );
				MDBDAModelRoot mr1 = (MDBDAModelRoot) getBusinessObjectForPictogramElement( diaList.get(1) );

				String uri0 = mr0.eResource().getURI().toPlatformString(false);
				String uri1 = mr1.eResource().getURI().toPlatformString(false);
				
				for(int i =  1; i < uri0.length() && i < uri1.length() ; i++){//gleichen teile von hinten abschneiden
					if(! uri0.endsWith(uri1.substring(uri1.length() - i , uri1.length() ))){
						String substr = uri0.substring(0,uri0.length() - i + 1);
						if(substr.endsWith("/bin") || substr.endsWith("/target/classes")) {//uri0 is 
							return (MDBDAModelRoot) getBusinessObjectForPictogramElement( diaList.get(1) );
						}

						substr = uri1.substring(0,uri1.length() - i +1);
						if(substr.endsWith("/bin") || substr.endsWith("/target/classes")) {//uri1 is 
							return (MDBDAModelRoot) getBusinessObjectForPictogramElement( diaList.get(0) );
						}
					}
				}
			}
						
			
			throw new RuntimeException("Ups remote diagram with name '" + name + "' was found multiple: " + diaList );
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
			MDBDAModelRoot remoteModelRoot = getRemoteMDBDAModelRoot(rwf.getName());		
			EList<Resource> remoteResources = remoteModelRoot.getResources();
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
			MDBDAModelRoot remoteModelRoot = getRemoteMDBDAModelRoot(rwf.getName());

			EList<Resource> remoteResources = remoteModelRoot.getResources();
			List<Resource> remoteInputs     = filterInputs(remoteResources);
			List<Resource> remoteOutputs    = filterOutputs(remoteResources);
			
			HashSet<Anchor> anchorsToRemove = new HashSet<Anchor>();
			for(Anchor a : rootContainerShape.getAnchors()){
				if(a instanceof BoxRelativeAnchor){
					BoxRelativeAnchor ba = (BoxRelativeAnchor) a;
					EObject anchorResource = (EObject) getBusinessObjectForPictogramElement(ba);
					if(ba.isUseAnchorLocationAsConnectionEndpoint()){
						//input
						if( ! remoteInputs.contains(anchorResource) ) {
							anchorsToRemove.add(ba);
							somethingchanged = true;
							//rootContainerShape.getAnchors().remove(ba);
						}else{
							//remove resources that are okay
							remoteInputs.remove(anchorResource);
						}						
					}else{
						//output
						if( ! remoteOutputs.contains(anchorResource) ) {
							anchorsToRemove.add(ba);
							somethingchanged = true;
							//rootContainerShape.getAnchors().remove(ba);
						}else{
							//remove resources that are okay
							remoteOutputs.remove(anchorResource);
						}
					}
				} 				
			}

			for(Anchor atr : anchorsToRemove){
				Iterator<Connection> iter = atr.getIncomingConnections().iterator();
				while(iter.hasNext() ){
					Connection ctr = iter.next();
					iter.remove();
					Graphiti.getPeService().deletePictogramElement(ctr);	
				}
				iter = atr.getOutgoingConnections().iterator();
				while(iter.hasNext() ){
					Connection ctr = iter.next();
					iter.remove();
					Graphiti.getPeService().deletePictogramElement(ctr);	
				}
				
				Graphiti.getPeService().deletePictogramElement(atr);	
		//		rootContainerShape.getAnchors().remove(atr);
			}
			
			
			//add new anchors
			for(Resource rr : remoteInputs){				
				AbstactIOMDBDAAddFeature.addInputAnchor(rr, rootContainerShape, 1, getDiagram(), getFeatureProvider());
			}
			for(Resource rr : remoteOutputs){
				AbstactIOMDBDAAddFeature.addOutputAnchor(rr, rootContainerShape, 1, getDiagram(), getFeatureProvider());
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
		return remoteResources.stream().filter(
				rr -> rr.getOutputResources().size() == 0 &&
				rr.getInputResources().size() != 0).collect(Collectors.toList());
	}

	private List<Resource> filterInputs(EList<Resource> remoteResources) {
		return remoteResources.stream().filter(
				rr -> rr.getInputResources().size() == 0 && 
				rr.getOutputResources().size() != 0).collect(Collectors.toList());
	}

}
