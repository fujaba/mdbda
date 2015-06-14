package org.mdbda.diagrameditor.datatransformation.features

import org.eclipse.graphiti.features.IFeatureProvider
import org.eclipse.graphiti.features.context.IAddContext
import org.eclipse.graphiti.mm.algorithms.Rectangle
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle
import org.eclipse.graphiti.mm.pictograms.ContainerShape
import org.eclipse.graphiti.mm.pictograms.PictogramElement
import org.eclipse.graphiti.services.Graphiti
import org.eclipse.graphiti.services.IGaService
import org.eclipse.graphiti.services.IPeCreateService
import org.mdbda.diagrameditor.datatransformation.helper.DataObjectNameAndTypeHelper
import org.mdbda.diagrameditor.features.AbstractSimpleMDBDAAddFeature
import org.mdbda.model.DataObject
import org.mdbda.diagrameditor.datatransformation.helper.AttributeListHelper
import org.eclipse.graphiti.datatypes.IDimension
import org.eclipse.graphiti.mm.pictograms.Shape
import org.eclipse.graphiti.util.ColorConstant

class AddDataObject extends AbstractSimpleMDBDAAddFeature {
	public static final String SHAPE_KEY = "shape-id"
	protected static final String ROUNDED_RECTANGLE_ID = "RoundedRecangle"

	new(IFeatureProvider fp) {
		super(fp)
	}

	@Override override boolean canAdd(IAddContext context) {
		return context.getNewObject() instanceof DataObject
	}
	
	var DataObject dao
	
	override protected getBackgroundColor() {
		if(dao.linkedInputResource != null){
			//input element
			return new ColorConstant("F8B0B0");
		}else if(dao.linkedOutputResource != null){
			//output element
			return new ColorConstant("B3F8B0");
		}else{
			super.getBackgroundColor()
		}
	}

	@Override override PictogramElement add(IAddContext context) {
		val targetShape = context.getTargetContainer()
		dao = context.getNewObject() as DataObject
		val width = 200
		val height = 250
		val IPeCreateService peCreateService = Graphiti.getPeCreateService()
		val IGaService gaService = Graphiti.getGaService()
		val ContainerShape rootContainerShape = peCreateService.createContainerShape(targetShape, true)
		var RoundedRectangle roundedRectangle
		{
			val Rectangle invisibleRectangle = gaService.createInvisibleRectangle(rootContainerShape)
			gaService.setLocationAndSize(invisibleRectangle, context.getX(), context.getY(), width, height)
			roundedRectangle = gaService.createRoundedRectangle(invisibleRectangle, 10, 10)
			roundedRectangle.setBackground(manageColor(getBackgroundColor()))
			roundedRectangle.setForeground(manageColor(getForegroundColor()))
			roundedRectangle.setTransparency(0.2)
			roundedRectangle.setLineWidth(2)
			Graphiti.getPeService().setPropertyValue(roundedRectangle, SHAPE_KEY, ROUNDED_RECTANGLE_ID)
			gaService.setLocationAndSize(roundedRectangle, 0, 0, width, height)
		}
		link(rootContainerShape, dao)
		val IDimension dim = new DataObjectNameAndTypeHelper(dao, rootContainerShape, getFeatureProvider()).addNewShapeOnContainer(width, height)
		
		val Shape shape = peCreateService.createShape(rootContainerShape, false);
		
		val line = gaService.createPolyline(shape,#[0,dim.height + 7,width,dim.height + 7])
		line.lineWidth = 3
		
		new AttributeListHelper(dao,rootContainerShape,getFeatureProvider()).addNewShapeOnContainer(width, height,0,9)
		layoutPictogramElement(rootContainerShape)
		return rootContainerShape
	}

}
