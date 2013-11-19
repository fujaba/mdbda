package org.hahnpro.mdbda.diagrameditor.features.pattern.join;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.hahnpro.mdbda.diagrameditor.features.pattern.AddPatternFeature;


public class AddCompositeJoinFeature extends AddPatternFeature implements
		IAddFeature {

	public AddCompositeJoinFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Composite Join";
	}


}
