package org.mdbda.diagrameditor.features.updateFeatures;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.mdbda.model.Workflow;

public class WorkflowUpdateFeature extends AbstractUpdateFeature {

	public WorkflowUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		Object bo =
	            getBusinessObjectForPictogramElement(context.getPictogramElement());
	    return (bo instanceof Workflow);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		// TODO Auto-generated method stub
		return Reason.createFalseReason("not implemented");
	}

	@Override
	public boolean update(IUpdateContext context) {
		// TODO Auto-generated method stub
		return false;
	}

}
