package org.hahnpro.mdbda.diagrameditor.features.pattern.join;

import java.util.Arrays;
import java.util.List;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.hahnpro.mdbda.diagrameditor.features.AbstractGroupConfigurator;
import org.hahnpro.mdbda.diagrameditor.features.resources.AddCassandraResourceFeature;
import org.hahnpro.mdbda.model.Resource;

public class JoinPatternGroupConfigurator extends
		AbstractGroupConfigurator {
	public static final String JoinPatternType_CartesianProduct = "CartesianProduct";
	public static final String JoinPatternType_CompositeJoin = "CompositeJoin";
	public static final String JoinPatternType_ReduceSideJoin = "ReduceSideJoin";
	public static final String JoinPatternType_ReplicatedJoin = "ReplicatedJoin";
	
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
				&& context.getNewObject() instanceof Resource) {
			
			switch(((Resource)context.getNewObject()).getTypeId()){
				case JoinPatternType_CartesianProduct:	return new AddCartesianProductFeature(fp);
				case JoinPatternType_CompositeJoin: return new AddCompositeJoinFeature(fp);
				case JoinPatternType_ReduceSideJoin: return new AddReduceSideJoinFeature(fp);
				case JoinPatternType_ReplicatedJoin: return new AddReplicatedJoinFeature(fp);
			}
					} 
		return null;
	}

}
