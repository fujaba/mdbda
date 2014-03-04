package org.mdbda.codegen.plugins

import org.mdbda.model.Pattern
import org.mdbda.codegen.IPatternTemplate

import static extension org.mdbda.codegen.helper.ConfigurationReader.*
import org.mdbda.model.Dataformat

class PartitoningPattern implements IPatternTemplate {
	
	override generareStormBolt(Pattern pattern) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override generareMapReducePattern(Pattern pattern) {
		'''
		package test;

		import org.apache.hadoop.conf.Configurable;
		import org.apache.hadoop.conf.Configuration;
		import org.apache.hadoop.mapreduce.Mapper;
		import org.apache.hadoop.mapreduce.Partitioner;
		import org.apache.hadoop.mapreduce.Reducer;
		
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
																 «pattern.configurationClass.intermediateKeyType», «pattern.configurationClass.intermediateValueType»>
					
					«pattern.configurationClass.mapFields»
					
					protected void map(	«pattern.inputFormat.keyTypeClass» key, «pattern.inputFormat.valueTypeClass» value,
										org.apache.hadoop.mapreduce.Mapper.Context context)
										throws IOException, InterruptedException {
						«pattern.configurationClass.mapMethod»
					}
				}
				
				static class «pattern.name»Partitoner extends Partitioner<«pattern.configurationClass.intermediateKeyType», «pattern.configurationClass.intermediateValueType»> implements Configurable{
			
					«pattern.configurationClass.partitonerFields»
						
					private Configuration conf = null;
					@Override
					public void setConf(Configuration conf) {
						this.conf = conf;
						«pattern.configurationClass.partitonerConfiguration»
					}
			
					@Override
					public Configuration getConf() {
						return conf;
					}
			
					@Override
					public int getPartition(«pattern.configurationClass.intermediateKeyType» key, 
											«pattern.configurationClass.intermediateValueType» value, 
											int numPartitions) {						
						«pattern.configurationClass.partitionMethod»
					}
					
				}
				
				static class «pattern.name»Reducer extends Reducer<«pattern.configurationClass.intermediateKeyType», «pattern.configurationClass.intermediateValueType», «pattern.outputFormat.keyTypeClass», «pattern.outputFormat.valueTypeClass»>{
					«pattern.configurationClass.mapFields»
					protected void reduce(«pattern.configurationClass.intermediateKeyType» key, 
											Iterable<«pattern.configurationClass.intermediateValueType»> values, 
											org.apache.hadoop.mapreduce.Reducer.Context context)
											throws IOException, InterruptedException {
						«pattern.configurationClass.reduceMethod»
				}
		}
				
		'''
	}
	
	
	

}