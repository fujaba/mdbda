package org.mdbda.diagrameditor.utils.extendablehelper;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.mdbda.model.Resource;
import org.osgi.framework.Bundle;

public class ExtendableHelper {
	HashSet<IHelperPlugin> helperPlugins = new HashSet<IHelperPlugin>();
	
	static ExtendableHelper instance = null;
	
	public static Set<IHelperPlugin> getAllHelper(){
		if(instance == null){
			instance = new ExtendableHelper();
			IExtensionRegistry reg = Platform.getExtensionRegistry();
		    IConfigurationElement[] elements = reg.getConfigurationElementsFor("org.mdbda.diagrameditor.extendablehelper");
		    
		    for(IConfigurationElement el : elements){
		    	String helperExtentionClass = el.getAttribute("HelperExtentionClass");
		    	
		    	Bundle bundle = Platform.getBundle(el.getContributor().getName());
		    	try {
		    		Class<IHelperPlugin> helperClass = (Class<IHelperPlugin>) bundle.loadClass(helperExtentionClass);
		    		
		    		instance.helperPlugins.add( helperClass.newInstance() );
		    		
		    	} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
		    }
		}
	    return Collections.unmodifiableSet( instance.helperPlugins );
	}
	
	public static HashSet<IHelperPlugin> getHelpingHelper(Resource res){
		HashSet<IHelperPlugin> filterList = new HashSet<IHelperPlugin>();
		for(IHelperPlugin helper : getAllHelper()){
			if(helper.canHelp(res)){
				filterList.add(helper);
			}
		}
		
		return filterList;
	}
	
	
}
