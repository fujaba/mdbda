package org.mdbda.cassandra.codegen

import org.mdbda.model.Resource
import org.mdbda.codegen.CodegenContext
import org.mdbda.codegen.helper.MDBDAConfiguration
import org.mdbda.model.Task
import org.mdbda.codegen.AbstractResourceTemplate
import org.mdbda.codegen.styles.hadoop.HadoopCodeGen
import org.mdbda.cassandra.helper.CassandraConfigReader

class CassandraResourceHadoop extends AbstractResourceTemplate {
	
	override generareInputResouce(Resource res , Task pattern, CharSequence controledJobName,  CodegenContext context) '''
	{
		«val conf = new CassandraConfigReader(res.configurationString) »
		
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
	
	override generareOutputResouce(Resource res, CharSequence controledJobName , CodegenContext context) '''
	{
		 «val conf = new CassandraConfigReader(res.configurationString)»
		 «context.addImport("org.apache.cassandra.hadoop.ColumnFamilyOutputFormat")»
		 «controledJobName».getJob().setOutputFormatClass(ColumnFamilyOutputFormat.class);
		 «context.addImport("org.apache.cassandra.hadoop.ConfigHelper")»
		ConfigHelper.setOutputColumnFamily(«controledJobName».getJob().getConfiguration(), "«conf.getCassandraResourceKeyspace»" , "«conf.getCassandraResourceColumnFamily»");
	}
	'''
	
	override getCodeStyle() {
		HadoopCodeGen.codeStyle
	}

}