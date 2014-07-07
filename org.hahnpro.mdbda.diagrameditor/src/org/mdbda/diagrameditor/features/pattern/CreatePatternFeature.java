package org.mdbda.diagrameditor.features.pattern;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Path;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.mdbda.model.Pattern;
import org.mdbda.model.Workflow;
import org.mdbda.diagrameditor.features.AbstactMDBDAAddFeature;
import org.mdbda.diagrameditor.features.AbstractCreateMDBDAFeature;

public abstract class CreatePatternFeature extends AbstractCreateMDBDAFeature {


	public CreatePatternFeature(IFeatureProvider fp, String name,
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
