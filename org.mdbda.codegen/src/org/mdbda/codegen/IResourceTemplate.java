package org.mdbda.codegen;

import org.mdbda.model.Resource;

public interface IResourceTemplate {
	public CharSequence generareMapReduceInputResouce(Resource res, CodegenContext context);
	public CharSequence generareMapReduceOutputResouce(Resource res, CodegenContext context);
	

	public CharSequence generareStormInputResouce(Resource res, CodegenContext context);
	public CharSequence generareStormOutputResouce(Resource res, CodegenContext context);
}
