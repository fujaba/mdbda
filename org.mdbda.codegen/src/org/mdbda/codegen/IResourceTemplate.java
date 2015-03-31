package org.mdbda.codegen;

import org.mdbda.model.Task;
import org.mdbda.model.Resource;

public interface IResourceTemplate extends ITemplate{
	public CharSequence generareInputResouce(Resource res, Task pattern, CharSequence controledJobName, CodegenContext context);
	public CharSequence generareOutputResouce(Resource res, CharSequence controledJobName, CodegenContext context);
}
