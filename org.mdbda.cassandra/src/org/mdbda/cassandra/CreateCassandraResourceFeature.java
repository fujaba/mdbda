package org.mdbda.cassandra;

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.mdbda.diagrameditor.features.resources.CreateResourceFeature;
import org.mdbda.model.ModelFactory;
import org.mdbda.model.Resource; 
import org.osgi.framework.Bundle;

public class CreateCassandraResourceFeature extends CreateResourceFeature {

	public static final String TYPE_ID = "CassandraResource";




	public CreateCassandraResourceFeature(IFeatureProvider fp) {
		super(fp, name, description);
	}
	public static String name = "Cassandra Resource";
	public static String description = "Creates a new Cassandra Resource";
    
    


	@Override
	public Object[] create(ICreateContext context) {	
		Resource eInst = ModelFactory.eINSTANCE.createResource();
		
		initPattern(eInst, TYPE_ID);
		
		
		addToTargetBO(context,eInst);
		
		addGraphicalRepresentation(context, eInst);
		return new Object[] { eInst };
	}
	
	
	@Override
	public URL getDefaultConfigJSONFileLocation() {
		Bundle bundle = Platform.getBundle("org.mdbda.cassandra");
		URL fileURL = bundle.getEntry("/target/classes/org/mdbda/cassandra/CassandraConfig.json");
		return fileURL;
	}
}
