package org.hahnpro.mdbda.diagrameditor.features.pattern.summatization;

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
import org.hahnpro.mdbda.model.pattern.filtering.BloomFiltering;
import org.hahnpro.mdbda.model.pattern.filtering.Distinct;
import org.hahnpro.mdbda.model.pattern.filtering.TopTen;
import org.hahnpro.mdbda.model.pattern.summatization.CountingWithCounters;
import org.hahnpro.mdbda.model.pattern.summatization.InvertedIndexSummarization;
import org.hahnpro.mdbda.model.pattern.summatization.NumericalSummarization;
import org.hahnpro.mdbda.model.workflow.Workflow;

public class SummatizationPatternGroupConfigurator extends
		AbstractPatternGroupConfigurator {

	@Override
	public PaletteCompartmentEntry getPalette(IFeatureProvider fp) {
		// add new compartment at the end of the existing compartments
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(
				"Filtering", null); // TODO iconid

			
		for(Class<ICreateFeature> clazz : new Class[] {CreateCountingWithCountersFeature.class,CreateInvertedIndexSummarizationFeature.class,CreateNumericalSummarizationFeature.class }){
			addPaletteElementToCompartmentAndLinkCreateFeatureFromCreateFeatureClass(
					compartmentEntry, fp, clazz);
		}
		


		return compartmentEntry;

	}

	@Override
	public List<ICreateFeature> getCreateFeatures(IFeatureProvider fp) {

		ICreateFeature[] cf = new ICreateFeature[] {
				new CreateCountingWithCountersFeature(fp), new CreateInvertedIndexSummarizationFeature(fp),
				new CreateNumericalSummarizationFeature(fp) };

		return Arrays.asList(cf);

	}

	@Override
	public IAddFeature getAddFeature(IAddContext context, IFeatureProvider fp) {
		if (context instanceof IAddContext
				&& context.getNewObject() instanceof CountingWithCounters) {
			return new AddCountingWithCountersFeature(fp);
		} else if (context instanceof IAddContext
				&& context.getNewObject() instanceof InvertedIndexSummarization) {
			return new AddInvertedIndexSummarizationFeature(fp);
		} else if (context instanceof IAddContext
				&& context.getNewObject() instanceof NumericalSummarization) {
			return new AddNumericalSummarizationFeature(fp);
		}
		return null;
	}

}
