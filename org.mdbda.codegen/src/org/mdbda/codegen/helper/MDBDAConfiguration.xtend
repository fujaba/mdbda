package org.mdbda.codegen.helper

import org.json.simple.JSONObject
import org.json.simple.JSONValue
import java.util.HashMap
import org.json.simple.JSONArray

class MDBDAConfiguration {
	//var JSONObject config = new JSONObject();
	var HashMap <Object,Object> config = new HashMap ()
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
 			instance.config = JSONValue.parse(config) as JSONObject
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
		config.get(name)
	}
	
	//Pattern Config ###############################################################################################
	
	
	//MR Funktions ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	val MapFunction = "mapFunction";
	def getMapFunction(){
		config.get(MapFunction) as JSONObject
	}
	
	def setMapFunction(JSONObject mapFun){
		config.put(MapFunction ,mapFun)
	}
	
	val ReduceFunction = "reduceFunction";
	def getReduceFunction(){
		config.get(ReduceFunction) as JSONObject
	}
	def setReduceFunction(JSONObject reduceFun){
		config.put(ReduceFunction,reduceFun)
	}
	
	val TestInput = "testInput"
	def getTestInput(JSONObject MRFunction){
		MRFunction.get(TestInput) as JSONArray
	}
	def setTestInput(JSONObject MRFunction, JSONArray testInput){
		var HashMap map = MRFunction
		map.put(TestInput,testInput)
	}
	
	val TestOutput = "testOutput"
	def getTestOutput(JSONObject MRFunction){
		MRFunction.get(TestOutput) as JSONArray
	}
	def setTestOutput(JSONObject MRFunction, JSONArray testOutput){
		var HashMap map = MRFunction
		map.put(TestOutput,testOutput)
	}
	
	val KEYIN = "KEYIN"	
	def getKEYIN(JSONObject MRFunction){
		MRFunction.get(KEYIN) as String
	}
	def setKEYIN(JSONObject MRFunction, String keyin){
		var HashMap map = MRFunction
		map.put(KEYIN,keyin)
	}
	val VALUEIN = "VALUEIN"
		def getVALUEIN(JSONObject MRFunction){
		MRFunction.get(VALUEIN) as String
	}
	def setVALUEIN(JSONObject MRFunction, String valuein){
		var HashMap map = MRFunction
		map.put(VALUEIN,valuein)
	}
	
	val KEYOUT = "KEYOUT"
	def getKEYOUT(JSONObject MRFunction){
		MRFunction.get(KEYOUT) as String
	}
	def setKEYOUT(JSONObject MRFunction, String keyout){
		var HashMap map = MRFunction
		map.put(KEYOUT,keyout)
	}
	
	val VALUEOUT = "VALUEOUT"
	def getVALUEOUT(JSONObject MRFunction){
		MRFunction.get(VALUEOUT) as String
	}
	def setVALUEOUT(JSONObject MRFunction, String valueout){
		var HashMap map = MRFunction
		map.put(VALUEOUT,valueout)
	}
	
	val Function = "function"
	def getFunction(JSONObject MRFunction){
		MRFunction.get(Function) as String
	}
	def setFunction(JSONObject MRFunction, String function){
		var HashMap map = MRFunction
		map.put(Function,function)
	}
	
	val Setup = "setup"
	def getSetup(JSONObject MRFunction){
		if(MRFunction.containsKey(Setup)){
			return MRFunction.get(Setup) as String
		}else{
			return null;
		}
	}
	def setSetup(JSONObject MRFunction, String setup){
		var HashMap map = MRFunction
		map.put(Setup,setup)
	}
	
	val Fields = "fields"
	def getFields(JSONObject MRFunction){
		if(MRFunction.containsKey(Fields)){
			return MRFunction.get(Fields) as String
		}else{
			return null;
		}
	}
	def setFields(JSONObject MRFunction, String fields){
		var HashMap map = MRFunction
		map.put(Fields,fields)
	}
	//MR Partitioner ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	val Partitioner = "partitioner"
	def getPartitioner(){
		config.get(Partitioner) as JSONObject
	}
	
	def setPartitioner(JSONObject partitioner){
		config.put(Partitioner ,partitioner)
	}
	
	val PartitionerKEY = "KEY"
	def getPartitionerKEY(JSONObject Partitioner){
		Partitioner.get(PartitionerKEY) as String
	}
	def setPartitionerKEY(JSONObject Partitioner, String value){
		var HashMap map = Partitioner
		map.put(PartitionerKEY,value)
	}
	
	val PartitionerVALUE = "VALUE"
	def getPartitionerVALUE(JSONObject Partitioner){
		Partitioner.get(PartitionerVALUE) as String
	}
	def setPartitionerVALUE(JSONObject Partitioner, String value){
		var HashMap map = Partitioner
		map.put(PartitionerVALUE,value)
	}
	
	val PartitionFunction = "partitionFunction"
	def getPartitionFunction(JSONObject Partitioner){
		Partitioner.get(PartitionFunction) as String
	}
	def setPartitionFunction(JSONObject Partitioner, String value){
		var HashMap map = Partitioner
		map.put(PartitionFunction,value)
	}
	//######################################################################
	
	val Cleanup = "cleanup"
	def getCleanup(JSONObject MRFunction){
		if(MRFunction.containsKey(Cleanup)){
			return MRFunction.get(Cleanup) as String
		}else{
			return null;
		}
	}
	def setCleanup(JSONObject MRFunction, String cleanup){
		var HashMap map = MRFunction
		map.put(Cleanup,cleanup)
	}
	
	val JobConfig = "jobConfig"
	def getJobConfig(){
		if(config.containsKey(JobConfig)){
			return config.get(JobConfig) as String
		}else{
			return null;
		}
	}
	def setJobConfig( String jobConfig ){
		config.put(JobConfig,jobConfig)
	}	
	
//	val JobDriver = "driver"
//	def getJobDriver(){
//		if(config.containsKey(JobDriver)){
//			return config.get(JobDriver) as String
//		}else{
//			return null;
//		}
//	}
//	def setJobDriver( String jobDriver ){
//		config.put(JobDriver,jobDriver)
//	}
}