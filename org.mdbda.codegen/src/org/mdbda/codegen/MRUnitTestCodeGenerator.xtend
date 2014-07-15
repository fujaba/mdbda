package org.mdbda.codegen

import org.mdbda.model.Pattern
import org.mdbda.codegen.helper.CodeGenHelper
import org.mdbda.codegen.helper.MDBDAConfiguration
import java.util.ArrayList
import org.json.simple.JSONObject
import org.json.simple.JSONArray
import org.json.simple.JSONValue

class MRUnitTestCodeGenerator {
	
	def static CharSequence genMapReducePatternTestClass(Pattern p, CodegenContext context)'''
	
	 public class «CodeGenHelper.getMapReduceTestClassNameFromPattern(p)» {
		«context.addImport("org.apache.hadoop.mrunit.mapreduce.MapDriver")»
		«context.addImport("org.apache.hadoop.mrunit.mapreduce.MapReduceDriver")»
		«context.addImport("org.apache.hadoop.mrunit.mapreduce.ReduceDriver")»
		«val config = MDBDAConfiguration.readConfigString(p.configurationString)»
		
		«IF config.reduceFunction != null»
			ReduceDriver<«config.getKEYIN(config.reduceFunction)», «config.getVALUEIN(config.reduceFunction)», «config.getKEYOUT(config.reduceFunction)», «config.getVALUEOUT(config.reduceFunction)»> reduceDriver;
		«ENDIF»
		«var ArrayList<JSONObject> multipleMapFunction = getMultipleMap(config)»
		
		«context.addImport("org.junit.Before")»
		@Before
		public void setUp() {
			«var reducerClass = CodeGenHelper.getMapReduceClassNameFromPattern(p) + "." + CodeGenHelper.getReducerInnderClassName(p)»
			«IF config.reduceFunction != null»
				«reducerClass» reducer = new «reducerClass»();
				reduceDriver = ReduceDriver.newReduceDriver(reducer);
			«ENDIF»
			«var int fooCount2 = -1»
			«FOR mapfunc : multipleMapFunction»
				//Map Funktion for Input «fooCount2 = fooCount2 + 1»
				«IF config.isMultipleMapFunction»
					«var mapperClass = CodeGenHelper.getMapReduceClassNameFromPattern(p) + "." + CodeGenHelper.getMapperInnderClassName(p) + fooCount2»
					«mapperClass» mapper«fooCount2» = new «mapperClass»();
				«ELSE»
					«var mapperClass = CodeGenHelper.getMapReduceClassNameFromPattern(p) + "." + CodeGenHelper.getMapperInnderClassName(p) »
					«mapperClass» mapper«fooCount2» = new «mapperClass»();
				«ENDIF»
				mapDriver«fooCount2» = MapDriver.newMapDriver(mapper«fooCount2»);
				«IF config.reduceFunction != null»
					mapReduceDriver«fooCount2» = MapReduceDriver.newMapReduceDriver(mapper«fooCount2», reducer);
				«ENDIF»
			«ENDFOR»
		}
		
		«var int fooCount = -1»
		«FOR mapfunc : multipleMapFunction»
			«val MapDriver = "MapDriver<" + config.getKEYIN(mapfunc) + "," +config.getVALUEIN(mapfunc)+ "," +config.getKEYOUT(mapfunc)+ "," +config.getVALUEOUT(mapfunc) + ">"»
			«context.addImport("org.apache.hadoop.io.*")»
			«context.addImport("org.apache.hadoop.mapreduce.lib.output.*")»
			MapDriver<«config.getKEYIN(mapfunc)», «config.getVALUEIN(mapfunc)», «config.getKEYOUT(mapfunc)», «config.getVALUEOUT(mapfunc)»> mapDriver«fooCount = fooCount + 1»;
			«IF config.reduceFunction != null»
				MapReduceDriver<«config.getKEYIN(mapfunc)», «config.getVALUEIN(mapfunc)»,«config.getKEYIN(config.reduceFunction)», «config.getVALUEIN(config.reduceFunction)», «config.getKEYOUT(config.reduceFunction)», «config.getVALUEOUT(config.reduceFunction)»> mapReduceDriver«fooCount»;
			«ENDIF»
			
			«context.addImport("org.junit.Test")»
			«context.addImport("java.io.IOException")»
			@Test
			public void testMapper«fooCount»() throws IOException {
				«val JSONArray testMapInput = config.getTestInput(mapfunc)»
				«FOR inputString : testMapInput »
					«var String[] inputElements = (inputString as String).split(";")»
					mapDriver«fooCount».withInput( «CodeGenHelper.genWriterConstructorCall(config.getKEYIN(mapfunc),inputElements.get(0))» , «CodeGenHelper.genWriterConstructorCall(config.getVALUEIN(mapfunc),inputElements.get(1))» );
				«ENDFOR»
				«val JSONArray testMapOutput = config.getTestOutput(mapfunc)»
				«FOR outputString : testMapOutput »
					«var String[] outputElements = (outputString as String).split(";")»
					mapDriver«fooCount».withOutput( «CodeGenHelper.genWriterConstructorCall(config.getKEYOUT(mapfunc),outputElements.get(0))» , «CodeGenHelper.genWriterConstructorCall(config.getVALUEOUT(mapfunc),outputElements.get(1))» );
				«ENDFOR»
				mapDriver«fooCount».runTest(false);
			}
			
		«ENDFOR»
		
		«IF config.reduceFunction != null»
			@Test
			public void testReducer() throws IOException {
				«context.addImport("java.util.ArrayList")»
				«context.addImport("java.util.List")»
				«val JSONArray testReduceInput = config.getTestInput(config.reduceFunction)»
				«IF testReduceInput.length > 0»
					List<«config.getVALUEIN(config.reduceFunction)»> values = null;
					«FOR inputString : testReduceInput »
						«var String[] inputElements = (inputString as String).split(";")»
						values = new ArrayList<«config.getVALUEIN(config.reduceFunction)»>();
						«val el = JSONValue.parse(inputElements.get(1)) as JSONArray»
						«FOR n : el»
							values.add(«CodeGenHelper.genWriterConstructorCall(config.getVALUEIN(config.reduceFunction),n as String)»);
						«ENDFOR»
						reduceDriver.withInput(«CodeGenHelper.genWriterConstructorCall(config.getKEYIN(config.reduceFunction),inputElements.get(0))», values );
						
					«ENDFOR»
				«ENDIF»
				
				«val JSONArray testReduceOutput = config.getTestOutput(config.reduceFunction)»
				«IF testReduceOutput.length > 0»
					«FOR outputString : testReduceOutput »
						«var String[] outputElements = (outputString as String).split(";")»
						reduceDriver.withOutput( «CodeGenHelper.genWriterConstructorCall(config.getKEYOUT(config.reduceFunction),outputElements.get(0))» , «CodeGenHelper.genWriterConstructorCall(config.getVALUEOUT(config.reduceFunction),outputElements.get(1))» );
					«ENDFOR»
				«ENDIF»
				reduceDriver.runTest(false);
			}
		«ENDIF»
	}
	'''
	
	def static getMultipleMap(MDBDAConfiguration config) {
		var multipleMapFunction = new ArrayList<JSONObject>()
		if( config.isMultipleMapFunction ){
			multipleMapFunction = config.getMultipleMapFunction()
		}else{
			multipleMapFunction.add(config.mapFunction)
		}
		return multipleMapFunction
	}
}