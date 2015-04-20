package org.mdbda.diagrameditor.datatransformation.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.mdbda.diagrameditor.datatransformation.helper.DataObjectNameAndTypeHelper;
import org.mdbda.diagrameditor.features.AbstractSimpleMDBDAAddFeature;
import org.mdbda.model.DataObject;

public class AddDataObject extends AbstractSimpleMDBDAAddFeature {

	public static final String SHAPE_KEY = "shape-id";
	protected static final String ROUNDED_RECTANGLE_ID = "RoundedRecangle";
	
	public AddDataObject(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		return context.getNewObject() instanceof DataObject;
	}

	@Override
	public PictogramElement add(IAddContext context) {
		ContainerShape targetShape = context.getTargetContainer();
		DataObject dao = (DataObject) context.getNewObject();
		
		int width = 80;
        int height = 120;
        
        IPeCreateService peCreateService = Graphiti.getPeCreateService();
		IGaService gaService = Graphiti.getGaService();
		
		ContainerShape rootContainerShape = peCreateService.createContainerShape(targetShape, true);
		RoundedRectangle roundedRectangle;
		{
			Rectangle invisibleRectangle = gaService.createInvisibleRectangle(rootContainerShape);
			
			gaService.setLocationAndSize(invisibleRectangle, context.getX(), context.getY(), width , height);
		
			roundedRectangle = gaService.createRoundedRectangle(invisibleRectangle, 10, 10);
			roundedRectangle.setBackground(manageColor(getBackgroundColor()));
			roundedRectangle.setForeground(manageColor(getForegroundColor()));
			roundedRectangle.setTransparency(0.2);
			roundedRectangle.setLineWidth(2);
			Graphiti.getPeService().setPropertyValue(roundedRectangle,SHAPE_KEY,ROUNDED_RECTANGLE_ID);
            gaService.setLocationAndSize(roundedRectangle,0, 0, width, height);
       	}
		link(rootContainerShape, dao); 
		
		
		IFeatureProvider fp = getFeatureProvider();
		new DataObjectNameAndTypeHelper(dao, rootContainerShape, fp).addNewShapeOnContainer(width, height);
		
		
		layoutPictogramElement(rootContainerShape);
		return rootContainerShape;
	}

}
