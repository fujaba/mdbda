package org.hahnpro.mdbda.diagrameditor.features.pattern;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
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
import org.hahnpro.mdbda.model.pattern.Pattern;
import org.hahnpro.mdbda.model.workflow.Workflow;


public abstract class AddPatternFeature extends AbstractAddFeature implements
		IAddFeature {
    private static final IColorConstant PATTERN_TEXT_FOREGROUND =
            IColorConstant.BLACK;
     
        private static final IColorConstant PATTERN_FOREGROUND =
            new ColorConstant(148, 131, 167);

        private static final IColorConstant  PATTERN_BACKGROUND =
            new ColorConstant(237, 218, 247);

	protected String patternName = "Pattern";
	
	public AddPatternFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		return context.getNewObject() instanceof Pattern && getBusinessObjectForPictogramElement(context.getTargetContainer()) instanceof Workflow;
	}
	
	@Override
	public PictogramElement add(IAddContext context) {

		ContainerShape targetShape = context.getTargetContainer();
		Pattern pattern = (Pattern) context.getNewObject();
		
		int width = 80;
        int height = 50; 
		
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		IGaService gaService = Graphiti.getGaService();

		ContainerShape containerShape = peCreateService.createContainerShape(targetShape, true);
		RoundedRectangle roundedRectangle;
		
		{
			
			roundedRectangle = gaService.createRoundedRectangle(containerShape, 10, 10);
			roundedRectangle.setBackground(manageColor(PATTERN_BACKGROUND));
			roundedRectangle.setForeground(manageColor(PATTERN_FOREGROUND));
			roundedRectangle.setLineWidth(2);
            gaService.setLocationAndSize(roundedRectangle,
                context.getX(), context.getY(), width, height);
        
            
            // if added Class has no resource we add it to the resource 
            // of the diagram
            // in a real scenario the business model would have its own resource
            if (pattern.eResource() == null) {
                     getDiagram().eResource().getContents().add(pattern);
            }
            // create link and wire it
            link(containerShape, pattern);
		
		}
		
		
		
		
		
		 // SHAPE WITH TEXT
        {
        	
			Shape shape = peCreateService.createShape(containerShape, false);
			Text text = gaService.createText(shape, patternName );
			text.setForeground(manageColor(PATTERN_TEXT_FOREGROUND));
			text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
			text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
			gaService.setLocationAndSize(text, 0, 0, context.getWidth(), context.getHeight());
						
	
			link(containerShape, pattern);
        }
		return containerShape;
	}
}
