package org.mdbda.diagrameditor.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;

public abstract class AbstactMDBDAAddFeature extends AbstractAddFeature {



	protected String typeName = "Abstract Type";
	

	public AbstactMDBDAAddFeature(IFeatureProvider fp) {
		super(fp);
	}

}