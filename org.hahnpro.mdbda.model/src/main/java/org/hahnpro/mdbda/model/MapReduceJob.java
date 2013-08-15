package org.hahnpro.mdbda.model;

import org.hahnpro.mdbda.model.resources.Resource;
import org.sdmlib.models.classes.Clazz;

public class MapReduceJob extends Clazz {
	Resource inputResource;
	Resource outputResource;
	
	DataTransform mapTransform;
	DataTransform reduceTransform;
	
	public MapReduceJob(String clazzName) {
		super(clazzName);
	}

	public Resource getInputResource() {
		return inputResource;
	}

	public void setInputResource(Resource inputResource) {
		this.inputResource = inputResource;
	}

	public Resource getOutputResource() {
		return outputResource;
	}

	public void setOutputResource(Resource outputResource) {
		this.outputResource = outputResource;
	}

	public DataTransform getMapTransform() {
		return mapTransform;
	}

	public void setMapTransform(DataTransform mapTransform) {
		this.mapTransform = mapTransform;
	}

	public DataTransform getReduceTransform() {
		return reduceTransform;
	}

	public void setReduceTransform(DataTransform reduceTransform) {
		this.reduceTransform = reduceTransform;
	}
	
	
	

}
