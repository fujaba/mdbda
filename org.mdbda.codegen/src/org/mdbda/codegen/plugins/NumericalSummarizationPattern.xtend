package org.mdbda.codegen.plugins

import org.mdbda.codegen.IPatternTemplate
import org.mdbda.model.Pattern
import org.mdbda.codegen.CodegenContext
import org.mdbda.model.Workflow
import org.mdbda.codegen.helper.ConfigurationReader
import org.mdbda.codegen.DefaultMapReducePatternTemplate

class NumericalSummarizationPattern extends DefaultMapReducePatternTemplate implements IPatternTemplate {
	
	override generareStormBolt(Pattern pattern, CodegenContext context) '''
		//TODO
	'''

}