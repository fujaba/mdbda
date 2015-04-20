package org.mdbda.codegen

import org.mdbda.model.Task
import org.mdbda.model.Resource
import org.mdbda.codegen.styles.hadoop.HadoopCodeGen
import org.mdbda.codegen.helper.CodeGenHelper

abstract class AbstractResourceTemplate implements ITemplate {
	override doCodagenTemplateTask(String Task, CodegenContext context, Resource... mdbdaElements) {
		if (context == null) {
			throw new IllegalArgumentException("The argument context can not be null")
		}

		switch (Task) {
			case HadoopCodeGen.TEMPLATETASK_HADOOP_INPUT: {
				if (helper_doCodagenTemplateTask_checkargument(mdbdaElements)) {
					generareInputResouce(
						mdbdaElements.get(1),
						mdbdaElements.get(0) as Task,
						CodeGenHelper.getMapReduceControlledJobVarName(mdbdaElements.get(0) as Task),
						context
					)
				}
			}
			case HadoopCodeGen.TEMPLATETASK_HADOOP_OUTPUT: {
				if (helper_doCodagenTemplateTask_checkargument(mdbdaElements)) {
					generareOutputResouce(
						mdbdaElements.get(1),
						CodeGenHelper.getMapReduceControlledJobVarName(mdbdaElements.get(0) as Task),
						context
					)
				}
			}
			default: {
				throw new UnsupportedOperationException(
					'''The codegeneration task «Task» is not supported for MapReduce'''.toString)
			}
		}
	}

	def private boolean helper_doCodagenTemplateTask_checkargument(Resource... mdbdaElements) {
		if (mdbdaElements.size == 2 && mdbdaElements.get(0) instanceof Task &&
			mdbdaElements.get(1) instanceof Resource) {
			return true
		} else {
			throw new IllegalArgumentException(
				"The argument mdbdaElements has to have two elements. The first of type org.mdbda.model.Task and the second of type org.mdbda.model.Resource"
			)
		}
	}

	def abstract CharSequence generareInputResouce(Resource res, Task pattern, CharSequence controledJobName,
		CodegenContext context)

	def abstract CharSequence generareOutputResouce(Resource res, CharSequence controledJobName, CodegenContext context)

}
