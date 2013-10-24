package org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;
import org.hahnpro.mdbda.diagrameditor.features.AddLinkFeature;
import org.hahnpro.mdbda.diagrameditor.features.AddWorkflowFeature;
import org.hahnpro.mdbda.diagrameditor.features.CreateWorkflowFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.AbstractPatternGroupConfigurator;
import org.hahnpro.mdbda.model.pattern.dataorganization.Binning;
import org.hahnpro.mdbda.model.pattern.dataorganization.Partitioning;
import org.hahnpro.mdbda.model.pattern.dataorganization.Shuffling;
import org.hahnpro.mdbda.model.pattern.dataorganization.StructuredToHierachical;
import org.hahnpro.mdbda.model.pattern.dataorganization.TotalOrderSorting;
import org.hahnpro.mdbda.model.workflow.Workflow;

public class DataOrganizationPatternGroupConfigurator extends
		AbstractPatternGroupConfigurator {

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
				&& context.getNewObject() instanceof Binning) {
			return new AddBinningFeature(fp);
		} else if (context instanceof IAddContext
				&& context.getNewObject() instanceof Partitioning) {
			return new AddPartitoningFeature(fp);
		} else if (context instanceof IAddContext
				&& context.getNewObject() instanceof Shuffling) {
			return new AddShufflingFeature(fp);
		} else if (context instanceof IAddContext
				&& context.getNewObject() instanceof StructuredToHierachical) {
			return new AddStructuredToHierachicalFeature(fp);
		} else if (context instanceof IAddContext
				&& context.getNewObject() instanceof TotalOrderSorting) {
			return new AddTotalOrderSortingFeature(fp);
		}

		return null;
	}

}
