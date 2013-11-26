package org.mdbda.examples.counter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;
import org.mdbda.runtime.MinMaxCountTuple;

public class MinMaxCountTest {
	MapDriver<Object, DoubleWritable, Object, MinMaxCountTuple> mapDriver;
	ReduceDriver<Object, MinMaxCountTuple, Object, MinMaxCountTuple> reduceDriver;
	MapReduceDriver<Object, DoubleWritable, Object, MinMaxCountTuple, Object, MinMaxCountTuple> mapReduceDriver;

	@Before
	public void setUp() {
		MinMaxCountMapper  mapper = new MinMaxCountMapper();
		MinMaxCountReducer reducer = new MinMaxCountReducer();
		mapDriver = MapDriver.newMapDriver(mapper);		
		reduceDriver = ReduceDriver.newReduceDriver(reducer);
		mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);
	}

	@Test
	public void testMapper() throws IOException {
		mapDriver.withInput(new Text("TestKey"), new DoubleWritable(42));
		MinMaxCountTuple resultTuple = new MinMaxCountTuple();
		mapDriver.withOutput(new Text("TestKey"),  new MinMaxCountTuple(42.0));
		mapDriver.runTest();
	}

	@Test
	public void testReducer() throws IOException {
		List<MinMaxCountTuple> values = new ArrayList<MinMaxCountTuple>();
		values.add(new MinMaxCountTuple(42.0));
		values.add(new MinMaxCountTuple(23));
		reduceDriver.withInput(new Text("TestKey"), values);
		
		MinMaxCountTuple result = new MinMaxCountTuple();
		result.setCount(new BigDecimal(42.0 + 23.0));
		result.setMax(42);
		result.setMin(23);
		
		reduceDriver.withOutput(new Text("TestKey"), result);
		reduceDriver.runTest();
	}
}
