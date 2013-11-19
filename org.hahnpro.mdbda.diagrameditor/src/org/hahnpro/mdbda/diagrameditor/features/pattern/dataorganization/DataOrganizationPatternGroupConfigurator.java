package org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization;

import java.util.Arrays;
import java.util.List;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.hahnpro.mdbda.diagrameditor.features.AbstractGroupConfigurator;
import org.hahnpro.mdbda.model.Resource;

public class DataOrganizationPatternGroupConfigurator extends
		AbstractGroupConfigurator {
	public static final String DataOrganizationPatternType_Binning = "Binning";
	public static final String DataOrganizationPatternType_Partitioning = "Partitioning";
	public static final String DataOrganizationPatternType_Shuffling = "Shuffling";
	public static final String DataOrganizationPatternType_StructuredToHierachical = "StructuredToHierachical";
	public static final String DataOrganizationPatternType_TotalOrderSorting = "TotalOrderSorting";
	
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
				case DataOrganizationPatternType_Binning:			return new AddBinningFeature(fp);
				case DataOrganizationPatternType_Partitioning: return new AddPartitoningFeature(fp);
				case DataOrganizationPatternType_Shuffling: return new AddShufflingFeature(fp);
				case DataOrganizationPatternType_StructuredToHierachical: return new AddStructuredToHierachicalFeature(fp);
				case DataOrganizationPatternType_TotalOrderSorting: return new AddTotalOrderSortingFeature(fp);
			}
		} 
		return null;
	}

}
