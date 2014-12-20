package org.mdbda.diagrameditor.features.task;

import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.algorithms.styles.TextStyle;
import org.eclipse.graphiti.mm.algorithms.styles.TextStyleRegion;
import org.eclipse.graphiti.mm.pictograms.BoxRelativeAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;
import org.mdbda.model.Task;
import org.mdbda.model.Workflow;
import org.mdbda.diagrameditor.features.AbstactMDBDAAddFeature;
import org.mdbda.diagrameditor.pictogramelements.AbstractMDBDAShape;
import org.mdbda.diagrameditor.utils.AbstactMDBDAShapeHelper;
import org.mdbda.diagrameditor.utils.DataDescriptionShapeHelper;
import org.mdbda.diagrameditor.utils.NameShapeHelper;
import org.mdbda.diagrameditor.utils.SampleDataShapeHelper;
import org.mdbda.diagrameditor.utils.TypeIdShapeHelper;


public abstract class AddTaskFeature extends AbstactMDBDAAddFeature implements
		IAddFeature {
	
	public AddTaskFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		return context.getNewObject() instanceof Task && getBusinessObjectForPictogramElement(context.getTargetContainer()) instanceof Workflow;
	}
	
	
	@Override
	public PictogramElement add(IAddContext context) {

		ContainerShape targetShape = context.getTargetContainer();
		Task task = (Task) context.getNewObject();
		
		int width = 120;
        int height = 80; 
		
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		IGaService gaService = Graphiti.getGaService();

		ContainerShape rootContainerShape = peCreateService.createContainerShape(targetShape, true);
		RoundedRectangle roundedRectangle;
		{
			Rectangle invisibleRectangle = gaService.createInvisibleRectangle(rootContainerShape);
			gaService.setLocationAndSize(invisibleRectangle, context.getX(), context.getY(), width + 2 * AbstractMDBDAShape.INVISIBLE_RECT_SIDE, height);
		
			roundedRectangle = gaService.createRoundedRectangle(invisibleRectangle, 10, 10);
			roundedRectangle.setBackground(getBackgroundColor());
			roundedRectangle.setForeground(getForegroundColor());
			roundedRectangle.setLineWidth(2);
			Graphiti.getPeService().setPropertyValue(roundedRectangle,AbstactMDBDAShapeHelper.SHAPE_KEY,ROUNDED_RECTANGLE_ID);
            gaService.setLocationAndSize(roundedRectangle,
            		AbstractMDBDAShape.INVISIBLE_RECT_SIDE, 0, width, height);
       	}

        // create link and wire it
		link(rootContainerShape, task);
		
				
		
//        int typeTextHeight = addTypeIdText(pattern, width, height, rootContainerShapeForResourceElement,0).getHeight();
//        addNameText(pattern, width, height,	rootContainerShapeForResourceElement, typeTextHeight);
//                
//        addSampleDataShape(pattern, width, height, rootContainerShapeForResourceElement);
//        addDataDescriptionShape(pattern, width, height, rootContainerShapeForResourceElement);
		IFeatureProvider fp = getFeatureProvider();
        IDimension dim = new TypeIdShapeHelper(task, rootContainerShape,fp).addNewShapeOnContainer(width, height);
		new NameShapeHelper(task, rootContainerShape, fp).addNewShapeOnContainer(width, height, 0, dim.getHeight());
		new SampleDataShapeHelper(task, rootContainerShape, fp).addNewShapeOnContainer(width, height);
		new DataDescriptionShapeHelper(task, rootContainerShape, fp).addNewShapeOnContainer(width, height);
		
        
        
        //Anchor
//		boolean singleoutput = (Math.random() > 0.5);

		int max = task.getMaxInputCount();
		if (max < 1)
			max = 1;

		double faktor = 1.0 / (max + 1.0);
		for (int i = 1; i <= max; i++) {
			addInputAnchor(task, rootContainerShape, faktor * i, getDiagram(),fp);
		}
        
		max = task.getMaxOutputCount();
		if (max < 1)
			max = 1;
		faktor = 1.0 / (max + 1.0);
		for (int i = 1; i <= max; i++) {
			addOutputAnchor(task, rootContainerShape, faktor * i, getDiagram(),fp);
		}
		
//		
//		if(singleoutput){
//			addOutputAnchor(task, rootContainerShape, roundedRectangle, 0.5);
//        }else{
//        	addOutputAnchor(task, rootContainerShape, roundedRectangle, 0.4);
//        	addOutputAnchor(task, rootContainerShape, roundedRectangle, 0.75);
//        }
        
//        boolean singleinput = (Math.random() > 0.5);
//		if(singleinput){
//        	addInputAnchor(task, rootContainerShape, roundedRectangle, 0.5);
//        }else{ 
//        	addInputAnchor(task, rootContainerShape, roundedRectangle, 0.4);
//        	addInputAnchor(task, rootContainerShape, roundedRectangle, 0.75);
//        }
        

    	layoutPictogramElement(rootContainerShape);
		return rootContainerShape;
	}


}
