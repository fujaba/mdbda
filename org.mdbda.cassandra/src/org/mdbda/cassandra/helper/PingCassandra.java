package org.mdbda.cassandra.helper;

import java.util.HashMap;

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
//		long ts1 = System.currentTimeMillis();
		int p =ping(conf.getLiveServerIP());
//		long ts2 = System.currentTimeMillis();
//		System.out.println("ping live in : " + (ts2 - ts1) + "ms ping: " + p);
		return p;
	}

	@Override
	public int pingTestResource(Resource res) {
		CassandraConfigReader conf = new CassandraConfigReader(res.getConfigurationString());
//		long ts1 = System.currentTimeMillis();
		int p = ping(conf.getTestServerIP());
//		long ts2 = System.currentTimeMillis();
//		System.out.println("ping test in : " + (ts2 - ts1) + "ms ping: " + p);
		return p;
	}

	static HashMap<String,Long>		cacheTimeout	= new HashMap<String, Long>();
	static HashMap<String,Integer>	cachedValue		= new HashMap<String, Integer>();
	
	private int ping(Object cassandraServerIP){
		if(cassandraServerIP != null){
			String ip = (String)cassandraServerIP;
			if("".equals(ip)) return -1;
			if(!cacheTimeout.containsKey(ip)){
				cacheTimeout.put(ip, new Long(42));
				cachedValue.put(ip, new Integer(42));
			}else{
				if(cacheTimeout.get(ip) > System.currentTimeMillis()){
					return cachedValue.get(ip);
				}
			}
			
			String el[] = (ip).split(":");
			CassandraConnector con = new CassandraConnector();
			try{

				long ts1 = System.currentTimeMillis();
				try{
					con.connect(el[0], el.length == 2 ? Integer.parseInt(el[1]) : 9042);
				}catch(IllegalArgumentException ill){//user is typing ?
					cacheTimeout.put(ip, System.currentTimeMillis() + 1000); //5s
					cachedValue.put(ip, new Integer(-1));
					return -1;
				}
				ResultSet rs = con.getSession().execute("SELECT now() FROM system.local;");
				long ts2 = System.currentTimeMillis();
				int pingTime = (int) (ts2 - ts1);
				cacheTimeout.put(ip, System.currentTimeMillis() + 1000); //1s
				cachedValue.put(ip, new Integer(pingTime));
				return pingTime ;
			}catch(NoHostAvailableException e){
				cacheTimeout.put(ip, System.currentTimeMillis() + 1000*30); //30s
				cachedValue.put(ip, new Integer(-1));
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
