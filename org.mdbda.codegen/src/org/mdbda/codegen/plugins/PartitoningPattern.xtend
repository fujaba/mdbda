package org.mdbda.codegen.plugins

import org.mdbda.model.Pattern
import org.mdbda.codegen.IPatternTemplate

import static extension org.mdbda.codegen.helper.ConfigurationReader.*
import org.mdbda.model.Dataformat
import org.mdbda.codegen.CodegenContext
import org.mdbda.codegen.helper.MDBDAConfiguration

class PartitoningPattern implements IPatternTemplate {
	
	override generareStormBolt(Pattern pattern, CodegenContext context) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override generareMapReducePattern(Pattern pattern, CodegenContext context) {
		'''
			«val config = MDBDAConfiguration.readConfigString(pattern.configurationString)»
		
		class «pattern.name»MapReducePattern {
						
			public static Job getJob(Configuration conf, Path input, Path intermediate)  throws IOException{
				
				Job job =  Job.getInstance(conf, "«pattern.name»-Job" );
				
				job.setJarByClass(«pattern.name»MapReducePattern.class);
				
				job.setMapperClass(«pattern.name»Mapper.class);
				job.setPartitionerClass(«pattern.name»Partitoner.class);
				job.setReducerClass(«pattern.name»Reducer.class);
				
				job.setOutputKeyClass(«pattern.outputFormat.keyTypeClass»);
				job.setOutputValueClass(«pattern.outputFormat.valueTypeClass»);
				
				job.setInputFormatClass(«pattern.inputFormat.formatClass»);
				«pattern.inputFormat.formatClassConfigutation(pattern)»
												
				job.setOutputFormatClass(«pattern.outputFormat.formatClass»);
				«pattern.outputFormat.formatClassConfigutation(pattern)»
								
				
				return job;		
			}
				static class «pattern.name»Mapper extends Mapper<«pattern.inputFormat.keyTypeClass», «pattern.inputFormat.valueTypeClass»,
																 «pattern.configurationString.intermediateKeyType», «pattern.configurationString.intermediateValueType»>
					
					«pattern.configurationString.mapFields»
					
					protected void map(	«pattern.inputFormat.keyTypeClass» key, «pattern.inputFormat.valueTypeClass» value,
										org.apache.hadoop.mapreduce.Mapper.Context context)
										throws IOException, InterruptedException {
						
						//in: «config.getTestInput(config.mapFunction)»
						«config.getFunction(config.mapFunction)»
						//out: «config.getTestOutput(config.mapFunction)»
					}
				}
				
				static class «pattern.name»Partitoner extends Partitioner<«pattern.configurationString.intermediateKeyType», «pattern.configurationString.intermediateValueType»> implements Configurable{
			
					«pattern.configurationString.partitonerFields»
						
					private Configuration conf = null;
					@Override
					public void setConf(Configuration conf) {
						this.conf = conf;
						«pattern.configurationString.partitonerConfiguration»
					}
			
					@Override
					public Configuration getConf() {
						return conf;
					}
			
					@Override
					public int getPartition(«pattern.configurationString.intermediateKeyType» key, 
											«pattern.configurationString.intermediateValueType» value, 
											int numPartitions) {						
						«pattern.configurationString.partitionMethod»
					}
					
				}
				
				static class «pattern.name»Reducer extends Reducer<«pattern.configurationString.intermediateKeyType», «pattern.configurationString.intermediateValueType», «pattern.outputFormat.keyTypeClass», «pattern.outputFormat.valueTypeClass»>{
					«pattern.configurationString.mapFields»
					protected void reduce(«pattern.configurationString.intermediateKeyType» key, 
											Iterable<«pattern.configurationString.intermediateValueType»> values, 
											org.apache.hadoop.mapreduce.Reducer.Context context)
											throws IOException, InterruptedException {
						«pattern.configurationString.reduceMethod»
				}
		}
				
		'''
	}
	
	override genJobConf(Pattern pattern, CodegenContext context) '''
	sadf
	'''
	
	override genTempOutputs(Pattern pattern, CodegenContext context) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	
	

}