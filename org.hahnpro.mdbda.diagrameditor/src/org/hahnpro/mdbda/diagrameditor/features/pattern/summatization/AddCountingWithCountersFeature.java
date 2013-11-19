package org.hahnpro.mdbda.diagrameditor.features.pattern.summatization;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.hahnpro.mdbda.diagrameditor.features.pattern.AddPatternFeature;


public class AddCountingWithCountersFeature extends AddPatternFeature implements
		IAddFeature {

	public AddCountingWithCountersFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Counting with counters";
	}


}
