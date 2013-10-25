package org.hahnpro.mdbda.diagrameditor.features.resources;

import java.util.Arrays;
import java.util.List;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.hahnpro.mdbda.diagrameditor.features.AbstractGroupConfigurator;
import org.hahnpro.mdbda.diagrameditor.features.pattern.join.AddCartesianProductFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.join.CreateCartesianProductFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.join.CreateCompositeJoinFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.join.CreateReduceSideJoinFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.join.CreateReplicatedJoinFeature;
import org.hahnpro.mdbda.model.pattern.join.CartesianProduct;
import org.hahnpro.mdbda.model.resources.CassandraResource;
import org.hahnpro.mdbda.model.resources.HDFSResource;
public class ResourceGroupConfigurator extends AbstractGroupConfigurator {

	@Override
	public PaletteCompartmentEntry getPalette(IFeatureProvider fp) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(
				"Resources", null); // TODO iconid

			
		for(Class<ICreateFeature> clazz : new Class[] {CreateCassandraResourceFeature.class,CreateHDFSResourceFeature.class}){
			addPaletteElementToCompartmentAndLinkCreateFeatureFromCreateFeatureClass(
					compartmentEntry, fp, clazz);
		}
		


		return compartmentEntry;
	}

	@Override
	public List<ICreateFeature> getCreateFeatures(IFeatureProvider fp) {
		ICreateFeature[] cf = new ICreateFeature[] {
				new CreateCassandraResourceFeature(fp), new CreateHDFSResourceFeature(fp)};

		return Arrays.asList(cf);
	}

	@Override
	public IAddFeature getAddFeature(IAddContext context, IFeatureProvider fp) {
		if (context instanceof IAddContext
				&& context.getNewObject() instanceof CassandraResource) {
			return new AddCassandraResourceFeature(fp);
		} else if (context instanceof IAddContext
				&& context.getNewObject() instanceof HDFSResource) {
			return new AddHDFSResourceFeature(fp);
		} 
		return null;
	}

	
	
}
