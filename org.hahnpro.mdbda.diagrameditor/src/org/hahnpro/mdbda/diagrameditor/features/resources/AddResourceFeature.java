package org.hahnpro.mdbda.diagrameditor.features.resources;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.BoxRelativeAnchor;
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
import org.hahnpro.mdbda.model.resources.Resource;
import org.hahnpro.mdbda.model.workflow.Workflow;


public abstract class AddResourceFeature extends AbstractAddFeature implements
		IAddFeature {
	private static final int[] outputPolygon = new int[] {1,1,  10,1,  20,5,  10,10,  1,10, 1,1};
	private static final int[] inputPolygon = new int[] {1,1,  20,1,  20,10,  1,10,  10,5,  1,1};
    
	public static final int INVISIBLE_RECT_SIDE = 10;

	private static final IColorConstant RESOURCE_TEXT_FOREGROUND =
            IColorConstant.BLACK;
     
        private static final IColorConstant RESOURCE_FOREGROUND = IColorConstant.BLUE; 
//            new ColorConstant(148, 131, 167);

        private static final IColorConstant  RESOURCE_BACKGROUND = IColorConstant.LIGHT_BLUE;
//            new ColorConstant(237, 218, 247);

	protected String resourceTypeName = "Resource";
	
	public AddResourceFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		return context.getNewObject() instanceof Resource && getBusinessObjectForPictogramElement(context.getTargetContainer()) instanceof Workflow;
	}
	
	@Override
	public PictogramElement add(IAddContext context) {

		ContainerShape targetShape = context.getTargetContainer();
		Resource resource = (Resource) context.getNewObject();
		
		int width = 80;
        int height = 50; 
		
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		IGaService gaService = Graphiti.getGaService();

		ContainerShape containerShape = peCreateService.createContainerShape(targetShape, true);
		RoundedRectangle roundedRectangle;
		
		{
			Rectangle invisibleRectangle = gaService.createInvisibleRectangle(containerShape);
			gaService.setLocationAndSize(invisibleRectangle, context.getX(), context.getY(), width + 2 * INVISIBLE_RECT_SIDE, height);
			
		
			
			roundedRectangle = gaService.createRoundedRectangle(invisibleRectangle, 10, 10);
			roundedRectangle.setBackground(manageColor(RESOURCE_BACKGROUND));
			roundedRectangle.setForeground(manageColor(RESOURCE_FOREGROUND));
			roundedRectangle.setLineWidth(2);
            gaService.setLocationAndSize(roundedRectangle,
            		INVISIBLE_RECT_SIDE, 0, width, height);
        
            
            // if added Class has no resource we add it to the resource 
            // of the diagram
            // in a real scenario the business model would have its own resource
            if (resource.eResource() == null) {
                     getDiagram().eResource().getContents().add(resource);
            }
            // create link and wire it
            link(containerShape, resource);
		
		}
		
		
		
		
		
		 // SHAPE WITH TEXT
        {
        	
			Shape shape = peCreateService.createShape(containerShape, false);
			Text text = gaService.createText(shape, resourceTypeName );
			text.setForeground(manageColor(RESOURCE_TEXT_FOREGROUND));
			text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
			text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
			gaService.setLocationAndSize(text, 0, 0, context.getWidth(), context.getHeight());
	
			link(containerShape, resource);
        }
        
        
        //Anchor
        {
        	BoxRelativeAnchor boxAnchor = peCreateService.createBoxRelativeAnchor(containerShape);
        	boxAnchor.setUseAnchorLocationAsConnectionEndpoint(false);
        	boxAnchor.setRelativeWidth(1.0);
        	boxAnchor.setRelativeHeight(0.5);
        	boxAnchor.setReferencedGraphicsAlgorithm(roundedRectangle);
        	
        	link(boxAnchor,resource);
        	
        	Polygon poly = gaService.createPolygon(boxAnchor, outputPolygon);
        	poly.setBackground(manageColor(RESOURCE_BACKGROUND));
        	poly.setForeground(manageColor(RESOURCE_FOREGROUND));
        	poly.setLineWidth(2);
        	gaService.setLocationAndSize(poly, -12, -6, 12, 12);
        }
        {
        	BoxRelativeAnchor boxAnchor = peCreateService.createBoxRelativeAnchor(containerShape);
        	boxAnchor.setUseAnchorLocationAsConnectionEndpoint(true);
        	
        	boxAnchor.setRelativeWidth(0.0);
        	boxAnchor.setRelativeHeight(0.5);
        	boxAnchor.setReferencedGraphicsAlgorithm(roundedRectangle);
        	
        	link(boxAnchor,resource);
        	
        	Polygon poly = gaService.createPolygon(boxAnchor, inputPolygon);
        	poly.setBackground(manageColor(RESOURCE_BACKGROUND));
        	poly.setForeground(manageColor(RESOURCE_FOREGROUND));
        	poly.setLineWidth(2);
        	gaService.setLocationAndSize(poly, -12, -6, 12, 12);
        }
        

    	layoutPictogramElement(containerShape);
		return containerShape;
	}
}
