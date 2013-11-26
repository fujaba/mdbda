package org.mdbda.diagrameditor.features.pattern.summatization;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.mdbda.diagrameditor.features.pattern.AddPatternFeature;


public class AddInvertedIndexSummarizationFeature extends AddPatternFeature implements
		IAddFeature {

	public AddInvertedIndexSummarizationFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Inverted index summarization";
	}


}
