package org.mdbda.codegen.plugins

import org.mdbda.codegen.IResourceTemplate
import org.mdbda.model.Resource 
import org.mdbda.codegen.helper.ConfigurationReader
import org.mdbda.codegen.CodegenContext

class HDFSResource implements IResourceTemplate{
	 
	override generareMapReduceInputResouce(Resource res, CodegenContext context ) '''
			«context.addImport("org.apache.hadoop.fs.Path")»
			Path «res.name»Input = new Path("«ConfigurationReader.getHDFSPath(res.configurationString)»");
	'''
	
	override generareMapReduceOutputResouce(Resource res, CodegenContext context) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override generareStormInputResouce(Resource res, CodegenContext context) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override generareStormOutputResouce(Resource res, CodegenContext context) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
}