package org.mdbda.diagrameditor.features.resources;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.mdbda.diagrameditor.features.AbstactIOMDBDAAddFeature;
import org.mdbda.diagrameditor.utils.NameShapeHelper;
import org.mdbda.model.Resource;
import org.mdbda.model.Task;

public class DirectEditingResourceFeature extends AbstractDirectEditingFeature {

	public DirectEditingResourceFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public int getEditingType() {
		return TYPE_MULTILINETEXT;
	}
	
	@Override
	public String getInitialValue(IDirectEditingContext context) {
		Resource res = (Resource) getBusinessObjectForPictogramElement(context.getPictogramElement());
		return res.getName();
	}
	
	@Override
	public void setValue(String value, IDirectEditingContext context) {
		Resource res = (Resource) getBusinessObjectForPictogramElement(context.getPictogramElement());
		ContainerShape rootContainerShape = AbstactIOMDBDAAddFeature.getRootContainerShapeForResourceElement(res, getFeatureProvider());
		res.setName(value);
		
		NameShapeHelper helper = new NameShapeHelper(res, rootContainerShape, getFeatureProvider());
		
		updatePictogramElement(helper.getShape().getContainer());
	}

}
