package org.mdbda.diagrameditor.features.resources;

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.mdbda.model.ModelFactory;
import org.mdbda.model.Resource;
import org.mdbda.model.ResourcesTemplateConstatns;
import org.osgi.framework.Bundle;

public class CreateHazelcastResourceFeature extends CreateResourceFeature {

	public CreateHazelcastResourceFeature(IFeatureProvider fp) {
		super(fp, name, description);
	}
	public static String name = "Hazelcast Resource";
	public static String description = "Creates a new Hazelcast Resource";
    
    


	@Override
	public Object[] create(ICreateContext context) {		
		Resource eInst = ModelFactory.eINSTANCE.createResource();
		
		initPattern(eInst, ResourcesTemplateConstatns.RESOURCETYPE_HAZELCAST);
		
		addToTargetBO(context,eInst);
		
		addGraphicalRepresentation(context, eInst);
		return new Object[] { eInst };
	}
	
	@Override
	public URL getDefaultConfigJSONFileLocation() {
		Bundle bundle = Platform.getBundle("org.mdbda.diagrameditor");
		URL fileURL = bundle.getEntry("/target/classes/org/mdbda/diagrameditor/features/resources/HazelcastConfig.json");
		return fileURL;
	}
}
