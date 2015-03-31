package org.mdbda.codegen

import org.mdbda.model.Task

interface IPatternTemplate extends ITemplate{
	def String generarePattern(Task pattern, CodegenContext context);
}