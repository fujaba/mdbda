package org.mdbda.join.reducesidejoin;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.mdbda.diagrameditor.features.task.AddTaskFeature;


public class AddReduceSideJoinFeature extends AddTaskFeature implements
		IAddFeature {

	public AddReduceSideJoinFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Reduce side join";
	}


}
