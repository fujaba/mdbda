package org.mdbda.diagrameditor.features.resources;

import org.eclipse.graphiti.features.IFeatureProvider;

public class AddHazelcastResourceFeature extends AddResourceFeature {

	public AddHazelcastResourceFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Hazelcast Resource";
	}

}
