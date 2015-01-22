package org.mdbda.diagrameditor.utils;

import org.eclipse.graphiti.mm.pictograms.Connection;
import org.mdbda.diagrameditor.internal.PublicGetBusinessObjectForPictogramElementInterface;
import org.mdbda.model.Resource;

@Deprecated
public class DataformatUtils {

	public static String getConnectionTextDecoration(Connection conn, PublicGetBusinessObjectForPictogramElementInterface context) {
		String textDecoration = "";
		
	//	Resource resource = (Resource) context.getBusinessObjectForPictogramElement(  conn.getStart().getParent() );
//		Dataformat dataFormat = resource.getOutputFormat();
//		if(dataFormat == null){
//			textDecoration = "Dataformat is not set";
//		}else{
//			textDecoration = dataFormat.getName() + " :\n"
//			 + dataFormat.getKeyTypeClass() + "\n"
//			 + "\\|/" + "\n"
//			 + dataFormat.getValueTypeClass();
//		}

		return textDecoration;
	}

}
