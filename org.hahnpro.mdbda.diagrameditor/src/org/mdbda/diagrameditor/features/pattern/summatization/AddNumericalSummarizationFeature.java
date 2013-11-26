package org.mdbda.diagrameditor.features.pattern.summatization;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.mdbda.diagrameditor.features.pattern.AddPatternFeature;


public class AddNumericalSummarizationFeature extends AddPatternFeature implements
		IAddFeature {

	public AddNumericalSummarizationFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Numerical summarization";
	}


}
