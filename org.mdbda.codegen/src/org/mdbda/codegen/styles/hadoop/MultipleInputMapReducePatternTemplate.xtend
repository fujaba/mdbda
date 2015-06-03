package org.mdbda.codegen.styles.hadoop

import org.mdbda.model.Task
import org.mdbda.codegen.helper.MDBDAConfiguration
import org.mdbda.codegen.helper.CodeGenHelper
import org.json.simple.JSONObject

class MultipleInputMapReducePatternTemplate extends DefaultMapReducePatternTemplate {
	
	override genMapperClass(Task pattern, org.mdbda.codegen.CodegenContext context)'''
		«val config = new MDBDAConfiguration(pattern.configurationString)»
		«val functions = config.getMultipleMapFunction()»
		
		«var int fooCount = 0»
		«FOR foo : functions»
			«val JSONObject function = foo as JSONObject»
			«val Mapper = "Mapper<" + config.getKEYIN(function) + "," +config.getVALUEIN(function)+ "," +config.getKEYOUT(function)+ "," +config.getVALUEOUT(function) + ">"»
			
			public static class «CodeGenHelper.getMapperInnerClassName(pattern)»«fooCount» extends «Mapper» {
				
				«val fields = config.getFields(function)»
				«IF fields != null»
					«CodeGenHelper.beautifyJava(fields,0)»
					
				«ENDIF»
				«val setup = config.getSetup(function)»
				«IF setup != null»
					@Override
					protected void setup(org.apache.hadoop.mapreduce.Mapper.Context context)
						throws IOException, InterruptedException {
						«CodeGenHelper.beautifyJava(setup,0)»
					}
					
				«ENDIF»
				@Override
				public void map(«config.getKEYIN(function)» key, «config.getVALUEIN(function)» value, «Mapper».Context context) throws IOException, InterruptedException{
					//in: «config.getTestInput(function)»
					«CodeGenHelper.beautifyJava(config.getFunction(function),0)»
					//out: «config.getTestOutput(function)»
				}
				«val cleanup = config.getCleanup(function)»
				«IF cleanup != null»
					@Override
					protected void cleanup(org.apache.hadoop.mapreduce.Mapper.Context context)
						throws IOException, InterruptedException {
						«CodeGenHelper.beautifyJava(cleanup,0)»
					}
				«ENDIF»
			}
			«{fooCount = fooCount + 1 ; ""}»
		«ENDFOR»
	'''
		
	override genJobConf(Task pattern, org.mdbda.codegen.CodegenContext context) '''	
		«val config = new MDBDAConfiguration(pattern.configurationString)»
		//org.mdbda.codegen.DefaultMapReducePatternTemplate
		
		«context.addImport("org.apache.hadoop.mapreduce.Job")»
		Job job = Job.getInstance(conf, "«CodeGenHelper.getMapReduceClassNameFromPattern(pattern)» awesome MDBDA Job");
		
		«IF config.partitioner != null»
			job.setPartitionerClass(«CodeGenHelper.getPartitonerInnerClassName(pattern)».class);
		«ENDIF»
		«IF config.reduceFunction != null»
			job.setReducerClass(«CodeGenHelper.getReducerInnerClassName(pattern)».class);
		«ENDIF»
		«IF config.getMultipleMapFunction() != null»
			//job.setMapperClass(<CodeGenHelper.getMapperInnderClassName(pattern)>.class);
			
		«ENDIF»
		
		«val jobConfig = config.jobConfig»
		«IF jobConfig != null»
			«CodeGenHelper.beautifyJava(jobConfig,0)»
		«ENDIF»
		
		return job.getConfiguration();
	'''
}