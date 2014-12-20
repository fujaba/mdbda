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

public class SampleDataShapeHelper  extends AbstactMDBDAShapeHelper {
	static final String SHAPE_ID = "ExampleData";
	static final IColorConstant FOREGROUND_COLOR = IColorConstant.DARK_BLUE;

	public SampleDataShapeHelper(Resource resource,
			ContainerShape rootContainerShapeForResourceElement,
			IFeatureProvider fp) {
		super(resource, rootContainerShapeForResourceElement, fp);
	}


	@Override
	public IDimension addNewShapeOnContainer(int parentWidth, int parentHeight,
			int leftOffset, int topOffset) {
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		IGaService gaService = Graphiti.getGaService();

		Shape shape = peCreateService.createShape(
				rootContainerShapeForResourceElement, false);
		Graphiti.getPeService().setPropertyValue(shape, SHAPE_KEY,
				SHAPE_ID);

		Text text = gaService.createText(shape, "Sample Data");
		// IDimension calculateSize = gaService.calculateSize(text);
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);

		text.setFont(ButtonTextFont);

		text.setFilled(true);

		boolean isOnline = (Math.random() > 0.5);
		if (isOnline) {
			text.setForeground(manageColor(IColorConstant.BLACK));
			text.setBackground(manageColor(IColorConstant.LIGHT_GRAY));
		} else {
			text.setForeground(manageColor(IColorConstant.BLACK));
			text.setBackground(manageColor(IColorConstant.RED));
		}


		return setLocationAndSize(text, parentWidth, parentHeight, leftOffset, topOffset);
	}

	@Override
	public Shape getShape() {
		if(rootContainerShapeForResourceElement == null) return null;
		for(Shape child : rootContainerShapeForResourceElement.getChildren()){
			if(SHAPE_ID.equals( Graphiti.getPeService()
	           .getPropertyValue(child, SHAPE_KEY))){
				return child;
			}
		}
		return null;
	}

	@Override
	public int calculateX(int parentWidth, int parentHeight, int leftOffset,
			int topOffset) {
		return AbstractMDBDAShape.INVISIBLE_RECT_SIDE + 4 + parentWidth / 2 ;
	}

	@Override
	public int calculateY(int parentWidth, int parentHeight, int leftOffset,
			int topOffset) {
		return parentHeight - 13; 
	}

	@Override
	public int calculateWidth(int parentWidth, int parentHeight,
			int leftOffset, int topOffset) {
		return parentWidth / 2 - 6;
	}

	@Override
	public int calculateHeight(int parentWidth, int parentHeight,
			int leftOffset, int topOffset) {
		return 10;
	}

}
