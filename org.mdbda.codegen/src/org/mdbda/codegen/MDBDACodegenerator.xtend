package org.mdbda.codegen

import java.util.HashMap
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.emf.common.util.URI
import org.mdbda.model.ModelPackage
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.xtext.generator.JavaIoFileSystemAccess
import com.google.inject.Guice
import org.eclipse.xtext.service.AbstractGenericModule
import org.eclipse.xtext.parser.IEncodingProvider
import org.mdbda.model.ResourcesTemplateConstatns
import org.mdbda.codegen.plugins.HDFSResource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.mdbda.model.MDBDADiagram
import org.mdbda.model.Pattern
import org.mdbda.codegen.helper.CodeGenHelper
import org.mdbda.model.Workflow

class MDBDACodegenerator implements IGenerator {
	
	static var patternTemplates = new HashMap<String, IPatternTemplate>();
	static var resourceTemplates = new HashMap<String, IResourceTemplate>();
	
	var jobResourcesGeneratedName = new HashMap<String, String>();

	def addJobResource(org.mdbda.model.Resource res, String genName){
		jobResourcesGeneratedName.put(res.name, genName)
	} 
	
	def getJobResourceGenName(org.mdbda.model.Resource res, String genName){
		jobResourcesGeneratedName.put(res.name, genName)
	} 
	
	public static def addPatternTemplat(String patternId, IPatternTemplate template ){
		patternTemplates.put(patternId,template);		
	}
	
	public static def addResourceTemplates(String patternId, IResourceTemplate template ){
		resourceTemplates.put(patternId,template);		
	}
	 
	
	
	public def init(){
		addResourceTemplates(ResourcesTemplateConstatns.RESOURCETYPE_HDFS,new HDFSResource);
	}
	
	override doGenerate(Resource input, IFileSystemAccess fsa) {
		
		//patternTemplates.put("test",[Pattern p | '''//something funny Lambda «p.name» '''])
			
		for(root: input.allContents.toIterable.filter(MDBDADiagram)){
			var context = new CodegenContext(fsa,root.name + 'JobConfiguration.java','')
			genFile(root.jobConfiguration(context),context)
		}
		
		//generate MapReduce
		for(pattern: input.allContents.toIterable.filter(Pattern)){
			var context = new CodegenContext(fsa,CodeGenHelper.getMapReduceClassNameFromPattern(pattern) + '.java','')
			genFile(pattern.genMapReducePatternClass(context),context)
		}
		//generate Storm
		for(pattern: input.allContents.toIterable.filter(Pattern)){
			var context = new CodegenContext(fsa,CodeGenHelper.getStormClassNameFromPattern(pattern) + '.java','')
			genFile(pattern.genStormPatternClass(context),context)			
		}
	} 
	
	
	def genFile(CharSequence content, CodegenContext context){
		
		var sb = new StringBuilder();
		if(context.packageName != ""){
			sb.append("package ")
			sb.append(context.packageName)
			sb.append('\n')
			sb.append('\n')		
		}
		
		for(String imp : context.imports){
			sb.append("import ")
			sb.append(imp)
			sb.append(';')
			sb.append('\n')
		}
		
		sb.append('\n')
		sb.append(content)
		
		context.fileSystemAccess.generateFile(context.fileName, sb.toString);
	}
	
