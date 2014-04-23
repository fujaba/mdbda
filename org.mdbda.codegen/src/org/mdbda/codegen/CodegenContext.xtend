package org.mdbda.codegen

import java.util.HashSet
import org.eclipse.xtext.generator.IFileSystemAccess

class CodegenContext {
	
		var imports = new HashSet<String>()
		String packageName = ""
		IFileSystemAccess fsa
		String filename
		
		
	new( IFileSystemAccess fsa , String filename, String packageName) {
		 this.fsa = fsa
		 this.filename = filename
		 this.packageName = packageName
	}
	
	
	public def void addImport(String imp){
		imports.add(imp)
	}
	
	public def getImports(){
		return imports
	}
	
	public def getFileSystemAccess(){
		return fsa
	}
	
	public def getFileName(){
		return filename
	}
	
	public def getPackageName(){
		return packageName
	}
	
}