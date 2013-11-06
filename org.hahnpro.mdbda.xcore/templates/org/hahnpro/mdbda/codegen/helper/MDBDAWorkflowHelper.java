package org.hahnpro.mdbda.codegen.helper;

import java.util.HashSet;

import org.hahnpro.mdbda.model.pattern.Pattern;

public class MDBDAWorkflowHelper {

	private static HashSet<Pattern> addedPattern = new HashSet<>();
	
	public static void addPattern(Pattern p){
		addedPattern.add(p);
	}
	
	public static boolean containsPattern(Pattern p){
		return addedPattern.contains(p);
	}
}
