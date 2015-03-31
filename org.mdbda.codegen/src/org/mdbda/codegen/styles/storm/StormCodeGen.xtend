package org.mdbda.codegen.styles.storm

import org.eclipse.xtext.generator.IFileSystemAccess
import org.mdbda.codegen.MDBDACodegenerator
import org.mdbda.model.MDBDADiagram
import org.eclipse.emf.ecore.resource.Resource
import org.mdbda.model.Task
import org.mdbda.model.Workflow
import org.mdbda.codegen.helper.CodeGenHelper

class StormCodeGen  implements org.mdbda.codegen.styles.ICodeGen{
	public static val codeStyle = "Storm"
	
	override  doGenerate(Resource emfInputResource, IFileSystemAccess fsa, String codeGenStyle, MDBDACodegenerator codegen) {
		if(codeGenStyle == null || !codeGenStyle.equals(codeStyle) ){
			return;
		}
		//patternTemplates.put("test",[Pattern p | '''//something funny Lambda «p.name» '''])
			
		for(root: emfInputResource.allContents.toIterable.filter(MDBDADiagram)){
			//Storm
			var context = new CodegenContext(fsa,root.name + 'StormTopology.java','')
			codegen.genFile(root.stormTopology(context),context)
		}
		
		//generate Storm
		for(pattern: emfInputResource.allContents.toIterable.filter(Task)){
			if(! (pattern instanceof Workflow)){
				var context = new CodegenContext(fsa,CodeGenHelper.getStormClassNameFromPattern(pattern) + '.java','')
				codegen.genFile(pattern.genStormPatternClass(context),context)
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