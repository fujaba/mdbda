package org.mdbda.codegen.styles

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.mdbda.codegen.MDBDACodegenerator

interface ICodeGen {	
	public static val TEMPLATETASK_MDBDA_TASK = "TEMPLATETASK_MDBDA_TASK";
	def void doGenerate(Resource emfInputResource, IFileSystemAccess fsa, String codeGenStyle, MDBDACodegenerator codegen);
}