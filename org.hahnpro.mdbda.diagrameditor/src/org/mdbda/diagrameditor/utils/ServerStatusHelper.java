package org.mdbda.diagrameditor.utils;

import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.util.IColorConstant;
import org.mdbda.model.Resource;

public class ServerStatusHelper {

	public static IColorConstant getColor(int ping) {
		if(ping < 0 ){
			return IColorConstant.GRAY; //server not set or ping back in time
		}
		if(ping >= 0 && ping < 100){
			return IColorConstant.GREEN;
		}
		if(ping >= 100 && ping < 150){
			return IColorConstant.LIGHT_GREEN;
		}
		if(ping >= 150 && ping < 200){
			return IColorConstant.YELLOW;
		}
		if(ping >= 200 && ping < 300){
			return IColorConstant.LIGHT_ORANGE;
		}
		if(ping >= 300 && ping < 500){
			return IColorConstant.ORANGE;
		}
		
		
		
		return IColorConstant.RED;
	}

	public static int getStatus(Resource resource) {

		int ping = (int) (Math.random() * 700.0);
		//if(ping > 700) ping = Integer.MAX_VALUE;
		
		
		return ping;
	}

}
