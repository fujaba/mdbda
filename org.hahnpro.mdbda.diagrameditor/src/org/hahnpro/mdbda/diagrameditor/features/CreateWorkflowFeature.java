package org.hahnpro.mdbda.diagrameditor.features;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.hahnpro.mdbda.model.workflow.Workflow;
import org.hahnpro.mdbda.model.workflow.WorkflowFactory;

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

	@Override
	public Object[] create(ICreateContext context) {
		// TODO: create the domain object here
		// Object newDomainObject = null;
		Resource resource = context.getTargetContainer().eResource();
		Workflow workflow = WorkflowFactory.eINSTANCE.createWorkflow();

		// TODO: in case of an EMF object add the new object to a suitable
		// resource
		// getDiagram().eResource().getContents().add(workflow);
		resource.getContents().add(workflow);

		addGraphicalRepresentation(context, workflow);
		return new Object[] { workflow };
	}
}
