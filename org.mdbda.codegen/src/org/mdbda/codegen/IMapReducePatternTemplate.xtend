package org.mdbda.codegen

import org.mdbda.model.Pattern 

interface IMapReducePatternTemplate {
	def CharSequence generareMapReducePattern(Pattern pattern, CodegenContext context);
	
	def CharSequence genJobConf(Pattern pattern, CodegenContext context);
	def CharSequence genTempOutputs(Pattern pattern, CodegenContext context);
}