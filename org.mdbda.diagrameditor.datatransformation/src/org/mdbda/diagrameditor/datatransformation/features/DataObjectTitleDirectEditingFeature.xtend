package org.mdbda.diagrameditor.datatransformation.features

import org.eclipse.graphiti.features.IFeatureProvider
import org.eclipse.graphiti.features.context.IDirectEditingContext
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature
import org.eclipse.graphiti.mm.pictograms.PictogramElement
import org.mdbda.model.DataObject
import org.mdbda.diagrameditor.datatransformation.helper.DataTypeHelper

class DataObjectTitleDirectEditingFeature extends AbstractDirectEditingFeature {
	
	new(IFeatureProvider fp) {
		super(fp)
	}
	
	override getEditingType() { TYPE_TEXT }
	
	override getInitialValue(IDirectEditingContext context) {
		val dataObject = getBusinessObjectForPictogramElement(context.pictogramElement) as DataObject
		
		val typeName = dataObject.dataType.packageName + "." + dataObject.dataType.typeName
		return dataObject.name + " : " + typeName;
	}
	
	override checkValueValid(String value, IDirectEditingContext context) {
		val error  = "Please enter a Name and a Type like \"Alice : java.lang.Object \""
		if(value == null || value.length < 3 ){		return error	}
		
		val split = value.split(":")
		if(split.size != 2){	return error	}
		
		if(split.get(0).trim.length < 1){ return "Please set a Name" }
		
		val type = split.get(1).trim		
		val typeName = type.substring(type.lastIndexOf(".")+1)
		
		if(type.length < 1 || typeName.length < 1 || type.contains("..")){ return "Please set a correct Type" }
		
		var typePackage = ""
		if(type.length - typeName.length - 1 >= 1){
			typePackage = type.substring(0,type.length - typeName.length - 1)
		}
			
		val dataObject = getBusinessObjectForPictogramElement(context.pictogramElement) as DataObject
		
		if(DataTypeHelper.searchType(dataObject.containerTask.workflow.modelRoot, typeName, typePackage) == null){
			return "Unkown DataType: " + type
		}
	
		null
	}
	
	override getValueProposals(String value, int caretPos, IDirectEditingContext context) {
		super.getValueProposals(value, caretPos, context)
	}
	
	override setValue(String value, IDirectEditingContext context) {
		val dataObject = getBusinessObjectForPictogramElement(context.pictogramElement) as DataObject
		
		val split = value.split(":")
		val name = split.get(0).trim
		val type = split.get(1).trim
		
		val typeName = type.substring(type.lastIndexOf(".")+1)
		val typePackage = type.substring(0,type.length - typeName.length - 1)
		
		dataObject.name = name
		
		dataObject.dataType = DataTypeHelper.searchType(dataObject.containerTask.workflow.modelRoot, typeName, typePackage)
		
	}
	
}