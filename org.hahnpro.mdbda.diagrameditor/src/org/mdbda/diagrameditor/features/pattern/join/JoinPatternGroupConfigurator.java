package org.mdbda.diagrameditor.features.pattern.join;

import java.util.Arrays;
import java.util.List;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.mdbda.model.Resource;
import org.mdbda.diagrameditor.features.AbstractGroupConfigurator;
import org.mdbda.diagrameditor.features.resources.AddCassandraResourceFeature;
import org.mdbda.model.JoinPatternTemplateConstatns;

public class JoinPatternGroupConfigurator extends
		AbstractGroupConfigurator {

	
	@Override
	public PaletteCompartmentEntry getPalette(IFeatureProvider fp) {
		// add new compartment at the end of the existing compartments
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(
				"Join", null); // TODO iconid

			
		for(Class<ICreateFeature> clazz : new Class[] {CreateCartesianProductFeature.class, CreateReduceSideJoinFeature.class, CreateReplicatedJoinFeature.class}){
			addPaletteElementToCompartmentAndLinkCreateFeatureFromCreateFeatureClass(
					compartmentEntry, fp, clazz);
		}
		


		return compartmentEntry;

	}

	@Override
	public List<ICreateFeature> getCreateFeatures(IFeatureProvider fp) {

		ICreateFeature[] cf = new ICreateFeature[] {
				new CreateCartesianProductFeature(fp), 
				new CreateReduceSideJoinFeature(fp), new CreateReplicatedJoinFeature(fp) };

		return Arrays.asList(cf);

	}

	@Override
	public IAddFeature getAddFeature(IAddContext context, IFeatureProvider fp) {
		
		if (context instanceof IAddContext
				&& context.getNewObject() instanceof Resource) {
			
			switch(((Resource)context.getNewObject()).getTypeId()){
				case JoinPatternTemplateConstatns.CartesianProduct:	return new AddCartesianProductFeature(fp);
				case JoinPatternTemplateConstatns.ReduceSideJoin: return new AddReduceSideJoinFeature(fp);
				case JoinPatternTemplateConstatns.ReplicatedJoin: return new AddReplicatedJoinFeature(fp);
			}
					} 
		return null;
	}

}
