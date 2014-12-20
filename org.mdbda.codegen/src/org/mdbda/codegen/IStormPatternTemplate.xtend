package org.mdbda.codegen

import org.mdbda.model.Task 

interface IStormPatternTemplate {	
	def String generareStormPattern(Task pattern, CodegenContext context);
}