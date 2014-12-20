package org.mdbda.model;

import org.mdbda.model.Task;
import org.mdbda.model.Resource;

public class ModelUtils {

	public static boolean createLink(Resource src, Resource tgt){
		boolean addsrc = false;
		boolean addtgt = false;
		
		if(src != null && src instanceof Resource){
			addsrc = ((Resource)src).getOutputResources().add(tgt);
		}
		if(tgt != null && tgt instanceof Resource){
			addtgt = ((Resource)tgt).getInputResources().add(src);
		}
		
		
		return addsrc || addtgt;
	}
	
	public static boolean removeLink(Resource src, Resource tgt){
		boolean remsrc = false;
		boolean remtgt = false;
		
		if(src != null && src instanceof Resource){
			remsrc = ((Resource)src).getOutputResources().remove(tgt);
		}
		if(tgt != null && tgt instanceof Resource){
			remtgt = ((Resource)tgt).getInputResources().remove(src);
		}
		return remsrc || remtgt;
	}
}
