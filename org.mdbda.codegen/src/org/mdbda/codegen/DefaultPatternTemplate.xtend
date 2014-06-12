package org.mdbda.codegen

import org.mdbda.codegen.IPatternTemplate
import org.mdbda.model.Pattern

class DefaultPatternTemplate implements IPatternTemplate {
	DefaultMapReducePatternTemplate defaultMapReducePatternTemplate = new DefaultMapReducePatternTemplate();
	
	override generareStormBolt(Pattern pattern, CodegenContext context) '''
		//not implemented 	generareStormBolt @ DefaultPatternTemplat
	'''
	
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