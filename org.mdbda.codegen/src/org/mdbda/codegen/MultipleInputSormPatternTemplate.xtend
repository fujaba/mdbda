package org.mdbda.codegen

import org.mdbda.codegen.DefaultStormPatternTemplate
import org.mdbda.model.Pattern
import org.mdbda.model.Resource

class MultipleInputSormPatternTemplate extends DefaultStormPatternTemplate {
	
	override generareStormBolt(Pattern pattern, CodegenContext context) '''
		«FOR in : pattern.inputResources»
			«generateInputMapperBolt(in, context)»
		«ENDFOR»
	'''
	
	def generateInputMapperBolt(Resource resource, CodegenContext context) '''
	
	«context.addImport("backtype.storm.topology.BasicOutputCollector")»
	«context.addImport("backtype.storm.topology.OutputFieldsDeclarer")»
	«context.addImport("backtype.storm.topology.base.BaseBasicBolt")»
	«context.addImport("backtype.storm.tuple.Tuple")»
	
	public class «resource.name»InputMapperBolt extends BaseBasicBolt{

		@Override
		public void execute(Tuple input, BasicOutputCollector collector) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			// TODO Auto-generated method stub
			
		}

	}
	'''
	
	
}