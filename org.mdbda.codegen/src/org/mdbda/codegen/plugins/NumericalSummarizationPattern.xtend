package org.mdbda.codegen.plugins

import org.mdbda.codegen.IPatternTemplate
import org.mdbda.model.Pattern
import org.mdbda.codegen.CodegenContext
import org.mdbda.model.Workflow
import org.mdbda.codegen.helper.ConfigurationReader

class NumericalSummarizationPattern implements IPatternTemplate {
	
	override generareStormBolt(Pattern pattern, CodegenContext context) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override generareMapReducePattern(Pattern pattern, CodegenContext context) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override genJobConf(Pattern pattern, CodegenContext context) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	/*
	 * Es gibt nur eine Temp Resource
	 */
	override genTempOutputs(Pattern pattern, CodegenContext context) '''
		«var needsTempOutput = false»
		«FOR outputResource : pattern.outputResources»
			«IF outputResource instanceof Workflow || outputResource instanceof Pattern»	
				«needsTempOutput = true»
			«ENDIF»«/* ist eine Resource */»			
		«ENDFOR»
		
		«IF needsTempOutput»
			«context.addImport("org.apache.hadoop.fs.Path")»
			«val tmpPathName = "tempRessourceFor" + pattern.name»
			
			Path «tmpPathName» = new Path("«ConfigurationReader.getMapReduceTempPath(pattern.workflow.diagram.configurationString)»");
		«ENDIF»
	
	'''
	
}