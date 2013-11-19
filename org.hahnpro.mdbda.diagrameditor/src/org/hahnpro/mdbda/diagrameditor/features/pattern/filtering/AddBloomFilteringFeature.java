package org.hahnpro.mdbda.diagrameditor.features.pattern.filtering;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.hahnpro.mdbda.diagrameditor.features.pattern.AddPatternFeature;


public class AddBloomFilteringFeature extends AddPatternFeature implements
		IAddFeature {

	public AddBloomFilteringFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "BloomFiltering";
	}


}
