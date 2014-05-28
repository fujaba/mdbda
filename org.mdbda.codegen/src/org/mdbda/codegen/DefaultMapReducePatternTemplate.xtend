package org.mdbda.codegen

import org.mdbda.codegen.IMapReducePatternTemplate
import org.mdbda.model.Pattern
import static extension org.mdbda.codegen.helper.ConfigurationReader.*;
import org.mdbda.codegen.helper.CodeGenHelper
import org.mdbda.codegen.helper.MDBDAConfiguration

class DefaultMapReducePatternTemplate implements IMapReducePatternTemplate {
	
	
	
	override generareMapReducePattern(Pattern pattern, CodegenContext context) '''
			«val config = MDBDAConfiguration.readConfigString(pattern.configurationString)»
			«context.addImport("org.apache.hadoop.mapreduce.Mapper")»
			«context.addImport("org.apache.hadoop.io.*")»
			«context.addImport("java.io.IOException")»
			
		//org.mdbda.codegen.DefaultMapReducePatternTemplate
		«val Mapper = "Mapper<" + config.getKEYIN(config.mapFunction) + "," +config.getVALUEIN(config.mapFunction)+ "," +config.getKEYOUT(config.mapFunction)+ "," +config.getVALUEOUT(config.mapFunction) + ">"»
			public static class «CodeGenHelper.getMapperInnderClassName(pattern)» extends «Mapper» {
				@Override
				public void map(«config.getKEYIN(config.mapFunction)» key, «config.getVALUEIN(config.mapFunction)» value, «Mapper».Context context) throws IOException, InterruptedException{
					//in: «config.getTestInput(config.mapFunction)»
					«config.getFunction(config.mapFunction)»
					//out: «config.getTestOutput(config.mapFunction)»
				}
			}
			
			«context.addImport("org.apache.hadoop.mapreduce.Reducer")»
		«val Reducer = "Reducer<" + config.getKEYIN(config.reduceFunction) + "," +config.getVALUEIN(config.reduceFunction)+ "," +config.getKEYOUT(config.reduceFunction)+ "," +config.getVALUEOUT(config.reduceFunction) + ">"»
			public static class «CodeGenHelper.getReducerInnderClassName(pattern)» extends «Reducer»{
				@Override
				protected void reduce(«config.getKEYIN(config.reduceFunction)» key, Iterable<«config.getVALUEIN(config.reduceFunction)»> values, «Reducer».Context context)
						throws IOException, InterruptedException {
					//in: «config.getTestInput(config.reduceFunction)»
					«config.getFunction(config.reduceFunction)»
					//out: «config.getTestOutput(config.reduceFunction)»
				}
			}
	'''
	
	
	
	
	override genJobConf(Pattern pattern, CodegenContext context) '''
		//org.mdbda.codegen.DefaultMapReducePatternTemplate
		return new Configuration();
	'''
	
	override genTempOutputs(Pattern pattern, CodegenContext context) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	
}