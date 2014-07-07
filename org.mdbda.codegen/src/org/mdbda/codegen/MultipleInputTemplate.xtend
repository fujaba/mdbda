package org.mdbda.codegen

import org.mdbda.codegen.IPatternTemplate
import org.mdbda.model.Pattern

class MultipleInputTemplate implements IPatternTemplate {
	MultipleInputMapReducePatternTemplate mrTemplateImpl = new MultipleInputMapReducePatternTemplate();
	override generareStormBolt(Pattern pattern, CodegenContext context) '''
		//not implemented 	generareStormBolt @ MultipleInputTemplate
	'''
	
	override generareMapReducePattern(Pattern pattern, CodegenContext context) {
		mrTemplateImpl.generareMapReducePattern(pattern,context)
	}
	
	override genJobConf(Pattern pattern, CodegenContext context) {
		mrTemplateImpl.genJobConf(pattern,context)
	}
	
	override genTempOutputs(Pattern pattern, CodegenContext context) {
		mrTemplateImpl.genTempOutputs(pattern,context)
	}
	
}