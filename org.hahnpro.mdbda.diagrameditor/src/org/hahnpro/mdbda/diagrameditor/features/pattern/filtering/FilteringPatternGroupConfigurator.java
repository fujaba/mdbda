package org.hahnpro.mdbda.diagrameditor.features.pattern.filtering;

import java.util.Arrays;
import java.util.List;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.hahnpro.mdbda.diagrameditor.features.AbstractGroupConfigurator;
import org.hahnpro.mdbda.model.Resource;

public class FilteringPatternGroupConfigurator extends
		AbstractGroupConfigurator {
	public static final String FilteringPatternType_BloomFiltering  = "BloomFiltering";
	public static final String FilteringPatternType_TopTen  = "TopTen";
	public static final String FilteringPatternType_Distinct  = "Distinct";
	
	@Override
	public PaletteCompartmentEntry getPalette(IFeatureProvider fp) {
		// add new compartment at the end of the existing compartments
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(
				"Filtering", null); // TODO iconid

			
		for(Class<ICreateFeature> clazz : new Class[] {CreateBloomFilteringFeature.class,CreateTopTenFeature.class,CreateDistinctFeature.class }){
			addPaletteElementToCompartmentAndLinkCreateFeatureFromCreateFeatureClass(
					compartmentEntry, fp, clazz);
		}
		


		return compartmentEntry;

	}

	@Override
	public List<ICreateFeature> getCreateFeatures(IFeatureProvider fp) {

		ICreateFeature[] cf = new ICreateFeature[] {
				new CreateBloomFilteringFeature(fp), new CreateTopTenFeature(fp),
				new CreateDistinctFeature(fp) };

		return Arrays.asList(cf);

	}

	@Override
	public IAddFeature getAddFeature(IAddContext context, IFeatureProvider fp) {		
		if (context instanceof IAddContext
				&& context.getNewObject() instanceof Resource) {
			
			switch(((Resource)context.getNewObject()).getTypeId()){
				case FilteringPatternType_BloomFiltering:	return new AddBloomFilteringFeature(fp);
				case FilteringPatternType_TopTen: 			return new AddTopTenFeature(fp);
				case FilteringPatternType_Distinct: 		return new AddDistinctFeature(fp);
			}
					} 
		return null;
	}

}
