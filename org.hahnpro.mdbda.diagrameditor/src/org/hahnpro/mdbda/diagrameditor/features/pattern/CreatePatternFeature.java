package org.hahnpro.mdbda.diagrameditor.features.pattern;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;
import org.hahnpro.mdbda.diagrameditor.features.AbstractCreateMDBDAFeature;
import org.hahnpro.mdbda.model.pattern.Pattern;
import org.hahnpro.mdbda.model.pattern.dataorganization.Binning;
import org.hahnpro.mdbda.model.workflow.Workflow;

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
		wf.eResource().getContents().add(eInst);
		eInst.setWorkflow(wf);
	}
}
