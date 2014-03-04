package org.mdbda.codegen

import org.mdbda.model.Pattern

interface IStormPatternTemplate {	
	def CharSequence generareStormBolt(Pattern pattern);
}