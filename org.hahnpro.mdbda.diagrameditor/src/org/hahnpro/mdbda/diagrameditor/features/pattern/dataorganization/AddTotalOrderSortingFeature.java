package org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.hahnpro.mdbda.diagrameditor.features.pattern.AddPatternFeature;


public class AddTotalOrderSortingFeature extends AddPatternFeature implements
		IAddFeature {

	public AddTotalOrderSortingFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Total Order Sorting";
	}


}
