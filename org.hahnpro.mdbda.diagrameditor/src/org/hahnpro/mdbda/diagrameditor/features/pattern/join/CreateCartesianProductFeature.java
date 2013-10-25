package org.hahnpro.mdbda.diagrameditor.features.pattern.join;

import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.hahnpro.mdbda.diagrameditor.features.pattern.CreatePatternFeature;
import org.hahnpro.mdbda.model.pattern.dataorganization.Binning;
import org.hahnpro.mdbda.model.pattern.dataorganization.DataorganizationFactory;
import org.hahnpro.mdbda.model.pattern.filtering.BloomFiltering;
import org.hahnpro.mdbda.model.pattern.filtering.Distinct;
import org.hahnpro.mdbda.model.pattern.filtering.FilteringFactory;
import org.hahnpro.mdbda.model.pattern.join.CartesianProduct;
import org.hahnpro.mdbda.model.pattern.join.JoinFactory;
import org.hahnpro.mdbda.model.pattern.join.JoinPackage;
import org.hahnpro.mdbda.model.workflow.Workflow;

public class CreateCartesianProductFeature extends CreatePatternFeature implements
		ICreateFeature {
	public static String name = "Cartesian Product";
	public static String description = "Creates a new cartesian product join pattern";
    
    
	public CreateCartesianProductFeature(IFeatureProvider fp) {
		super(fp, name, description);
	}



	@Override
	public Object[] create(ICreateContext context) {
		// TODO: create the domain object here
		//Object newDomainObject = null;
		
		CartesianProduct eInst = JoinFactory.eINSTANCE.createCartesianProduct();

		addToWorkflow(getWorkflow(context), eInst);
		
		// TODO: in case of an EMF object add the new object to a suitable resource
//		getDiagram().eResource().getContents().add(eInst);
		
		
		
		addGraphicalRepresentation(context, eInst);
		return new Object[] { eInst };
	}




}
