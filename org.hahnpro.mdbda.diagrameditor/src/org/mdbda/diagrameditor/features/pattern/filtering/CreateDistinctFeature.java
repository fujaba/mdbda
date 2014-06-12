package org.mdbda.diagrameditor.features.pattern.filtering;

import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.mdbda.model.ModelFactory;
import org.mdbda.model.Pattern;
import org.mdbda.diagrameditor.features.pattern.CreatePatternFeature;
import org.mdbda.model.FilteringPatternTemplateConstatns;

public class CreateDistinctFeature extends CreatePatternFeature implements
		ICreateFeature {
	public static String name = "Distinct";
	public static String description = "Creates a new distinct filter pattern";
    
    
	public CreateDistinctFeature(IFeatureProvider fp) {
		super(fp, name, description);
	}



	@Override
	public Object[] create(ICreateContext context) {
		// TODO: create the domain object here
		//Object newDomainObject = null;

		Pattern eInst = ModelFactory.eINSTANCE.createPattern();
		initPattern(eInst,FilteringPatternTemplateConstatns.Distinct);
		
		addToWorkflow(getWorkflow(context), eInst);
		
		// TODO: in case of an EMF object add the new object to a suitable resource
//		getDiagram().eResource().getContents().add(eInst);
		
		
		
		addGraphicalRepresentation(context, eInst);
		return new Object[] { eInst };
	}
	
	@Override
	public String getDefaultConfigJSONFileLocation() {
		return "/target/classes/org/mdbda/diagrameditor/features/pattern/filtering/DistinctConfig.json";
	}
}
