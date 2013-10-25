package org.hahnpro.mdbda.diagrameditor.features;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.hahnpro.mdbda.diagrameditor.utils.DiagramUtils;
import org.hahnpro.mdbda.model.workflow.Workflow;

public class AddWorkflowFeature extends AbstractAddFeature implements
		IAddFeature {
	private static final IColorConstant WORKFLOW_TEXT_FOREGROUND = IColorConstant.BLACK;

	private static final IColorConstant ROOTWORKFLOW_FOREGROUND = new ColorConstant(
			98, 131, 167);

	private static final IColorConstant ROOTWORKFLOW_BACKGROUND = new ColorConstant(
			187, 218, 247);

	private static final IColorConstant WORKFLOW_FOREGROUND = new ColorConstant(
			80, 222, 99);

	private static final IColorConstant WORKFLOW_BACKGROUND = new ColorConstant(
			83, 230, 100);

	public AddWorkflowFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		if (context.getNewObject() instanceof Workflow) {
			if (context.getTargetContainer() instanceof Diagram) {
				return true;
			} else {// workflow in workflow
				if (context.getTargetContainer().getLink() != null) {
					EList<EObject> businessObjects = context
							.getTargetContainer().getLink()
							.getBusinessObjects();
					boolean workflow = false;
					for (EObject eo : businessObjects) {
						if (eo instanceof Workflow) {
							workflow = true;
							break;
						}
					}

					if (workflow) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public PictogramElement add(IAddContext context) {

		ContainerShape targetDiagram = context.getTargetContainer();
		Workflow workflow = null;
		Diagram refDiagram = null;
		boolean isRootWorkflow = false;
		if(context.getNewObject() instanceof Workflow){
			refDiagram = getDiagram();
			workflow = (Workflow) context.getNewObject();
			isRootWorkflow = true;
		}else if(context.getNewObject() instanceof Diagram){
			refDiagram = (Diagram) context.getNewObject() ;
			workflow = DiagramUtils.getMDBDADiagram(refDiagram).getWorkflow();

			isRootWorkflow = false;
		}
		
		
//		if (!isRootWorkflow) {
//			// ask new Diagram or open Diagram
//
//			MessageDialog dialog = new MessageDialog(new Shell(
//					Display.getDefault()),
//					"Create new workflow diagram or open diagram", null,
//					"Open workflow MDBDA diagram or create a new one",
//					MessageDialog.QUESTION, new String[] { "new", "open" }, 0);
//			int res = dialog.open();
//
//			if (res == 0) {// new
//				refDiagram = DiagramUtils.newDiagramDialog();
//				
//			} else {// open
//				refDiagram = DiagramUtils.openDiagramDialog(getDiagram());
//			}
//
//		}else{
//		}

		int width = isRootWorkflow ? 600 : 60;
		int height = isRootWorkflow ? 500 : 50;

		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		IGaService gaService = Graphiti.getGaService();

		ContainerShape containerShape = peCreateService.createContainerShape(
				targetDiagram, true);

		RoundedRectangle roundedRectangle;
		{
			roundedRectangle = gaService.createRoundedRectangle(containerShape,
					5, 5);
			roundedRectangle
					.setBackground(manageColor(isRootWorkflow ? ROOTWORKFLOW_BACKGROUND
							: WORKFLOW_BACKGROUND));
			roundedRectangle
					.setForeground(manageColor(isRootWorkflow ? ROOTWORKFLOW_FOREGROUND
							: WORKFLOW_FOREGROUND));
			gaService.setLocationAndSize(roundedRectangle, context.getX(),
					context.getY(), width, height);

			// if added Class has no resource we add it to the resource
			// of the diagram
			// in a real scenario the business model would have its own resource
			if (workflow.eResource() == null) { // TODO: ??
				getDiagram().eResource().getContents().add(workflow);
			}

			link(containerShape, workflow);
			link(getDiagram(), refDiagram);
		}

		// SHAPE WITH LINE
		{
			// create shape for line
			Shape shape = peCreateService.createShape(containerShape, false);

			// create and set graphics algorithm
			Polyline polyline = gaService.createPolyline(shape, new int[] { 0,
					20, width, 20 });
			polyline.setForeground(manageColor(isRootWorkflow ? ROOTWORKFLOW_FOREGROUND
					: WORKFLOW_FOREGROUND));
			polyline.setLineWidth(4);
		}

		// SHAPE WITH TEXT
		{
			// create shape for text
			Shape shape = peCreateService.createShape(containerShape, false);

			// create and set text graphics algorithm
			Text text = gaService.createText(shape, workflow.getName()
					+ " : Workflow");
			text.setForeground(manageColor(WORKFLOW_TEXT_FOREGROUND));
			text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
			// vertical alignment has as default value "center"
			text.setFont(gaService.manageDefaultFont(getDiagram(), false, true));
			gaService.setLocationAndSize(text, 0, 0, width, 20);

			// create link and wire it
			link(shape, workflow);
		}

		// peCreateService.createChopboxAnchor(containerShape);

		return containerShape;
	}
}
