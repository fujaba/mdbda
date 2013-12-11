package org.mdbda.diagrameditor.features.resources;

import org.eclipse.graphiti.features.IFeatureProvider;

public class AddGenericResourceFeature extends AddResourceFeature {

	public AddGenericResourceFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Generic Resource";
	}

}
