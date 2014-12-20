package org.mdbda.codegen

import org.mdbda.codegen.IPatternTemplate
import org.mdbda.model.Task

class DefaultPatternTemplate implements IPatternTemplate {
	DefaultMapReducePatternTemplate defaultMapReducePatternTemplate = new DefaultMapReducePatternTemplate();
	DefaultStormPatternTemplate defaultStormPatternTemplate = new DefaultStormPatternTemplate();
	override generareStormPattern(Task pattern, CodegenContext context) {
		defaultStormPatternTemplate.generareStormPattern(pattern, context)
	}
	
	override generareMapReducePattern(Task pattern, CodegenContext context) {
		defaultMapReducePatternTemplate.generareMapReducePattern(pattern,context)
	}
	
	override genJobConf(Task pattern, CodegenContext context) {
		defaultMapReducePatternTemplate.genJobConf(pattern,context)
	}
	
	override genTempOutputs(Task pattern, CodegenContext context) {
		defaultMapReducePatternTemplate.genTempOutputs(pattern,context)
	}
}