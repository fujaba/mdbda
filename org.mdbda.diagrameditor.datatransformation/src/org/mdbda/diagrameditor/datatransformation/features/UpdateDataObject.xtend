package org.mdbda.diagrameditor.datatransformation.features

import org.eclipse.graphiti.features.impl.AbstractUpdateFeature
import org.eclipse.graphiti.features.IFeatureProvider
import org.eclipse.graphiti.features.context.IUpdateContext
import org.eclipse.graphiti.mm.pictograms.ContainerShape
import org.mdbda.model.DataObject
import org.mdbda.diagrameditor.datatransformation.helper.DataObjectNameAndTypeHelper
import org.eclipse.graphiti.mm.algorithms.Text
import org.eclipse.graphiti.features.impl.Reason

class UpdateDataObject extends AbstractUpdateFeature {
	
	new(IFeatureProvider fp) {
		super(fp)
	}
	
	override canUpdate(IUpdateContext context) {
		if(context.pictogramElement instanceof ContainerShape 
			&& getBusinessObjectForPictogramElement(context.pictogramElement) instanceof DataObject	){
			return true
		}
		return false
	}
	
	override update(IUpdateContext context) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
		hier gehts weiter :)
	}
	
	override updateNeeded(IUpdateContext context) {
		val dao = getBusinessObjectForPictogramElement(context.pictogramElement) as DataObject
		val helper = new DataObjectNameAndTypeHelper(dao,context.pictogramElement as ContainerShape,featureProvider)
		
		val textGA = helper.shape.graphicsAlgorithm as Text
		
		if(textGA.value.equals(helper.nameAndTypeText)){
			return Reason.createTrueReason()
		}
		
	}
	
}