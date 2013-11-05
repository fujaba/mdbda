package org.hahnpro.mdbda.diagrameditor.features.resources;

import org.eclipse.graphiti.features.IFeatureProvider;

public class AddHDFSResourceFeature extends AddResourceFeature {

	public AddHDFSResourceFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "HDFS Resource";
	}

}
