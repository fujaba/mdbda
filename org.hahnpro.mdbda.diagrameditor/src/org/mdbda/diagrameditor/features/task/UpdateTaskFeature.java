package org.mdbda.diagrameditor.features.task;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.mdbda.diagrameditor.features.AbstactIOMDBDAAddFeature;
import org.mdbda.diagrameditor.utils.DataDescriptionShapeHelper;
import org.mdbda.diagrameditor.utils.LiveStatusShapeHelper;
import org.mdbda.diagrameditor.utils.NameShapeHelper;
import org.mdbda.model.Resource;
import org.mdbda.model.Task;
import org.mdbda.model.Workflow;

public class UpdateTaskFeature extends AbstractUpdateFeature {

	public UpdateTaskFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getPictogramElement());
		return bo instanceof Task && !(bo instanceof  Workflow);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		Resource resource = (Resource) getBusinessObjectForPictogramElement( context.getPictogramElement() );
		ContainerShape rootContainerShape = AbstactIOMDBDAAddFeature.getRootContainerShapeForResourceElement(resource, getFeatureProvider());
		
		NameShapeHelper nameHelper = new NameShapeHelper(resource,rootContainerShape,getFeatureProvider());
		if( nameHelper.hasChanged()){
			return Reason.createTrueReason("The name is out of date");
		}
		
		DataDescriptionShapeHelper ddHelper = new DataDescriptionShapeHelper(resource, rootContainerShape, getFeatureProvider());
		if(ddHelper.hasChanged()){
			return Reason.createTrueReason("Data Desciption status is out of date");
		}
		
		return Reason.createFalseReason();
	}

	@Override
	public boolean update(IUpdateContext context) {
		Resource resource = (Resource) getBusinessObjectForPictogramElement( context.getPictogramElement() );
		ContainerShape rootContainerShape = AbstactIOMDBDAAddFeature.getRootContainerShapeForResourceElement(resource, getFeatureProvider());
		
		boolean somethingchanged = false;
		NameShapeHelper nameHelper = new NameShapeHelper(resource,rootContainerShape,getFeatureProvider());
		if( nameHelper.hasChanged()){
			somethingchanged = true;
			nameHelper.update();			
		}
		
		DataDescriptionShapeHelper ddHelper = new DataDescriptionShapeHelper(resource, rootContainerShape, getFeatureProvider());
		if(ddHelper.hasChanged()){
			somethingchanged = true;
			ddHelper.update();
		}
		return somethingchanged;
	}

}
