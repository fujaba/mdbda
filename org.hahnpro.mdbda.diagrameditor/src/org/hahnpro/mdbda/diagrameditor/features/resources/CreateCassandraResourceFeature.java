package org.hahnpro.mdbda.diagrameditor.features.resources;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.hahnpro.mdbda.model.pattern.join.CartesianProduct;
import org.hahnpro.mdbda.model.pattern.join.JoinFactory;
import org.hahnpro.mdbda.model.resources.CassandraResource;
import org.hahnpro.mdbda.model.resources.ResourcesFactory;

public class CreateCassandraResourceFeature extends CreateResourceFeature {

	public CreateCassandraResourceFeature(IFeatureProvider fp) {
		super(fp, name, description);
	}
	public static String name = "Cassandra Resource";
	public static String description = "Creates a new Cassandra Resource";
    
    


	@Override
	public Object[] create(ICreateContext context) {		
		CassandraResource eInst = ResourcesFactory.eINSTANCE.createCassandraResource();

		getWorkflow(context).getResources().add(eInst);
		
		addGraphicalRepresentation(context, eInst);
		return new Object[] { eInst };
	}

}
