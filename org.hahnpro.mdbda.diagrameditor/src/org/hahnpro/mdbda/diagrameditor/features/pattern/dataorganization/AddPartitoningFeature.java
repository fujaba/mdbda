package org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.hahnpro.mdbda.diagrameditor.features.pattern.AddPatternFeature;


public class AddPartitoningFeature extends AddPatternFeature implements
		IAddFeature {

	public AddPartitoningFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Partitoning";
	}


}
