package org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization;

import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.hahnpro.mdbda.diagrameditor.features.pattern.CreatePatternFeature;
import org.hahnpro.mdbda.model.pattern.dataorganization.Binning;
import org.hahnpro.mdbda.model.pattern.dataorganization.DataorganizationFactory;
import org.hahnpro.mdbda.model.pattern.dataorganization.Partitioning;
import org.hahnpro.mdbda.model.workflow.Workflow;
import org.hahnpro.mdbda.model.workflow.WorkflowFactory;

public class CreatePartitoningFeature extends CreatePatternFeature implements
		ICreateFeature {
   
	public static String name = "Partitoning";
	public static String description = "Creates a new partitoning pattern";
    
    
	public CreatePartitoningFeature(IFeatureProvider fp) {
		super(fp, name, description);
	}
	@Override
	public Object[] create(ICreateContext context) {
		// TODO: create the domain object here
		//Object newDomainObject = null;

		Partitioning eInst = DataorganizationFactory.eINSTANCE.createPartitioning();
		
		addToWorkflow(getWorkflow(context), eInst);
		
		// TODO: in case of an EMF object add the new object to a suitable resource
		getDiagram().eResource().getContents().add(eInst);
		
		
		
		addGraphicalRepresentation(context, eInst);
		return new Object[] { eInst };
	}
}
