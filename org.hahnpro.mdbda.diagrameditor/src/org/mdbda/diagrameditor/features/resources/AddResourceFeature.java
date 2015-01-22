package org.mdbda.diagrameditor.features.resources;

import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.BoxRelativeAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;
import org.mdbda.model.MDBDADiagram;
import org.mdbda.model.Resource;
import org.mdbda.model.Workflow;
import org.mdbda.diagrameditor.features.AbstactMDBDAAddFeature;
import org.mdbda.diagrameditor.pictogramelements.AbstractMDBDAShape;
import org.mdbda.diagrameditor.utils.AbstactMDBDAShapeHelper;
import org.mdbda.diagrameditor.utils.DataDescriptionShapeHelper;
import org.mdbda.diagrameditor.utils.LiveStatusShapeHelper;
import org.mdbda.diagrameditor.utils.NameShapeHelper;
import org.mdbda.diagrameditor.utils.SampleDataShapeHelper;
import org.mdbda.diagrameditor.utils.TestStatusShapeHelper;
import org.mdbda.diagrameditor.utils.TypeIdShapeHelper;

public abstract class AddResourceFeature extends AbstactMDBDAAddFeature
		implements IAddFeature {

	public AddResourceFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		if (context.getNewObject() instanceof Resource) {
			if (getBusinessObjectForPictogramElement(context
					.getTargetContainer()) instanceof Workflow) {
				return true;
			}

			if (getBusinessObjectForPictogramElement(context
					.getTargetContainer()) instanceof MDBDADiagram) {
				return true;
			}
		}

		return false;
	}
	protected ColorConstant getBackgroundColor(){
	//	return  manageColor(new ColorConstant(127, 254, 165));//7FFEA5 (IColorConstant.LIGHT_BLUE);
		return new ColorConstant("99FFB7");//  manageColor(new ColorConstant(153, 255, 183));//99FFB7
	}
	
	@Override
	protected ColorConstant getForegroundColor() {
		return new ColorConstant("00661E");// manageColor(new ColorConstant(0,102,30));//(IColorConstant.BLUE);
	}
	@Override
	public PictogramElement add(IAddContext context) {

		ContainerShape targetShape = context.getTargetContainer();
		Resource resource = (Resource) context.getNewObject();

		int width = 100;
		int height = 80;

		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		IGaService gaService = Graphiti.getGaService();

		ContainerShape rootContainerShapeForResourceElement = peCreateService
				.createContainerShape(targetShape, true);
		RoundedRectangle roundedRectangle;

		{
			Rectangle invisibleRectangle = gaService
					.createInvisibleRectangle(rootContainerShapeForResourceElement);
			gaService.setLocationAndSize(invisibleRectangle, context.getX(),
					context.getY(), width + 2
							* AbstractMDBDAShape.INVISIBLE_RECT_SIDE, height);

			roundedRectangle = gaService.createRoundedRectangle(
					invisibleRectangle, AbstractMDBDAShape.INVISIBLE_RECT_SIDE,
					AbstractMDBDAShape.INVISIBLE_RECT_SIDE);
			roundedRectangle
					.setBackground(manageColor(getBackgroundColor()));
			roundedRectangle
					.setForeground(manageColor(getForegroundColor()));
			roundedRectangle.setTransparency(0.2);
			roundedRectangle.setLineWidth(2);
			
			Graphiti.getPeService().setPropertyValue(roundedRectangle, AbstactMDBDAShapeHelper.SHAPE_KEY , ROUNDED_RECTANGLE_ID);
			gaService.setLocationAndSize(roundedRectangle,
					AbstractMDBDAShape.INVISIBLE_RECT_SIDE, 0, width, height);

			// if added Class has no resource we add it to the resource
			// of the diagram
			// in a real scenario the business model would have its own resource
			if (resource.eResource() == null) {
				getDiagram().eResource().getContents().add(resource);
			}
		}
		// create link and wire it
		link(rootContainerShapeForResourceElement, resource);

//		int typeTextHeight = addTypeIdText(resource, width, height,
//				rootContainerShapeForResourceElement, 5).getHeight();
//		addNameText(resource, width, height,
	//			rootContainerShapeForResourceElement, typeTextHeight);
		IFeatureProvider fp =  getFeatureProvider();
		IDimension status = new TestStatusShapeHelper(resource, rootContainerShapeForResourceElement, fp).addNewShapeOnContainer(width, height);
		new LiveStatusShapeHelper(resource, rootContainerShapeForResourceElement, fp).addNewShapeOnContainer(width, height);
			
		IDimension typeIdDim = new TypeIdShapeHelper(resource, rootContainerShapeForResourceElement,fp).addNewShapeOnContainer(width, height, 0, status.getHeight());
		new NameShapeHelper(resource, rootContainerShapeForResourceElement,fp).addNewShapeOnContainer(width, height, 0, typeIdDim.getHeight()+status.getHeight());

		new SampleDataShapeHelper(resource, rootContainerShapeForResourceElement, fp).addNewShapeOnContainer(width, height);
		new DataDescriptionShapeHelper(resource, rootContainerShapeForResourceElement, fp).addNewShapeOnContainer(width, height);
		

		// Anchor

		addOutputAnchor(resource, rootContainerShapeForResourceElement,
				0.5, getDiagram(), getFeatureProvider());

		addInputAnchor(resource, rootContainerShapeForResourceElement,
				0.5, getDiagram(), getFeatureProvider());

		layoutPictogramElement(rootContainerShapeForResourceElement);
		return rootContainerShapeForResourceElement;
	}
}
