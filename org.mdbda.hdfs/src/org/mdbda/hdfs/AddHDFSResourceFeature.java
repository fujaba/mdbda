package org.mdbda.hdfs;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.mdbda.diagrameditor.features.resources.AddResourceFeature;

public class AddHDFSResourceFeature extends AddResourceFeature {

	public AddHDFSResourceFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Hazelcast Resource";
	}

}
