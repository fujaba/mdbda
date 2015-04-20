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

public class LiveStatusShapeHelper extends AbstactMDBDAResourceShapeHelper {
	static final IColorConstant FOREGROUND_COLOR = IColorConstant.BLACK;

	@Override
	protected String getShapeId() {
		return "LiveServerStatus";
	}
	public LiveStatusShapeHelper(Resource resource,
			ContainerShape rootContainerShapeForResourceElement, IFeatureProvider fp) {
		super(resource, rootContainerShapeForResourceElement,fp);
	}

	@Override
	public int calculateX(int parentWidth, int parentHeight, int leftOffset, int topOffset) {
		return AbstractMDBDAShape.INVISIBLE_RECT_SIDE + 3;
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
	
		Shape shape = createNewShapeOnRootContainer();
		IGaService gaService = Graphiti.getGaService();
	
		Text text = gaService.createText(shape, "Live" );
		//IDimension calculateSize = gaService.calculateSize(text);			
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_TOP);			

		text.setFont(Graphiti.getGaService().manageFont(getDiagram(), "Arial", 6, false, true));//(StatusTextFont);
		
		text.setFilled(true);
		
//		boolean isOnline = (Math.random() > 0.5);
//		if(isOnline){
//			text.setForeground(manageColor(IColorConstant.BLACK));
//			text.setBackground(manageColor(IColorConstant.GREEN));
//		}else{
			text.setForeground(manageColor(IColorConstant.BLACK));
			text.setBackground(manageColor(IColorConstant.RED));
//		}
		
	
		return setLocationAndSize(text, parentWidth, parentHeight, leftOffset, topOffset);
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
