package org.mdbda.diagrameditor.features.resources;

import java.util.Arrays;
import java.util.List;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.mdbda.model.Resource;
import org.mdbda.diagrameditor.features.AbstractGroupConfigurator;
import org.mdbda.model.ResourcesTemplateConstatns;
public class ResourceGroupConfigurator extends AbstractGroupConfigurator {


			
	@Override
	public PaletteCompartmentEntry getPalette(IFeatureProvider fp) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(
				"Resources", null); // TODO iconid

			
		for(Class<ICreateFeature> clazz : new Class[] {CreateCassandraResourceFeature.class,CreateHDFSResourceFeature.class, CreateNeo4jResourceFeature.class, CreateGenericResourceFeature.class, CreateHazelcastResourceFeature.class}){
			addPaletteElementToCompartmentAndLinkCreateFeatureFromCreateFeatureClass(
					compartmentEntry, fp, clazz);
		}
		


		return compartmentEntry;
	}

	@Override
	public List<ICreateFeature> getCreateFeatures(IFeatureProvider fp) {
		ICreateFeature[] cf = new ICreateFeature[] {
				new CreateCassandraResourceFeature(fp), new CreateHDFSResourceFeature(fp), new CreateNeo4jResourceFeature(fp), new CreateGenericResourceFeature(fp), new CreateHazelcastResourceFeature(fp)};

		return Arrays.asList(cf);
	}

	@Override
	public IAddFeature getAddFeature(IAddContext context, IFeatureProvider fp) {
		if (context instanceof IAddContext
				&& context.getNewObject() instanceof Resource) {
			
			switch(((Resource)context.getNewObject()).getTypeId()){
				case ResourcesTemplateConstatns.RESOURCETYPE_CASSANDRA:			return new AddCassandraResourceFeature(fp);
				case ResourcesTemplateConstatns.RESOURCETYPE_HAZELCAST:			return new AddHazelcastResourceFeature(fp);
				case ResourcesTemplateConstatns.RESOURCETYPE_HDFS: return new AddHDFSResourceFeature(fp);
				case ResourcesTemplateConstatns.RESOURCETYPE_GENERIC: return new AddGenericResourceFeature(fp);
				case ResourcesTemplateConstatns.RESOURCETYPE_NEO4J: return new AddNeo4jResourceFeature(fp);
				
			}
					} 
		return null;
	}

	
	
}