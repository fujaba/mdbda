package org.mdbda.diagrameditor.datatransformation.helper;

import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.util.IColorConstant;
import org.mdbda.diagrameditor.utils.AbstractSimpleMDBDAShapeHelper;
import org.mdbda.model.DataAttribute;
import org.mdbda.model.DataObject;

public class AttributeListHelper extends AbstractSimpleMDBDAShapeHelper {

	DataObject dataObject;
	static final IColorConstant FOREGROUND_COLOR = IColorConstant.BLACK;
	
	public AttributeListHelper(
			DataObject dao,
			ContainerShape rootContainerShapeForResourceElement,
			IFeatureProvider fp) {
		super(rootContainerShapeForResourceElement, fp);
		this.dataObject = dao;
	}
	
	@Override
	public IDimension addNewShapeOnContainer(int parentWidth, int parentHeight,
			int leftOffset, int topOffset) {
		//ContainerShape shape = createNewContainerShapeOnRootContainer();
		
		int top = topOffset;
		for(DataAttribute atr : dataObject.getAttributes()){
			IDimension dim = new AttributeHelper(atr, rootContainerShapeForResourceElement, getFeatureProvider()).addNewShapeOnContainer(parentWidth, parentHeight, leftOffset, top);
			top += dim.getHeight();
		}
		
		return null;
	}

	@Override
	protected String getShapeId() {
		return "AttributeList";
	}

	@Override
	public int calculateX(int parentWidth, int parentHeight, int leftOffset,
			int topOffset) {
		return 5;
	}

	@Override
	public int calculateY(int parentWidth, int parentHeight, int leftOffset,
			int topOffset) {
		return 12;
	}

	@Override
	public int calculateWidth(int parentWidth, int parentHeight,
			int leftOffset, int topOffset) {
		return parentWidth - 5;
	}

	@Override
	public int calculateHeight(int parentWidth, int parentHeight,
			int leftOffset, int topOffset) {
		return parentHeight - 5;
	}

}
