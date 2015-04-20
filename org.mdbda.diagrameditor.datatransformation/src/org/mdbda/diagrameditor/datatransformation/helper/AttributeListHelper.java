package org.mdbda.diagrameditor.datatransformation.helper;

import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IDirectEditingInfo;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.MultiText;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
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
		Shape shape = createNewShapeOnRootContainer();
		IGaService gaService = Graphiti.getGaService();
		
		StringBuilder sb = new StringBuilder();
		for(DataAttribute atr : dataObject.getAttributes()){			
			sb.append(atr.getName()).append(atr.getCondition().getLiteral()).append(atr.getValue()).append("\r\n");
		}
		
		MultiText text = gaService.createMultiText(shape, sb.toString());
		text.setForeground(manageColor(FOREGROUND_COLOR));
		text.setHorizontalAlignment(Orientation.ALIGNMENT_LEFT);
		text.setVerticalAlignment(Orientation.ALIGNMENT_TOP);

		IDirectEditingInfo directEditingInfo =
                getFeatureProvider().getDirectEditingInfo();
		
		directEditingInfo.setMainPictogramElement(rootContainerShapeForResourceElement);
		directEditingInfo.setPictogramElement(shape);
        directEditingInfo.setGraphicsAlgorithm(text);
        
        
		link(shape, dataObject);
		
		return setLocationAndSize(text, parentWidth, parentHeight, leftOffset, topOffset);
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
