package org.mdbda.hdfs.codegen

import org.mdbda.model.Resource 
import org.mdbda.codegen.CodegenContext
import org.mdbda.codegen.helper.MDBDAConfiguration
import org.mdbda.model.Task
import org.mdbda.codegen.helper.CodeGenHelper
import org.mdbda.codegen.AbstractResourceTemplate

class HDFSResourceHadoop extends AbstractResourceTemplate{
	 
		override getCodeStyle() {
			"Hadoop"
		}
		
	override generareInputResouce(Resource res, Task pattern, CharSequence controledJobName , CodegenContext context ) '''
		{
			«context.addImport("org.apache.hadoop.fs.Path")»
			Path inputPath = new Path("«(new MDBDAConfiguration(res.configurationString)).getHDFSPath()»");
			«IF  pattern.inputResources.size > 1»
				//MULTI INPUT
				«context.addImport("org.apache.hadoop.mapreduce.lib.input.TextInputFormat")»
				«context.addImport("org.apache.hadoop.mapreduce.lib.input.MultipleInputs")»
				«val mapperClass = CodeGenHelper.getMapReduceClassNameFromPattern(pattern) + "." + CodeGenHelper.getMapperInnerClassName(pattern,res.inputResources.get(0))» 
				MultipleInputs.addInputPath(«controledJobName».getJob(),inputPath,TextInputFormat.class,«mapperClass».class);
			«ELSE»
				«context.addImport("org.apache.hadoop.mapreduce.lib.input.TextInputFormat")»
				«controledJobName».getJob().setInputFormatClass(TextInputFormat.class);
				TextInputFormat.setInputPaths(«controledJobName».getJob(), inputPath);
			«ENDIF»
		}
	'''
	
	override generareOutputResouce(Resource res, CharSequence controledJobName , CodegenContext context) '''
		{
			«context.addImport("org.apache.hadoop.fs.Path")»
			Path outputPath = new Path("«(new MDBDAConfiguration(res.configurationString)).getHDFSPath()»");
			«context.addImport("org.apache.hadoop.mapreduce.lib.output.TextOutputFormat")»
			«controledJobName».getJob().setOutputFormatClass(TextOutputFormat.class);
			TextOutputFormat.setOutputPath(«controledJobName».getJob(), outputPath);
		}
	'''
	
}