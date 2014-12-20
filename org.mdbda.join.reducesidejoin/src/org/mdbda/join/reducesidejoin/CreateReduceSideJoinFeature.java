package org.mdbda.join.reducesidejoin;

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.mdbda.model.ModelFactory;
import org.mdbda.model.Task;
import org.mdbda.diagrameditor.features.task.CreateTaskFeature;
import org.osgi.framework.Bundle;

public class CreateReduceSideJoinFeature extends CreateTaskFeature implements
		ICreateFeature {
	public static String name = "Reduce side join";
	public static String description = "Creates a new reduce side join pattern";
    
    
	public CreateReduceSideJoinFeature(IFeatureProvider fp) {
		super(fp, name, description);
	}



	@Override
	public Object[] create(ICreateContext context) {
		// TODO: create the domain object here
		//Object newDomainObject = null;
		
		Task eInst = ModelFactory.eINSTANCE.createTask();
		initPattern(eInst,"ReduceSideJoin");
		eInst.setMaxInputCount(2);
		
		addToWorkflow(getWorkflow(context), eInst);
		
		// TODO: in case of an EMF object add the new object to a suitable resource
//		getDiagram().eResource().getContents().add(eInst);
		
		
		
		addGraphicalRepresentation(context, eInst);
		return new Object[] { eInst };
	}
	
	@Override
	public URL getDefaultConfigJSONFileLocation() {
		Bundle bundle = Platform.getBundle("org.mdbda.join.reducesidejoin");
		URL fileURL = bundle.getEntry("/bin/org/mdbda/join/reducesidejoin/ReduceSideConfig.json");
		return fileURL;
	}
}
