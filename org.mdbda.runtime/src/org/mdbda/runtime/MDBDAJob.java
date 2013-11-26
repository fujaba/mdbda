package org.mdbda.runtime;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;

public interface MDBDAJob {

	public Job getJob();
	public Configuration getConfiguration();
}
