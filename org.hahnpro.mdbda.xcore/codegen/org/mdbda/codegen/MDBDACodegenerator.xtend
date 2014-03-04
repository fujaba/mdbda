package org.mdbda.codegen

import org.eclipse.xtext.generator.IGenerator
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.mdbda.model.MDBDADiagram
import org.mdbda.model.Workflow
import org.mdbda.model.Pattern
import java.util.HashMap

class MDBDACodegenerator implements IGenerator {
	
	static var patternTemplates = new HashMap<String, IPatternTemplate>();
	
	public static def addPatternTemplat(String patternId, IPatternTemplate template ){
		patternTemplates.put(patternId,template);		
	}
	
	override doGenerate(Resource input, IFileSystemAccess fsa) {
		
		//patternTemplates.put("test",[Pattern p | '''//something funny Lambda «p.name» '''])
			
		for(root: input.allContents.toIterable.filter(MDBDADiagram)){
			fsa.generateFile(root.name + 'JobConfiguration', root.jobConfiguration);
		}
		
		//generate MapReduce
		for(pattern: input.allContents.toIterable.filter(Pattern)){
			fsa.generateFile(pattern.name, pattern.genMapReducePatternClass);//TODO add package
		}
		//generate Storm
		for(pattern: input.allContents.toIterable.filter(Pattern)){
			fsa.generateFile(pattern.name, pattern.genMapReducePatternClass);//TODO add package
		}
	}
	
	def CharSequence jobConfiguration(MDBDADiagram diagram){
		'''
		class «diagram.name»JobConfiguration{
			/**
			  * name    = «diagram.name»
			  *	author  = «diagram.author»
			  * version = «diagram.version»
			 */
			public static void main(String... args){
					Configuration conf = new Configuration();
					//...
					«diagram.rootWorkflow.workflowConfiguration»
					«FOR pattern : diagram.rootWorkflow.pattern»
						//«pattern.name»
					«ENDFOR»
			}
		}
		'''
	}
	
	def CharSequence getWorkflowConfiguration(Workflow workflow){
		'''«FOR pattern : workflow.pattern»
			//pattern conf: «pattern.name»
			«ENDFOR»
		'''
	}
	
	def CharSequence genMapReducePatternClass(Pattern p){
		if(patternTemplates.containsKey(p.typeId)){
			patternTemplates.get(p.typeId).generareMapReducePattern(p);
		}else{
			'''
			class «p.name» {
				//NOT IMPLEMENTED
			}
			'''
		}
	}
		
	def CharSequence genStormPatternClass(Pattern p){
		if(patternTemplates.containsKey(p.typeId)){
			patternTemplates.get(p.typeId).generareStormBolt(p);
		}else{
			'''
			class «p.name» {
				//NOT IMPLEMENTED
			}
			'''
		}
	}
}