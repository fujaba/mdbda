package org.mdbda.diagrameditor.features.pattern.summatization;

import java.util.Arrays;
import java.util.List;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.mdbda.model.Resource;
import org.mdbda.diagrameditor.features.AbstractGroupConfigurator;
import org.mdbda.model.SummatizationPatternTemplateConstatns;

public class SummatizationPatternGroupConfigurator extends
		AbstractGroupConfigurator {

	@Override
	public PaletteCompartmentEntry getPalette(IFeatureProvider fp) {
		// add new compartment at the end of the existing compartments
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(
				"Summatization", null); // TODO iconid

			
		for(Class<ICreateFeature> clazz : new Class[] {CreateCustomCalculationFeature.class,CreateNumericalSummarizationFeature.class }){
			addPaletteElementToCompartmentAndLinkCreateFeatureFromCreateFeatureClass(
					compartmentEntry, fp, clazz);
		}
		


		return compartmentEntry;

	}

	@Override
	public List<ICreateFeature> getCreateFeatures(IFeatureProvider fp) {

		ICreateFeature[] cf = new ICreateFeature[] {
				new CreateCustomCalculationFeature(fp),
				new CreateNumericalSummarizationFeature(fp) };

		return Arrays.asList(cf);

	}

	@Override
	public IAddFeature getAddFeature(IAddContext context, IFeatureProvider fp) {

		if (context instanceof IAddContext
				&& context.getNewObject() instanceof Resource) {
			
			switch(((Resource)context.getNewObject()).getTypeId()){
				case SummatizationPatternTemplateConstatns.CustomCalculation:			return new AddCustomCalculationFeature(fp);
				case SummatizationPatternTemplateConstatns.NumericalSummarization: return new AddNumericalSummarizationFeature(fp);
			}
		} 
		return null;
	}

}