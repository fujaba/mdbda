package org.mdbda.diagrameditor.features;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.mdbda.model.MDBDADiagram;
import org.mdbda.model.ModelFactory;
import org.mdbda.model.Workflow;
import org.mdbda.diagrameditor.utils.DiagramUtils;

public class CreateWorkflowFeature extends AbstractCreateMDBDAFeature implements
		ICreateFeature {
	public static final String name = "Workflow";

	private static final String TITLE = "Create workflow";

	private static final String USER_QUESTION = "Enter new workflow name";

	public CreateWorkflowFeature(IFeatureProvider fp) {
		super(fp, name, "Creates a new MDBDA Workflow");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		if (context.getTargetContainer() instanceof Diagram) {
			// Add new workflow only in case of an empty diagram
			return context.getTargetContainer().getChildren().size() == 0;
		}
		if (context.getTargetContainer().getLink() != null) {
			EList<EObject> businessObjects = context.getTargetContainer()
					.getLink().getBusinessObjects();
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
		return false;
	}
	
	public static final String DIALOG_TITLE = "Create new workflow diagram or open diagram";

	@Override
	public Object[] create(ICreateContext context) {
		
		ContainerShape targetDiagram = context.getTargetContainer();
		
		boolean isRootWorkflow = targetDiagram instanceof Diagram;
		

		if (!isRootWorkflow) {
			// ask new Diagram or open Diagram

			MessageDialog dialog = new MessageDialog(new Shell(
					Display.getDefault()),
					DIALOG_TITLE , null,
					"Open workflow MDBDA diagram or create a new one",
					MessageDialog.QUESTION, new String[] { "new", "open" }, 0);
			int res = dialog.open();
			Diagram refDiagram = null;
			if (res == 0) {// new
				refDiagram = DiagramUtils.newDiagramDialog();
				
			} else {// open
				refDiagram = DiagramUtils.openDiagramDialog(getDiagram());
			}
			
			MDBDADiagram mdbdaDiagram = DiagramUtils.getMDBDADiagram(refDiagram);

			addGraphicalRepresentation(context, refDiagram);
			
			return new Object[] { mdbdaDiagram.getRootWorkflow() };

		}else{
			
			MDBDADiagram mdbdaDiagram = DiagramUtils.getMDBDADiagram(getDiagram());
			Resource resource = context.getTargetContainer().eResource();//TODO get model resource
			
			Workflow workflow = ModelFactory.eINSTANCE.createWorkflow();

			resource.getContents().add(workflow);

			addGraphicalRepresentation(context, workflow);
			
			return new Object[] { workflow };
		}

	}
}
