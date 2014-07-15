package org.mdbda.codegen

import org.mdbda.codegen.IPatternTemplate
import org.mdbda.model.Pattern

class DefaultPatternTemplate implements IPatternTemplate {
	DefaultMapReducePatternTemplate defaultMapReducePatternTemplate = new DefaultMapReducePatternTemplate();
	DefaultStormPatternTemplate defaultStormPatternTemplate = new DefaultStormPatternTemplate();
	override generareStormPattern(Pattern pattern, CodegenContext context) {
		defaultStormPatternTemplate.generareStormPattern(pattern, context)
	}
	
	override generareMapReducePattern(Pattern pattern, CodegenContext context) {
		defaultMapReducePatternTemplate.generareMapReducePattern(pattern,context)
	}
	
	override genJobConf(Pattern pattern, CodegenContext context) {
		defaultMapReducePatternTemplate.genJobConf(pattern,context)
	}
	
	override genTempOutputs(Pattern pattern, CodegenContext context) {
		defaultMapReducePatternTemplate.genTempOutputs(pattern,context)
	}
}