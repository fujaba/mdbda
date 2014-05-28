package org.mdbda.codegen.plugins

import org.mdbda.codegen.IResourceTemplate
import org.mdbda.model.Resource 
import org.mdbda.codegen.helper.ConfigurationReader
import org.mdbda.codegen.CodegenContext
import org.mdbda.codegen.helper.MDBDAConfiguration

class HDFSResource implements IResourceTemplate{
	 
	override generareMapReduceInputResouce(Resource res, CharSequence controledJobName , CodegenContext context ) '''
		{
			«context.addImport("org.apache.hadoop.fs.Path")»
			Path inputPath = new Path("«MDBDAConfiguration.readConfigString(res.configurationString).getHDFSPath()»");
			«context.addImport("org.apache.hadoop.mapreduce.lib.input.TextInputFormat")»
			«controledJobName».getJob().setInputFormatClass(TextInputFormat.class);
			TextInputFormat.setInputPaths(«controledJobName».getJob(), inputPath);
		}
	'''
	
	override generareMapReduceOutputResouce(Resource res, CharSequence controledJobName , CodegenContext context) '''
		{
			«context.addImport("org.apache.hadoop.fs.Path")»
			Path outputPath = new Path("«MDBDAConfiguration.readConfigString(res.configurationString).getHDFSPath()»");
			«context.addImport("org.apache.hadoop.mapreduce.lib.output.TextOutputFormat")»
			«controledJobName».getJob().setOutputFormatClass(TextOutputFormat.class);
			TextOutputFormat.setOutputPath(«controledJobName».getJob(), outputPath);
		}
	'''
	
	override generareStormInputResouce(Resource res, CodegenContext context) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	} 
	
	override generareStormOutputResouce(Resource res, CodegenContext context) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
}