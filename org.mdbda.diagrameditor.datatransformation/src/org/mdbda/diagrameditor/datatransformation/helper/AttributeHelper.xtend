package org.mdbda.diagrameditor.datatransformation.helper

import org.eclipse.graphiti.datatypes.IDimension
import org.eclipse.graphiti.features.IDirectEditingInfo
import org.eclipse.graphiti.features.IFeatureProvider
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm
import org.eclipse.graphiti.mm.algorithms.MultiText
import org.eclipse.graphiti.mm.algorithms.Text
import org.eclipse.graphiti.mm.algorithms.styles.Orientation
import org.eclipse.graphiti.mm.pictograms.ContainerShape
import org.eclipse.graphiti.mm.pictograms.Shape
import org.eclipse.graphiti.services.Graphiti
import org.eclipse.graphiti.services.IGaService
import org.eclipse.graphiti.util.IColorConstant
import org.mdbda.diagrameditor.utils.AbstractSimpleMDBDAShapeHelper
import org.mdbda.model.DataAttribute
import org.mdbda.model.DataObject

class AttributeHelper extends AbstractSimpleMDBDAShapeHelper {
	package DataAttribute dataAttribute
	static final package IColorConstant FOREGROUND_COLOR = IColorConstant.BLACK

	new(DataAttribute attr, ContainerShape rootContainerShapeForResourceElement, IFeatureProvider fp) {
		super(rootContainerShapeForResourceElement, fp)
		this.dataAttribute = attr
	}

	private Shape __newShape

	def Shape getNewShape() {
		return __newShape
	}

	def String getText(DataAttribute attr){
		if(dataAttribute.getValue() != null){
			return '''«dataAttribute.getName()» «dataAttribute.getCondition().getLiteral()» «dataAttribute.getValue()»'''
		}else{
			return '''«dataAttribute.getName()» «dataAttribute.getCondition().getLiteral()»'''
		}
	}
	
	override getShape() {
		featureProvider.getPictogramElementForBusinessObject(dataAttribute) as Shape
	}
	
	override update() {
		if(shape==null) return
		val textGA = shape.graphicsAlgorithm as Text
		textGA.value = dataAttribute.text
	}
	
	override updateNeeded() {
		if(shape==null) return false
		val textGA = shape.graphicsAlgorithm as Text
		
		return !textGA.value.equals(dataAttribute.text)
	}
	
	override IDimension addNewShapeOnContainer(int parentWidth, int parentHeight, int leftOffset, int topOffset) {
		val newShape = createNewShapeOnRootContainer()
		var IGaService gaService = Graphiti.getGaService()

//##################################################################################################################### hier		
		var Text text = gaService.createText(newShape, dataAttribute.text)
		text.setForeground(manageColor(FOREGROUND_COLOR))
		text.setHorizontalAlignment(Orientation.ALIGNMENT_LEFT)
		text.setVerticalAlignment(Orientation.ALIGNMENT_TOP)
		text.setLineVisible(true) // IDirectEditingInfo directEditingInfo =
		// getFeatureProvider().getDirectEditingInfo();
		//
		// directEditingInfo.setMainPictogramElement(rootContainerShapeForResourceElement);
		// directEditingInfo.setPictogramElement(shape);
		// directEditingInfo.setGraphicsAlgorithm(text);
		link(newShape, dataAttribute)
		__newShape = newShape;
		return setLocationAndSize(text, parentWidth, parentHeight, leftOffset, topOffset)
	}

	override IDimension setLocationAndSize(GraphicsAlgorithm ga, int parentWidth, int parentHeight, int leftOffset,
		int topOffset) {
		return super.setLocationAndSize(ga, parentWidth, parentHeight, leftOffset, topOffset)
	}

	override String getShapeId() {
		return "Attribute"
	}

	override int calculateX(int parentWidth, int parentHeight, int leftOffset, int topOffset) {
		return 5
	}

	override int calculateY(int parentWidth, int parentHeight, int leftOffset, int topOffset) {
		return 12 + topOffset
	}

	override int calculateWidth(int parentWidth, int parentHeight, int leftOffset, int topOffset) {
		return parentWidth - 5
	}

	override int calculateHeight(int parentWidth, int parentHeight, int leftOffset, int topOffset) {
		return 15
	}

}
