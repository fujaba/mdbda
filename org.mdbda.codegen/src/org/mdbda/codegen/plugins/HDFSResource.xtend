package org.mdbda.codegen.plugins

import org.mdbda.codegen.IResourceTemplate
import org.mdbda.model.Resource 
import org.mdbda.codegen.helper.ConfigurationReader
import org.mdbda.codegen.CodegenContext
import org.mdbda.codegen.helper.MDBDAConfiguration
import org.mdbda.model.Task
import org.mdbda.codegen.helper.CodeGenHelper

class HDFSResource implements IResourceTemplate{
	 
	override generareMapReduceInputResouce(Resource res, Task pattern, CharSequence controledJobName , CodegenContext context ) '''
		{
			«context.addImport("org.apache.hadoop.fs.Path")»
			Path inputPath = new Path("«MDBDAConfiguration.readConfigString(res.configurationString).getHDFSPath()»");
			«IF  pattern.inputResources.size > 1»
				//MULTI INPUT
				«context.addImport("org.apache.hadoop.mapreduce.lib.input.TextInputFormat")»
				«context.addImport("org.apache.hadoop.mapreduce.lib.input.MultipleInputs")»
				«var fooCount = context.getCounter("MULTIINPUT"+pattern.toString)»
				«context.setCounter("MULTIINPUT"+pattern.toString, fooCount + 1)»
				«val mapperClass = CodeGenHelper.getMapReduceClassNameFromPattern(pattern) + "." + CodeGenHelper.getMapperInnderClassName(pattern) + fooCount» 
				MultipleInputs.addInputPath(«controledJobName».getJob(),inputPath,TextInputFormat.class,«mapperClass».class);
			«ELSE»
				«context.addImport("org.apache.hadoop.mapreduce.lib.input.TextInputFormat")»
				«controledJobName».getJob().setInputFormatClass(TextInputFormat.class);
				TextInputFormat.setInputPaths(«controledJobName».getJob(), inputPath);
			«ENDIF»
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