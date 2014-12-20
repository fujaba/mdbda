package org.mdbda.diagrameditor.utils;

import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.mm.algorithms.MultiText;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;
import org.mdbda.diagrameditor.pictogramelements.AbstractMDBDAShape;
import org.mdbda.model.Resource;

public class TestStatusShapeHelper extends AbstactMDBDAShapeHelper {
	static final String SHAPE_ID = "TestServerStatus";
	static final IColorConstant FOREGROUND_COLOR = IColorConstant.BLACK;
	
	public TestStatusShapeHelper(Resource resource,
			ContainerShape rootContainerShapeForResourceElement, IFeatureProvider fp) {
		super(resource, rootContainerShapeForResourceElement,fp);
	}

	@Override
	public int calculateX(int parentWidth, int parentHeight, int leftOffset, int topOffset) {
		return parentWidth	- AbstractMDBDAShape.INVISIBLE_RECT_SIDE - 3;
	}

	@Override
	public int calculateY(int parentWidth, int parentHeight, int leftOffset, int topOffset) {
		return  2;
	}

	@Override
	public int calculateWidth(int parentWidth, int parentHeight, int leftOffset, int topOffset) {
		return 20;
	}

	@Override
	public int calculateHeight(int parentWidth, int parentHeight, int leftOffset, int topOffset) {
		return 10;
	}
	
	@Override
	public IDimension addNewShapeOnContainer(int parentWidth, int parentHeight, int leftOffset, int topOffset) {
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		IGaService gaService = Graphiti.getGaService();

		Shape shape = peCreateService.createShape(
				rootContainerShapeForResourceElement, false);
		Graphiti.getPeService().setPropertyValue(shape, AbstactMDBDAShapeHelper.SHAPE_KEY,
				SHAPE_ID);

		Text text = gaService.createText(shape, "Test");
		// IDimension calculateSize = gaService.calculateSize(text);
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_TOP);

		text.setFont(StatusTextFont);;

		text.setFilled(true);

		boolean isOnline = (Math.random() > 0.5);
		if (isOnline) {
			text.setForeground(manageColor(IColorConstant.BLACK));
			text.setBackground(manageColor(IColorConstant.GREEN));
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
	           .getPropertyValue(child, AbstactMDBDAShapeHelper.SHAPE_KEY))){
				return child;
			}
		}
		return null;
	}

	

	int ping = 42;
	
	public boolean hasChanged() {
		return Math.random() < 0.9;
	}

	public void update() {
		ping = ServerStatusHelper.getStatus(this.resource);
		getShape().getGraphicsAlgorithm().setBackground(manageColor( ServerStatusHelper.getColor(ping) ));
	}

}