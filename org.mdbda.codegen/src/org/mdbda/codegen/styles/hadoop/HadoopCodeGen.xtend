package org.mdbda.codegen.styles.hadoop

import org.eclipse.xtext.generator.IFileSystemAccess
import org.mdbda.model.MDBDAModelRoot
import org.mdbda.codegen.CodegenContext
import org.mdbda.codegen.MDBDACodegenerator
import org.eclipse.emf.ecore.resource.Resource
import org.mdbda.model.Task
import org.mdbda.model.Workflow
import org.mdbda.codegen.helper.MDBDAConfiguration
import org.mdbda.model.ModelFactory
import org.mdbda.codegen.helper.CodeGenHelper
import org.mdbda.codegen.CodeGeneratorRegistry
import org.mdbda.codegen.styles.ICodeGen

class HadoopCodeGen implements ICodeGen{
	
	public static val codeStyle = "Hadoop"
	
	public static val TEMPLATETASK_HADOOP_JOB_CONFIG  = "TEMPLATETASK_HADOOP_JOB_CONFIG";
	public static val TEMPLATETASK_HADOOP_TEMPOUTPUTS = "TEMPLATETASK_HADOOP_TEMPOUTPUTS";
	public static val TEMPLATETASK_HADOOP_INPUT		  = "TEMPLATETASK_HADOOP_INPUT";
	public static val TEMPLATETASK_HADOOP_OUTPUT	  = "TEMPLATETASK_HADOOP_OUTPUT";
	
	val	codeGenReg = CodeGeneratorRegistry.get
	
	override doGenerate(Resource emfInputResource, IFileSystemAccess fsa, String codeGenStyle, MDBDACodegenerator codegen) {
		if(codeGenStyle == null || !codeGenStyle.equals(codeStyle) ){
			return;
		}
		//patternTemplates.put("test",[Pattern p | '''//something funny Lambda «p.name» '''])
			
		for(root: emfInputResource.allContents.toIterable.filter(MDBDAModelRoot)){
			//MR
			var context = new CodegenContext(fsa,root.name + 'JobConfiguration.java','',codeStyle)
			codegen.genFile(root.jobConfiguration(context),context)
		}
		
		//generate MapReduce
		for(pattern: emfInputResource.allContents.toIterable.filter(Task)){
			if(! (pattern instanceof Workflow)){
				var context = new CodegenContext(fsa,CodeGenHelper.getMapReduceClassNameFromPattern(pattern) + '.java','',codeStyle)
				codegen.genFile(pattern.genMapReduceTaskClass(context),context)
			}
		}

		//generate MRUnit Test
		for(pattern: emfInputResource.allContents.toIterable.filter(Task)){
			if(! (pattern instanceof Workflow)){
				var context = new CodegenContext(fsa,CodeGenHelper.getMapReduceTestClassNameFromPattern(pattern) + '.java','',codeStyle)
				codegen.genFile(MRUnitTestCodeGenerator.genMapReducePatternTestClass(pattern,context),context)
			
			}
		}
	}
	
	def CharSequence genMapReduceTaskClass(Task task, CodegenContext context)'''
		public class «CodeGenHelper.getMapReduceClassNameFromPattern(task)» {
			«context.addImport("org.apache.hadoop.conf.Configuration")»
			«context.addImport("java.io.IOException")»
			
			public static Configuration getJobConf(Configuration conf)  throws IOException{
				«IF codeGenReg.existGenerator(context.codeStyle,task.typeId)»
					« codeGenReg.getGenerator(context.codeStyle,task.typeId).doCodagenTemplateTask(TEMPLATETASK_HADOOP_JOB_CONFIG, context, task)»
				«ELSE»
					//keine implementierung in CodeGeneratorRegistry fuer «task.typeId» vorhanden
					return null;
				«ENDIF»
			}
		
		«IF codeGenReg.existGenerator(context.codeStyle,task.typeId)»
			«codeGenReg.getGenerator(context.codeStyle,task.typeId).doCodagenTemplateTask(ICodeGen.TEMPLATETASK_MDBDA_TASK, context, task)»
		«ELSE»
			//keine implementierung in CodeGeneratorRegistry fuer «task.typeId» vorhanden
		«ENDIF»
		}
		
	'''
	
		
	def CharSequence genTempOutputs(Task task, CodegenContext context)'''
		«IF codeGenReg.existGenerator(context.codeStyle,task.typeId)»
			«codeGenReg.getGenerator(context.codeStyle,task.typeId).doCodagenTemplateTask(TEMPLATETASK_HADOOP_TEMPOUTPUTS, context, task)»
		«ELSE»
			//keine implementierung in CodeGeneratorRegistry fuer «task.typeId» vorhanden
		«ENDIF»
	'''
		
