package org.hahnpro.mdbda.model;

import org.hahnpro.mdbda.model.pattern.Pattern;
import org.hahnpro.mdbda.model.resources.Resource;

public class ModelUtils {

	public static boolean createLink(Resource src, Resource tgt){
		boolean addsrc = false;
		boolean addtgt = false;
		
		if(src != null && src instanceof Pattern){
			addsrc = ((Pattern)src).getOutputResources().add(tgt);
		}
		if(tgt != null && tgt instanceof Pattern){
			addtgt = ((Pattern)tgt).getInputResources().add(src);
		}
		
		
		return addsrc || addtgt;
	}
	
	public static boolean removeLink(Resource src, Resource tgt){
		boolean remsrc = false;
		boolean remtgt = false;
		
		if(src != null && src instanceof Pattern){
			remsrc = ((Pattern)src).getOutputResources().remove(tgt);
		}
		if(tgt != null && tgt instanceof Pattern){
			remtgt = ((Pattern)tgt).getInputResources().remove(src);
		}
		return remsrc || remtgt;
	}
}
