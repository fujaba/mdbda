package org.mdbda.diagrameditor.features.resources;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.mdbda.model.ModelFactory;
import org.mdbda.model.Resource;
import org.mdbda.model.ResourcesTemplateConstatns;

public class CreateNeo4jResourceFeature extends CreateResourceFeature {

	public CreateNeo4jResourceFeature(IFeatureProvider fp) {
		super(fp, name, description);
	}
	public static String name = "Neo4j Resource";
	public static String description = "Creates a new Neo4j Resource";
    
    


	@Override
	public Object[] create(ICreateContext context) {		
		Resource eInst = ModelFactory.eINSTANCE.createResource();
		
		initPattern(eInst, ResourcesTemplateConstatns.RESOURCETYPE_NEO4J);
		
		addToTargetBO(context, eInst);
		
		addGraphicalRepresentation(context, eInst);
		return new Object[] { eInst };
	}
	
	@Override
	public String getDefaultConfigJSONFileLocation() {
		return "/target/classes/org/mdbda/diagrameditor/features/resources/Neo4jConfig.json";
	}
}
