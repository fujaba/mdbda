package org.mdbda.cassandra.helper

import org.mdbda.codegen.helper.MDBDAConfiguration

class CassandraConfigReader extends MDBDAConfiguration {
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
	
	val CassandraTestServerIP = "CassandraTestServerIP"
	def getTestServerIP() {
		get(CassandraTestServerIP)
	}

	val CassandraLiveServerIP = "CassandraLiveServerIP"
	
	new(String config) {
		super(config)
	}
	
	def getLiveServerIP() {		
		get(CassandraLiveServerIP)
	}

}
