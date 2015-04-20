package org.mdbda.diagrameditor.features.resources;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.mdbda.diagrameditor.features.AbstactIOMDBDAAddFeature;
import org.mdbda.diagrameditor.utils.LiveStatusShapeHelper;
import org.mdbda.diagrameditor.utils.NameShapeHelper;
import org.mdbda.diagrameditor.utils.TestStatusShapeHelper;
import org.mdbda.model.Resource;
import org.mdbda.model.Task;

public class UpdateResourceFeature extends AbstractUpdateFeature {

	public UpdateResourceFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getPictogramElement());
		return bo instanceof Resource && !(bo instanceof Task);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		Resource resource = (Resource) getBusinessObjectForPictogramElement( context.getPictogramElement() );
		ContainerShape rootContainerShape = AbstactIOMDBDAAddFeature.getRootContainerShapeForResourceElement(resource, getFeatureProvider());
		
		LiveStatusShapeHelper liveHelper = new LiveStatusShapeHelper(resource,rootContainerShape,getFeatureProvider());
		TestStatusShapeHelper testHelper = new TestStatusShapeHelper(resource,rootContainerShape,getFeatureProvider());
		
		if( liveHelper.hasChanged()){
			return Reason.createTrueReason("Live Server status is out of date");
		}
		if(testHelper.hasChanged()){
			return Reason.createTrueReason("Test Server status is out of date");
		}
		
		return Reason.createFalseReason();
	}
	
	

	@Override
	public boolean update(IUpdateContext context) {
		Resource resource = (Resource) getBusinessObjectForPictogramElement( context.getPictogramElement() );
		ContainerShape rootContainerShape = AbstactIOMDBDAAddFeature.getRootContainerShapeForResourceElement(resource, getFeatureProvider());
		
		LiveStatusShapeHelper liveHelper = new LiveStatusShapeHelper(resource,rootContainerShape,getFeatureProvider());
		TestStatusShapeHelper testHelper = new TestStatusShapeHelper(resource,rootContainerShape,getFeatureProvider());
		NameShapeHelper nameHelper = new NameShapeHelper(resource,rootContainerShape,getFeatureProvider());
		
		boolean somethingChanged = false;
		if( nameHelper.hasChanged()){
			somethingChanged = true;
			nameHelper.update();
		}
		if( liveHelper.hasChanged()){
			somethingChanged = true;
			liveHelper.update();
		}
		if(testHelper.hasChanged()){
			somethingChanged = true;
			testHelper.update();
		}
		return somethingChanged;
	}

}
