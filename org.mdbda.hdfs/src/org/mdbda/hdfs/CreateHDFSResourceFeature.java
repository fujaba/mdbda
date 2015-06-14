package org.mdbda.hdfs;

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.mdbda.diagrameditor.features.resources.CreateResourceFeature;
import org.mdbda.model.ModelFactory;
import org.mdbda.model.Resource; 
import org.osgi.framework.Bundle;

public class CreateHDFSResourceFeature extends CreateResourceFeature {

	public CreateHDFSResourceFeature(IFeatureProvider fp) {
		super(fp, name, description);
	}
	public static String name = "HDFS Resource";
	public static String description = "Creates a new HDFS Resource";
    
    


	@Override
	public Object[] create(ICreateContext context) {		
		Resource eInst = ModelFactory.eINSTANCE.createResource();

		initPattern(eInst, "HDFSResource");
		
		addToTargetBO(context,eInst);
		
		addGraphicalRepresentation(context, eInst);
		return new Object[] { eInst };
	}
	
	@Override
	public URL getDefaultConfigJSONFileLocation() {
		Bundle bundle = Platform.getBundle("org.mdbda.hdfs");
		URL fileURL = bundle.getEntry("/target/classes/org/mdbda/hdfs/HDFSConfig.json");
		return fileURL;
	}
}
