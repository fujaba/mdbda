package org.mdbda.diagrameditor.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.mdbda.diagrameditor.features.AbstactMDBDAAddFeature;
import org.mdbda.diagrameditor.pictogramelements.AbstractMDBDAShape;
import org.mdbda.diagrameditor.utils.NameShapeHelper;
import org.mdbda.model.RemoteWorkflow;
import org.mdbda.model.Resource;
import org.mdbda.model.Workflow;

public class ResizeWorkflowFeature extends DefaultResizeShapeFeature {

	public ResizeWorkflowFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		Object bo = getBusinessObjectForPictogramElement( context.getPictogramElement() );
		return bo instanceof Workflow || bo instanceof RemoteWorkflow;
	}
	int minWidth = 60;
	int minHeight = 40;
	
	
	@Override
	public void resizeShape(IResizeShapeContext context) {
		Resource res = (Resource) getBusinessObjectForPictogramElement( context.getPictogramElement() );
		
		if(AddWorkflowFeature.isRootWorkflow(res, context.getShape().getContainer())){
			minWidth = 600;
			minHeight = 400;
		}
		
		
		ContainerShape rootContainerShape = (ContainerShape)context.getShape();// AbstactMDBDAAddFeature.getRootContainerShapeForResourceElement(res, getFeatureProvider());
		int width = context.getWidth();
		if(width < minWidth) width = minWidth;
		int height = context.getHeight();
		if(height < minHeight) height = minHeight;

		rootContainerShape.getGraphicsAlgorithm().setWidth(width);
		rootContainerShape.getGraphicsAlgorithm().setHeight(height);
		
		if(AddWorkflowFeature.isWorkflowReference(res, context.getShape().getContainer())){
			width = width  - 2* AbstractMDBDAShape.INVISIBLE_RECT_SIDE;
		}
		RoundedRectangle rr = AbstactMDBDAAddFeature.getResourceRoundedRectangle(rootContainerShape, getFeatureProvider());
		rr.setWidth(width);
		rr.setHeight(height);
		
		new NameShapeHelper(res, rootContainerShape, getFeatureProvider()).resize(width, height);
		
	}

}