	def CharSequence jobConfiguration(MDBDADiagram diagram, CodegenContext context)'''
	
		class «diagram.name»JobConfiguration{
			/**
			  * name    = «diagram.name»
			  *	author  = «diagram.author»
			  * version = «diagram.version»
			  * @throws IOException 
			*/			
			«context.addImport("java.io.IOException")»
			public static void main(String... args) throws IOException{
					«context.addImport("org.apache.hadoop.conf.Configuration")»
					Configuration conf = new Configuration();
					
					//diagram input resources
					«FOR inputResources : diagram.resources.filter[inputResources.empty]»
					//«inputResources.name»
						«IF resourceTemplates.containsKey(inputResources.typeId)»
							«resourceTemplates.get(inputResources.typeId).generareMapReduceInputResouce(inputResources, context)»
						«ELSE»
							//NOT IMPLEMENTED
						«ENDIF»
					«ENDFOR»
					//rootworkflow input resources
					«FOR inputResources : diagram.rootWorkflow.dataResources.filter[inputResources.empty]»
					//«inputResources.name»
						«IF resourceTemplates.containsKey(inputResources.typeId)»
							«resourceTemplates.get(inputResources.typeId).generareMapReduceInputResouce(inputResources, context)»
						«ELSE»
							//NOT IMPLEMENTED
						«ENDIF»
					«ENDFOR»
					
					//temp i/o resources
					
					«FOR p : diagram.rootWorkflow.pattern»
						«p.genTempOutputs(context)»
					«ENDFOR»
					
					
					
					//diagram output resources
					«FOR inputResources : diagram.resources.filter[outputResources.empty]»
					//«inputResources.name»
						«IF resourceTemplates.containsKey(inputResources.typeId)»
							«resourceTemplates.get(inputResources.typeId).generareMapReduceInputResouce(inputResources, context)»
						«ELSE»
							//NOT IMPLEMENTED
						«ENDIF»
					«ENDFOR»
					//rootworkflow output resources
					«FOR inputResources : diagram.rootWorkflow.dataResources.filter[outputResources.empty]»
					//«inputResources.name»
						«IF resourceTemplates.containsKey(inputResources.typeId)» 
							«resourceTemplates.get(inputResources.typeId).generareMapReduceInputResouce(inputResources, context)»
						«ELSE»
							//NOT IMPLEMENTED
						«ENDIF»
					«ENDFOR»
					
					«diagram.rootWorkflow.genWorkflowConfiguration(context)»
					«FOR pattern : diagram.rootWorkflow.pattern»
						//«pattern.name»
					«ENDFOR»
			}
		}
		'''
	

	
	def CharSequence genWorkflowConfiguration(Workflow workflow, CodegenContext context)'''
	«FOR pattern : workflow.pattern»
			//pattern conf: «pattern.name»
			«context.addImport("org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob")»
			ControlledJob «CodeGenHelper.getMapReduceClassNameFromPattern(pattern)»ControlledJob = new ControlledJob(
					«CodeGenHelper.getMapReduceClassNameFromPattern(pattern)».getJobConf());
	«ENDFOR»
	'''
	
	def CharSequence genMapReducePatternClass(Pattern p, CodegenContext context)'''
			
		class «CodeGenHelper.getMapReduceClassNameFromPattern(p)» {
			«context.addImport("org.apache.hadoop.conf.Configuration")»
			public static Configuration getJobConf(){		
				«IF patternTemplates.containsKey(p.typeId)»
					«patternTemplates.get(p.typeId).genJobConf(p, context)»
				«ELSE»
					//NOT IMPLEMENTED
					return null;
				«ENDIF»
			}
			
		«IF patternTemplates.containsKey(p.typeId)»
			«patternTemplates.get(p.typeId).generareMapReducePattern(p, context)»
		«ELSE»
			//NOT IMPLEMENTED
		«ENDIF»
		}
		
	'''
	
	def CharSequence genTempOutputs(Pattern p, CodegenContext context)'''
		«IF patternTemplates.containsKey(p.typeId)»
			«patternTemplates.get(p.typeId).genTempOutputs(p, context)»
		«ELSE»
			//NOT IMPLEMENTED
		«ENDIF»
	'''
	
	def CharSequence genStormPatternClass(Pattern p, CodegenContext context)'''
		class «CodeGenHelper.getStormClassNameFromPattern(p)» {
		«IF patternTemplates.containsKey(p.typeId)»
			«patternTemplates.get(p.typeId).generareStormBolt(p, context)»
		«ELSE»
			//NOT IMPLEMENTED
		«ENDIF»
		}
	'''
}
	