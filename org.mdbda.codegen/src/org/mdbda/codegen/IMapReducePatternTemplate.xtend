package org.mdbda.codegen

import org.mdbda.model.Task 

interface IMapReducePatternTemplate {
	def CharSequence generareMapReducePattern(Task pattern, CodegenContext context);
	
	def CharSequence genJobConf(Task pattern, CodegenContext context);
	
	def CharSequence genTempOutputs(Task pattern, CodegenContext context);
}