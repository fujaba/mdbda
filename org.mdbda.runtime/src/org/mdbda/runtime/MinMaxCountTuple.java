package org.mdbda.runtime;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.math.BigDecimal;

import org.apache.hadoop.io.Writable;

public class MinMaxCountTuple implements Writable {
	double min = Double.MAX_VALUE;
	double max = Double.MIN_VALUE;
	BigDecimal count = new BigDecimal(0.0);
	
	public MinMaxCountTuple() {
	}
	
	public MinMaxCountTuple(double initValue) {
		setMin(initValue);
		setMax(initValue);
		setCount(new BigDecimal(initValue));
	}
	
	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public BigDecimal getCount() {
		return count;
	}

	public void setCount(BigDecimal count) {
		this.count = count;
	}

	public void readFields(DataInput in) throws IOException {
		min = in.readDouble();
		max = in.readDouble();
		count = new BigDecimal(in.readLine());
	}

	public void write(DataOutput out) throws IOException {
		out.writeDouble(min);
		out.writeDouble(max);
		out.writeChars(count.toPlainString() + "\n");//writeLine
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((count == null) ? 0 : count.hashCode());
		long temp;
		temp = Double.doubleToLongBits(max);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(min);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MinMaxCountTuple other = (MinMaxCountTuple) obj;
		if (count == null) {
			if (other.count != null)
				return false;
		} else if (!count.equals(other.count))
			return false;
		if (Double.doubleToLongBits(max) != Double.doubleToLongBits(other.max))
			return false;
		if (Double.doubleToLongBits(min) != Double.doubleToLongBits(other.min))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MinMaxCountTuple [min=" + min + ", max=" + max + ", count="
				+ count + "]";
	}
	
	
	
	
}
