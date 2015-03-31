package org.mdbda.codegen.styles.hadoop

import org.mdbda.codegen.IPatternTemplate
import org.mdbda.model.Task

class MultipleInputTemplate implements IPatternTemplate {
	MultipleInputMapReducePatternTemplate mrTemplateImpl = new MultipleInputMapReducePatternTemplate();
	org.mdbda.codegen.MultipleInputSormPatternTemplate stormTemplateImpl = new org.mdbda.codegen.MultipleInputSormPatternTemplate();
	override generareStormPattern(Task pattern, org.mdbda.codegen.CodegenContext context) {
		stormTemplateImpl.generareStormPattern(pattern,context)
	}
	
	override generareMapReducePattern(Task pattern, org.mdbda.codegen.CodegenContext context) {
		mrTemplateImpl.generareMapReducePattern(pattern,context)
	}
	
	override genJobConf(Task pattern, org.mdbda.codegen.CodegenContext context) {
		mrTemplateImpl.genJobConf(pattern,context)
	}
	
	override genTempOutputs(Task pattern, org.mdbda.codegen.CodegenContext context) {
		mrTemplateImpl.genTempOutputs(pattern,context)
	}
	
}