package org.mdbda.codegen.helper;

import java.util.HashSet;

import org.mdbda.model.Task;
import org.mdbda.model.Workflow;
import org.mdbda.model.*;

public class MDBDAWorkflowHelper {

	private static HashSet<Task> addedPattern = new HashSet<>();
	
	public static void addPattern(Task p){
		addedPattern.add(p);
	}
	
	public static boolean containsPattern(Task p){
		return addedPattern.contains(p);
	}
	
	public static String getGeneratePackageName(Task p){
		String packageName = null;
		
		if(p instanceof Workflow){
			packageName = ((Workflow)p).getDiagram().getName();
		}else if(p instanceof Task){
			packageName = p.getWorkflow().getDiagram().getName();
		}
		return packageName;
	}
	
	public static String getGenerateClassName(Task p){
		String className = null;
		
		if(p instanceof Workflow){
			className = p.getName();
		}else if(p instanceof Task){
			className = p.getWorkflow().getName() + p.getName();
		}
		return className;
	}
	

}
