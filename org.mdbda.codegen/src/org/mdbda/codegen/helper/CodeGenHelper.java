package org.mdbda.codegen.helper;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;
import org.eclipse.xtext.parser.IEncodingProvider;
import org.eclipse.xtext.service.AbstractGenericModule;
import org.mdbda.codegen.MDBDACodegenerator;
import org.mdbda.model.ModelPackage;
import org.mdbda.model.Pattern;

import com.google.inject.Guice;

public class CodeGenHelper {
	static String fixClassName(String className){
		if(className == null) return "Null";
		
		String[] s = className.split("\\s+");
		
		String ret = "";
		for (String str : s) {
			ret += str.substring(0, 1).toUpperCase() + str.substring(1);
		}
		
		return ret;
	}
	
	public static String getMapReduceClassNameFromPattern(Pattern p){
		return fixClassName(p.getName()) + "MapReduce";
	}
	public static String getStormClassNameFromPattern(Pattern p){
		return fixClassName(p.getName()) + "Storm";		
	}
	
	public static void doGenerate(String modelUri, String targetUri){
		MDBDACodegenerator gen = new MDBDACodegenerator();
		ModelPackage.eINSTANCE.eClass();//init
				
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		
	    Map m = reg.getExtensionToFactoryMap();
	    m.put("mdbdamodel", new XMIResourceFactoryImpl());

	    // Obtain a new resource set
	    ResourceSetImpl resSet = new ResourceSetImpl();

	    // Get the resource
	    Resource resource = resSet.getResource(URI.createURI(modelUri), true);
		
	    JavaIoFileSystemAccess fsa = new JavaIoFileSystemAccess();
	    fsa.setOutputPath(targetUri + "/mdbda-gen/");
	    
	    
	//DOFUQ?? http://www.eclipse.org/forums/index.php/t/628292/	
	    Guice.createInjector(new AbstractGenericModule() {
	    	
			public Class<? extends IEncodingProvider> bindIEncodingProvider() {
				return IEncodingProvider.Runtime.class;
			}
			
		}).injectMembers(fsa);
	    
	    gen.init();
		gen.doGenerate(resource, fsa);
	}
}
