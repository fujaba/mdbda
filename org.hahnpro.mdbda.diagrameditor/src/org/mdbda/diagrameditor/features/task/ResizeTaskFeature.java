package org.mdbda.diagrameditor.features.task;

import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.mdbda.diagrameditor.features.AbstactMDBDAAddFeature;
import org.mdbda.diagrameditor.pictogramelements.AbstractMDBDAShape;
import org.mdbda.diagrameditor.utils.DataDescriptionShapeHelper;
import org.mdbda.diagrameditor.utils.NameShapeHelper;
import org.mdbda.diagrameditor.utils.SampleDataShapeHelper;
import org.mdbda.diagrameditor.utils.TypeIdShapeHelper;
import org.mdbda.model.Task;

public class ResizeTaskFeature extends DefaultResizeShapeFeature {

	public ResizeTaskFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		return getBusinessObjectForPictogramElement( context.getPictogramElement() ) instanceof Task;
	}
	static final int minWidth = 60;
	static final int minHeight = 40;
	
	
	@Override
	public void resizeShape(IResizeShapeContext context) {
		Task task = (Task) getBusinessObjectForPictogramElement( context.getPictogramElement() );
		ContainerShape rootContainerShape = (ContainerShape) context.getPictogramElement();//AbstactMDBDAAddFeature.getRootContainerShapeForResourceElement(task, getFeatureProvider());
		int width = context.getWidth();
		if(width < minWidth) width = minWidth;
		int height = context.getHeight();
		if(height < minHeight) height = minHeight;

		rootContainerShape.getGraphicsAlgorithm().setWidth(width);
		rootContainerShape.getGraphicsAlgorithm().setHeight(height);
		width = width  - 2* AbstractMDBDAShape.INVISIBLE_RECT_SIDE;
		RoundedRectangle rr = AbstactMDBDAAddFeature.getResourceRoundedRectangle(rootContainerShape, getFeatureProvider());
		rr.setWidth(width);
		rr.setHeight(height);
		
		IFeatureProvider fp = getFeatureProvider();

		IDimension dim = new TypeIdShapeHelper(task, rootContainerShape, fp).resize(width, height);
		new NameShapeHelper(task, rootContainerShape, fp).resize(width, height ,0,dim.getHeight());
		new DataDescriptionShapeHelper(task, rootContainerShape, fp).resize(width, height);
		new SampleDataShapeHelper(task, rootContainerShape, fp).resize(width, height);
		
	}

}
