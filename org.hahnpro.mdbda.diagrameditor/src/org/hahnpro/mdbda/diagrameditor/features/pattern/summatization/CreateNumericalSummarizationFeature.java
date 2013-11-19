package org.hahnpro.mdbda.diagrameditor.features.pattern.summatization;

import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.hahnpro.mdbda.diagrameditor.features.pattern.CreatePatternFeature;
import org.hahnpro.mdbda.model.ModelFactory;
import org.hahnpro.mdbda.model.Pattern;

public class CreateNumericalSummarizationFeature extends CreatePatternFeature implements
		ICreateFeature {
	public static String name = "Numerical summarization";
	public static String description = "Creates a new numerical summarization pattern";
    
    
	public CreateNumericalSummarizationFeature(IFeatureProvider fp) {
		super(fp, name, description);
	}



	@Override
	public Object[] create(ICreateContext context) {
		// TODO: create the domain object here
		//Object newDomainObject = null;
		

		Pattern eInst = ModelFactory.eINSTANCE.createPattern();
		eInst.setTypeId(SummatizationPatternGroupConfigurator.SummatizationPatternTYPE_NumericalSummarization);
		
		addToWorkflow(getWorkflow(context), eInst);
		
		// TODO: in case of an EMF object add the new object to a suitable resource
//		getDiagram().eResource().getContents().add(eInst);
		
		
		
		addGraphicalRepresentation(context, eInst);
		return new Object[] { eInst };
	}




}
