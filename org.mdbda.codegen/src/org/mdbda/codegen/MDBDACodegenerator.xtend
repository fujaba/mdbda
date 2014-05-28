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
import org.mdbda.codegen.plugins.PartitoningPattern
import org.json.JSONArray

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
		addResourceTemplates(ResourcesTemplateConstatns.RESOURCETYPE_CASSANDRA,new CassandraResource);
		
		addPatternTemplat(SummatizationPatternTemplateConstatns.NumericalSummarization, new NumericalSummarizationPattern)
	//	addPatternTemplat(SummatizationPatternTemplateConstatns.CustomCalculation, new )
	//	addPatternTemplat(SummatizationPatternTemplateConstatns.InvertedIndexSummarization, new )
		
//		addPatternTemplat(DataOrganizationPatternTemplateConstatns.Binning, new )
		addPatternTemplat(DataOrganizationPatternTemplateConstatns.Partitioning, new PartitoningPattern)
//		addPatternTemplat(DataOrganizationPatternTemplateConstatns.Shuffling, new )
//		addPatternTemplat(DataOrganizationPatternTemplateConstatns.StructuredToHierachical, new )
//		addPatternTemplat(DataOrganizationPatternTemplateConstatns.TotalOrderSorting, new )

//		addPatternTemplat(FilteringPatternTemplateConstatns.BloomFiltering, new )
//		addPatternTemplat(FilteringPatternTemplateConstatns.Distinct, new )
//		addPatternTemplat(FilteringPatternTemplateConstatns.SimpleMatcherFilter, new )
//		addPatternTemplat(FilteringPatternTemplateConstatns.TopTen, new )


//		addPatternTemplat(JoinPatternTemplateConstatns.CartesianProduct, new )
//		addPatternTemplat(JoinPatternTemplateConstatns.CompositeJoin, new )
//		addPatternTemplat(JoinPatternTemplateConstatns.ReduceSideJoin, new )
//		addPatternTemplat(JoinPatternTemplateConstatns.ReplicatedJoin, new )
		
		
		
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
					«CodeGenHelper.getMapReduceClassNameFromPattern(pattern)».getJobConf());
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
		MapDriver<«config.getKEYIN(config.mapFunction)», «config.getVALUEIN(config.mapFunction)», «config.getKEYOUT(config.mapFunction)», «config.getVALUEOUT(config.mapFunction)»> mapDriver;
		ReduceDriver<«config.getKEYIN(config.reduceFunction)», «config.getVALUEIN(config.reduceFunction)», «config.getKEYOUT(config.reduceFunction)», «config.getVALUEOUT(config.reduceFunction)»> reduceDriver;
		MapReduceDriver<«config.getKEYIN(config.mapFunction)», «config.getVALUEIN(config.mapFunction)»,«config.getKEYIN(config.reduceFunction)», «config.getVALUEIN(config.reduceFunction)», «config.getKEYOUT(config.reduceFunction)», «config.getVALUEOUT(config.reduceFunction)»> mapReduceDriver;
		«context.addImport("org.junit.Before")»
		@Before
		public void setUp() {
			«var mapperClass = CodeGenHelper.getMapReduceClassNameFromPattern(p) + "." + CodeGenHelper.getMapperInnderClassName(p)»
			«var reducerClass = CodeGenHelper.getMapReduceClassNameFromPattern(p) + "." + CodeGenHelper.getReducerInnderClassName(p)»
				«mapperClass» mapper = new «mapperClass»();
				«reducerClass» reducer = new «reducerClass»();
				mapDriver = MapDriver.newMapDriver(mapper);
				reduceDriver = ReduceDriver.newReduceDriver(reducer);
				mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);
			}
			
		«context.addImport("org.junit.Test")»
		«context.addImport("java.io.IOException")»
			@Test
			public void testMapper() throws IOException {
				«val JSONArray testMapInput = config.getTestInput(config.mapFunction)»
				«FOR Integer i : (0..testMapInput.length-1)»
					«var String inputString = testMapInput.getString(i)»
					«var String[] inputElements = inputString.split(";")»
					mapDriver.withInput(new «config.getKEYIN(config.mapFunction)»(«inputElements.get(0)»), new «config.getVALUEIN(config.mapFunction)»( «inputElements.get(1)» ));
				«ENDFOR»
				
				«val JSONArray testMapOutput = config.getTestOutput(config.mapFunction)»
				«FOR Integer i : (0..testMapOutput.length-1)»
					«var String outputString = testMapOutput.getString(i)»
					«var String[] outputElements = outputString.split(";")»
					mapDriver.withOutput(new «config.getKEYOUT(config.mapFunction)»(«outputElements.get(0)»), new «config.getVALUEOUT(config.mapFunction)»( «outputElements.get(1)» ));
				«ENDFOR»
				
				
				mapDriver.runTest(false);
				
			}
			 
			@Test
			public void testReducer() throws IOException {
				«context.addImport("java.util.ArrayList")»
				«context.addImport("java.util.List")»
				«val JSONArray testReduceInput = config.getTestInput(config.reduceFunction)»
				List<IntWritable> values = null;
				
				«FOR Integer i : (0..testReduceInput.length-1)»
					«var String inputString = testReduceInput.getString(i)»
					«var String[] inputElements = inputString.split(";")»
					values = new ArrayList<IntWritable>();
					«val el = new JSONArray(inputElements.get(1))»
					«FOR Integer n : (0..el.length-1)»
						values.add(new «config.getVALUEIN(config.reduceFunction)»(«el.get(n)»));
					«ENDFOR»
					reduceDriver.withInput(new «config.getKEYIN(config.reduceFunction)»(«inputElements.get(0)»), values );
					
				«ENDFOR»
				
				«val JSONArray testReduceOutput = config.getTestOutput(config.reduceFunction)»
				«FOR Integer i : (0..testReduceOutput.length-1)»
					«var String outputString = testReduceOutput.getString(i)»
					«var String[] outputElements = outputString.split(";")»
					reduceDriver.withOutput(new «config.getKEYOUT(config.reduceFunction)»(«outputElements.get(0)»), new «config.getVALUEOUT(config.reduceFunction)»( «outputElements.get(1)» ));
				«ENDFOR»
				reduceDriver.runTest(false);
			}
		}
	'''
	
	def CharSequence genMapReducePatternClass(Pattern p, CodegenContext context)'''
		public class «CodeGenHelper.getMapReduceClassNameFromPattern(p)» {
			«context.addImport("org.apache.hadoop.conf.Configuration")»
			public static Configuration getJobConf(){		
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
	