package org.mdbda.diagrameditor.datatransformation.features

import org.eclipse.graphiti.features.impl.AbstractLayoutFeature
import org.eclipse.graphiti.features.context.ILayoutContext
import org.eclipse.graphiti.features.IFeatureProvider
import org.eclipse.graphiti.mm.pictograms.ContainerShape
import org.mdbda.model.DataObject
import org.eclipse.graphiti.mm.pictograms.Shape
import org.mdbda.diagrameditor.datatransformation.helper.AttributeHelper
import org.mdbda.diagrameditor.utils.AbstractSimpleMDBDAShapeHelper
import org.eclipse.graphiti.services.Graphiti

class LayoutDataObject extends AbstractLayoutFeature {
	
	new(IFeatureProvider fp) {
		super(fp)
	}
	
	override canLayout(ILayoutContext context) {
		if(context.pictogramElement instanceof ContainerShape
			&& getBusinessObjectForPictogramElement(context.pictogramElement) instanceof DataObject
			&& (getBusinessObjectForPictogramElement(context.pictogramElement) as DataObject).attributes.size > 0
		){
			return true
		}
		return false
	}
	
	override layout(ILayoutContext context) {
		var anythingChanged = false;
		val rootContainerShape = context.pictogramElement as ContainerShape
		val helper = new AttributeHelper(null,rootContainerShape,featureProvider)
		
		var width = 200
		var height = 250
		
		var top = 9
		//find and order attribute shapes
		for(Shape child : rootContainerShape.children){
			if(helper.shapeId.equals(Graphiti.peService.getPropertyValue(child,AbstractSimpleMDBDAShapeHelper.SHAPE_KEY))){
				//getBusinessObjectForPictogramElement(child);
				val oldX = child.graphicsAlgorithm.x
				val oldY = child.graphicsAlgorithm.y
				top += helper.setLocationAndSize(child.graphicsAlgorithm, width,height,0,top).height
				
				if(child.graphicsAlgorithm.x != oldX || child.graphicsAlgorithm.y != oldY){
					anythingChanged = true
				}
			}
		}
		
		
		return anythingChanged
	}
	
}