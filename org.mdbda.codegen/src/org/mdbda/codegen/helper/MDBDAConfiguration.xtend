package org.mdbda.codegen.helper

import org.json.JSONObject
import org.json.JSONException
import org.json.JSONArray

class MDBDAConfiguration {
	JSONObject config = new JSONObject();
	
	val HDFSPath = "HDFSPath";
	
	def void setHDFSPath(String path){
		config.put(HDFSPath , path)
	}
	
	def getHDFSPath(){
		get(HDFSPath)
	}
	
	def writeConfigString(){
		config.toString
	}
	
 	static def readConfigString(String config){ 		
 		val instance = new MDBDAConfiguration();
 		
 		if(config != null && !"".equals(config)){ 			
 			instance.config = new JSONObject(config)
 		}
 		return instance
	}
	
	val CassandraResourceKeyspace = "CassandraResourceKeyspace"
	
	def void setCassandraResourceKeyspace(String ks){
		config.put(CassandraResourceKeyspace , ks)
	}
	
	def getCassandraResourceKeyspace() {
		get(CassandraResourceKeyspace)
	}
	
	
	val CassandraResourceColumnFamily = "CassandraResourceColumnFamily"
	def void setCassandraResourceColumnFamily(String cf){
		config.put(CassandraResourceColumnFamily , cf)
	}
	def getCassandraResourceColumnFamily() {
		get(CassandraResourceColumnFamily)
	}
	
	
	
	val CassandraColumnName = "CassandraColumnName"
	def void setCassandraColumnName(String cn){
		config.put(CassandraColumnName , cn)
	}
	def getCassandraColumnName() {
		get(CassandraColumnName)
	}
	
	def get(String name) {
		try{
			config.getString(name)			
		}catch(JSONException e){
			return name + "_NOT_SET"
		}
	}
	
	//Pattern Config
	
	val MapFunction = "mapFunction";
	def getMapFunction(){
		config.getJSONObject(MapFunction)
	}
	
	def setMapFunction(JSONObject mapFun){
		config.put(MapFunction,mapFun)
	}
	
	val ReduceFunction = "reduceFunction";
	def getReduceFunction(){
		config.getJSONObject(ReduceFunction)
	}
	def setReduceFunction(JSONObject reduceFun){
		config.put(ReduceFunction,reduceFun)
	}
	
	val TestInput = "testInput"
	def getTestInput(JSONObject MRFunction){
		MRFunction.getJSONArray(TestInput)
	}
	def setTestInput(JSONObject MRFunction, JSONArray testInput){
		MRFunction.put(TestInput,testInput)
	}
	
	val TestOutput = "testOutput"
	def getTestOutput(JSONObject MRFunction){
		MRFunction.getJSONArray(TestOutput)
	}
	def setTestOutput(JSONObject MRFunction, JSONArray testOutput){
		MRFunction.put(TestOutput,testOutput)
	}
	
	val KEYIN = "KEYIN"	
	def getKEYIN(JSONObject MRFunction){
		MRFunction.getString(KEYIN)
	}
	def setKEYIN(JSONObject MRFunction, String keyin){
		MRFunction.put(KEYIN,keyin)
	}
	val VALUEIN = "VALUEIN"
		def getVALUEIN(JSONObject MRFunction){
		MRFunction.getString(VALUEIN)
	}
	def setVALUEIN(JSONObject MRFunction, String valuein){
		MRFunction.put(VALUEIN,valuein)
	}
	
	val KEYOUT = "KEYOUT"
	def getKEYOUT(JSONObject MRFunction){
		MRFunction.getString(KEYOUT)
	}
	def setKEYOUT(JSONObject MRFunction, String keyout){
		MRFunction.put(KEYOUT,keyout)
	}
	
	val VALUEOUT = "VALUEOUT"
	def getVALUEOUT(JSONObject MRFunction){
		MRFunction.getString(VALUEOUT)
	}
	def setVALUEOUT(JSONObject MRFunction, String valueout){
		MRFunction.put(VALUEOUT,valueout)
	}
	
	val Function = "function"
	def getFunction(JSONObject MRFunction){
		MRFunction.getString(Function)
	}
	def setFunction(JSONObject MRFunction, String function){
		MRFunction.put(Function,function)
	}
	
}