package org.mdbda.diagrameditor.features.pattern.filtering;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.mdbda.diagrameditor.features.pattern.AddPatternFeature;


public class AddDistinctFeature extends AddPatternFeature implements
		IAddFeature {

	public AddDistinctFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Distinct";
	}


}