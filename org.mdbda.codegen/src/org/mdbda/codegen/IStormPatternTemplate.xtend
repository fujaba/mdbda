package org.mdbda.codegen

import org.mdbda.model.Pattern 

interface IStormPatternTemplate {	
	def String generareStormBolt(Pattern pattern, CodegenContext context);
}