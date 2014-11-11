package org.mdbda.join.reducesidejoin;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.mdbda.diagrameditor.features.pattern.AddPatternFeature;


public class AddReduceSideJoinFeature extends AddPatternFeature implements
		IAddFeature {

	public AddReduceSideJoinFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Reduce side join";
	}


}
