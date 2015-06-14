package org.mdbda.diagrameditor.datatransformation.features

import org.eclipse.graphiti.features.IFeatureProvider
import org.eclipse.graphiti.features.context.IDirectEditingContext
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature
import org.mdbda.model.DataAttribute
import org.mdbda.model.DataCondition
import org.eclipse.graphiti.mm.pictograms.PictogramElement

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
		val in = replaceWS(value);
		val split = in.split(" ")
		
		if(split.size >= 2){
			attr.name = split.get(0)
			attr.condition = parseCondition(split.get(1))
			attr.value = null
		}
		if(split.size >= 3){
			attr.value = in.substring(in.indexOf(split.get(1)) +split.get(1).length + 1 )
		}

		
		updatePictogramElement(context.pictogramElement.eContainer as PictogramElement)
	}
	
	def String replaceWS(String value){
		var value_tmp =	value.replace("  "," ")//repace double whitespaces
		while(value_tmp.length != value_tmp.replace("  "," ").length){
			value_tmp = value_tmp.replace("  "," ")
		}
		value_tmp
	}
	
	override checkValueValid(String value, IDirectEditingContext context) {
		
		
		val split = replaceWS(value).split(" ")
		if(split.size < 2 ){
			return '''An attribute condition should be like "name == 'Alice'" or "phone «DataCondition.EXIST»" 
it must match the pattern: <name>' '<condition>' '<value?>'''
		}
		
		//check attribute
		
		//check condition
		val condition = parseCondition(split.get(1))
		if(condition == null){
			return '''The conditon «split.get(1)» is not valid'''
		}
		//check value
		if((condition == DataCondition.EXIST || condition == DataCondition.NOT_EXIST) && split.size == 3){
			return '''If the condition is «DataCondition.EXIST» or «DataCondition.NOT_EXIST» resp. «split.get(1)», the <value> part should not be set.
Try "«split.get(0)» «split.get(1)»" instead of "«split.get(0)» «split.get(1)» «split.get(2)»"'''
		}
		
		return null
	}
	
	def DataCondition parseCondition(String string) {
		var dcon = DataCondition.get(string)
		if( dcon == null ){
			dcon = DataCondition.getByName(string)
		}
		
		if(dcon == null){
			switch(string){
				case "ex":
					dcon =  DataCondition.EXIST
				case "nex",
				case "!ex":
					dcon =  DataCondition.NOT_EXIST
			}
		}
		
		return dcon
	}
}
