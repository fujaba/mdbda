package org.mdbda.codegen;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;
import org.eclipse.xtext.parser.IEncodingProvider;
import org.eclipse.xtext.service.AbstractGenericModule;
import org.junit.Test;
import org.mdbda.codegen.helper.CodeGenHelper;
import org.mdbda.model.ModelPackage;

import com.google.inject.Guice;

public class GeneratorTest {

//	@Test
	public void test() {
		MDBDACodegenerator gen = new MDBDACodegenerator();
		
		ModelPackage.eINSTANCE.eClass();//init
				
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
	    Map<String, Object> m = reg.getExtensionToFactoryMap();
	    m.put("mdbdamodel", new XMIResourceFactoryImpl());

	    // Obtain a new resource set
	    ResourceSet resSet = new ResourceSetImpl();

	    // Get the resource
	    Resource resource = resSet.getResource(URI
	        .createURI("testFiles/Wordcount.mdbdamodel"), true);
		
	    JavaIoFileSystemAccess fsa = new JavaIoFileSystemAccess();
	    fsa.setOutputPath("./test-gen-output/");
	    
	    
	//DOFUQ?? http://www.eclipse.org/forums/index.php/t/628292/	
	    Guice.createInjector(new AbstractGenericModule() {
	    	
			public Class<? extends IEncodingProvider> bindIEncodingProvider() {
				return IEncodingProvider.Runtime.class;
			}
			
		}).injectMembers(fsa);
	    
	    gen.init();
		gen.doGenerate(resource, fsa);
	}
	
	@Test
	public void testGeberateWordCound(){
		CodeGenHelper.doGenerate("testFiles/Wordcount.mdbdamodel", "./");
	}

}
