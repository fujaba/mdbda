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
import org.mdbda.codegen.plugins.CassandraResource
import org.mdbda.model.ModelFactory
import org.mdbda.codegen.helper.MDBDAConfiguration
import org.mdbda.model.SummatizationPatternTemplateConstatns
import org.mdbda.codegen.plugins.NumericalSummarizationPattern
import org.mdbda.model.DataOrganizationPatternTemplateConstatns
import org.mdbda.model.FilteringPatternTemplateConstatns
import org.mdbda.model.JoinPatternTemplateConstatns
import static org.mdbda.codegen.MDBDACodegenerator.*
import org.json.simple.JSONArray
import org.json.simple.JSONValue

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
		addResourceTemplates(ResourcesTemplateConstatns.RESOURCETYPE_HDFS,new HDFSResource);
		addResourceTemplates(ResourcesTemplateConstatns.RESOURCETYPE_CASSANDRA,new CassandraResource);
		
		addPatternTemplate(SummatizationPatternTemplateConstatns.NumericalSummarization, new NumericalSummarizationPattern)
		addPatternTemplate(SummatizationPatternTemplateConstatns.CustomCalculation, new DefaultPatternTemplate)
		addPatternTemplate(SummatizationPatternTemplateConstatns.InvertedIndexSummarization, new DefaultPatternTemplate)
		
		addPatternTemplate(DataOrganizationPatternTemplateConstatns.Binning, new DefaultPatternTemplate)
		addPatternTemplate(DataOrganizationPatternTemplateConstatns.Partitioning, new DefaultPatternTemplate)
		addPatternTemplate(DataOrganizationPatternTemplateConstatns.Shuffling, new DefaultPatternTemplate)
		addPatternTemplate(DataOrganizationPatternTemplateConstatns.StructuredToHierachical, new DefaultPatternTemplate)
		addPatternTemplate(DataOrganizationPatternTemplateConstatns.TotalOrderSorting, new DefaultPatternTemplate)

		addPatternTemplate(FilteringPatternTemplateConstatns.BloomFiltering, new DefaultPatternTemplate)
		addPatternTemplate(FilteringPatternTemplateConstatns.Distinct, new DefaultPatternTemplate)
		addPatternTemplate(FilteringPatternTemplateConstatns.SimpleMatcherFilter, new DefaultPatternTemplate)
		addPatternTemplate(FilteringPatternTemplateConstatns.TopTen, new DefaultPatternTemplate)


		addPatternTemplate(JoinPatternTemplateConstatns.CartesianProduct, new DefaultPatternTemplate)
		addPatternTemplate(JoinPatternTemplateConstatns.CompositeJoin, new DefaultPatternTemplate)
		addPatternTemplate(JoinPatternTemplateConstatns.ReduceSideJoin, new DefaultPatternTemplate)
		addPatternTemplate(JoinPatternTemplateConstatns.ReplicatedJoin, new DefaultPatternTemplate)
		
		
		
	}
	
	override doGenerate(Resource input, IFileSystemAccess fsa) {
		//patternTemplates.put("test",[Pattern p | '''//something funny Lambda «p.name» '''])
			
		for(root: input.allContents.toIterable.filter(MDBDADiagram)){
			var context = new CodegenContext(fsa,root.name + 'JobConfiguration.java','')
			genFile(root.jobConfiguration(context),context)
		}
		
		//generate MapReduce
		for(pattern: input.allContents.toIterable.filter(Pattern)){
			if(! (pattern instanceof Workflow)){
				var context = new CodegenContext(fsa,CodeGenHelper.getMapReduceClassNameFromPattern(pattern) + '.java','')
				genFile(pattern.genMapReducePatternClass(context),context)
			}
		}
		//generate Storm
		for(pattern: input.allContents.toIterable.filter(Pattern)){
			var context = new CodegenContext(fsa,CodeGenHelper.getStormClassNameFromPattern(pattern) + '.java','')
			genFile(pattern.genStormPatternClass(context),context)			
		}
		
		//generate MRUnit Test
		for(pattern: input.allContents.toIterable.filter(Pattern)){
			if(! (pattern instanceof Workflow)){
				var context = new CodegenContext(fsa,CodeGenHelper.getMapReduceTestClassNameFromPattern(pattern) + '.java','')
				genFile(pattern.genMapReducePatternTestClass(context),context)
			
			}
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
					«diagram.rootWorkflow.genWorkflowConfiguration(context)»
					«diagram.rootWorkflow.genResources(context)»
			}
		}
	'''
	
	def CharSequence genResources(Workflow workflow, CodegenContext context)'''
	«FOR pattern : workflow.pattern»
		«FOR input : pattern.inputResources»
			«IF input instanceof Workflow»
				//Workflow input ... TODO
			«ELSEIF input instanceof Pattern» 
				«genIntermediateResourceConfig(pattern,input as Pattern, context)»
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
	
	def genIntermediateResourceConfig(Pattern to, Pattern from, CodegenContext context) '''
		«val intermediateResourceName = "intermRes" + from.name + "2" + to.name»
		«val intermediateResourcePath = "/temp/" + intermediateResourceName»
		«context.addTempResource(intermediateResourcePath)»
		«val MDBDAConfiguration config = new MDBDAConfiguration()»
		«config.setHDFSPath(intermediateResourcePath)»
		«val org.mdbda.model.Resource intermediateResource = ModelFactory.eINSTANCE.createResource»
		«intermediateResource.setConfigurationString(config.writeConfigString)»
		«intermediateResource.setTypeId(ResourcesTemplateConstatns.RESOURCETYPE_HDFS)»
		«intermediateResource.setName(intermediateResourceName)»
		//in
		«genInputResourceConfig(to,intermediateResource,context)»
		//out
		«genOutputResourceConfig(from,intermediateResource,context)»
		
	'''
	
	def CharSequence  genInputResourceConfig(Pattern pattern, org.mdbda.model.Resource resource, CodegenContext context) '''
		//«resource.name»
		«IF resourceTemplates.containsKey(resource.typeId)»
			«resourceTemplates.get(resource.typeId).generareMapReduceInputResouce(resource,CodeGenHelper.getMapReduceControlledJobVarName(pattern), context)»
		«ELSE»
			//NOT IMPLEMENTED «resource.typeId» 
		«ENDIF»
	'''
	
	def CharSequence  genOutputResourceConfig(Pattern pattern, org.mdbda.model.Resource resource, CodegenContext context) '''
		//«resource.name»
		«IF resourceTemplates.containsKey(resource.typeId)»
			«resourceTemplates.get(resource.typeId).generareMapReduceOutputResouce(resource,CodeGenHelper.getMapReduceControlledJobVarName(pattern), context)»
		«ELSE»
			//NOT IMPLEMENTED «resource.typeId» 
		«ENDIF»
	'''
	
	def CharSequence genWorkflowConfiguration(Workflow workflow, CodegenContext context)'''
	//JobControl
	«FOR pattern : workflow.pattern»
			//pattern conf: «pattern.name»
			«context.addImport("org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob")»
			ControlledJob «CodeGenHelper.getMapReduceControlledJobVarName(pattern)» = new ControlledJob(
					«CodeGenHelper.getMapReduceClassNameFromPattern(pattern)».getJobConf(conf));
	«ENDFOR»
	//JobHierarchie
	«FOR pattern : workflow.pattern»
		«FOR dep : pattern.inputResources»
			«IF dep instanceof Pattern»
				«CodeGenHelper.getMapReduceControlledJobVarName(pattern)».addDependingJob(«CodeGenHelper.getMapReduceControlledJobVarName(pattern)»);
			«ENDIF»
		«ENDFOR»
	«ENDFOR»
	'''
	
	def CharSequence genMapReducePatternTestClass(Pattern p, CodegenContext context)'''
	
	 public class «CodeGenHelper.getMapReduceTestClassNameFromPattern(p)» {
		«context.addImport("org.apache.hadoop.mrunit.mapreduce.MapDriver")»
		«context.addImport("org.apache.hadoop.mrunit.mapreduce.MapReduceDriver")»
		«context.addImport("org.apache.hadoop.mrunit.mapreduce.ReduceDriver")»
		«val config = MDBDAConfiguration.readConfigString(p.configurationString)»
		«val MapDriver = "MapDriver<" + config.getKEYIN(config.mapFunction) + "," +config.getVALUEIN(config.mapFunction)+ "," +config.getKEYOUT(config.mapFunction)+ "," +config.getVALUEOUT(config.mapFunction) + ">"»
		«context.addImport("org.apache.hadoop.io.*")»
		«context.addImport("org.apache.hadoop.mapreduce.lib.output.*")»
		MapDriver<«config.getKEYIN(config.mapFunction)», «config.getVALUEIN(config.mapFunction)», «config.getKEYOUT(config.mapFunction)», «config.getVALUEOUT(config.mapFunction)»> mapDriver;
	
	«IF config.reduceFunction != null»
		ReduceDriver<«config.getKEYIN(config.reduceFunction)», «config.getVALUEIN(config.reduceFunction)», «config.getKEYOUT(config.reduceFunction)», «config.getVALUEOUT(config.reduceFunction)»> reduceDriver;
		MapReduceDriver<«config.getKEYIN(config.mapFunction)», «config.getVALUEIN(config.mapFunction)»,«config.getKEYIN(config.reduceFunction)», «config.getVALUEIN(config.reduceFunction)», «config.getKEYOUT(config.reduceFunction)», «config.getVALUEOUT(config.reduceFunction)»> mapReduceDriver;
	«ENDIF»
		«context.addImport("org.junit.Before")»
		@Before
		public void setUp() {
		«var mapperClass = CodeGenHelper.getMapReduceClassNameFromPattern(p) + "." + CodeGenHelper.getMapperInnderClassName(p)»
		«var reducerClass = CodeGenHelper.getMapReduceClassNameFromPattern(p) + "." + CodeGenHelper.getReducerInnderClassName(p)»
			«mapperClass» mapper = new «mapperClass»();
			mapDriver = MapDriver.newMapDriver(mapper);
			«IF config.reduceFunction != null»
				«reducerClass» reducer = new «reducerClass»();
				reduceDriver = ReduceDriver.newReduceDriver(reducer);
				mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);
			«ENDIF»
		}
		
	«context.addImport("org.junit.Test")»
	«context.addImport("java.io.IOException")»
		@Test
		public void testMapper() throws IOException {
			«val JSONArray testMapInput = config.getTestInput(config.mapFunction)»
			«FOR inputString : testMapInput »
				«var String[] inputElements = (inputString as String).split(";")»
				mapDriver.withInput(new «config.getKEYIN(config.mapFunction)»(«inputElements.get(0)»), new «config.getVALUEIN(config.mapFunction)»( «inputElements.get(1)» ));
			«ENDFOR»
			«val JSONArray testMapOutput = config.getTestOutput(config.mapFunction)»
			«FOR outputString : testMapOutput »
				«var String[] outputElements = (outputString as String).split(";")»
				mapDriver.withOutput(new «config.getKEYOUT(config.mapFunction)»(«outputElements.get(0)»), new «config.getVALUEOUT(config.mapFunction)»( «outputElements.get(1)» ));
			«ENDFOR»
			
			mapDriver.runTest(false);
			
		}
		
		«IF config.reduceFunction != null»
			@Test
			public void testReducer() throws IOException {
				«context.addImport("java.util.ArrayList")»
				«context.addImport("java.util.List")»
				«val JSONArray testReduceInput = config.getTestInput(config.reduceFunction)»
				«IF testReduceInput.length > 0»
					List<IntWritable> values = null;
					«FOR inputString : testReduceInput »
						«var String[] inputElements = (inputString as String).split(";")»
						values = new ArrayList<IntWritable>();
						«val el = JSONValue.parse(inputElements.get(1)) as JSONArray»
						«FOR n : el»
							values.add(new «config.getVALUEIN(config.reduceFunction)»(«n»));
						«ENDFOR»
						reduceDriver.withInput(new «config.getKEYIN(config.reduceFunction)»(«inputElements.get(0)»), values );
						
					«ENDFOR»
				«ENDIF»
				
				«val JSONArray testReduceOutput = config.getTestOutput(config.reduceFunction)»
				«IF testReduceOutput.length > 0»
				
					«FOR outputString : testReduceOutput »
						«var String[] outputElements = (outputString as String).split(";")»
						reduceDriver.withOutput(new «config.getKEYOUT(config.reduceFunction)»(«outputElements.get(0)»), new «config.getVALUEOUT(config.reduceFunction)»( «outputElements.get(1)» ));
					«ENDFOR»
				«ENDIF»
				reduceDriver.runTest(false);
			}
		«ENDIF»
	}
	'''
	
	def CharSequence genMapReducePatternClass(Pattern p, CodegenContext context)'''
		public class «CodeGenHelper.getMapReduceClassNameFromPattern(p)» {
			«context.addImport("org.apache.hadoop.conf.Configuration")»
			«context.addImport("java.io.IOException")»
			
			public static Configuration getJobConf(Configuration conf)  throws IOException{		
				«IF patternTemplates.containsKey(p.typeId)»
					«patternTemplates.get(p.typeId).genJobConf(p, context)»
				«ELSE»
					//keine implementierung in patternTemplates für «p.typeId» vorhanden
					return null;
				«ENDIF»
			}

			
		«IF patternTemplates.containsKey(p.typeId)»
			«patternTemplates.get(p.typeId).generareMapReducePattern(p, context)»
		«ELSE»
			//keine implementierung in patternTemplates für «p.typeId» vorhanden
		«ENDIF»
		}
		
	'''
	
	def CharSequence genTempOutputs(Pattern p, CodegenContext context)'''
		«IF patternTemplates.containsKey(p.typeId)»
			«patternTemplates.get(p.typeId).genTempOutputs(p, context)»
		«ELSE»
			//keine implementierung in patternTemplates für «p.typeId» vorhanden
		«ENDIF»
	'''
	
	def CharSequence genStormPatternClass(Pattern p, CodegenContext context)'''
		class «CodeGenHelper.getStormClassNameFromPattern(p)» {
		«IF patternTemplates.containsKey(p.typeId)»
			«patternTemplates.get(p.typeId).generareStormBolt(p, context)»
		«ELSE»
			//keine implementierung in patternTemplates für «p.typeId» vorhanden
		«ENDIF»
		}
	'''
}
	