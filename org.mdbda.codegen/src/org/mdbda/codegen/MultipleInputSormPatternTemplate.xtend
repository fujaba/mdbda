package org.mdbda.codegen

import org.mdbda.codegen.DefaultStormPatternTemplate
import org.mdbda.model.Pattern
import org.mdbda.model.Resource
import org.mdbda.codegen.helper.MDBDAConfiguration
import org.json.simple.JSONObject
import org.mdbda.codegen.helper.CodeGenHelper

class MultipleInputSormPatternTemplate extends DefaultStormPatternTemplate {
	
	
	val InputMapperBoltClass = "InputMapperBolt"
	override genMapBolt(Pattern resource, CodegenContext context) '''
	
		«context.addImport("backtype.storm.topology.BasicOutputCollector")»
		«context.addImport("backtype.storm.topology.OutputFieldsDeclarer")»
		«context.addImport("backtype.storm.topology.base.BaseBasicBolt")»
		«context.addImport("backtype.storm.tuple.Tuple")»
		«val config = MDBDAConfiguration.readConfigString(resource.configurationString)»
		«val funktions = config.getMultipleMapFunction»
		
		«var int fooCount = 0»
		«FOR foo : funktions»
			«val JSONObject funktion = foo as JSONObject»
			public class «InputMapperBoltClass»«fooCount» extends BaseBasicBolt{
			
				@Override
				public void execute(Tuple input, BasicOutputCollector collector) {
					String _streamId = "«fooCount»";
					«val stormExec = config.getStormExecuteFunction(funktion)»
					«IF stormExec == null || stormExec.empty»
						//stormExecute is not configured
					«ELSE»
						«CodeGenHelper.beautifyJava(stormExec,0)»
					«ENDIF»
				}
			
				@Override
				public void declareOutputFields(OutputFieldsDeclarer declarer) {
					declarer.declare(new Fields("key","value"));
				}
			
			}
			«{fooCount = fooCount + 1 ; ""}»
		«ENDFOR»
	'''
	
	override genTestDataBolt(Pattern pattern,CodegenContext context){
		val config = MDBDAConfiguration.readConfigString(pattern.configurationString)
		val StringBuilder sb = new StringBuilder();
		var int counter = 0
		for(foo : config.getMultipleMapFunction()){			
			sb.append(genTestDataBolt(pattern,context, foo as JSONObject , counter + ""));
			counter = counter + 1;
			sb.append("\n")
		}
		
		return sb.toString
	

	
	}
	
	override genJUnitTest(Pattern pattern,CodegenContext context)'''
		«context.addImport("org.junit.Test")»
		@Test
		public void test() {

				// build the test topology
		«context.addImport("backtype.storm.topology.TopologyBuilder")»
				TopologyBuilder builder = new TopologyBuilder();
		«context.addImport("backtype.storm.tuple.Fields")»
		«context.addImport("backtype.storm.testing.FeederSpout")»
		
		«val config = MDBDAConfiguration.readConfigString(pattern.configurationString)»
		«val numberOfMultipleMapFunction = config.getMultipleMapFunction().size»
		
		«FOR n : (0..numberOfMultipleMapFunction-1)»
			builder.setSpout("testInput«n»", new «TestDataSpoutClass»«n»());
			builder.setBolt("mapBolt«n»", new «InputMapperBoltClass»«n»()).fieldsGrouping("testInput«n»",new Fields("key"));
		«ENDFOR»
			
		builder.setBolt("reduceBolt", new «ReduceBoltBoltClass»())
		«FOR n : (0..numberOfMultipleMapFunction-1)»
			.fieldsGrouping("mapBolt«n»", new Fields("key"))
		«ENDFOR»
		;
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
}