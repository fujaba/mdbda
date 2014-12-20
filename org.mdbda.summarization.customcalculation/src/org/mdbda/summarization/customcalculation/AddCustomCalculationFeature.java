package org.mdbda.summarization.customcalculation;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.mdbda.diagrameditor.features.task.AddTaskFeature;


public class AddCustomCalculationFeature extends AddTaskFeature implements
		IAddFeature {

	public AddCustomCalculationFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Custom Calculation";
	}


}
