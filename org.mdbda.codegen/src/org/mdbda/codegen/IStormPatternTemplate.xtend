package org.mdbda.codegen

import org.mdbda.model.Pattern 

interface IStormPatternTemplate {	
	def String generareStormPattern(Pattern pattern, CodegenContext context);
}