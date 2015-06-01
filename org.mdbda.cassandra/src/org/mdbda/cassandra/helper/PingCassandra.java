package org.mdbda.cassandra.helper;

import org.mdbda.cassandra.CreateCassandraResourceFeature;
import org.mdbda.diagrameditor.utils.extendablehelper.IPingResourceHelper;
import org.mdbda.model.Resource;

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
		if(cassandraServerIP != null)
			return 23;//TODO ping the Server (cql: SELECT now() FROM system.local;)
		
		return -1;
	}

	@Override
	public boolean canHelp(Resource resource) {
		return CreateCassandraResourceFeature.TYPE_ID.equals(resource.getTypeId());
	}

}
