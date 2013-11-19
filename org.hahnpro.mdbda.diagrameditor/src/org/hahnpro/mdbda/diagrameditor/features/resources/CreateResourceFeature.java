package org.hahnpro.mdbda.diagrameditor.features.resources;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.hahnpro.mdbda.diagrameditor.features.AbstractCreateMDBDAFeature;
import org.hahnpro.mdbda.model.Pattern;
import org.hahnpro.mdbda.model.Workflow;

public abstract class CreateResourceFeature extends AbstractCreateMDBDAFeature {


	public CreateResourceFeature(IFeatureProvider fp, String name,
			String description) {
		super(fp, name, description);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canCreate(ICreateContext context) {	
		return getWorkflow(context) != null;		
	}

	protected Workflow getWorkflow(ICreateContext context) {
		Object bo =  getBusinessObjectForPictogramElement(context.getTargetContainer());
		
		if(bo instanceof Workflow) return (Workflow) bo;
		return null;
		
	}
	
	protected void addToWorkflow(Workflow wf, Pattern eInst) {
		//wf.eResource().getContents().add(eInst);
		eInst.setWorkflow(wf);
	}
}
