package org.mdbda.codegen.styles.storm

import org.mdbda.codegen.CodegenContext
import org.mdbda.model.Resource
import org.mdbda.codegen.AbstractResourceTemplate
import org.mdbda.model.Task

class GenericResourceStrom extends  AbstractResourceTemplate{
	
	override getCodeStyle() {
		StormCodeGen.codeStyle
	}
	
	override generareInputResouce(Resource res, Task pattern, CharSequence controledJobName, CodegenContext context) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override generareOutputResouce(Resource res, CharSequence controledJobName, CodegenContext context) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
}