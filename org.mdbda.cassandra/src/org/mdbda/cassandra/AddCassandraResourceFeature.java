package org.mdbda.cassandra;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.mdbda.diagrameditor.features.resources.AddResourceFeature;

public class AddCassandraResourceFeature extends AddResourceFeature {

	public AddCassandraResourceFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Cassandra Resource";
	}

}
