package org.mdbda.diagrameditor.features.updateFeatures;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.mdbda.model.Resource;
import org.mdbda.model.Workflow;

public class ResourceUpdateFeature extends AbstractUpdateFeature {

	public ResourceUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		Object bo =
	            getBusinessObjectForPictogramElement(context.getPictogramElement());
	    return (bo instanceof Resource);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		return Reason.createFalseReason("not implemented");
	}

	@Override
	public boolean update(IUpdateContext context) {
		// TODO Auto-generated method stub
		return false;
	}


}
