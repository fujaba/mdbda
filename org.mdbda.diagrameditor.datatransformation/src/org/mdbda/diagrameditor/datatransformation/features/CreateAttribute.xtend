package org.mdbda.diagrameditor.datatransformation.features

import org.eclipse.graphiti.features.impl.AbstractCreateFeature
import org.eclipse.graphiti.features.context.ICreateContext
import org.eclipse.graphiti.features.IFeatureProvider
import org.eclipse.graphiti.mm.pictograms.ContainerShape
import org.mdbda.model.DataObject
import org.mdbda.model.ModelFactory
import org.mdbda.model.DataCondition

class CreateAttribute extends AbstractCreateFeature {
	
	new(IFeatureProvider fp) {
		super(fp, "Add Attribute", "Creates a new Attribute for DataObject")
	}
	
	override canCreate(ICreateContext context) {
		if(context.targetContainer instanceof ContainerShape 
			&& getBusinessObjectForPictogramElement(context.targetContainer) instanceof DataObject
		) {
			return true;
		}
		return false;
	}
	
	override create(ICreateContext context) {
		val dao = getBusinessObjectForPictogramElement(context.targetContainer) as DataObject
		
		val attr = ModelFactory.eINSTANCE.createDataAttribute
		attr.name = "attr"
		attr.condition = DataCondition.EQUALS
		attr.value = "42"
		
		dao.attributes.add(attr)
		
		addGraphicalRepresentation(context, attr)
		
		return #[attr]
	}
	
}