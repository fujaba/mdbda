package org.mdbda.join.reducesidejoin.codegen

import org.mdbda.codegen.styles.hadoop.DefaultMapReducePatternTemplate
import org.mdbda.model.Task
import org.mdbda.codegen.CodegenContext
import org.mdbda.codegen.helper.MDBDAConfiguration
import org.mdbda.codegen.helper.CodeGenHelper
import org.mdbda.model.Resource

class ReduceSideJoinHadoop extends DefaultMapReducePatternTemplate{
	
	override genJobConf(Task task, CodegenContext context) '''
		«val config = new MDBDAConfiguration(task.configurationString)»
		//org.mdbda.codegen.DefaultMapReducePatternTemplate
		
		«context.addImport("org.apache.hadoop.mapreduce.Job")»
		Job job = Job.getInstance(conf, "«CodeGenHelper.getMapReduceClassNameFromPattern(task)» awesome MDBDA Job");
		job.setJarByClass(«CodeGenHelper.getMapReduceClassNameFromPattern(task)».class);
		
		«genPartitionerConfig(task,context)»
		«genReducerConfig(task,context)»
		«genMapperConfig(task,context)»
		
		«val jobConfig = config.jobConfig»
		«IF jobConfig != null»
			«CodeGenHelper.beautifyJava(jobConfig,0)»
		«ENDIF»
		
		return job.getConfiguration();
	'''
	
	//geht nur mit hdfs resourcen
	override genMapperConfig(Task task, CodegenContext context) {
		var ret = '''
		//ReduceSideJoinHadoop MultiMap config 
		'''
		for(Resource input : task.inputResources){
			ret += '''
				//«input.name» «input.typeId»
			'''
			switch(input){
				Task:
					ret += genIntermediateMapperConf(task, input as Task, context)
				Resource:
					switch(input.typeId){
						case "HDFSResource":
							ret +=  genHDFSResourceMapperConf(task, input , context)				
						default: 
							ret +=  '''
							ERROR: «input.typeId» is not Supported.. jet
							'''
					}
					
				default:
					ret +=  '''?????'''
			}
		}
		return ret
	}
	
	def CharSequence genHDFSResourceMapperConf(Task task, Resource resource, CodegenContext context) '''
	
	'''
	
	def CharSequence genIntermediateMapperConf(Task task, Task inputTask, CodegenContext context) '''
	
	'''
	
	override genMapperClass(Task task, CodegenContext context) {
		var ret = '''
		//ReduceSideJoinHadoop MultiMapper 
		'''
		for(Resource input : task.inputResources){
			ret += genMultiMapperClass(task,input,context)
		}
		return ret
	}
	
	//«val Mapper = "Mapper<" + config.getKEYIN(function) + "," +config.getVALUEIN(function)+ "," +config.getKEYOUT(function)+ "," +config.getVALUEOUT(function) + ">"»		
	def genMultiMapperClass(Task task, Resource input, CodegenContext context	)'''	
		«val config = new MDBDAConfiguration(task.configurationString)»
			public static class «CodeGenHelper.getMapperInnerClassName(task, input)» extends Mapper«/*Mapper*/» {
				
			}
	'''

}