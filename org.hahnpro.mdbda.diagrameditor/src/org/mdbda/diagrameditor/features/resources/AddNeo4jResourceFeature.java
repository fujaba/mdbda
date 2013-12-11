package org.mdbda.diagrameditor.features.resources;

import org.eclipse.graphiti.features.IFeatureProvider;

public class AddNeo4jResourceFeature extends AddResourceFeature {

	public AddNeo4jResourceFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Neo4j Resource";
	}

}
