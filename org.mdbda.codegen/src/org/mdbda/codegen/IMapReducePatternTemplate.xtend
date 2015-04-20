package org.mdbda.codegen

import org.mdbda.model.Task 

interface IMapReducePatternTemplate extends ITemplate{
		
	def CharSequence genJobConf(Task pattern, CodegenContext context);
	
	def CharSequence genTempOutputs(Task pattern, CodegenContext context);
}