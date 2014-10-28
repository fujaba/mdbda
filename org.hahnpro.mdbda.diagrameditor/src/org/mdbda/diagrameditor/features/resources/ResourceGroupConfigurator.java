package org.mdbda.diagrameditor.features.resources;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.mdbda.model.Resource;
import org.mdbda.diagrameditor.features.AbstractGroupConfigurator;
import org.mdbda.model.ResourcesTemplateConstatns;
public class ResourceGroupConfigurator extends AbstractGroupConfigurator {

	
//	@Inject 
//	public void doSomething(IExtensionRegistry registry){
//	  // do something
//	  IConfigurationElement[] configurationElements = registry.getConfigurationElementsFor("org.mdbda.diagrameditor.pattern");
//	  
//	  for(IConfigurationElement cEl : configurationElements){
//		  System.out.println("IConfigurationElement: " + cEl);
//	  }
//	} 
	
	public ResourceGroupConfigurator() {
		loadPlugins();
	}

	ArrayList<Class<ICreateFeature>> createFeatureClasses = new ArrayList<Class<ICreateFeature>>();	
	HashMap<String,Class<IAddFeature>> resourceTypeIdAddFreature = new HashMap<String,Class<IAddFeature>>();	
	
	public void loadPlugins(){
		IExtensionRegistry reg = Platform.getExtensionRegistry();
	    IConfigurationElement[] elements = reg.getConfigurationElementsFor("org.mdbda.diagrameditor.pattern");
	    
	    for(IConfigurationElement cEl : elements){
	    	 
	    	
	    	 System.out.println("IConfigurationElement: " + cEl);
	    	 String createFeatureClassName = cEl.getAttribute("createFeatureClass");
	    	 try {
//	    		 Object createExecutableExtension = cEl.createExecutableExtension(createFeatureClassName);
	    		 System.out.println(createFeatureClassName);
	    		 
	    		 
	    		 
	    		 createFeatureClasses.add((Class<ICreateFeature>) Platform.getBundle(cEl.getContributor().getName()).loadClass(createFeatureClassName));
//				createFeatureClasses.add((Class<ICreateFeature>) Class.forName(createFeatureClassName));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
//			} catch (CoreException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
	    	
	    	 
	    	 
	    	 String addFeatureClassName = cEl.getAttribute("addFeatureClass");
	    	 String typeId = cEl.getAttribute("typeId");
	    	 try {
	    		// resourceTypeIdAddFreature.put(typeId, (Class<IAddFeature>) Class.forName(addFeatureClassName));
	    		 resourceTypeIdAddFreature.put(typeId, (Class<IAddFeature>) Platform.getBundle("org.mdbda.Neo4jPlugin").loadClass(addFeatureClassName));

	    	 } catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
	    	 
		  }
	}
	
	@Override
	public PaletteCompartmentEntry getPalette(IFeatureProvider fp) {
		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(
				"Resources", null); // TODO iconid

//CreateNeo4jResourceFeature.class
		
		for(Class<ICreateFeature> clazz : new Class[] {CreateCassandraResourceFeature.class,CreateHDFSResourceFeature.class, CreateGenericResourceFeature.class, CreateHazelcastResourceFeature.class}){
			addPaletteElementToCompartmentAndLinkCreateFeatureFromCreateFeatureClass(
					compartmentEntry, fp, clazz);
		}
		
		for(Class<ICreateFeature> clazz : createFeatureClasses){
			addPaletteElementToCompartmentAndLinkCreateFeatureFromCreateFeatureClass(
					compartmentEntry, fp, clazz);
		}


		return compartmentEntry;
	}

	@Override
	public List<ICreateFeature> getCreateFeatures(IFeatureProvider fp) {
		//new CreateNeo4jResourceFeature(fp)
		ICreateFeature[] cf = new ICreateFeature[] {
				new CreateCassandraResourceFeature(fp), new CreateHDFSResourceFeature(fp), new CreateGenericResourceFeature(fp), new CreateHazelcastResourceFeature(fp)};

		ArrayList<ICreateFeature> cfList = new ArrayList<ICreateFeature>(Arrays.asList(cf));
		
		for(Class<ICreateFeature> clazz : createFeatureClasses){
			try {
				cfList.add( clazz.getConstructor(IFeatureProvider.class).newInstance(fp) );
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return cfList;
	}

	@Override
	public IAddFeature getAddFeature(IAddContext context, IFeatureProvider fp) {
		if (context instanceof IAddContext
				&& context.getNewObject() instanceof Resource) {
			
			switch(((Resource)context.getNewObject()).getTypeId()){
				case ResourcesTemplateConstatns.RESOURCETYPE_CASSANDRA:			return new AddCassandraResourceFeature(fp);
				case ResourcesTemplateConstatns.RESOURCETYPE_HAZELCAST:			return new AddHazelcastResourceFeature(fp);
				case ResourcesTemplateConstatns.RESOURCETYPE_HDFS: return new AddHDFSResourceFeature(fp);
				case ResourcesTemplateConstatns.RESOURCETYPE_GENERIC: return new AddGenericResourceFeature(fp);
//				case ResourcesTemplateConstatns.RESOURCETYPE_NEO4J: return new AddNeo4jResourceFeature(fp);
				default:
					if(resourceTypeIdAddFreature.containsKey(((Resource)context.getNewObject()).getTypeId())){
						Class<IAddFeature> clazz = resourceTypeIdAddFreature.get(((Resource)context.getNewObject()).getTypeId());
						try {
							return clazz.getConstructor(IFeatureProvider.class).newInstance(fp) ;
						} catch (InstantiationException | IllegalAccessException
								| IllegalArgumentException | InvocationTargetException
								| NoSuchMethodException | SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				
			}
					} 
		return null;
	}

	
	
}
