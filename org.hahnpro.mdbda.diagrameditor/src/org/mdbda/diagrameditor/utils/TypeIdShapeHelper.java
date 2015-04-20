package org.mdbda.diagrameditor.utils;

import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.algorithms.styles.TextStyle;
import org.eclipse.graphiti.mm.algorithms.styles.TextStyleRegion;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;
import org.mdbda.diagrameditor.pictogramelements.AbstractMDBDAShape;
import org.mdbda.model.Resource;

public class TypeIdShapeHelper  extends AbstactMDBDAResourceShapeHelper {

	static final IColorConstant FOREGROUND_COLOR = IColorConstant.DARK_BLUE;
	@Override
	protected String getShapeId() {
		return "type";
	}
	public TypeIdShapeHelper(Resource resource,
			ContainerShape rootContainerShapeForResourceElement,
			IFeatureProvider fp) {
		super(resource, rootContainerShapeForResourceElement, fp);
	}


	@Override
	public IDimension addNewShapeOnContainer(int parentWidth, int parentHeight,
			int leftOffset, int topOffset) {
		Shape shape = createNewShapeOnRootContainer();
		IGaService gaService = Graphiti.getGaService();
			
		Text text = gaService.createText(shape, resource.getTypeId() );
		
		text.setForeground(manageColor(FOREGROUND_COLOR));
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_TOP);
		text.setFont(gaService.manageFont(getDiagram(), "Arial", 8, false, true));
		
		TextStyleRegion textStyleRegion = gaService.createTextStyleRegion(text);
		TextStyle textStyle = gaService.createTextStyle(textStyleRegion);
		textStyle.setUnderline(true);	
		
		return setLocationAndSize(text, parentWidth, parentHeight, leftOffset, topOffset);
	}

	@Override
	public int calculateX(int parentWidth, int parentHeight, int leftOffset,
			int topOffset) {
		return AbstractMDBDAShape.INVISIBLE_RECT_SIDE + 2 ;
	}

	@Override
	public int calculateY(int parentWidth, int parentHeight, int leftOffset,
			int topOffset) {
		return 2 + topOffset;
	}

	@Override
	public int calculateWidth(int parentWidth, int parentHeight,
			int leftOffset, int topOffset) {
		return parentWidth - 4;
	}

	@Override
	public int calculateHeight(int parentWidth, int parentHeight,
			int leftOffset, int topOffset) {
		return 20;
	}

}
