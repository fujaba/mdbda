package org.mdbda.diagrameditor.features.resources;

import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.mdbda.diagrameditor.features.AbstactIOMDBDAAddFeature;
import org.mdbda.diagrameditor.pictogramelements.AbstractMDBDAShape;
import org.mdbda.diagrameditor.utils.DataDescriptionShapeHelper;
import org.mdbda.diagrameditor.utils.LiveStatusShapeHelper;
import org.mdbda.diagrameditor.utils.NameShapeHelper;
import org.mdbda.diagrameditor.utils.SampleDataShapeHelper;
import org.mdbda.diagrameditor.utils.TestStatusShapeHelper;
import org.mdbda.diagrameditor.utils.TypeIdShapeHelper;
import org.mdbda.model.Resource;
import org.mdbda.model.Task;

public class ResizeResourceFeature extends DefaultResizeShapeFeature {

	public ResizeResourceFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		return getBusinessObjectForPictogramElement( context.getPictogramElement() ) instanceof Resource;
	}
	static final int minWidth = 60;
	static final int minHeight = 50;
	
	
	@Override
	public void resizeShape(IResizeShapeContext context) {
		Resource resource = (Resource) getBusinessObjectForPictogramElement( context.getPictogramElement() );
		ContainerShape rootContainerShape = AbstactIOMDBDAAddFeature.getRootContainerShapeForResourceElement(resource, getFeatureProvider());
		int width = context.getWidth();
		if(width < minWidth) width = minWidth;
		int height = context.getHeight();
		if(height < minHeight) height = minHeight;

		rootContainerShape.getGraphicsAlgorithm().setWidth(width);
		rootContainerShape.getGraphicsAlgorithm().setHeight(height);
		width = width  - 2* AbstractMDBDAShape.INVISIBLE_RECT_SIDE;
		RoundedRectangle rr = AbstactIOMDBDAAddFeature.getResourceRoundedRectangle(rootContainerShape, getFeatureProvider());
		rr.setWidth(width);
		rr.setHeight(height);
		
		IFeatureProvider fp = getFeatureProvider();
		
		IDimension status = new TestStatusShapeHelper(resource, rootContainerShape, fp).resize(width, height);
		new LiveStatusShapeHelper(resource, rootContainerShape, fp).resize(width, height);
		
		IDimension dim = new TypeIdShapeHelper(resource, rootContainerShape, fp).resize(width, height,0,status.getHeight());
		new NameShapeHelper(resource, rootContainerShape, fp).resize(width, height ,0,dim.getHeight()+status.getHeight());
		new DataDescriptionShapeHelper(resource, rootContainerShape, fp).resize(width, height);
		new SampleDataShapeHelper(resource, rootContainerShape, fp).resize(width, height);
		
	}

}
