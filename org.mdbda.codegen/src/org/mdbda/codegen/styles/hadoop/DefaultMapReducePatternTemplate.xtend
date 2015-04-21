package org.mdbda.codegen.styles.hadoop

import org.mdbda.codegen.IMapReducePatternTemplate
import org.mdbda.model.Task
import org.mdbda.codegen.helper.CodeGenHelper
import org.mdbda.codegen.helper.MDBDAConfiguration
import org.mdbda.model.Workflow
import org.mdbda.codegen.styles.hadoop.HadoopCodeGen
import org.mdbda.codegen.CodegenContext
import org.mdbda.model.Resource

class DefaultMapReducePatternTemplate implements IMapReducePatternTemplate {

	override doCodagenTemplateTask(String Task, CodegenContext context, Resource... mdbdaElements) {
		if (context == null) {
			throw new IllegalArgumentException("The argument context can not be null")
		}

		switch (Task) {
			
			case HadoopCodeGen.TEMPLATETASK_HADOOP_JOB_CONFIG: {
				if (helper_doCodagenTemplateTask_argument_mdbdaElements_has_only_one_task_element(mdbdaElements)) {
						genJobConf(mdbdaElements.get(0) as Task, context)
				}
			}
			case HadoopCodeGen.TEMPLATETASK_HADOOP_TEMPOUTPUTS: {
				if (helper_doCodagenTemplateTask_argument_mdbdaElements_has_only_one_task_element(mdbdaElements)) {
						genTempOutputs(mdbdaElements.get(0) as Task, context)
				}
			}
			case HadoopCodeGen.TEMPLATETASK_MDBDA_TASK: {
				if (helper_doCodagenTemplateTask_argument_mdbdaElements_has_only_one_task_element(mdbdaElements)) {
					genTask(mdbdaElements.get(0) as Task, context)
				}
			}
			default:
				throw new UnsupportedOperationException("The codegeneration task " + Task +
					" is not supported for MapReduce")
		}

	}

	def private boolean helper_doCodagenTemplateTask_argument_mdbdaElements_has_only_one_task_element(Resource... mdbdaElements) {
		if (mdbdaElements.size == 1 && mdbdaElements.get(0) instanceof Task) {
			return true
		} else {
			throw new IllegalArgumentException(
				"The argument mdbdaElements has to have only one element of type org.mdbda.model.Task")
		}
	}

	override getCodeStyle() {
		HadoopCodeGen.codeStyle
	}

	def genTask(Task task, CodegenContext context) '''
			«context.addImport("org.apache.hadoop.mapreduce.Mapper")»
			«context.addImport("org.apache.hadoop.io.*")»
			«context.addImport("java.io.IOException")»
			
		//org.mdbda.codegen.DefaultMapReducePatternTemplate
			«genMapperClass(task,context)»
			«genReducerClass(task,context)»
			«genPartitonerClass(task,context)»
			
	'''

	def genPartitonerClass(Task pattern, CodegenContext context) '''
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

	def genReducerClass(Task task, CodegenContext context) '''
		«val config = MDBDAConfiguration.readConfigString(task.configurationString)»
		«val funktion = config.reduceFunction»
		«IF funktion != null»
			«context.addImport("org.apache.hadoop.mapreduce.Reducer")»
			«val Reducer = "Reducer<" + config.getKEYIN(funktion) + "," +config.getVALUEIN(funktion)+ "," +config.getKEYOUT(funktion)+ "," +config.getVALUEOUT(funktion) + ">"»
			public static class «CodeGenHelper.getReducerInnderClassName(task)» extends «Reducer»{
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

	def genMapperClass(Task task,
		CodegenContext context
	) '''
		«val config = MDBDAConfiguration.readConfigString(task.configurationString)»
		«val funktion = config.mapFunction»
		«val Mapper = "Mapper<" + config.getKEYIN(funktion) + "," +config.getVALUEIN(funktion)+ "," +config.getKEYOUT(funktion)+ "," +config.getVALUEOUT(funktion) + ">"»
		public static class «CodeGenHelper.getMapperInnderClassName(task)» extends «Mapper» {
		
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

	override genJobConf(Task task, CodegenContext context) '''	
		«val config = MDBDAConfiguration.readConfigString(task.configurationString)»
		//org.mdbda.codegen.DefaultMapReducePatternTemplate
		
		«context.addImport("org.apache.hadoop.mapreduce.Job")»
		Job job = Job.getInstance(conf, "«CodeGenHelper.getMapReduceClassNameFromPattern(task)» awesome MDBDA Job");
		
		«IF config.partitioner != null»
			job.setPartitionerClass(«CodeGenHelper.getPartitonerInnderClassName(task)».class);
		«ENDIF»
		«IF config.reduceFunction != null»
			job.setReducerClass(«CodeGenHelper.getReducerInnderClassName(task)».class);
		«ENDIF»
		«IF config.mapFunction != null»
			job.setMapperClass(«CodeGenHelper.getMapperInnderClassName(task)».class);
		«ENDIF»
		
		«val jobConfig = config.jobConfig»
		«IF jobConfig != null»
			«CodeGenHelper.beautifyJava(jobConfig,0)»
		«ENDIF»
		
		return job.getConfiguration();
	'''

	override genTempOutputs(Task task, CodegenContext context) '''
		
			«val diagramConfig = MDBDAConfiguration.readConfigString(task.workflow.modelRoot.configurationString)»
			«var needsTempOutput = false»
			«FOR outputResource : task.outputResources»
				«IF outputResource instanceof Workflow || outputResource instanceof Task»
					«needsTempOutput = true»
				«ENDIF»«/* ist eine Resource */»
			«ENDFOR»
			
			«IF needsTempOutput»
				«context.addImport("org.apache.hadoop.fs.Path")»
				«val tmpPathName = "tempRessourceFor" + task.name»
				
				Path «tmpPathName» = new Path("«diagramConfig.getHDFSPath»");
			«ENDIF»
	'''

}