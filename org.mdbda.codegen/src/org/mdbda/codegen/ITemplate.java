package org.mdbda.codegen;

import org.mdbda.model.Resource;

public interface ITemplate {
	public String getCodeStyle();
	public void doCodagenTemplateTask(String Task, CodegenContext context, Resource... mdbdaElements);
}