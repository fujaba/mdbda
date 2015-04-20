package org.mdbda.diagrameditor.utils;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.mdbda.model.Resource;

public abstract class AbstactMDBDAResourceShapeHelper extends AbstractSimpleMDBDAShapeHelper{
	
	Resource resource;
	public AbstactMDBDAResourceShapeHelper(Resource resource,ContainerShape rootContainerShapeForResourceElement, IFeatureProvider fp ){
		super(rootContainerShapeForResourceElement,fp);
		this.resource = resource;
		
	}
}

