package org.mdbda.diagrameditor.utils;

import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IDirectEditingInfo;
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

public class NameShapeHelper extends AbstactMDBDAResourceShapeHelper {
	static final IColorConstant FOREGROUND_COLOR = IColorConstant.BLACK;

	@Override
	protected String getShapeId() {
		return "name";
	}
	
	public NameShapeHelper(Resource resource,
			ContainerShape rootContainerShapeForResourceElement, IFeatureProvider fp) {
		super(resource, rootContainerShapeForResourceElement,fp);
	}

	@Override
	public int calculateX(int parentWidth, int parentHeight, int leftOffset, int topOffset) {
		return AbstractMDBDAShape.INVISIBLE_RECT_SIDE + 7;
	}

	@Override
	public int calculateY(int parentWidth, int parentHeight, int leftOffset, int topOffset) {
		return topOffset;
	}

	@Override
	public int calculateWidth(int parentWidth, int parentHeight, int leftOffset, int topOffset) {
		return parentWidth -  16;
	}

	@Override
	public int calculateHeight(int parentWidth, int parentHeight, int leftOffset, int topOffset) {
		return parentHeight - 20 - 4 - 15;
	}
	

	@Override
	public IDimension addNewShapeOnContainer(int parentWidth, int parentHeight, int leftOffset, int topOffset) {
	
		Shape shape = createNewShapeOnRootContainer();
		IGaService gaService = Graphiti.getGaService();
		
		String name = resource.getName();
		if(name == null || "".equals(name)){
			name = "---";
		}
		MultiText text = gaService.createMultiText(shape, name );
		//IDimension calculateSize = gaService.calculateSize(text);			
		text.setForeground(manageColor(FOREGROUND_COLOR));
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_TOP);
		IDirectEditingInfo directEditingInfo =
                getFeatureProvider().getDirectEditingInfo();
		
		directEditingInfo.setMainPictogramElement(rootContainerShapeForResourceElement);
		directEditingInfo.setPictogramElement(shape);
        directEditingInfo.setGraphicsAlgorithm(text);
	
        link(shape, resource);
        
		return setLocationAndSize(text, parentWidth, parentHeight, leftOffset, topOffset);
	}
	
	@Override
	public boolean hasChanged() {
		MultiText t = (MultiText)getGraphicsAlgorithm();
		return resource != null && t != null && resource.getName() != null && !resource.getName().equals(t.getValue());
	}
	
	@Override
	public void update() {
		((MultiText)getGraphicsAlgorithm()).setValue(resource.getName());
	}
}
