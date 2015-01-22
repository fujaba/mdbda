package org.mdbda.diagrameditor.features;


import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;
import org.mdbda.model.RemoteWorkflow;
import org.mdbda.model.Workflow;
import org.mdbda.diagrameditor.pictogramelements.AbstractMDBDAShape;
import org.mdbda.diagrameditor.utils.AbstactMDBDAShapeHelper;
import org.mdbda.diagrameditor.utils.DiagramUtils;
import org.mdbda.diagrameditor.utils.NameShapeHelper;

public class AddWorkflowFeature extends AbstactMDBDAAddFeature implements
		IAddFeature {

	
	
	public AddWorkflowFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public Diagram getDiagram() {
		return super.getDiagram();
	}

	@Override
	public void link(PictogramElement pe, Object businessObject) {
		super.link(pe, businessObject);
	}

	@Override
	public Color manageColor(IColorConstant colorConstant) {
		return super.manageColor(colorConstant);
	}
	
	@Override
	public boolean canAdd(IAddContext context) {
		if (isRootWorkflow(context.getNewObject(),context.getTargetContainer())) {//root wf
				return true;
		} else if(isWorkflowReference(context.getNewObject(),context.getTargetContainer()) ) {// workflow in workflow
				return true;
		}
		return false;
	}

	public static boolean isWorkflowReference(Object obj, ContainerShape tgt ) {
		return obj instanceof RemoteWorkflow;
	}

	public static  boolean isRootWorkflow(Object obj, ContainerShape tgt) {
		return obj instanceof Workflow 
				&& tgt instanceof Diagram;
	}

	@Override
	public PictogramElement add(IAddContext context) {		
		if(isRootWorkflow(context.getNewObject(), context.getTargetContainer())){
			return addRootWF(context);
		}else if(isWorkflowReference(context.getNewObject(), context.getTargetContainer()) ) {// workflow in workflo
			return addWFRef(context);
		}
		return null;
		//return WorkflowShape.createWorkflowShape(context, this);
	}

	private ContainerShape addWFRef(IAddContext context) {
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		IGaService gaService = Graphiti.getGaService();
		
		
		RemoteWorkflow rwf = (RemoteWorkflow) context.getNewObject();
//		Workflow wf =  DiagramUtils.getMDBDADiagram(dia).getRootWorkflow();
		
		
		ContainerShape targetShape = context.getTargetContainer();
		
		int width =  60;
		int height = 50;
		ContainerShape rootContainerShape = peCreateService.createContainerShape(targetShape, true);
		
		Rectangle invisibleRectangle = gaService.createInvisibleRectangle(rootContainerShape);
		gaService.setLocationAndSize(invisibleRectangle, context.getX(), context.getY(), width + 2 * AbstractMDBDAShape.INVISIBLE_RECT_SIDE, height);
		
		RoundedRectangle roundedRectangle = gaService.createRoundedRectangle(invisibleRectangle, 5, 5);
		roundedRectangle.setBackground(manageColor(AbstractMDBDAShape.WORKFLOW_BACKGROUND));
		roundedRectangle.setForeground(manageColor(AbstractMDBDAShape.WORKFLOW_FOREGROUND));
		Graphiti.getPeService().setPropertyValue(roundedRectangle,AbstactMDBDAShapeHelper.SHAPE_KEY,ROUNDED_RECTANGLE_ID);
		gaService.setLocationAndSize(roundedRectangle, AbstractMDBDAShape.INVISIBLE_RECT_SIDE,0, width, height);

		
		
		link(rootContainerShape, rwf);
		
		NameShapeHelper nameHelper = new NameShapeHelper(rwf, rootContainerShape, getFeatureProvider());
		nameHelper.addNewShapeOnContainer(width, height);
		
		
		return rootContainerShape;
	}

	private ContainerShape addRootWF(IAddContext context) {
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		IGaService gaService = Graphiti.getGaService();
		
		Workflow wf = (Workflow) context.getNewObject();
		ContainerShape targetShape = context.getTargetContainer();
		
		int width =  600;
		int height = 500;
		ContainerShape rootContainerShape = peCreateService.createContainerShape(targetShape, true);
		RoundedRectangle roundedRectangle = gaService.createRoundedRectangle(rootContainerShape, 5, 5);
		roundedRectangle.setBackground(manageColor(AbstractMDBDAShape.WORKFLOW_BACKGROUND));
		roundedRectangle.setForeground(manageColor(AbstractMDBDAShape.WORKFLOW_FOREGROUND));
		roundedRectangle.setTransparency(0.42);
		Graphiti.getPeService().setPropertyValue(roundedRectangle,AbstactMDBDAShapeHelper.SHAPE_KEY,ROUNDED_RECTANGLE_ID);
		gaService.setLocationAndSize(roundedRectangle, context.getX(),context.getY(), width, height);

		link(rootContainerShape, wf);
		
		
		NameShapeHelper nameHelper = new NameShapeHelper(wf, rootContainerShape, getFeatureProvider());
		nameHelper.addNewShapeOnContainer(width, height);
		
		
		return rootContainerShape;
	}
}
