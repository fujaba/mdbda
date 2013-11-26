package org.mdbda.examples.counter;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.mdbda.runtime.MinMaxCountTuple;


public class MinMaxCountMapper extends Mapper<Object, DoubleWritable, Object, MinMaxCountTuple> {

	MinMaxCountTuple outTuple = new MinMaxCountTuple();
	
	@Override
	protected void map(Object key, DoubleWritable value,
			Context context)
			throws IOException, InterruptedException {		
		outTuple.setCount(new BigDecimal(value.get()));
		outTuple.setMax(value.get());
		outTuple.setMin(value.get());
		
		context.write(key, outTuple);		
	}
	
}
