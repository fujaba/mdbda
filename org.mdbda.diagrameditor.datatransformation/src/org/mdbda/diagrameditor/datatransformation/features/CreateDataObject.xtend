package org.mdbda.diagrameditor.datatransformation.features

import org.eclipse.graphiti.features.ICreateFeature
import org.eclipse.graphiti.features.IFeatureProvider
import org.eclipse.graphiti.features.context.ICreateContext
import org.eclipse.graphiti.features.impl.AbstractCreateFeature
import org.eclipse.graphiti.mm.pictograms.Diagram
import org.mdbda.model.DataObject
import org.mdbda.model.MDBDAModelRoot
import org.mdbda.model.ModelFactory
import org.mdbda.model.Task

import static extension org.mdbda.diagrameditor.datatransformation.helper.DataTypeHelper.*
import org.mdbda.model.DataCondition

class CreateDataObject extends AbstractCreateFeature implements ICreateFeature {
	new(IFeatureProvider fp) {
		super(fp, "Data Object", "Create new Data Object")
	}

	@Override override boolean canCreate(ICreateContext context) {
		return context.getTargetContainer() instanceof Diagram &&
			getBusinessObjectForPictogramElement(context.getTargetContainer()) instanceof Task
	}

	@Override override Object[] create(ICreateContext context) {
		var DataObject dob = createDataObject(
			getBusinessObjectForPictogramElement(context.getTargetContainer()) as Task)
		addGraphicalRepresentation(context, dob)
		return #[dob]
	}
 
	def DataObject createDataObject(Task parentTask) {
		var DataObject dob = ModelFactory.eINSTANCE.createDataObject()
		parentTask.getDataObjects().add(dob)
		var MDBDAModelRoot modelRoot = parentTask.getWorkflow().getModelRoot()
		dob.dataType = parentTask.getWorkflow().getModelRoot().defaultType
		
		var attr1 = ModelFactory.eINSTANCE.createDataAttribute
		attr1.name = "attr1"
		attr1.condition = DataCondition.EXIST
		attr1.value = "42"
		dob.attributes.add(attr1)
		
		var attr2 = ModelFactory.eINSTANCE.createDataAttribute
		attr2.name = "name"
		attr2.condition = DataCondition.EQUALS
		attr2.value = "Alice"
		dob.attributes.add(attr2)	
		
		var attr3 = ModelFactory.eINSTANCE.createDataAttribute
		attr3.name = "age"
		attr3.condition = DataCondition.GEATER_OR_EQUALS
		attr3.value = "23"
		dob.attributes.add(attr3)	

		return dob
	}

}