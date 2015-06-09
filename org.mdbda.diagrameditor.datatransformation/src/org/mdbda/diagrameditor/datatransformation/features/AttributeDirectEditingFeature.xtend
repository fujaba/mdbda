package org.mdbda.diagrameditor.datatransformation.features

import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature
import org.eclipse.graphiti.features.IFeatureProvider
import org.eclipse.graphiti.features.context.IDirectEditingContext
import org.mdbda.model.DataAttribute
import org.mdbda.model.DataCondition

class AttributeDirectEditingFeature extends AbstractDirectEditingFeature {
	
	new(IFeatureProvider fp) {
		super(fp)
	}
	
	override getEditingType() { TYPE_TEXT }
	
	override getInitialValue(IDirectEditingContext context) {
		val attr = getBusinessObjectForPictogramElement(context.pictogramElement) as DataAttribute
		'''«attr.name» «attr.condition» «attr.value»'''
	}
	
	override setValue(String value, IDirectEditingContext context) {
		val attr = getBusinessObjectForPictogramElement(context.pictogramElement) as DataAttribute
		
		var value_tmp =	value.replace("  "," ")//repace double whitespaces
		while(value_tmp.length != value_tmp.replace("  "," ")){
			value_tmp = value_tmp.replace("  "," ")
		}
		
		val split = value_tmp.split(" ")
		
		if(split.size >= 2){
			attr.name = split.get(0)
			attr.condition = parseCondition(split.get(1))
		}
		if(split.size >= 3){
			val attrValue = split.get(2)
		}
		
	}
	
	def DataCondition parseCondition(String string) {
		switch(string){
			
		}
	}
	

	
}