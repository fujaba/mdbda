package org.mdbda.codegen

import org.mdbda.codegen.IPatternTemplate
import org.mdbda.model.Task

class MultipleInputTemplate implements IPatternTemplate {
	MultipleInputMapReducePatternTemplate mrTemplateImpl = new MultipleInputMapReducePatternTemplate();
	MultipleInputSormPatternTemplate stormTemplateImpl = new MultipleInputSormPatternTemplate();
	override generareStormPattern(Task pattern, CodegenContext context) {
		stormTemplateImpl.generareStormPattern(pattern,context)
	}
	
	override generareMapReducePattern(Task pattern, CodegenContext context) {
		mrTemplateImpl.generareMapReducePattern(pattern,context)
	}
	
	override genJobConf(Task pattern, CodegenContext context) {
		mrTemplateImpl.genJobConf(pattern,context)
	}
	
	override genTempOutputs(Task pattern, CodegenContext context) {
		mrTemplateImpl.genTempOutputs(pattern,context)
	}
	
}