	def CharSequence jobConfiguration(MDBDAModelRoot modelRoot, CodegenContext context)'''
		class «modelRoot.name»JobConfiguration{
			/**
			  * name    = «modelRoot.name»
			  *	author  = «modelRoot.author»
			  * version = «modelRoot.version»
			  * @throws IOException 
			*/			
			«context.addImport("java.io.IOException")»
			public static void main(String... args) throws IOException{
					«context.addImport("org.apache.hadoop.conf.Configuration")»
					Configuration conf = new Configuration();
					«modelRoot.rootWorkflow.genWorkflowConfiguration(context)»
					«modelRoot.rootWorkflow.genResources(context)»
			}
		}
	'''
	
	def CharSequence genResources(Workflow workflow, CodegenContext context)'''
	«FOR pattern : workflow.tasks»
		«FOR input : pattern.inputResources»
			«IF input instanceof Workflow»
				//Workflow input ... TODO
			«ELSEIF input instanceof Task»
				«genIntermediateResourceConfig(pattern,input as Task, context)»
			«ELSE»
				«genInputResourceConfig(pattern,input,context)»
			«ENDIF»
		«ENDFOR»
		
		«FOR output : pattern.outputResources.filter[outputResources.empty]»
			«genOutputResourceConfig(pattern,output,context)»
		«ENDFOR»
	«ENDFOR»
	«/*TODO: diagram output Resources*/»
	'''
	
	def genIntermediateResourceConfig(Task to, Task from, CodegenContext context) '''
		«val intermediateResourceName = "intermRes" + from.name + "2" + to.name»
		«val intermediateResourcePath = "/temp/" + intermediateResourceName»
		«context.addTempResource(intermediateResourcePath)»
		«val MDBDAConfiguration config = new MDBDAConfiguration()»
		«config.setHDFSPath(intermediateResourcePath)»
		«val org.mdbda.model.Resource intermediateResource = ModelFactory.eINSTANCE.createResource»
		«intermediateResource.setConfigurationString(config.writeConfigString)»
		«intermediateResource.setTypeId("HDFSResource")»«/*TODO: ResourcesTemplateConstatns.RESOURCETYPE_HDFS hdfs is now a plugin*/»
		«intermediateResource.setName(intermediateResourceName)»
		//in
		«genInputResourceConfig(to,intermediateResource,context)»
		//out
		«genOutputResourceConfig(from,intermediateResource,context)»
		
	''' 
	
	def CharSequence  genInputResourceConfig(Task task, org.mdbda.model.Resource resource, CodegenContext context) '''
		//«resource.name»
		«IF codeGenReg.existGenerator(context.codeStyle,task.typeId)»
			«codeGenReg.getGenerator(context.codeStyle,task.typeId).doCodagenTemplateTask(TEMPLATETASK_HADOOP_INPUT,context,task, resource)»
			«/*resourceTemplates.get(resource.typeId).generareInputResouce(resource, task ,CodeGenHelper.getMapReduceControlledJobVarName(task), context)*/»
		«ELSE»
			//NOT IMPLEMENTED «resource.typeId»
		«ENDIF»
	'''
	
	def CharSequence  genOutputResourceConfig(Task task, org.mdbda.model.Resource resource, CodegenContext context) '''
		//«resource.name»
		«IF codeGenReg.existGenerator(context.codeStyle,task.typeId)»
			«codeGenReg.getGenerator(context.codeStyle,task.typeId).doCodagenTemplateTask(TEMPLATETASK_HADOOP_OUTPUT,context,task, resource)»
			«/*resourceTemplates.get(resource.typeId).generareOutputResouce(resource,CodeGenHelper.getMapReduceControlledJobVarName(pattern), context)*/»
		«ELSE»
			//NOT IMPLEMENTED «resource.typeId»
		«ENDIF»
	'''
	
		 
	def CharSequence genWorkflowConfiguration(Workflow workflow, CodegenContext context)'''
	//JobControl
	«FOR pattern : workflow.tasks»
			//pattern conf: «pattern.name»
			«context.addImport("org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob")»
			ControlledJob «CodeGenHelper.getMapReduceControlledJobVarName(pattern)» = new ControlledJob(
					«CodeGenHelper.getMapReduceClassNameFromPattern(pattern)».getJobConf(conf));
	«ENDFOR»
	//JobHierarchie
	«FOR pattern : workflow.tasks»
		«FOR dep : pattern.inputResources»
			«IF dep instanceof Task»
				«CodeGenHelper.getMapReduceControlledJobVarName(pattern)».addDependingJob(«CodeGenHelper.getMapReduceControlledJobVarName(pattern)»);
			«ENDIF»
		«ENDFOR»
	«ENDFOR»
	'''
}