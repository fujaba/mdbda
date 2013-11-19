package org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.hahnpro.mdbda.diagrameditor.features.pattern.AddPatternFeature;


public class AddShufflingFeature extends AddPatternFeature implements
		IAddFeature {

	public AddShufflingFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Shuffling";
	}


}
