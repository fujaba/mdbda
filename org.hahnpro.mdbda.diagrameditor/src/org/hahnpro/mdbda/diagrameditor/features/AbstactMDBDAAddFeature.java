package org.hahnpro.mdbda.diagrameditor.features;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;

public abstract class AbstactMDBDAAddFeature extends AbstractAddFeature {



	protected String typeName = "Abstract Type";
	

	public AbstactMDBDAAddFeature(IFeatureProvider fp) {
		super(fp);
	}

}