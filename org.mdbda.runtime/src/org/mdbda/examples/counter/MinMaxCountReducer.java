package org.mdbda.examples.counter;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.mdbda.runtime.MinMaxCountTuple;

public class MinMaxCountReducer extends
		Reducer<Object, MinMaxCountTuple, Object, MinMaxCountTuple> {
	
	MinMaxCountTuple resultTuple = new MinMaxCountTuple();
	
	@Override
	protected void reduce(Object key, Iterable<MinMaxCountTuple> values,
			Context context)
			throws IOException, InterruptedException {

		for(MinMaxCountTuple tuple : values){
			
			if(tuple.getMin() < resultTuple.getMin()){
				resultTuple.setMin(tuple.getMin());
			}
			
			if(tuple.getMax() > resultTuple.getMax()){
				resultTuple.setMax(tuple.getMax());
			}
			
			resultTuple.setCount( resultTuple.getCount().add(tuple.getCount()) );
		}
		
		context.write(key, resultTuple);
		
	}

}
