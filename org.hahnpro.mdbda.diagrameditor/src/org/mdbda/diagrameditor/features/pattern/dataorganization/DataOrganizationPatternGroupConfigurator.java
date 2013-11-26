package org.mdbda.diagrameditor.features.pattern.dataorganization;

import java.util.Arrays;
import java.util.List;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.mdbda.model.Resource;
import org.mdbda.diagrameditor.features.AbstractGroupConfigurator;
import org.mdbda.model.DataOrganizationPatternTemplateConstatns;

public class DataOrganizationPatternGroupConfigurator extends
		AbstractGroupConfigurator {
	
	@Override
	public PaletteCompartmentEntry getPalette(IFeatureProvider fp) {
		// add new compartment at the end of the existing compartments
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(
				"Data Organization", null); // TODO iconid
		
		for(Class<ICreateFeature> clazz : new Class[] {CreateBinningFeature.class,CreatePartitoningFeature.class,CreateShufflingFeature.class, CreateStructuredToHierachicalFeature.class,CreateTotalOrderSortingFeature.class }){
			addPaletteElementToCompartmentAndLinkCreateFeatureFromCreateFeatureClass(
					compartmentEntry, fp, clazz);
		}
		
		return compartmentEntry;
	}

	@Override
	public List<ICreateFeature> getCreateFeatures(IFeatureProvider fp) {

		ICreateFeature[] cf = new ICreateFeature[] {
				new CreateBinningFeature(fp), new CreatePartitoningFeature(fp),
				new CreateShufflingFeature(fp),
				new CreateStructuredToHierachicalFeature(fp),
				new CreateTotalOrderSortingFeature(fp) };

		return Arrays.asList(cf);

	}

	@Override
	public IAddFeature getAddFeature(IAddContext context, IFeatureProvider fp) {
		if (context instanceof IAddContext
				&& context.getNewObject() instanceof Resource) {			
			switch(((Resource)context.getNewObject()).getTypeId()){
				case DataOrganizationPatternTemplateConstatns.Binning:			return new AddBinningFeature(fp);
				case DataOrganizationPatternTemplateConstatns.Partitioning: return new AddPartitoningFeature(fp);
				case DataOrganizationPatternTemplateConstatns.Shuffling: return new AddShufflingFeature(fp);
				case DataOrganizationPatternTemplateConstatns.StructuredToHierachical: return new AddStructuredToHierachicalFeature(fp);
				case DataOrganizationPatternTemplateConstatns.TotalOrderSorting: return new AddTotalOrderSortingFeature(fp);
			}
		} 
		return null;
	}

}
