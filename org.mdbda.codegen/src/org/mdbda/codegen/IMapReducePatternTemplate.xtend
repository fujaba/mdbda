package org.mdbda.codegen

import org.mdbda.model.Pattern

interface IMapReducePatternTemplate {
	def CharSequence generareMapReducePattern(Pattern pattern);
}