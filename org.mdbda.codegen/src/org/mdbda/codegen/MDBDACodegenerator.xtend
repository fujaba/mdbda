package org.mdbda.codegen

import java.util.HashMap
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import org.mdbda.codegen.helper.CodeGenHelper
import org.mdbda.codegen.helper.MDBDAConfiguration
import org.mdbda.model.MDBDADiagram
import org.mdbda.model.ModelFactory
import org.mdbda.model.Task
import org.mdbda.model.Workflow
import org.eclipse.core.runtime.IExtensionRegistry
import org.eclipse.core.runtime.Platform
import org.eclipse.core.runtime.IConfigurationElement

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
	
	public static def addPatternTemplate(String patternId, IPatternTemplate template ){
		patternTemplates.put(patternId,template);		
	}
	
	public static def addResourceTemplates(String patternId, IResourceTemplate template ){
		resourceTemplates.put(patternId,template);		
	}
	 
	
	
	public def init(){
		
		val IExtensionRegistry reg = Platform.getExtensionRegistry();
	    var IConfigurationElement[] elements = reg.getConfigurationElementsFor("org.mdbda.codegen.plugin");
	    
	    for(IConfigurationElement cEl : elements){
	    	var clazzName = cEl.getAttribute("ResourceTemplateClass")
	    	if(clazzName != null){
		    	val clazz = Platform.getBundle(cEl.getContributor().getName()).loadClass(clazzName) as Class<IResourceTemplate>
		    	//	addResourceTemplates(ResourcesTemplateConstatns.RESOURCETYPE_CASSANDRA,new CassandraResource);
		    	addResourceTemplates(cEl.getAttribute("typeId"),  clazz.newInstance)
	    	}
	    	
	    	clazzName = cEl.getAttribute("PatternTemplateClass")
	    	if(clazzName != null){
	    		var Class<IPatternTemplate> clazz = null
	    		try{
		    		clazz = Platform.getBundle(cEl.getContributor().getName()).loadClass(clazzName) as Class<IPatternTemplate>
		    	}catch(ClassNotFoundException e){
		    		clazz = Platform.getBundle("org.mdbda.codegen").loadClass(clazzName) as Class<IPatternTemplate>
		    	}
		    	addPatternTemplate(cEl.getAttribute("typeId"),  clazz.newInstance)
	    	}
	    	
	    }
		
		
		
		
//		addPatternTemplate(SummatizationPatternTemplateConstatns.NumericalSummarization, new NumericalSummarizationPattern)
//		addPatternTemplate(SummatizationPatternTemplateConstatns.CustomCalculation, new DefaultPatternTemplate)

		//addPatternTemplate(DataOrganizationPatternTemplateConstatns.Binning, new DefaultPatternTemplate)
//		addPatternTemplate(DataOrganizationPatternTemplateConstatns.Partitioning, new DefaultPatternTemplate)
//		addPatternTemplate(DataOrganizationPatternTemplateConstatns.Shuffling, new DefaultPatternTemplate)
//		addPatternTemplate(DataOrganizationPatternTemplateConstatns.StructuredToHierachical, new DefaultPatternTemplate)
//		addPatternTemplate(DataOrganizationPatternTemplateConstatns.TotalOrderSorting, new DefaultPatternTemplate)

//		addPatternTemplate(FilteringPatternTemplateConstatns.BloomFiltering, new DefaultPatternTemplate)
//		addPatternTemplate(FilteringPatternTemplateConstatns.Distinct, new DefaultPatternTemplate)
//		addPatternTemplate(FilteringPatternTemplateConstatns.TopTen, new DefaultPatternTemplate)


//		addPatternTemplate(JoinPatternTemplateConstatns.CartesianProduct, new MultipleInputTemplate)
//		addPatternTemplate(JoinPatternTemplateConstatns.CompositeJoin, new MultipleInputTemplate)
//		addPatternTemplate(JoinPatternTemplateConstatns.ReduceSideJoin, new MultipleInputTemplate)
//		addPatternTemplate(JoinPatternTemplateConstatns.ReplicatedJoin, new MultipleInputTemplate)
		
	}
	
	override doGenerate(Resource input, IFileSystemAccess fsa) {
		//patternTemplates.put("test",[Pattern p | '''//something funny Lambda «p.name» '''])
			
		for(root: input.allContents.toIterable.filter(MDBDADiagram)){
			//MR
			var context = new CodegenContext(fsa,root.name + 'JobConfiguration.java','')
			genFile(root.jobConfiguration(context),context)
			
			//Storm
			context = new CodegenContext(fsa,root.name + 'StormTopology.java','')
			genFile(root.stormTopology(context),context)
		}
		
		//generate MapReduce
		for(pattern: input.allContents.toIterable.filter(Task)){
			if(! (pattern instanceof Workflow)){
				var context = new CodegenContext(fsa,CodeGenHelper.getMapReduceClassNameFromPattern(pattern) + '.java','')
				genFile(pattern.genMapReducePatternClass(context),context)
			}
		}
		//generate Storm
		for(pattern: input.allContents.toIterable.filter(Task)){
			if(! (pattern instanceof Workflow)){
				var context = new CodegenContext(fsa,CodeGenHelper.getStormClassNameFromPattern(pattern) + '.java','')
				genFile(pattern.genStormPatternClass(context),context)
			}
		}
		
		//generate MRUnit Test
		for(pattern: input.allContents.toIterable.filter(Task)){
			if(! (pattern instanceof Workflow)){
				var context = new CodegenContext(fsa,CodeGenHelper.getMapReduceTestClassNameFromPattern(pattern) + '.java','')
				genFile(MRUnitTestCodeGenerator.genMapReducePatternTestClass(pattern,context),context)
			
			}
		}
	}
	
	def CharSequence stormTopology(MDBDADiagram diagram, CodegenContext context)'''
		class «diagram.name»StormTopology{
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
					«diagram.rootWorkflow.genWorkflowConfiguration(context)»
					«diagram.rootWorkflow.genResources(context)»
			}
		}
	'''
	
	
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
					«diagram.rootWorkflow.genWorkflowConfiguration(context)»
					«diagram.rootWorkflow.genResources(context)»
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
	
	def CharSequence  genInputResourceConfig(Task pattern, org.mdbda.model.Resource resource, CodegenContext context) '''
		//«resource.name»
		«IF resourceTemplates.containsKey(resource.typeId)»
			«resourceTemplates.get(resource.typeId).generareMapReduceInputResouce(resource, pattern ,CodeGenHelper.getMapReduceControlledJobVarName(pattern), context)»
		«ELSE»
			//NOT IMPLEMENTED «resource.typeId»
		«ENDIF»
	'''
	
	def CharSequence  genOutputResourceConfig(Task pattern, org.mdbda.model.Resource resource, CodegenContext context) '''
		//«resource.name»
		«IF resourceTemplates.containsKey(resource.typeId)»
			«resourceTemplates.get(resource.typeId).generareMapReduceOutputResouce(resource,CodeGenHelper.getMapReduceControlledJobVarName(pattern), context)»
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
	
	
	def CharSequence genMapReducePatternClass(Task p, CodegenContext context)'''
		public class «CodeGenHelper.getMapReduceClassNameFromPattern(p)» {
			«context.addImport("org.apache.hadoop.conf.Configuration")»
			«context.addImport("java.io.IOException")»
			
			public static Configuration getJobConf(Configuration conf)  throws IOException{		
				«IF patternTemplates.containsKey(p.typeId)»
					«patternTemplates.get(p.typeId).genJobConf(p, context)»
				«ELSE»
					//keine implementierung in patternTemplates fuer «p.typeId» vorhanden
					return null;
				«ENDIF»
			}
		
		«IF patternTemplates.containsKey(p.typeId)»
			«patternTemplates.get(p.typeId).generareMapReducePattern(p, context)»
		«ELSE»
			//keine implementierung in patternTemplates fuer «p.typeId» vorhanden
		«ENDIF»
		}
		
	'''
	
	def CharSequence genTempOutputs(Task p, CodegenContext context)'''
		«IF patternTemplates.containsKey(p.typeId)»
			«patternTemplates.get(p.typeId).genTempOutputs(p, context)»
		«ELSE»
			//keine implementierung in patternTemplates fuer «p.typeId» vorhanden
		«ENDIF»
	'''
	
	def CharSequence genStormPatternClass(Task p, CodegenContext context)'''
		«context.addImport("java.io.Serializable")»
		public class «CodeGenHelper.getStormClassNameFromPattern(p)» implements Serializable {
		«IF patternTemplates.containsKey(p.typeId)»
			«patternTemplates.get(p.typeId).generareStormPattern(p, context)»
		«ELSE»
			//keine implementierung in patternTemplates fuer "«p.typeId»" vorhanden («p.class»)
		«ENDIF»
		}
	'''
}
	