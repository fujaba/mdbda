package org.mdbda.diagrameditor.utils;

import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.impl.AbstractFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.styles.Font;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;

public abstract class AbstractSimpleMDBDAShapeHelper extends AbstractFeature {

	protected ContainerShape rootContainerShapeForResourceElement;
	protected Font ButtonTextFont = Graphiti.getGaService().manageFont(getDiagram(), "Arial", 6, false, false);
	protected Font StatusTextFont = Graphiti.getGaService().manageFont(getDiagram(), "Arial", 6, false, true);
	public static final String SHAPE_KEY = "shape-id";

	public AbstractSimpleMDBDAShapeHelper(ContainerShape rootContainerShapeForResourceElement, IFeatureProvider fp) {
		super(fp);
		this.rootContainerShapeForResourceElement = rootContainerShapeForResourceElement;
	}
	
	protected Shape createNewShapeOnRootContainer(){
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		
		Shape shape = peCreateService.createShape(rootContainerShapeForResourceElement, false);
		Graphiti.getPeService().setPropertyValue(shape,SHAPE_KEY, getShapeId());
		
		return shape;
	}

	public abstract IDimension addNewShapeOnContainer(int parentWidth, int parentHeight,
			int leftOffset, int topOffset);

	public IDimension addNewShapeOnContainer(int parentWidth, int parentHeight) {
		return addNewShapeOnContainer(parentWidth,parentHeight,0,0);
	}

	protected abstract String getShapeId();
	
	public Shape getShape() {
		if(rootContainerShapeForResourceElement == null) return null;
		for(Shape child : rootContainerShapeForResourceElement.getChildren()){
			if(getShapeId().equals( Graphiti.getPeService()
	           .getPropertyValue(child, SHAPE_KEY))){
				return child;
			}
		}
		return null;
	}

	public GraphicsAlgorithm getGraphicsAlgorithm() {
		Shape s = getShape( );
		if(s!=null){
			return s.getGraphicsAlgorithm();
		}
		return null;
	}

	public abstract int calculateX(int parentWidth, int parentHeight, int leftOffset,
			int topOffset);

	public abstract int calculateY(int parentWidth, int parentHeight, int leftOffset,
			int topOffset);

	public abstract int calculateWidth(int parentWidth, int parentHeight,
			int leftOffset, int topOffset);

	public abstract int calculateHeight(int parentWidth, int parentHeight,
			int leftOffset, int topOffset);

	public IDimension resize(int parentWidth, int parentHeight) {
		return resize(parentWidth, parentHeight, 0, 0);
	}

	public IDimension resize(int parentWidth, int parentHeight, int leftOffset, int topOffset) {
		return setLocationAndSize(getGraphicsAlgorithm(), parentWidth, parentHeight, leftOffset, topOffset);
	}

	protected IDimension setLocationAndSize(GraphicsAlgorithm ga, int parentWidth, int parentHeight,
			int leftOffset, int topOffset) {
					if(ga != null){
			//			ga.setX(calculateX(parentWidth, parentHeight, leftOffset, topOffset));
			//			ga.setY(calculateY(parentWidth, parentHeight, leftOffset, topOffset));
			//			ga.setWidth(calculateWidth(parentWidth, parentHeight, leftOffset, topOffset));
			//			ga.setHeight(calculateHeight(parentWidth, parentHeight, leftOffset, topOffset));
			//			
						IGaService gaService = Graphiti.getGaService();
						gaService.setLocationAndSize(ga, calculateX(parentWidth, parentHeight, leftOffset, topOffset), 
								calculateY(parentWidth, parentHeight, leftOffset, topOffset),
								calculateWidth(parentWidth, parentHeight, leftOffset, topOffset), 
								calculateHeight(parentWidth, parentHeight, leftOffset, topOffset));
						
						return gaService.calculateSize(ga);
					}
					
					return null;
				}

	public boolean hasChanged() {
		return false;
	}

	public void update() {		
	}

	@Override
	public boolean canExecute(IContext context) {
		return true;
	}

	@Override
	public void execute(IContext context) {		
	}

}