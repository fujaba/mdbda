package org.hahnpro.mdbda.diagrameditor.features.pattern.join;

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
import org.hahnpro.mdbda.diagrameditor.features.AbstractGroupConfigurator;
import org.hahnpro.mdbda.diagrameditor.features.AddLinkFeature;
import org.hahnpro.mdbda.diagrameditor.features.AddWorkflowFeature;
import org.hahnpro.mdbda.diagrameditor.features.CreateWorkflowFeature;
import org.hahnpro.mdbda.model.pattern.dataorganization.Binning;
import org.hahnpro.mdbda.model.pattern.dataorganization.Partitioning;
import org.hahnpro.mdbda.model.pattern.dataorganization.Shuffling;
import org.hahnpro.mdbda.model.pattern.dataorganization.StructuredToHierachical;
import org.hahnpro.mdbda.model.pattern.dataorganization.TotalOrderSorting;
import org.hahnpro.mdbda.model.pattern.filtering.BloomFiltering;
import org.hahnpro.mdbda.model.pattern.filtering.Distinct;
import org.hahnpro.mdbda.model.pattern.filtering.TopTen;
import org.hahnpro.mdbda.model.pattern.join.CartesianProduct;
import org.hahnpro.mdbda.model.pattern.join.CompositeJoin;
import org.hahnpro.mdbda.model.pattern.join.ReduceSideJoin;
import org.hahnpro.mdbda.model.pattern.join.ReplicatedJoin;
import org.hahnpro.mdbda.model.workflow.Workflow;

public class JoinPatternGroupConfigurator extends
		AbstractGroupConfigurator {

	@Override
	public PaletteCompartmentEntry getPalette(IFeatureProvider fp) {
		// add new compartment at the end of the existing compartments
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(
				"Join", null); // TODO iconid

			
		for(Class<ICreateFeature> clazz : new Class[] {CreateCartesianProductFeature.class, CreateCompositeJoinFeature.class, CreateReduceSideJoinFeature.class, CreateReplicatedJoinFeature.class}){
			addPaletteElementToCompartmentAndLinkCreateFeatureFromCreateFeatureClass(
					compartmentEntry, fp, clazz);
		}
		


		return compartmentEntry;

	}

	@Override
	public List<ICreateFeature> getCreateFeatures(IFeatureProvider fp) {

		ICreateFeature[] cf = new ICreateFeature[] {
				new CreateCartesianProductFeature(fp), new CreateCompositeJoinFeature(fp),
				new CreateReduceSideJoinFeature(fp), new CreateReplicatedJoinFeature(fp) };

		return Arrays.asList(cf);

	}

	@Override
	public IAddFeature getAddFeature(IAddContext context, IFeatureProvider fp) {
		if (context instanceof IAddContext
				&& context.getNewObject() instanceof CartesianProduct) {
			return new AddCartesianProductFeature(fp);
		} else if (context instanceof IAddContext
				&& context.getNewObject() instanceof CompositeJoin) {
			return new AddCompositeJoinFeature(fp);
		} else if (context instanceof IAddContext
				&& context.getNewObject() instanceof ReduceSideJoin) {
			return new AddReduceSideJoinFeature(fp);
		}else if (context instanceof IAddContext
				&& context.getNewObject() instanceof ReplicatedJoin) {
			return new AddReplicatedJoinFeature(fp);
		}
		return null;
	}

}
