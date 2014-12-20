package org.mdbda.diagrameditor.diagram;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.graphiti.dt.AbstractDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.tb.IToolBehaviorProvider;
import org.osgi.framework.Bundle;

public class MDBDADiagramTypeProvider extends AbstractDiagramTypeProvider {
	private IToolBehaviorProvider[] toolBehaviorProviders;
	
	HashMap<String, HashSet<Class<ICreateFeature>>> mapPaletteCategory2CreateFeatureClasses = new HashMap<String, HashSet<Class<ICreateFeature>>>();
	HashMap<String, HashSet<Class<IAddFeature>>> 	mapPaletteCategory2AddFeatureClasses 	= new HashMap<String, HashSet<Class<IAddFeature>>>();
	HashMap<String,Class<IAddFeature>> 				resourceTypeIdAddFreature			 	= new HashMap<String,Class<IAddFeature>>();	

	
	public MDBDADiagramTypeProvider() {
		super();
		
		IExtensionRegistry reg = Platform.getExtensionRegistry();
	    IConfigurationElement[] elements = reg.getConfigurationElementsFor("org.mdbda.diagrameditor.pattern");
	    
	    for(IConfigurationElement el : elements){
	    	String createFeatureClassName = el.getAttribute("createFeatureClass");
	    	String addFeatureClassName = el.getAttribute("addFeatureClass");
	    	String typeId = el.getAttribute("typeId");
	    	String categoryGroup = el.getAttribute("group");
	    	
	    	Bundle bundle = Platform.getBundle(el.getContributor().getName());
	    	try {
	    		if(!mapPaletteCategory2CreateFeatureClasses.containsKey(categoryGroup)){
	    			mapPaletteCategory2CreateFeatureClasses.put(categoryGroup, new HashSet<Class<ICreateFeature>>());
	    		}
	    		mapPaletteCategory2CreateFeatureClasses.get(categoryGroup).add(
	    				(Class<ICreateFeature>) bundle.loadClass(createFeatureClassName));
	    		
	    		
	    		if(!mapPaletteCategory2AddFeatureClasses.containsKey(categoryGroup)){
	    			mapPaletteCategory2AddFeatureClasses.put(categoryGroup, new HashSet<Class<IAddFeature>>());
	    		}

	    		Class<IAddFeature> addClazz = (Class<IAddFeature>) bundle.loadClass(addFeatureClassName);
	    		
	    		mapPaletteCategory2AddFeatureClasses.get(categoryGroup).add(addClazz);
	    		resourceTypeIdAddFreature.put(typeId, addClazz);
	    		
	    		
	    	} catch (ClassNotFoundException e) {
				e.printStackTrace();
	    	}
	    }
	    
		setFeatureProvider(new MDBDAFeatureProvider(this));
	}
	
	@Override
	public IToolBehaviorProvider[] getAvailableToolBehaviorProviders() {
		if(toolBehaviorProviders == null){
			toolBehaviorProviders = new IToolBehaviorProvider[] { new MDBDAToolBehaviorProvider(this) };
		}
		
		return toolBehaviorProviders;
	}
	
	
	
	@Override
	public boolean isAutoUpdateAtStartup() {
		return true;
	}
	
	@Override
	public boolean isAutoUpdateAtRuntime() {
		return true;
	}
	
	@Override
	public boolean isAutoUpdateAtRuntimeWhenEditorIsSaved() {
		return true;
	}
	
	@Override
	public boolean isAutoUpdateAtReset() {
		return true;
	}
}
