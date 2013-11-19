package org.hahnpro.mdbda.diagrameditor.features.pattern.join;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.hahnpro.mdbda.diagrameditor.features.pattern.AddPatternFeature;


public class AddCartesianProductFeature extends AddPatternFeature implements
		IAddFeature {

	public AddCartesianProductFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "CartesianProduct";
	}


}
