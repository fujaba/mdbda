package org.mdbda.diagrameditor.datatransformation.helper;

import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IDirectEditingInfo;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.MultiText;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.util.IColorConstant;
import org.mdbda.diagrameditor.utils.AbstractSimpleMDBDAShapeHelper;
import org.mdbda.model.DataAttribute;
import org.mdbda.model.DataObject;

public class AttributeHelper extends AbstractSimpleMDBDAShapeHelper {

	DataAttribute dataAttribute;
	static final IColorConstant FOREGROUND_COLOR = IColorConstant.BLACK;
	
	public AttributeHelper(
			DataAttribute attr,
			ContainerShape rootContainerShapeForResourceElement,
			IFeatureProvider fp) {
		super(rootContainerShapeForResourceElement, fp);
		this.dataAttribute = attr;
	}
	
	private static Shape shape;
	
	public static Shape getLastShape() {
		return shape;
	}
	
	@Override
	public IDimension addNewShapeOnContainer(int parentWidth, int parentHeight,
			int leftOffset, int topOffset) {
		shape = createNewShapeOnRootContainer();
		IGaService gaService = Graphiti.getGaService();
		StringBuilder sb = new StringBuilder();					
		sb.append(dataAttribute.getName()).append(dataAttribute.getCondition().getLiteral()).append(dataAttribute.getValue());
		
		
		Text text = gaService.createText(shape, sb.toString());
		text.setForeground(manageColor(FOREGROUND_COLOR));
		text.setHorizontalAlignment(Orientation.ALIGNMENT_LEFT);
		text.setVerticalAlignment(Orientation.ALIGNMENT_TOP);
		text.setLineVisible(true);
//		IDirectEditingInfo directEditingInfo =
//				getFeatureProvider().getDirectEditingInfo();
//		
//		directEditingInfo.setMainPictogramElement(rootContainerShapeForResourceElement);
//		directEditingInfo.setPictogramElement(shape);
//		directEditingInfo.setGraphicsAlgorithm(text);

		link(shape, dataAttribute);
		
		
		return setLocationAndSize(text, parentWidth, parentHeight, leftOffset, topOffset);
	}

	@Override
	public IDimension setLocationAndSize(GraphicsAlgorithm ga, int parentWidth, int parentHeight, int leftOffset,
			int topOffset) {
		return super.setLocationAndSize(ga, parentWidth, parentHeight, leftOffset, topOffset);
	}
	
	@Override
	public String getShapeId() {
		return "Attribute";
	}

	@Override
	public int calculateX(int parentWidth, int parentHeight, int leftOffset,
			int topOffset) {
		return 5;
	}

	@Override
	public int calculateY(int parentWidth, int parentHeight, int leftOffset,
			int topOffset) {
		return 12 + topOffset;
	}

	@Override
	public int calculateWidth(int parentWidth, int parentHeight,
			int leftOffset, int topOffset) {
		return parentWidth - 5;
	}

	@Override
	public int calculateHeight(int parentWidth, int parentHeight,
			int leftOffset, int topOffset) {
		return 15;
	}

}
