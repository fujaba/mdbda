package org.mdbda.codegen.helper

import org.mdbda.model.Resource


@Deprecated
class ConfigurationReader {

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

	//TODO: asd
	public static def CharSequence getHDFSPath(String string)'''someAwesomeHDFSPath'''
	
	public static def CharSequence getMapReduceTempPath(String string)'''someAwesomeTempPath'''
	
	
	public static def CharSequence getCassandraResourceKeyspace(Resource cassandraResource)''' "TODO_NOTIMPLEMENT_KEYSPACE" '''
	
	public static def CharSequence getCassandraResourceColumnFamily(Resource cassandraResource)''' "TODO_NOTIMPLEMENT_COLUMN_FAMILY" '''
	
	public static def CharSequence getCassandraColumnName(Resource resource) ''' "TODO_NOTIMPLEMENT_COLUMN_NAME" '''
	
}