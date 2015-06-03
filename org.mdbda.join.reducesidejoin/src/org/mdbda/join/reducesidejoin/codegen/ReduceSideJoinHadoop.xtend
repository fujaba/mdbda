package org.mdbda.join.reducesidejoin.codegen

import org.mdbda.codegen.styles.hadoop.DefaultMapReducePatternTemplate
import org.mdbda.model.Task
import org.mdbda.codegen.CodegenContext
import org.mdbda.codegen.helper.MDBDAConfiguration
import org.mdbda.codegen.helper.CodeGenHelper
import org.mdbda.model.Resource

class ReduceSideJoinHadoop extends DefaultMapReducePatternTemplate{
	override genMapperClass(Task task, CodegenContext context) {
		var ret = ''''''
		for(Resource input : task.inputResources){
			ret += genMultiMapperClass(task,input,context)
		}
		return ret
	}
	
	//«val Mapper = "Mapper<" + config.getKEYIN(function) + "," +config.getVALUEIN(function)+ "," +config.getKEYOUT(function)+ "," +config.getVALUEOUT(function) + ">"»		
	def genMultiMapperClass(Task task, Resource input, CodegenContext context	)'''	
		«val config = new MDBDAConfiguration(task.configurationString)»
			public static class «CodeGenHelper.getMapperInnerClassName(task, input)» extends Mapper«/*Mapper*/» {
				
			}
	'''

}