package org.hahnpro.mdbda.diagrameditor.features.resources;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.hahnpro.mdbda.model.ModelFactory;
import org.hahnpro.mdbda.model.Resource;

public class CreateCassandraResourceFeature extends CreateResourceFeature {

	public CreateCassandraResourceFeature(IFeatureProvider fp) {
		super(fp, name, description);
	}
	public static String name = "Cassandra Resource";
	public static String description = "Creates a new Cassandra Resource";
    
    


	@Override
	public Object[] create(ICreateContext context) {	
		Resource eInst = ModelFactory.eINSTANCE.createResource();
		eInst.setTypeId(ResourceGroupConfigurator.RESOURCETYPE_CASSANDRA);
		
		getWorkflow(context).getDataResources().add(eInst);
		
		addGraphicalRepresentation(context, eInst);
		return new Object[] { eInst };
	}

}
