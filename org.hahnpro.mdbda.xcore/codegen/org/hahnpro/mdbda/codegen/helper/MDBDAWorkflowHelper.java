package org.hahnpro.mdbda.codegen.helper;

import java.util.HashSet;

import org.eclipse.internal.xpand2.ast.Definition;
import org.hahnpro.mdbda.model.*;

public class MDBDAWorkflowHelper {

	private static HashSet<Pattern> addedPattern = new HashSet<>();
	
	public static void addPattern(Pattern p){
		addedPattern.add(p);
	}
	
	public static boolean containsPattern(Pattern p){
		return addedPattern.contains(p);
	}
	
	public static String getGeneratePackageName(Pattern p){
		String packageName = null;
		
		if(p instanceof Workflow){
			packageName = ((Workflow)p).getDiagram().getName();
		}else if(p instanceof Pattern){
			packageName = p.getWorkflow().getDiagram().getName();
		}
		return packageName;
	}
	
	public static String getGenerateClassName(Pattern p){
		String className = null;
		
		if(p instanceof Workflow){
			className = p.getName();
		}else if(p instanceof Pattern){
			className = p.getWorkflow().getName() + p.getName();
		}
		return className;
	}
	
	public static Definition getPatternTemplate(Pattern p){
		Definition def = null;
		return def;
		
	}
}
