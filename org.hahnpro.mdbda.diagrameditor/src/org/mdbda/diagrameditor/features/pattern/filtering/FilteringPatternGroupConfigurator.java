package org.mdbda.diagrameditor.features.pattern.filtering;

import java.util.Arrays;
import java.util.List;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.mdbda.model.Resource;
import org.mdbda.diagrameditor.features.AbstractGroupConfigurator;
import org.mdbda.model.FilteringPatternTemplateConstatns;

public class FilteringPatternGroupConfigurator extends
		AbstractGroupConfigurator {

	
	@Override
	public PaletteCompartmentEntry getPalette(IFeatureProvider fp) {
		// add new compartment at the end of the existing compartments
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(
				"Filtering", null); // TODO iconid

			
		for(Class<ICreateFeature> clazz : new Class[] {CreateTopTenFeature.class,CreateDistinctFeature.class,CreateSimpleMatcherFeature.class }){
			addPaletteElementToCompartmentAndLinkCreateFeatureFromCreateFeatureClass(
					compartmentEntry, fp, clazz);
		}
		


		return compartmentEntry;

	}

	@Override
	public List<ICreateFeature> getCreateFeatures(IFeatureProvider fp) {

		ICreateFeature[] cf = new ICreateFeature[] {
				new CreateTopTenFeature(fp),
				new CreateDistinctFeature(fp), new CreateSimpleMatcherFeature(fp) };

		return Arrays.asList(cf);

	}

	@Override
	public IAddFeature getAddFeature(IAddContext context, IFeatureProvider fp) {		
		if (context instanceof IAddContext
				&& context.getNewObject() instanceof Resource) {
			
			switch(((Resource)context.getNewObject()).getTypeId()){
				case FilteringPatternTemplateConstatns.TopTen: 				return new AddTopTenFeature(fp);
				case FilteringPatternTemplateConstatns.Distinct: 			return new AddDistinctFeature(fp);
				case FilteringPatternTemplateConstatns.SimpleMatcherFilter: return new AddSimpleMatcherFeature(fp);
				
			}
					} 
		return null;
	}

}
