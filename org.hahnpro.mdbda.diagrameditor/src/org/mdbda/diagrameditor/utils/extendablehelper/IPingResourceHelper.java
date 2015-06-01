package org.mdbda.diagrameditor.utils.extendablehelper;

import org.mdbda.model.Resource;

public interface IPingResourceHelper extends IHelperPlugin {
	public int pingLiveResource(Resource res);
	public int pingTestResource(Resource res);
}
