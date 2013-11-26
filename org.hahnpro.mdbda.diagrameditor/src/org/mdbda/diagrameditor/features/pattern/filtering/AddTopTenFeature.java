package org.mdbda.diagrameditor.features.pattern.filtering;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.mdbda.diagrameditor.features.pattern.AddPatternFeature;


public class AddTopTenFeature extends AddPatternFeature implements
		IAddFeature {

	public AddTopTenFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Top Ten";
	}


}
