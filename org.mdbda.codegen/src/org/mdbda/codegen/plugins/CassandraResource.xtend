package org.mdbda.codegen.plugins

import org.mdbda.codegen.IResourceTemplate
import org.mdbda.model.Resource
import org.mdbda.codegen.CodegenContext
import org.mdbda.codegen.helper.ConfigurationReader
import org.mdbda.codegen.helper.MDBDAConfiguration
import org.mdbda.model.Pattern

class CassandraResource implements IResourceTemplate {
	
	override generareMapReduceInputResouce(Resource res , Pattern pattern, CharSequence controledJobName,  CodegenContext context) '''
	{
		«val conf = MDBDAConfiguration.readConfigString(res.configurationString)»		
		«context.addImport("org.apache.cassandra.hadoop.ColumnFamilyInputFormat")»
		«controledJobName».getJob().setInputFormatClass(ColumnFamilyInputFormat.class);		
		«context.addImport("org.apache.cassandra.hadoop.ConfigHelper")»
		ConfigHelper.setInputColumnFamily(«controledJobName».getJob().getConfiguration(), "«conf.getCassandraResourceKeyspace»", "«conf.getCassandraResourceColumnFamily»");		
		«context.addImport("org.apache.cassandra.thrift.SlicePredicate")»
		SlicePredicate predicate = new SlicePredicate();
		«context.addImport("java.nio.ByteBuffer")»
		predicate.addToColumn_names(ByteBuffer.wrap("«conf.getCassandraColumnName»".getBytes()));		
		ConfigHelper.setInputSlicePredicate(«controledJobName».getJob().getConfiguration(), predicate);
	}
	'''
	
	
	
	override generareMapReduceOutputResouce(Resource res, CharSequence controledJobName , CodegenContext context) '''
	{
		«val conf = MDBDAConfiguration.readConfigString(res.configurationString)»
		«context.addImport("org.apache.cassandra.hadoop.ColumnFamilyOutputFormat")»
		«controledJobName».getJob().setOutputFormatClass(ColumnFamilyOutputFormat.class);
		«context.addImport("org.apache.cassandra.hadoop.ConfigHelper")»
		ConfigHelper.setOutputColumnFamily(«controledJobName».getJob().getConfiguration(), "«conf.getCassandraResourceKeyspace»" , "«conf.getCassandraResourceColumnFamily»");
	}
	'''
	
	override generareStormInputResouce(Resource res, CodegenContext context) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override generareStormOutputResouce(Resource res, CodegenContext context) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
}