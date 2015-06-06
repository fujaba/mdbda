package org.mdbda.diagrameditor.datatransformation.features

import org.mdbda.diagrameditor.features.AbstractSimpleMDBDAAddFeature
import org.eclipse.graphiti.features.context.IAddContext
import org.eclipse.graphiti.features.IFeatureProvider
import org.mdbda.model.DataObject
import org.eclipse.graphiti.mm.pictograms.ContainerShape
import org.mdbda.diagrameditor.datatransformation.helper.AttributeHelper
import org.mdbda.model.DataAttribute

class AddAttribute extends AbstractSimpleMDBDAAddFeature {
	
	new(IFeatureProvider fp) {
		super(fp)
	}
	
	override canAdd(IAddContext context) {
		if(context.targetContainer instanceof ContainerShape 
			&& getBusinessObjectForPictogramElement(context.targetContainer) instanceof DataObject
			&& context.newObject instanceof DataAttribute
		) {
			return true;
		}
		return false;
	}
	
	override add(IAddContext context) {
		val helper = new AttributeHelper(context.newObject as DataAttribute,context.targetContainer,featureProvider);
		helper.addNewShapeOnContainer()
		layoutPictogramElement(context.targetContainer)
		return AttributeHelper.getLastShape();
	}
}