package org.mdbda.diagrameditor.features.pattern.join;

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.mdbda.model.ModelFactory;
import org.mdbda.model.Pattern;
import org.mdbda.diagrameditor.features.pattern.CreatePatternFeature;
import org.mdbda.model.JoinPatternTemplateConstatns;
import org.osgi.framework.Bundle;

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

		Pattern eInst = ModelFactory.eINSTANCE.createPattern();
		initPattern(eInst,JoinPatternTemplateConstatns.CartesianProduct);

		addToWorkflow(getWorkflow(context), eInst);
		
		// TODO: in case of an EMF object add the new object to a suitable resource
//		getDiagram().eResource().getContents().add(eInst);
		
		
		
		addGraphicalRepresentation(context, eInst);
		return new Object[] { eInst };
	}
	
	@Override
	public URL getDefaultConfigJSONFileLocation() {
		Bundle bundle = Platform.getBundle("org.mdbda.diagrameditor");
		URL fileURL = bundle.getEntry("/target/classes/org/mdbda/diagrameditor/features/pattern/join/CartesianProductConfig.json");
		return fileURL;
	}
}
