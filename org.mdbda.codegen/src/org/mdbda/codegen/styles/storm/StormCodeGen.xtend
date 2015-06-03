package org.mdbda.codegen.styles.storm

import org.eclipse.xtext.generator.IFileSystemAccess
import org.mdbda.codegen.MDBDACodegenerator
import org.mdbda.model.MDBDAModelRoot
import org.eclipse.emf.ecore.resource.Resource
import org.mdbda.model.Task
import org.mdbda.model.Workflow
import org.mdbda.codegen.helper.CodeGenHelper
import org.mdbda.codegen.styles.ICodeGen
import org.mdbda.codegen.CodegenContext
import org.mdbda.codegen.CodeGeneratorRegistry

class StormCodeGen  implements ICodeGen{
	public static val codeStyle = "Storm"
	
	
	val	codeGenReg = CodeGeneratorRegistry.get
	
	override  doGenerate(Resource emfInputResource, IFileSystemAccess fsa, String codeGenStyle, MDBDACodegenerator codegen) {
		if(codeGenStyle == null || !codeGenStyle.equals(codeStyle) ){
			return;
		}
		//patternTemplates.put("test",[Pattern p | '''//something funny Lambda «p.name» '''])
			
		for(root: emfInputResource.allContents.toIterable.filter(MDBDAModelRoot)){
			//Storm
			var context = new CodegenContext(fsa,root.name , 'StormTopology.java','',codeStyle)
			codegen.genFile(root.stormTopology(context),context)
		}
		
		//generate Storm
		for(pattern: emfInputResource.allContents.toIterable.filter(Task)){
			if(! (pattern instanceof Workflow)){
				var context = new CodegenContext(fsa,CodeGenHelper.getStormClassNameFromPattern(pattern), '.java','',codeStyle)
				codegen.genFile(pattern.genStormPatternClass(context),context)
			}
		}
	}
	
		
	def CharSequence stormTopology(MDBDAModelRoot modelRoot, CodegenContext context)'''
		class «modelRoot.name»StormTopology{
			/**
			  * name    = «modelRoot.name»
			  *	author  = «modelRoot.author»
			  * version = «modelRoot.version»
			  * @throws IOException 
			*/			
			«context.addImport("java.io.IOException")»
			public static void main(String... args) throws IOException{
				«/*TODO: Storm Topology*/»
			}
		}
	'''
	
	
	def CharSequence genStormPatternClass(Task task, CodegenContext context)'''
		«context.addImport("java.io.Serializable")»
		public class «CodeGenHelper.getStormClassNameFromPattern(task)» implements Serializable {
		«IF codeGenReg.existGenerator(context.codeStyle,task.typeId)»
			«codeGenReg.getGenerator(context.codeStyle,task.typeId).doCodagenTemplateTask(TEMPLATETASK_MDBDA_TASK,context,task)»
			«/*patternTemplates.get(p.typeId).generareStormPattern(p, context)*/»
		«ELSE»
			//keine implementierung in patternTemplates fuer "«task.typeId»" vorhanden («task.class»)
		«ENDIF»
		}
	'''
}