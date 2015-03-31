package org.mdbda.codegen

import org.mdbda.model.Resource

interface ITemplate {
	def String getCodeStyle();
	def void doCodagenTemplateTask(String Task, Resource mdbdaElement ,CodegenContext context);
}