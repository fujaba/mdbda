package org.mdbda.diagrameditor.features.resources;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.BoxRelativeAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.mdbda.model.MDBDADiagram;
import org.mdbda.model.Resource;
import org.mdbda.model.Workflow;
import org.mdbda.diagrameditor.features.AbstactMDBDAAddFeature;
import org.mdbda.diagrameditor.pictogramelements.AbstractMDBDAShape;


public abstract class AddResourceFeature extends AbstactMDBDAAddFeature  implements
		IAddFeature {

	public AddResourceFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		if(context.getNewObject() instanceof Resource){
			if(getBusinessObjectForPictogramElement(context.getTargetContainer()) instanceof Workflow){
				return true;
			}
			
			if(getBusinessObjectForPictogramElement(context.getTargetContainer()) instanceof MDBDADiagram){
				return true;
			}
		}
		
		return  false;
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
			gaService.setLocationAndSize(invisibleRectangle, context.getX(), context.getY(), width + 2 * AbstractMDBDAShape.INVISIBLE_RECT_SIDE, height);
			
		
			
			roundedRectangle = gaService.createRoundedRectangle(invisibleRectangle, 10, 10);
			roundedRectangle.setBackground(manageColor(AbstractMDBDAShape.RESOURCE_BACKGROUND));
			roundedRectangle.setForeground(manageColor(AbstractMDBDAShape.RESOURCE_FOREGROUND));
			roundedRectangle.setLineWidth(2);
            gaService.setLocationAndSize(roundedRectangle,
            		AbstractMDBDAShape.INVISIBLE_RECT_SIDE, 0, width, height);
        
            
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
			String txtStr = typeName.trim().replace(' ', '\n');
			Text text = gaService.createText(shape, txtStr );
			text.setForeground(manageColor(AbstractMDBDAShape.RESOURCE_TEXT_FOREGROUND));
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
        	
        	Polygon poly = gaService.createPolygon(boxAnchor, AbstractMDBDAShape.OUTPUTPOLYGON);
        	poly.setBackground(manageColor(AbstractMDBDAShape.RESOURCE_BACKGROUND));
        	poly.setForeground(manageColor(AbstractMDBDAShape.RESOURCE_FOREGROUND));
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
        	
        	Polygon poly = gaService.createPolygon(boxAnchor, AbstractMDBDAShape.INPUTPOLYGON);
        	poly.setBackground(manageColor(AbstractMDBDAShape.RESOURCE_BACKGROUND));
        	poly.setForeground(manageColor(AbstractMDBDAShape.RESOURCE_FOREGROUND));
        	poly.setLineWidth(2);
        	gaService.setLocationAndSize(poly, -12, -6, 12, 12);
        }
        

    	layoutPictogramElement(containerShape);
		return containerShape;
	}
}