package org.mdbda.cassandra.codegen

import org.mdbda.cassandra.helper.CassandraConfigReader
import org.mdbda.codegen.AbstractResourceTemplate
import org.mdbda.codegen.CodegenContext
import org.mdbda.codegen.helper.MDBDAConfiguration
import org.mdbda.codegen.styles.hadoop.HadoopCodeGen
import org.mdbda.model.Resource
import org.mdbda.model.Task

class CassandraResourceHadoop extends AbstractResourceTemplate {
	
	override generareInputResouce(Resource res , Task pattern, CharSequence controledJobName, CodegenContext context) '''
	«val conf = new CassandraConfigReader(res.configurationString) »
	{/*CassandraResourceHadoop
		Keyspace: «conf.getCassandraResourceKeyspace»
		ColumnFamily: «conf.getCassandraResourceColumnFamily»
		ColumnNames : «conf.getCassandraColumnName»
		*/
		«context.addImport("org.apache.cassandra.hadoop.ColumnFamilyInputFormat")»
		«controledJobName».getJob().setInputFormatClass(ColumnFamilyInputFormat.class);
		«context.addImport("org.apache.cassandra.hadoop.ConfigHelper")»
		ConfigHelper.setInputColumnFamily(«controledJobName».getJob().getConfiguration(),
			"«conf.getCassandraResourceKeyspace»",//Keyspace
			"«conf.getCassandraResourceColumnFamily»"); //ColumnFamily
		«addColumNamesToPredicate(conf, context)»
		ConfigHelper.setInputSlicePredicate(«controledJobName».getJob().getConfiguration(), predicate);
	}
	'''
	
	def addColumNamesToPredicate(CassandraConfigReader conf, CodegenContext context)'''
		«IF conf.getCassandraColumnName != null»
			«context.addImport("org.apache.cassandra.thrift.SlicePredicate")»
			SlicePredicate predicate = new SlicePredicate();
			«context.addImport("java.nio.ByteBuffer")»
			«val ColumnNames = (conf.getCassandraColumnName as String).split(" ")»
			«FOR cn : ColumnNames»
				predicate.addToColumn_names(ByteBuffer.wrap("«cn»".getBytes()));
			«ENDFOR»
		«ELSE»
			//ColumnNames aren't set
		«ENDIF»
	'''
	
	
	
	override generareOutputResouce(Resource res, CharSequence controledJobName , CodegenContext context) '''
	{/*CassandraResourceHadoop
		*/
		 «val conf = new CassandraConfigReader(res.configurationString)»
		 «context.addImport("org.apache.cassandra.hadoop.ColumnFamilyOutputFormat")»
		 «controledJobName».getJob().setOutputFormatClass(ColumnFamilyOutputFormat.class);
		 «context.addImport("org.apache.cassandra.hadoop.ConfigHelper")»
		ConfigHelper.setOutputColumnFamily(«controledJobName».getJob().getConfiguration(),
			"«conf.getCassandraResourceKeyspace»", //Keyspace
			"«conf.getCassandraResourceColumnFamily»"); //ColumnFamily
	}
	'''
	
	override getCodeStyle() {
		HadoopCodeGen.codeStyle
	}

}