package org.mdbda.cassandra;

import java.util.HashMap;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.Session;

public class CassandraConnector {
	/** Cassandra Cluster. */
	private Cluster cluster;
	
	private static HashMap<String,Cluster > clusterPool = new HashMap<String, Cluster>();
	
	/** Cassandra Session. */
	private Session session;
	
	PoolingOptions poolingOptions = new PoolingOptions();
	
	public void connect(String node, int port){
		
		if(clusterPool.containsKey(node+port)){
			this.cluster = clusterPool.get(node+port);
			try{
				session = cluster.connect();
				return;
			}catch(IllegalStateException e){//cluster was previously closed
				System.out.println("cluster was closed");
				clusterPool.remove(node+port);
			}
		}
		
		
		poolingOptions.setCoreConnectionsPerHost(HostDistance.LOCAL, 2)
		.setMaxConnectionsPerHost(HostDistance.LOCAL, 5)
		.setCoreConnectionsPerHost(HostDistance.REMOTE, 2)
		.setMaxConnectionsPerHost(HostDistance.REMOTE, 5)
		.setPoolTimeoutMillis(1000*60*10)
		.setIdleTimeoutSeconds(1000*60*10);
		
		this.cluster = Cluster.builder().withPoolingOptions(poolingOptions).addContactPoint(node).withPort(port).build();

		session = cluster.connect();
		clusterPool.put(node+port, this.cluster );
//		final Metadata metadata = cluster.getMetadata();
//
//		System.out.println(printf("Connected to cluster: %s\n", metadata.getClusterName());
//
//		for (final Host host : metadata.getAllHosts())
//		{
//			out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
//			host.getDatacenter(), host.getAddress(), host.getRack());
//		}
		
	}
	
	/**
	* Provide my Session.
	*
	* @return My session.
	*/
	public Session getSession()	{
	return this.session;
	}
	
	/** Close cluster. */
	public void close()	{
	cluster.close();
	}
}
