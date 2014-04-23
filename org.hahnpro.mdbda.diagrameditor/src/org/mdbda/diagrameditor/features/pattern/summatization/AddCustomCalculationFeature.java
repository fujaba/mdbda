package org.mdbda.diagrameditor.features.pattern.summatization;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.mdbda.diagrameditor.features.pattern.AddPatternFeature;


public class AddCustomCalculationFeature extends AddPatternFeature implements
		IAddFeature {

	public AddCustomCalculationFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Custom Calculation";
	}


}
