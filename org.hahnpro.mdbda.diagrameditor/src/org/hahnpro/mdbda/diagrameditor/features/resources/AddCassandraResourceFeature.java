package org.hahnpro.mdbda.diagrameditor.features.resources;

import org.eclipse.graphiti.features.IFeatureProvider;

public class AddCassandraResourceFeature extends AddResourceFeature {

	public AddCassandraResourceFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Cassandra Resource";
	}

}
