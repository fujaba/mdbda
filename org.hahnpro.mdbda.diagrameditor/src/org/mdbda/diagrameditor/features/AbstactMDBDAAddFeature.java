package org.mdbda.diagrameditor.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.mdbda.diagrameditor.internal.PublicGetBusinessObjectForPictogramElementInterface;

public abstract class AbstactMDBDAAddFeature extends AbstractAddFeature implements PublicGetBusinessObjectForPictogramElementInterface{



	protected String typeName = "Abstract Type";
	

	public AbstactMDBDAAddFeature(IFeatureProvider fp) {
		super(fp);
	}

	
	public Object getBusinessObjectForPictogramElement(PictogramElement pe) {
		return super.getBusinessObjectForPictogramElement(pe);
	}
}