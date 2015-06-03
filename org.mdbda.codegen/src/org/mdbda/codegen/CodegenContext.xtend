package org.mdbda.codegen

import java.util.HashSet
import org.eclipse.xtext.generator.IFileSystemAccess
import java.util.HashMap

class CodegenContext {
	var imports = new HashSet<String>()
	String packageName = ""
	IFileSystemAccess fsa
	String modelRootName
	String fileSuffix
	
	var tempResources = new HashSet<String>()
	var String codeStyle;

	new(IFileSystemAccess fsa, String modelRootName, String fileSuffix , String packageName, String codeStyle) {
		this.fsa = fsa
		this.modelRootName = modelRootName
		this.packageName = packageName
		this.codeStyle = codeStyle
		this.fileSuffix = fileSuffix
	}

	public def void addImport(String imp) {
		imports.add(imp)
	}

	public def getImports() {
		return imports
	}

	public def getFileSuffix(){
		return fileSuffix
	}
	
	public def void addTempResource(String path) {
		tempResources.add(path)
	}

	public def getTempResources() {
		return tempResources
	}

	public def getFileSystemAccess() {
		return fsa
	}

	public def getModelRootName() {
		return modelRootName
	}

	public def getPackageName() {
		return packageName
	}

	var counter = new HashMap<String, Long>()

	def getCounter(String key) {
		var c = counter.get(key)
		if (c == null) {
			c = new Long(0)
		}
		return c
	}

	def void setCounter(String key, Long value) {
		counter.put(key, value)
	}
	
	public def getCodeStyle(){
		return codeStyle
	}
}