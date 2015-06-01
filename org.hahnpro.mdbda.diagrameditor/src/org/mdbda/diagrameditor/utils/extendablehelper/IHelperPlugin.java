package org.mdbda.diagrameditor.utils.extendablehelper;

import org.mdbda.model.Resource;

public interface IHelperPlugin {
	public boolean canHelp(Resource resource);
}
