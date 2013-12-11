package org.mdbda.diagrameditor.features.updateFeatures;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.mdbda.diagrameditor.internal.PublicGetBusinessObjectForPictogramElementInterface;
import org.mdbda.model.Resource;
import org.mdbda.model.Workflow;

public abstract class AbstractConnectionUpdateFeature extends AbstractUpdateFeature implements PublicGetBusinessObjectForPictogramElementInterface {

	public AbstractConnectionUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		if ( context.getPictogramElement() instanceof Connection ) {
		    return true;
		}
		return false;
	}
	
	@Override
	public Object getBusinessObjectForPictogramElement(PictogramElement pe) {
		return super.getBusinessObjectForPictogramElement(pe);
	}

}
