package org.mdbda.diagrameditor.pictogramelements;

import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.BoxRelativeAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.mdbda.model.Resource;
import org.mdbda.model.Workflow;
import org.mdbda.diagrameditor.features.AddWorkflowFeature;
import org.mdbda.diagrameditor.utils.DiagramUtils;

public class WorkflowShape extends AbstractMDBDAShape {
	private WorkflowShape(){
		
	}
	public static ContainerShape getWorkflowShape(IAddContext context, AddWorkflowFeature feature) {
		//super(context, feature);

		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		IGaService gaService = Graphiti.getGaService();
		
		ContainerShape workflowShape = peCreateService.createContainerShape(context.getTargetContainer(), true);

		
		Workflow workflow = null;
		Diagram refDiagram = null;
		boolean isRootWorkflow = false;
		if(context.getNewObject() instanceof Workflow){
			workflow = (Workflow) context.getNewObject();
			refDiagram = feature.getDiagram();
			isRootWorkflow = true;
		}else if(context.getNewObject() instanceof Diagram){
			refDiagram = (Diagram) context.getNewObject() ;
			workflow = DiagramUtils.getMDBDADiagram(refDiagram).getRootWorkflow();

			isRootWorkflow = false;
		}
		
		
//		if (!isRootWorkflow) {
//			// ask new Diagram or open Diagram
//
//			MessageDialog dialog = new MessageDialog(new Shell(
//					Display.getDefault()),
//					"Create new workflow diagram or open diagram", null,
//					"Open workflow MDBDA diagram or create a new one",
//					MessageDialog.QUESTION, new String[] { "new", "open" }, 0);
//			int res = dialog.open();
//
//			if (res == 0) {// new
//				refDiagram = DiagramUtils.newDiagramDialog();
//				
//			} else {// open
//				refDiagram = DiagramUtils.openDiagramDialog(getDiagram());
//			}
//
//		}else{
//		}

		int width = isRootWorkflow ? 600 : 60;
		int height = isRootWorkflow ? 500 : 50;


//		ContainerShape containerShape = peCreateService.createContainerShape(targetDiagram, true);

		RoundedRectangle roundedRectangle;
		{
			roundedRectangle = gaService.createRoundedRectangle(workflowShape,
					5, 5);
			roundedRectangle
					.setBackground( feature.manageColor(isRootWorkflow ? ROOTWORKFLOW_BACKGROUND
							: WORKFLOW_BACKGROUND));
			roundedRectangle
					.setForeground(feature.manageColor(isRootWorkflow ? ROOTWORKFLOW_FOREGROUND
							: WORKFLOW_FOREGROUND));
			gaService.setLocationAndSize(roundedRectangle, context.getX(),
					context.getY(), width, height);

			// if added Class has no resource we add it to the resource
			// of the diagram
			// in a real scenario the business model would have its own resource
			if (workflow.eResource() == null) { // TODO: ??
				feature.getDiagram().eResource().getContents().add(workflow);
			}

			feature.link(workflowShape, workflow);
			//link(getDiagram(), refDiagram);
		} 

		// SHAPE WITH LINE
		{
			// create shape for line
			Shape shape = peCreateService.createShape(workflowShape, false);

			// create and set graphics algorithm
			Polyline polyline = gaService.createPolyline(shape, new int[] { 0,
					20, width, 20 });
			polyline.setForeground(feature.manageColor(isRootWorkflow ? ROOTWORKFLOW_FOREGROUND
					: WORKFLOW_FOREGROUND));
			polyline.setLineWidth(4);
		}

		// SHAPE WITH TEXT
		{
			// create shape for text
			Shape shape = peCreateService.createShape(workflowShape, false);

			// create and set text graphics algorithm
			Text text = gaService.createText(shape, workflow.getName()
					+ " : Workflow");
			text.setForeground(feature.manageColor(WORKFLOW_TEXT_FOREGROUND));
			text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
			// vertical alignment has as default value "center"
			text.setFont(gaService.manageDefaultFont(feature.getDiagram(), false, true));
			gaService.setLocationAndSize(text, 0, 0, width, 20);

			// create link and wire it
			feature.link(shape, workflow);
		}

		
		if(!isRootWorkflow){
	        //Anchor
	        {
	        	EList<Resource> ioDataResources = workflow.getOutputDataResources();
	        	
	        	double factor = 1.0d / (ioDataResources.size() + 1);
	        	double pos = factor;
	        	
	        	for(int i = 0 ; i<ioDataResources.size() ; i++){

		        	
		        	BoxRelativeAnchor boxAnchor = peCreateService.createBoxRelativeAnchor(workflowShape);
		        	boxAnchor.setUseAnchorLocationAsConnectionEndpoint(false);
		        	boxAnchor.setRelativeWidth(1.0);
		        	
		        	boxAnchor.setRelativeHeight(pos);
		        	pos+=factor;
		        	
		        	boxAnchor.setReferencedGraphicsAlgorithm(roundedRectangle);
		        	
		        	feature.link(boxAnchor,workflow);
		        	
		        	Polygon poly = gaService.createPolygon(boxAnchor, OUTPUTPOLYGON);
		        	poly.setBackground(feature.manageColor(RESOURCE_BACKGROUND));
		        	poly.setForeground(feature.manageColor(RESOURCE_FOREGROUND));
		        	poly.setLineWidth(2);
		        	gaService.setLocationAndSize(poly, -12, -6, 12, 12);
	        		
	        	}
	        }
	        {
	        	EList<Resource> ioDataResources = workflow.getOutputDataResources();
	        	double factor = 1.0d / (ioDataResources.size() + 1);
	        	double pos = factor;
	        	
		        for(int i = 0 ; i<ioDataResources.size() ; i++){
	
		        	BoxRelativeAnchor boxAnchor = peCreateService.createBoxRelativeAnchor(workflowShape);
		        	boxAnchor.setUseAnchorLocationAsConnectionEndpoint(true);
		        	
		        	boxAnchor.setRelativeWidth(0.0);
		        	
		        	boxAnchor.setRelativeHeight(pos);
		        	pos+=factor;
		        	
		        	boxAnchor.setReferencedGraphicsAlgorithm(roundedRectangle);
		        	
		        	feature.link(boxAnchor,workflow);
		        	
		        	Polygon poly = gaService.createPolygon(boxAnchor, INPUTPOLYGON);
		        	poly.setBackground(feature.manageColor(RESOURCE_BACKGROUND));
		        	poly.setForeground(feature.manageColor(RESOURCE_FOREGROUND));
		        	poly.setLineWidth(2);
		        	gaService.setLocationAndSize(poly, -12, -6, 12, 12);
	        	}
	        }
		}
		return workflowShape;
	}
	
	

}
