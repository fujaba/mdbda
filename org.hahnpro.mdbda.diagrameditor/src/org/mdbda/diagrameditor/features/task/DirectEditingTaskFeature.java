package org.mdbda.diagrameditor.features.task;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.mdbda.diagrameditor.features.AbstactMDBDAAddFeature;
import org.mdbda.diagrameditor.utils.NameShapeHelper;
import org.mdbda.model.Task;

public class DirectEditingTaskFeature extends AbstractDirectEditingFeature {

	public DirectEditingTaskFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public int getEditingType() {
		return TYPE_MULTILINETEXT;
	}
	
	@Override
	public String getInitialValue(IDirectEditingContext context) {
		Task task = (Task) getBusinessObjectForPictogramElement(context.getPictogramElement());
		return task.getName();
	}
	
	@Override
	public void setValue(String value, IDirectEditingContext context) {
		Task task = (Task) getBusinessObjectForPictogramElement(context.getPictogramElement());
		ContainerShape rootContainerShape = AbstactMDBDAAddFeature.getRootContainerShapeForResourceElement(task, getFeatureProvider());
		task.setName(value);
		
		NameShapeHelper helper = new NameShapeHelper(task, rootContainerShape, getFeatureProvider());
		
		updatePictogramElement(helper.getShape().getContainer());
	}
}
