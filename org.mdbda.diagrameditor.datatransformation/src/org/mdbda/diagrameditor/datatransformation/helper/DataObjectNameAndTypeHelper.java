package org.mdbda.diagrameditor.datatransformation.helper;

import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IDirectEditingInfo;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.algorithms.styles.Style;
import org.eclipse.graphiti.mm.algorithms.styles.StylesFactory;
import org.eclipse.graphiti.mm.algorithms.styles.TextStyle;
import org.eclipse.graphiti.mm.algorithms.styles.TextStyleRegion;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.tb.TextDecorator;
import org.eclipse.graphiti.util.IColorConstant;
import org.mdbda.diagrameditor.utils.AbstractSimpleMDBDAShapeHelper;
import org.mdbda.model.DataObject;

public class DataObjectNameAndTypeHelper extends AbstractSimpleMDBDAShapeHelper {

	static final IColorConstant FOREGROUND_COLOR = IColorConstant.BLACK;
	
	DataObject dataObject;

	public DataObjectNameAndTypeHelper(
			DataObject dao,
			ContainerShape rootContainerShapeForResourceElement,
			IFeatureProvider fp) {
		super(rootContainerShapeForResourceElement, fp);
		this.dataObject = dao;
	}

	@Override
	protected String getShapeId() {
		return "DataObjectNameAndType";
	}
	
	@Override
	public IDimension addNewShapeOnContainer(int parentWidth, int parentHeight,
			int leftOffset, int topOffset) {
		Shape shape = createNewShapeOnRootContainer();
		IGaService gaService = Graphiti.getGaService();
		
		String name = dataObject.getName();
		if(dataObject.getDataType() == null){
			dataObject.setDataType(DataTypeHelper.getDefaultType(dataObject.getContainerTask().getWorkflow().getModelRoot()));
		}
		String typeName = dataObject.getDataType().getTypeName();
		
		
		
		Text text = gaService.createText(shape, name + " : " + typeName);
		
		text.setForeground(manageColor(FOREGROUND_COLOR));
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_TOP);
		
		
		TextStyleRegion textStyleRegion = gaService.createTextStyleRegion(text);
		TextStyle textStyle = gaService.createTextStyle(textStyleRegion);
		textStyle.setUnderline(true);	
		
		
		IDirectEditingInfo directEditingInfo =
                getFeatureProvider().getDirectEditingInfo();
		
		directEditingInfo.setMainPictogramElement(rootContainerShapeForResourceElement);
		directEditingInfo.setPictogramElement(shape);
        directEditingInfo.setGraphicsAlgorithm(text);
	
        link(shape, dataObject);
        
		return setLocationAndSize(text, parentWidth, parentHeight, leftOffset, topOffset);
	}

	@Override
	public int calculateX(int parentWidth, int parentHeight, int leftOffset,
			int topOffset) {
		return 5;
	}

	@Override
	public int calculateY(int parentWidth, int parentHeight, int leftOffset,
			int topOffset) {
		return 5;
	}

	@Override
	public int calculateWidth(int parentWidth, int parentHeight,
			int leftOffset, int topOffset) {
		return parentWidth - 5;
	}

	@Override
	public int calculateHeight(int parentWidth, int parentHeight,
			int leftOffset, int topOffset) {		
		return 12;
	}

}
