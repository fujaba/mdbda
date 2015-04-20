package org.mdbda.diagrameditor.features.resources;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.mdbda.model.MDBDAModelRoot;
import org.mdbda.model.Task;
import org.mdbda.model.Resource;
import org.mdbda.model.Workflow;
import org.mdbda.diagrameditor.features.AbstractCreateMDBDAFeature;

public abstract class CreateResourceFeature extends AbstractCreateMDBDAFeature {


	public CreateResourceFeature(IFeatureProvider fp, String name,
			String description) {
		super(fp, name, description);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canCreate(ICreateContext context) {	
		if( getBusinessObjectForPictogramElement(context.getTargetContainer()) instanceof Workflow ){
			return true;
		}
		if( getBusinessObjectForPictogramElement(context.getTargetContainer()) instanceof MDBDAModelRoot ){
			return true;
		}
		
		return false;		
	}

	protected void addToTargetBO(ICreateContext context,Resource eInst) {
		Object bo =  getBusinessObjectForPictogramElement(context.getTargetContainer());
		
		if(bo instanceof Workflow){
			((Workflow) bo).getInternalDataResources().add(eInst);
		}
		if(bo instanceof MDBDAModelRoot){
			((MDBDAModelRoot) bo).getResources().add(eInst);
		}
	}
	
	protected void addToWorkflow(Workflow wf, Task eInst) {
		//wf.eResource().getContents().add(eInst);
		eInst.setWorkflow(wf);
	}
	

}
