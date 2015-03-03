package org.mdbda.codegen

import org.mdbda.model.Task
import org.mdbda.codegen.helper.MDBDAConfiguration
import org.mdbda.codegen.helper.CodeGenHelper
import org.json.simple.JSONObject

class MultipleInputMapReducePatternTemplate extends DefaultMapReducePatternTemplate {
	
	override genMapperClass(Task pattern, CodegenContext context)'''
		«val config = MDBDAConfiguration.readConfigString(pattern.configurationString)»
		«val funktions = config.getMultipleMapFunction()»
		
		«var int fooCount = 0»
		«FOR foo : funktions»
			«val JSONObject funktion = foo as JSONObject»
			«val Mapper = "Mapper<" + config.getKEYIN(funktion) + "," +config.getVALUEIN(funktion)+ "," +config.getKEYOUT(funktion)+ "," +config.getVALUEOUT(funktion) + ">"»
			
			public static class «CodeGenHelper.getMapperInnderClassName(pattern)»«fooCount» extends «Mapper» {
				
				«val fields = config.getFields(funktion)»
				«IF fields != null»
					«CodeGenHelper.beautifyJava(fields,0)»
					
				«ENDIF»
				«val setup = config.getSetup(funktion)»
				«IF setup != null»
					@Override
					protected void setup(org.apache.hadoop.mapreduce.Mapper.Context context)
						throws IOException, InterruptedException {
						«CodeGenHelper.beautifyJava(setup,0)»
					}
					
				«ENDIF»
				@Override
				public void map(«config.getKEYIN(funktion)» key, «config.getVALUEIN(funktion)» value, «Mapper».Context context) throws IOException, InterruptedException{
					//in: «config.getTestInput(funktion)»
					«CodeGenHelper.beautifyJava(config.getFunction(funktion),0)»
					//out: «config.getTestOutput(funktion)»
				}
				«val cleanup = config.getCleanup(funktion)»
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
	
	override genJobConf(Task pattern, CodegenContext context) '''	
		«val config = MDBDAConfiguration.readConfigString(pattern.configurationString)»
		//org.mdbda.codegen.DefaultMapReducePatternTemplate
		
		«context.addImport("org.apache.hadoop.mapreduce.Job")»
		Job job = Job.getInstance(conf, "«CodeGenHelper.getMapReduceClassNameFromPattern(pattern)» awesome MDBDA Job");
		
		«IF config.partitioner != null»
			job.setPartitionerClass(«CodeGenHelper.getPartitonerInnderClassName(pattern)».class);
		«ENDIF»
		«IF config.reduceFunction != null»
			job.setReducerClass(«CodeGenHelper.getReducerInnderClassName(pattern)».class);
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