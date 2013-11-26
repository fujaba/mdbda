package org.mdbda.diagrameditor.features.pattern.join;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.mdbda.diagrameditor.features.pattern.AddPatternFeature;


public class AddReplicatedJoinFeature extends AddPatternFeature implements
		IAddFeature {

	public AddReplicatedJoinFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Replicated join";
	}


}
