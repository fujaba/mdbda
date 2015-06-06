package org.mdbda.diagrameditor.datatransformation.features

import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature
import org.eclipse.graphiti.features.IFeatureProvider
import org.eclipse.graphiti.features.context.IDirectEditingContext
import org.mdbda.model.DataAttribute

class AttributeDirectEditingFeature extends AbstractDirectEditingFeature {
	
	new(IFeatureProvider fp) {
		super(fp)
	}
	
	override getEditingType() { TYPE_TEXT }
	
	override getInitialValue(IDirectEditingContext context) {
		val attr = getBusinessObjectForPictogramElement(context.pictogramElement) as DataAttribute
		
		'''«attr.name» «attr.condition» «attr.value»'''
	}
	
}