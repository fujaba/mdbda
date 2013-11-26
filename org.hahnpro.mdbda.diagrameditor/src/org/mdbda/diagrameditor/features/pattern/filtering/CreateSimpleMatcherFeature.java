package org.mdbda.diagrameditor.features.pattern.filtering;

import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.mdbda.model.ModelFactory;
import org.mdbda.model.Pattern;
import org.mdbda.diagrameditor.features.pattern.CreatePatternFeature;
import org.mdbda.model.FilteringPatternTemplateConstatns;

public class CreateSimpleMatcherFeature extends CreatePatternFeature implements
		ICreateFeature {
	public static String name = "SimpleMatcher";
	public static String description = "Creates a new simple matcher filter pattern";
    
    
	public CreateSimpleMatcherFeature(IFeatureProvider fp) {
		super(fp, name, description);
	}



	@Override
	public Object[] create(ICreateContext context) {
		// TODO: create the domain object here
		//Object newDomainObject = null;
		

		Pattern eInst = ModelFactory.eINSTANCE.createPattern();
		eInst.setTypeId(FilteringPatternTemplateConstatns.SimpleMatcherFilter);
		
		addToWorkflow(getWorkflow(context), eInst);
		
		// TODO: in case of an EMF object add the new object to a suitable resource
//		getDiagram().eResource().getContents().add(eInst);
		
		
		addGraphicalRepresentation(context, eInst);
		return new Object[] { eInst };
	}




}
