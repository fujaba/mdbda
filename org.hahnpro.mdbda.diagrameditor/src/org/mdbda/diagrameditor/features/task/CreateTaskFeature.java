package org.mdbda.diagrameditor.features.task;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Path;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.mdbda.model.Task;
import org.mdbda.model.Workflow;
import org.mdbda.diagrameditor.features.AbstactIOMDBDAAddFeature;
import org.mdbda.diagrameditor.features.AbstractCreateMDBDAFeature;

public abstract class CreateTaskFeature extends AbstractCreateMDBDAFeature {


	public CreateTaskFeature(IFeatureProvider fp, String name,
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
	
	protected void addToWorkflow(Workflow wf, Task eInst) {
		//wf.eResource().getContents().add(eInst);
		eInst.setWorkflow(wf);
	}
}
