package org.mdbda.diagrameditor.features.resources;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.mdbda.model.ModelFactory;
import org.mdbda.model.Resource;
import org.mdbda.model.ResourcesTemplateConstatns;

public class CreateGenericResourceFeature extends CreateResourceFeature {

	public CreateGenericResourceFeature(IFeatureProvider fp) {
		super(fp, name, description);
	}
	public static String name = "Generic Resource";
	public static String description = "Creates a new Generic Resource";
    
    


	@Override
	public Object[] create(ICreateContext context) {		
		Resource eInst = ModelFactory.eINSTANCE.createResource();

		initPattern(eInst, ResourcesTemplateConstatns.RESOURCETYPE_GENERIC);
		
		addToTargetBO(context,eInst);
		
		addGraphicalRepresentation(context, eInst);
		return new Object[] { eInst };
	}
	
	@Override
	public String getDefaultConfigJSONFileLocation() {
		return "/target/classes/org/mdbda/diagrameditor/features/resources/GenericConfig.json";
	}
}
