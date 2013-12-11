package org.mdbda.model;

public class ResourcesTemplateConstatns {
	public static String getResourcetypeCassandra() {
		return RESOURCETYPE_CASSANDRA;
	}
	public static String getResourcetypeHdfs() {
		return RESOURCETYPE_HDFS;
	}
	public static final String RESOURCETYPE_CASSANDRA = "CassandraResource";
	public static final String RESOURCETYPE_HDFS = "HDFSResource";
	public static final String RESOURCETYPE_GENERIC = "GenericResource";
	public static final String RESOURCETYPE_NEO4J = "Neo4jResource";
}
