package org.mdbda.codegen

import org.mdbda.codegen.IMapReducePatternTemplate
import org.mdbda.model.Task
import static extension org.mdbda.codegen.helper.ConfigurationReader.*;
import org.mdbda.codegen.helper.CodeGenHelper
import org.mdbda.codegen.helper.MDBDAConfiguration
import org.mdbda.model.Workflow
import org.mdbda.codegen.helper.ConfigurationReader

class DefaultMapReducePatternTemplate implements IMapReducePatternTemplate {
	
	
	
	override generareMapReducePattern(Task pattern, CodegenContext context) '''
			«context.addImport("org.apache.hadoop.mapreduce.Mapper")»
			«context.addImport("org.apache.hadoop.io.*")»
			«context.addImport("java.io.IOException")»
			
		//org.mdbda.codegen.DefaultMapReducePatternTemplate
			«genMapperClass(pattern,context)»
			«genReducerClass(pattern,context)»
			«genPartitonerClass(pattern,context)»
			
	'''
	def genPartitonerClass(Task pattern, CodegenContext context)'''
		«val config = MDBDAConfiguration.readConfigString(pattern.configurationString)»
		«val funktion = config.partitioner»
		«IF funktion != null»
			«context.addImport("org.apache.hadoop.mapreduce.Partitioner")»
			public static class «CodeGenHelper.getPartitonerInnderClassName(pattern)» extends Partitioner<«config.getPartitionerKEY(funktion)»,«config.getPartitionerVALUE(funktion)»>{
				«val fields = config.getFields(funktion)»
				«IF fields != null»
					«CodeGenHelper.beautifyJava(fields,0)»
					
				«ENDIF»
				@Override
				public int getPartition(«config.getPartitionerKEY(funktion)» key, «config.getPartitionerVALUE(funktion)» value, int numPartitions) {
					«CodeGenHelper.beautifyJava(config.getPartitionFunction(funktion),0)»
				}
			}
		«ENDIF»
	'''
	
	def genReducerClass(Task pattern, CodegenContext context)'''
		«val config = MDBDAConfiguration.readConfigString(pattern.configurationString)»
		«val funktion = config.reduceFunction»
		«IF funktion != null»
			«context.addImport("org.apache.hadoop.mapreduce.Reducer")»
			«val Reducer = "Reducer<" + config.getKEYIN(funktion) + "," +config.getVALUEIN(funktion)+ "," +config.getKEYOUT(funktion)+ "," +config.getVALUEOUT(funktion) + ">"»
			public static class «CodeGenHelper.getReducerInnderClassName(pattern)» extends «Reducer»{
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
				«val cleanup = config.getCleanup(funktion)»
				«IF cleanup != null»
					@Override
					protected void cleanup(org.apache.hadoop.mapreduce.Mapper.Context context)
						throws IOException, InterruptedException {
						«CodeGenHelper.beautifyJava(cleanup,0)»
					}
					
				«ENDIF»
				@Override
				protected void reduce(«config.getKEYIN(funktion)» key, Iterable<«config.getVALUEIN(funktion)»> values, «Reducer».Context context)
						throws IOException, InterruptedException {
					//in: «config.getTestInput(funktion)»
					«CodeGenHelper.beautifyJava(config.getFunction(funktion),0)»
					//out: «config.getTestOutput(funktion)»
				}
			}
		«ENDIF»
	'''
	
	def genMapperClass(Task pattern, CodegenContext context)'''
		«val config = MDBDAConfiguration.readConfigString(pattern.configurationString)»
		«val funktion = config.mapFunction»
		«val Mapper = "Mapper<" + config.getKEYIN(funktion) + "," +config.getVALUEIN(funktion)+ "," +config.getKEYOUT(funktion)+ "," +config.getVALUEOUT(funktion) + ">"»
		public static class «CodeGenHelper.getMapperInnderClassName(pattern)» extends «Mapper» {

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
		«IF config.mapFunction != null»
			job.setMapperClass(«CodeGenHelper.getMapperInnderClassName(pattern)».class);
		«ENDIF»
		
		«val jobConfig = config.jobConfig»
		«IF jobConfig != null»
			«CodeGenHelper.beautifyJava(jobConfig,0)»
		«ENDIF»
		
		return job.getConfiguration();
	'''
	
	
	override genTempOutputs(Task pattern, CodegenContext context)'''
	
		«val diagramConfig = MDBDAConfiguration.readConfigString(pattern.workflow.diagram.configurationString)»
		«var needsTempOutput = false»
		«FOR outputResource : pattern.outputResources»
			«IF outputResource instanceof Workflow || outputResource instanceof Task»
				«needsTempOutput = true»
			«ENDIF»«/* ist eine Resource */»
		«ENDFOR»
		
		«IF needsTempOutput»
			«context.addImport("org.apache.hadoop.fs.Path")»
			«val tmpPathName = "tempRessourceFor" + pattern.name»
			
			Path «tmpPathName» = new Path("«diagramConfig.HDFSPath»");
		«ENDIF»
	'''

	
}