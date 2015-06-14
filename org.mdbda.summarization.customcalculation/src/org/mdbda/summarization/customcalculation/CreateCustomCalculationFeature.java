package org.mdbda.summarization.customcalculation;

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.mdbda.model.ModelFactory;
import org.mdbda.model.Task;
import org.mdbda.diagrameditor.features.task.CreateTaskFeature;
import org.osgi.framework.Bundle;

public class CreateCustomCalculationFeature extends CreateTaskFeature implements
		ICreateFeature {
	public static String name = "Custom Calculation";
	public static String description = "Creates a new Custom Calculation pattern";
    
    
	public CreateCustomCalculationFeature(IFeatureProvider fp) {
		super(fp, name, description);
	}



	@Override
	public Object[] create(ICreateContext context) {
		// TODO: create the domain object here
		//Object newDomainObject = null;

		Task eInst = ModelFactory.eINSTANCE.createTask();
		initPattern(eInst,"CustomCalculation");

		addToWorkflow(getWorkflow(context), eInst);
		
		// TODO: in case of an EMF object add the new object to a suitable resource
//		getDiagram().eResource().getContents().add(eInst);
		
		
		
		addGraphicalRepresentation(context, eInst);
		return new Object[] { eInst };
	}
	
	@Override
	public URL getDefaultConfigJSONFileLocation() {
		Bundle bundle = Platform.getBundle("org.mdbda.summarization.customcalculation");
		URL fileURL = bundle.getEntry("/target/classes/org/mdbda/summarization/customcalculation/CustomCalculationConfig.json");
		return fileURL;
	}
}
