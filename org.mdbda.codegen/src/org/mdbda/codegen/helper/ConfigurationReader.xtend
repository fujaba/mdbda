package org.mdbda.codegen.helper

import org.mdbda.model.Dataformat 
import org.mdbda.model.Pattern

class ConfigurationReader {
	
	public static def getMapMethod(String configurationClass) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	public static def getMapFields(String configurationClass) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	public static def getReduceMethod(String configurationClass) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	public static def getReduceFields(String configurationClass) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	public static def getPartitonerFields(String configurationClass) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	public static def getPartitionMethod(String configurationClass) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	public static def getPartitonerConfiguration(String configurationClass){
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
		
	}
	
	public static def getIntermediateKeyType(String configurationClass){
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
		
	}
	public static def getIntermediateValueType(String configurationClass){
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
		
	}
	
	public static def CharSequence formatClassConfigutation(Dataformat dataformat, Pattern pattern){
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	//TODO: asd
	public static def CharSequence getHDFSPath(String string)'''someAwesomePath'''
	
	public static def CharSequence getMapReduceTempPath(String string)'''someAwesomeTempPath'''
	
}