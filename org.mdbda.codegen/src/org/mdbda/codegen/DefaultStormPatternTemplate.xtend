package org.mdbda.codegen

import org.mdbda.codegen.IStormPatternTemplate
import org.mdbda.model.Pattern
import org.mdbda.codegen.helper.MDBDAConfiguration
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.mdbda.codegen.helper.CodeGenHelper

class DefaultStormPatternTemplate implements IStormPatternTemplate {
	
	override generareStormPattern(Pattern pattern, CodegenContext context) '''
		«val config = MDBDAConfiguration.readConfigString(pattern.configurationString)»
		«context.addImport("backtype.storm.topology.BasicOutputCollector")»
		«context.addImport("backtype.storm.topology.OutputFieldsDeclarer")»
		«context.addImport("backtype.storm.topology.base.BaseBasicBolt")»
		«context.addImport("backtype.storm.tuple.Tuple")»

		«genMapBolt(pattern, context)»
		«genReduceBolt(pattern, context)»
		«genTestDataValidationBolt(pattern, context)»
		
		«genTestDataBolt(pattern, context)»
		
		//############## junit test  ###########
		«genJUnitTest(pattern, context)»
	'''
	
	def genJUnitTest(Pattern pattern,CodegenContext context)'''
		«context.addImport("org.junit.Test")»
		@Test
		public void test() {

				// build the test topology
		«context.addImport("backtype.storm.topology.TopologyBuilder")»
				TopologyBuilder builder = new TopologyBuilder();
		«context.addImport("backtype.storm.tuple.Fields")»
		«context.addImport("backtype.storm.testing.FeederSpout")»
				builder.setSpout("testInput", new «TestDataSpoutClass»());
		
				builder.setBolt("mapBolt", new «MapBoltClass»()).fieldsGrouping("testInput",new Fields("key"));
				builder.setBolt("reduceBolt", new «ReduceBoltBoltClass»()).fieldsGrouping("mapBolt",new Fields("key"));
				builder.setBolt("validationBolt", new «TestDataValidationBoltClass»()).globalGrouping("reduceBolt");
				
				
		«context.addImport("backtype.storm.generated.StormTopology")»
				StormTopology topology = builder.createTopology();
		
				// complete the topology
		
				// prepare the config
		«context.addImport("backtype.storm.Config")»
				Config conf = new Config();
				conf.setNumWorkers(2);
		«context.addImport("backtype.storm.LocalCluster")»
				LocalCluster cluster = new LocalCluster();
		
				cluster.submitTopology("test", conf, builder.createTopology());
		«context.addImport("backtype.storm.utils.Utils")»
				Utils.sleep(5000);
				cluster.shutdown();
		
		}
	'''
	def genTestDataBolt(Pattern pattern,CodegenContext context){
		val config = MDBDAConfiguration.readConfigString(pattern.configurationString)
		genTestDataBolt(pattern,context,config.mapFunction , "");
	}
	
	protected val ReduceBoltBoltClass = "ReduceBolt"
	def genReduceBolt(Pattern pattern, CodegenContext context) '''
		«val config = MDBDAConfiguration.readConfigString(pattern.configurationString)»
		public static class «ReduceBoltBoltClass» extends BaseBasicBolt{
			
			«config.getFields(config.reduceFunction)»
			
			«context.addImport("java.util.Queue")»
			«context.addImport("java.util.LinkedList")»
			Queue<Tuple> collectedTupels = new LinkedList<>();
		«context.addImport("backtype.storm.Constants")»
			@Override
			public void execute(Tuple input, BasicOutputCollector collector) {
				if(input.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID) && input.getSourceStreamId().equals(Constants.SYSTEM_TICK_STREAM_ID) ){
					«config.getStormExecuteFunction(config.reduceFunction)»
				}else{
					collectTuple(input);
				}
			}
			
			void collectTuple(Tuple input){
				collectedTupels.offer(input);
			}
			
			@Override
			public void declareOutputFields(OutputFieldsDeclarer declarer) {
				declarer.declare(new Fields("key","value"));
			}
			
			
			
			@Override
			public Map<String, Object> getComponentConfiguration() {
				«context.addImport("java.util.HashMap")»
				Map<String, Object> conf = new HashMap<String, Object>();
				conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 1);//collect data 1 sec
				return conf;
			}
		}
	'''
	protected val MapBoltClass = "MapBolt"
	def genMapBolt(Pattern pattern, CodegenContext context) '''
		«val config = MDBDAConfiguration.readConfigString(pattern.configurationString)»
		public static class «MapBoltClass» extends BaseBasicBolt{

			@Override
			public void execute(Tuple input, BasicOutputCollector collector) {
				«config.getStormExecuteFunction(config.mapFunction)»
			}
		
			@Override
			public void declareOutputFields(OutputFieldsDeclarer declarer) {
				declarer.declare(new Fields("key","value"));
			}
		}
	'''
	
