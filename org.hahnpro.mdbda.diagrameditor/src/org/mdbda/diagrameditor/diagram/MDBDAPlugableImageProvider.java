package org.mdbda.diagrameditor.diagram;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.graphiti.platform.AbstractExtension;
import org.eclipse.graphiti.ui.platform.AbstractImageProvider;
import org.eclipse.graphiti.ui.platform.IImageProvider;
import org.osgi.framework.Bundle;

public class MDBDAPlugableImageProvider extends AbstractExtension implements IImageProvider {
	
	HashSet<IImageProvider> plugedImageProvider = new HashSet<IImageProvider>();
	
	HashMap<String, String> imageIdToUrl = new HashMap<String, String>();
	
	static final String PNG16 = "_16.png";
	static final String PNG32 = "_32.png";
	static final String PNG64 = "_64.png";
	
	static final String ASTRONAUT_URL = "platform:/plugin/org.mdbda.diagrameditor/icon/astronaut";
	static final String DATA_URL = "platform:/plugin/org.mdbda.diagrameditor/icon/data29";
	static final String FILTER_URL = "platform:/plugin/org.mdbda.diagrameditor/icon/filter13";
	static final String GEN_URL = "platform:/plugin/org.mdbda.diagrameditor/icon/gen";
	static final String HIERACHICAL_URL = "platform:/plugin/org.mdbda.diagrameditor/icon/hierarchical9";
	static final String JOIN_URL = "platform:/plugin/org.mdbda.diagrameditor/icon/join";
	static final String SUM_URL = "platform:/plugin/org.mdbda.diagrameditor/icon/spreadsheet1_sum";
	
	public static final String MAIN_ICON = "MDBDA MAIN_ICON";
	public static final String FILTER_ICON = "MDBDA FILTER_ICON";
	public static final String JOIN_ICON = "MDBDA JOIN_ICON";
	public static final String SUMMARIZATION_ICON = "MDBDA SUMMARIZATION_ICON";
	public static final String RESSOURCE_ICON = "MDBDA RESSOURCE_ICON";
	public static final String STRUCTURE_ICON = "MDBDA STRUCTURE_ICON";
	
	public MDBDAPlugableImageProvider() {
		super();
		IExtensionRegistry reg = Platform.getExtensionRegistry();
	    IConfigurationElement[] elements = reg.getConfigurationElementsFor("org.mdbda.diagrameditor.imageprovider");
		
	    for(IConfigurationElement el : elements){
	    	String imageProviderClass = el.getAttribute("ImageProviderClass");
	    	Bundle bundle = Platform.getBundle(el.getContributor().getName());
	    	try {
				Class<IImageProvider> ipClazz = (Class<IImageProvider>) bundle.loadClass(imageProviderClass);
				IImageProvider ip = ipClazz.newInstance();
				plugedImageProvider.add(ip);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    

	    imageIdToUrl.put(MAIN_ICON, ASTRONAUT_URL + PNG16);
	    imageIdToUrl.put(FILTER_ICON, FILTER_URL + PNG16);
	    imageIdToUrl.put(JOIN_ICON, JOIN_URL + PNG16);
	    imageIdToUrl.put(SUMMARIZATION_ICON, SUM_URL + PNG16);
	    imageIdToUrl.put(RESSOURCE_ICON, DATA_URL + PNG16);
	    imageIdToUrl.put(STRUCTURE_ICON, HIERACHICAL_URL + PNG16);
	}

	private String pluginId;

	@Override
	final public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}


	@Override
	final public String getPluginId() {
		return this.pluginId;
	}


	@Override
	final public String getImageFilePath(String imageId) {
		for(IImageProvider ip : plugedImageProvider){
			String imageFilePath = ip.getImageFilePath(imageId);
			if(imageFilePath != null){
				return imageFilePath;
			}
		}
		
		return imageIdToUrl.get(imageId);
	}
}
