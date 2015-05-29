package org.mdbda.diagrameditor.features;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.graphiti.examples.common.FileService;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.PictogramsFactory;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.features.AbstractDrillDownFeature;
import org.mdbda.diagrameditor.utils.DiagramUtils;
import org.mdbda.model.DataObject;
import org.mdbda.model.ModelFactory;
import org.mdbda.model.RemoteWorkflow;
import org.mdbda.model.Task;

public class DataTransformationDrillDownFeature extends AbstractDrillDownFeature {

	public DataTransformationDrillDownFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public String getName() {
		return "Open remote datatransformation diagram";
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			Object bo = getBusinessObjectForPictogramElement(pes[0]);
			if (bo instanceof Task ) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void execute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			Task task = (Task) getBusinessObjectForPictogramElement(pes[0]);
			
			if(task.getDataTransformationDiagramURI() == null || task.getDataTransformationDiagramURI() == ""){
				//create URI and return true
				//DiagramUtils.newDiagramDialog();
				String diagramname = task.getName();
				Diagram diagram = Graphiti.getPeCreateService().createDiagram( DiagramUtils.DATA_TRANSFORMATION_DIAGRAM_TYPEID, diagramname, true);
			

				Resource diagramResource = diagram.eResource();
				
				if(diagramResource == null){
					//ResourceSet resourceSet = task.eResource().getResourceSet();
					URI modelRootUri = task.eResource().getURI();
					URI newDiagramUri = modelRootUri.trimFileExtension().appendFileExtension(task.getName()).appendFileExtension("diagram");
					//eResource = resourceSet.createResource(newDiagramUri);
					FileService.createEmfFileForDiagram(newDiagramUri, diagram);
					ResourceSet resourceSet = diagram.eResource().getResourceSet();

					diagramResource = resourceSet.getResource(newDiagramUri, true);
					
					diagramResource.setTrackingModification(true);
					diagramResource.getContents().add(diagram);
					
					diagram.setLink(PictogramsFactory.eINSTANCE.createPictogramLink());
					diagram.getLink().getBusinessObjects().add(task);

					task.eResource().setTrackingModification(true);
					
					for( org.mdbda.model.Resource inRes : task.getInputResources()){
						DataObject dao = ModelFactory.eINSTANCE.createDataObject();
						dao.setLinkedInputResource(inRes);
						dao.setName(inRes.getName().replaceAll(" ", ""));
						dao.setContainerTask(task);
//						task.getDataObjects().add(dao);
					}
					for( org.mdbda.model.Resource outRes : task.getOutputResources()){
						DataObject dao = ModelFactory.eINSTANCE.createDataObject();
						dao.setLinkedOutputResource(outRes);
						dao.setName(outRes.getName().replaceAll(" ", ""));
						dao.setContainerTask(task);						
//						task.getDataObjects().add(dao);
					}
				}
				try {
					task.setDataTransformationDiagramURI(diagramResource.getURI().toPlatformString(true));
					task.eResource().save(Collections.<Resource, Map<?, ?>> emptyMap());
					diagramResource.save(Collections.<Resource, Map<?, ?>> emptyMap());
					openDiagramEditor(diagram);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}else{
			
				Collection<Diagram> diagrams = DiagramUtils
						.getDiagrams(getDiagram(), DiagramUtils.DATA_TRANSFORMATION_DIAGRAM_TYPEID);
	
				for (Diagram dia : diagrams) {
					if (dia.eResource().getURI().toPlatformString(true)
							.equals(task.getDataTransformationDiagramURI())) {
						openDiagramEditor(dia);
					}
				}
			}
		}
	}

	@Override
	protected Collection<Diagram> getDiagrams() {
		return DiagramUtils.getDiagrams(getDiagram(), DiagramUtils.DATA_TRANSFORMATION_DIAGRAM_TYPEID);
	}

}