	protected val TestDataValidationBoltClass = "TestDataValidationBolt"
	
	def genTestDataValidationBolt(Pattern pattern, CodegenContext context) '''
		«val config = MDBDAConfiguration.readConfigString(pattern.configurationString)»
		public static class «TestDataValidationBoltClass» extends BaseBasicBolt{
			«context.addImport("java.util.Queue")»
			«context.addImport("java.util.LinkedList")»
			Queue<Values> testData = new LinkedList<>();
			
			public «TestDataValidationBoltClass»(){
				«val JSONArray testReduceOutput = config.getTestOutput(config.reduceFunction)»
				«FOR inputString : testReduceOutput »
					«var String[] inputElements = (inputString as String).split(";")»
					testData.offer(new Values(«CodeGenHelper.fixInputString(inputElements.get(0))»,«CodeGenHelper.fixInputString(inputElements.get(1))»));
				«ENDFOR»
			}
			
			@Override
			public void execute(Tuple input, BasicOutputCollector collector) {
				for( Values v : testData){
					if(		input.getString(0).equals(v.get(0)) 
						&& 	((input.getString(1) == null && v.get(1) == null) || input.getString(1) != null && input.getString(1).equals(v.get(1)))  ){
						testData.remove(v);
						return;
					}
				}
				System.err.println("Fond no match for: " + input);
				System.err.println("\t in list: " + testData);
			}
		«context.addImport("static org.junit.Assert.assertTrue")»
			@Override
			public void cleanup() {
				assertTrue( "Not all data was processed" , testData.isEmpty() );
				System.out.println("All data was valid");
				super.cleanup();
			}
			@Override
			public void declareOutputFields(OutputFieldsDeclarer declarer) {
				declarer.declare(new Fields("key","value"));
			}
		}
	'''
	
	protected val TestDataSpoutClass = "TestDataSpout"
	
	def genTestDataBolt(Pattern pattern, CodegenContext context, JSONObject jsonFunktion, String suffix) '''
		«context.addImport("backtype.storm.spout.SpoutOutputCollector")»
		«context.addImport("backtype.storm.task.TopologyContext")»
		«context.addImport("backtype.storm.topology.OutputFieldsDeclarer")»
		«context.addImport("backtype.storm.topology.base.BaseRichSpout")»
		«context.addImport("backtype.storm.tuple.Fields")»
		«context.addImport("backtype.storm.tuple.Values")»
		«context.addImport("backtype.storm.utils.Utils")»
		«context.addImport("java.util.EmptyStackException")»
		«context.addImport("java.util.Map")»
		«context.addImport("java.util.Stack")»
		«val config = MDBDAConfiguration.readConfigString(pattern.configurationString)»
		
		public static class «TestDataSpoutClass»«suffix» extends BaseRichSpout {
			SpoutOutputCollector _collector;
			«context.addImport("java.util.Queue")»
			«context.addImport("java.util.LinkedList")»
			Queue<Values> testData = new LinkedList<>();
			
			@Override
			public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
				_collector = collector;
				«val JSONArray testMapInput = config.getTestInput(jsonFunktion)» 
				«FOR inputString : testMapInput »
					«var String[] inputElements = (inputString as String).split(";")»
					testData.offer(new Values(«CodeGenHelper.fixInputString(inputElements.get(0))»,«CodeGenHelper.fixInputString(inputElements.get(1))»));
				«ENDFOR»
			}
			
			@Override
			public void nextTuple() {
				Utils.sleep(100);
		
				Values data = testData.poll();
				if(data != null){
					System.out.println("Send test data: " + data.toString());
					_collector.emit(data);
				}else{
					System.out.println("No more Test data");
					Utils.sleep(1000);
				}
			}
			
			@Override
			public void ack(Object id) {
			}
			
			@Override
			public void fail(Object id) {
			}
			
			@Override
			public void declareOutputFields(OutputFieldsDeclarer declarer) {
				declarer.declare(new Fields("key","value"));
			}
		}
	'''
	
}