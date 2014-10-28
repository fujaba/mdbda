package org.mdbda.diagrameditor.features.pattern.filtering;

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.mdbda.model.ModelFactory;
import org.mdbda.model.Pattern;
import org.mdbda.diagrameditor.features.pattern.CreatePatternFeature;
import org.mdbda.model.FilteringPatternTemplateConstatns;
import org.osgi.framework.Bundle;

public class CreateSimpleMatcherFeature extends CreatePatternFeature implements
		ICreateFeature {
	public static String name = "SimpleMatcher";
	public static String description = "Creates a new simple matcher filter pattern";
    
	public CreateSimpleMatcherFeature(IFeatureProvider fp) {
		super(fp, name, description);
	}
	
	@Override
	public Object[] create(ICreateContext context) {
		Pattern eInst = ModelFactory.eINSTANCE.createPattern();
		initPattern(eInst,FilteringPatternTemplateConstatns.SimpleMatcherFilter);
		
		addToWorkflow(getWorkflow(context), eInst);
		
		addGraphicalRepresentation(context, eInst);
		return new Object[] { eInst };
	}
	
	@Override
	public URL getDefaultConfigJSONFileLocation() {
		Bundle bundle = Platform.getBundle("org.mdbda.diagrameditor");
		URL fileURL = bundle.getEntry("/target/classes/org/mdbda/diagrameditor/features/pattern/filtering/SimpleMatcherConfig.json");
		return fileURL;
	}
}
