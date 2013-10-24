package org.hahnpro.mdbda.diagrameditor.features;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;
import org.hahnpro.mdbda.model.workflow.Workflow;


public class AddWorkflowFeature extends AbstractAddFeature implements
		IAddFeature {
    private static final IColorConstant WORKFLOW_TEXT_FOREGROUND =
            IColorConstant.BLACK;
     
        private static final IColorConstant WORKFLOW_FOREGROUND =
            new ColorConstant(98, 131, 167);

        private static final IColorConstant  WORKFLOW_BACKGROUND =
            new ColorConstant(187, 218, 247);
        
        
	public AddWorkflowFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		return context.getNewObject() instanceof Workflow &&  context.getTargetContainer() instanceof Diagram;
	}

	@Override
	public PictogramElement add(IAddContext context) {

		Diagram targetDiagram = (Diagram) context.getTargetContainer();
		Workflow workflow = (Workflow) context.getNewObject();
		
		int width = 600;
        int height = 500; 
		
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		IGaService gaService = Graphiti.getGaService();

		ContainerShape containerShape = peCreateService.createContainerShape(targetDiagram, true);
				
		
		RoundedRectangle roundedRectangle;
		{
			roundedRectangle	= gaService.createRoundedRectangle(containerShape, 5, 5);
			roundedRectangle.setBackground(manageColor(WORKFLOW_BACKGROUND));
			roundedRectangle.setForeground(manageColor(WORKFLOW_FOREGROUND));		
			gaService.setLocationAndSize(roundedRectangle, context.getX(), context.getY(), width, height);
			
			
			// if added Class has no resource we add it to the resource 
	        // of the diagram
	        // in a real scenario the business model would have its own resource
	        if (workflow.eResource() == null) { //TODO: ??
	                 getDiagram().eResource().getContents().add(workflow);
	        }
	        
	        link(containerShape, workflow);
		}
        
		
		// SHAPE WITH LINE
        {
            // create shape for line
            Shape shape = peCreateService.createShape(containerShape, false);
 
            // create and set graphics algorithm
            Polyline polyline =
                gaService.createPolyline(shape, new int[] { 0, 20, width, 20 });
            polyline.setForeground(manageColor(WORKFLOW_FOREGROUND));
            polyline.setLineWidth(4);
        }
		
		
        // SHAPE WITH TEXT
        {
            // create shape for text
            Shape shape = peCreateService.createShape(containerShape, false);
 
            // create and set text graphics algorithm
            Text text = gaService.createText(shape, workflow.getName() + " : Workflow");
            text.setForeground(manageColor(WORKFLOW_TEXT_FOREGROUND));
            text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER ); 
            // vertical alignment has as default value "center"
            text.setFont(gaService.manageDefaultFont(getDiagram(), false, true));
            gaService.setLocationAndSize(text, 0, 0, width, 20);
 
            // create link and wire it
            link(shape, workflow);
        }
        
		//peCreateService.createChopboxAnchor(containerShape);
 
		

		return containerShape;
	}
}
