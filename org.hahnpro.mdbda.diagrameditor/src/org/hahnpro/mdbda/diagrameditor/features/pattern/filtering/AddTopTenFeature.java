package org.hahnpro.mdbda.diagrameditor.features.pattern.filtering;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.hahnpro.mdbda.diagrameditor.features.pattern.AddPatternFeature;


public class AddTopTenFeature extends AddPatternFeature implements
		IAddFeature {

	public AddTopTenFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Top Ten";
	}


}
