package org.mdbda.diagrameditor.utils;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.util.IColorConstant;
import org.mdbda.model.Resource;

public abstract class AbstactMDBDAResourceShapeHelper extends AbstractSimpleMDBDAShapeHelper{
	
	Resource resource;
	public AbstactMDBDAResourceShapeHelper(Resource resource,ContainerShape rootContainerShapeForResourceElement, IFeatureProvider fp ){
		super(rootContainerShapeForResourceElement,fp);
		this.resource = resource;
		
	}
	
	public boolean isColorEqual(IColorConstant c1, Color c2) {
		if(c1.getBlue() == c2.getBlue() &&
				c1.getGreen() == c2.getGreen() &&
				c1.getRed() == c2.getRed()) {
			return true;
		}
		return false;
	}
}

