package org.hahnpro.mdbda.diagrameditor.features.resources;

import java.util.Arrays;
import java.util.List;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.hahnpro.mdbda.diagrameditor.features.AbstractGroupConfigurator;
import org.hahnpro.mdbda.model.Resource;
public class ResourceGroupConfigurator extends AbstractGroupConfigurator {

	public static final String RESOURCETYPE_CASSANDRA = "CassandraResource";
	public static final String RESOURCETYPE_HDFS = "HDFSResource";
			
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
				&& context.getNewObject() instanceof Resource) {
			
			switch(((Resource)context.getNewObject()).getTypeId()){
				case RESOURCETYPE_CASSANDRA:			return new AddCassandraResourceFeature(fp);
				case RESOURCETYPE_HDFS: return new AddCassandraResourceFeature(fp);
			}
					} 
		return null;
	}

	
	
}
