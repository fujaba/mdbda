package org.mdbda.examples.filter;

import org.mdbda.runtime.annotations.Matcher;

public class FilterConf {

	@Matcher
	public boolean matcher(Object value){
		if(value instanceof Double){
			Double d = (Double) value;
			if(d >= 23 && d <= 42) return true;
		} 
		return false;
	}
}
