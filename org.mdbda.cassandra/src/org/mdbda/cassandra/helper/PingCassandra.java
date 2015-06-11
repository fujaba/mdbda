package org.mdbda.cassandra.helper;

import org.mdbda.cassandra.CassandraConnector;
import org.mdbda.cassandra.CreateCassandraResourceFeature;
import org.mdbda.diagrameditor.utils.extendablehelper.IPingResourceHelper;
import org.mdbda.model.Resource;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

public class PingCassandra implements IPingResourceHelper {

	@Override
	public int pingLiveResource(Resource res) {
		CassandraConfigReader conf = new CassandraConfigReader(res.getConfigurationString());
		return ping(conf.getLiveServerIP());
	}

	@Override
	public int pingTestResource(Resource res) {
		CassandraConfigReader conf = new CassandraConfigReader(res.getConfigurationString());
		return ping(conf.getTestServerIP());
	}
	
	private int ping(Object cassandraServerIP){
		if(cassandraServerIP != null){
			String el[] = ((String)cassandraServerIP).split(":");
			CassandraConnector con = new CassandraConnector();
			try{
				con.connect(el[0], el.length == 2 ? Integer.parseInt(el[1]) : 9042);
				long ts1 = System.currentTimeMillis();
				ResultSet rs = con.getSession().execute("SELECT now() FROM system.local;");
				long ts2 = System.currentTimeMillis();
				return (int) (ts2 - ts1);
			}catch(NoHostAvailableException e){
				return -1;
			}
			//return 23;//TODO ping the Server (cql: SELECT now() FROM system.local;)
			
		}
		
		return -1;
	}

	@Override
	public boolean canHelp(Resource resource) {
		return CreateCassandraResourceFeature.TYPE_ID.equals(resource.getTypeId());
	}

}
