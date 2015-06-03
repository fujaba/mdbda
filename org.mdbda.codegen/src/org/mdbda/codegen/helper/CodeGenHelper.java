package org.mdbda.codegen.helper;

import java.util.HashSet;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;
import org.eclipse.xtext.parser.IEncodingProvider;
import org.eclipse.xtext.service.AbstractGenericModule;
import org.mdbda.codegen.CodegenContext;
import org.mdbda.codegen.MDBDACodegenerator;
import org.mdbda.model.ModelPackage;
import org.mdbda.model.Task;

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
	
	public static String beautifyJava(String code, int tab_deep){
		//TODO Remove extra tabs and spaces at each line beginning
		String[] lines = code.split("\n");
		
		int min_deep = 1000;
		//calc min tab deep
		for(String line : lines){
			if(line.replaceAll("\\s+","").length() > 0){//has context
				int line_deep = countPrefixWhitespaces(line);
				
				if(line_deep < min_deep){
					min_deep = line_deep;
				}
			}
		}
		 
		String newPrefix = "";
		
		for(int n = 0 ; n < tab_deep ; n++){
			newPrefix += "\t";
		}

		StringBuilder sb = new StringBuilder();
		//remove min_deep and add tab_deep
		for(String line : lines){
			line = line.replace('\r', ' ');
			if(line.replaceAll("\\s+","").length() > 0){//has context
				
				
				line = newPrefix + line.subSequence(min_deep, line.length());
			}
			

			sb.append(line).append('\n');
		}
		return sb.toString();
	}

	
	private static int countPrefixWhitespaces(String line) {
		int line_deep = 0;
		for(int i = 0 ; i < line.length(); i++){
			switch (line.charAt(i)) {
			case ' '://space
			case '\t'://tab
				line_deep++;
				break;

			default:
				return line_deep;
			}
		}
		return line_deep;
	}

	public static String getMapReduceControlledJobVarName(org.mdbda.model.Resource p){
		String name = getMapReduceClassNameFromPattern(p) + "ControlledJob";
		return name.substring(0,1).toLowerCase() + name.substring(1);		
	}
	
	public static String getMapperInnerClassName(org.mdbda.model.Resource p){
		return p.getName() + "Mapper";
	}
	
	public static String getMapperInnerClassName(org.mdbda.model.Resource r1,org.mdbda.model.Resource r2){
		return r1.getName() + "_" + r2.getName() + "Mapper";
	}
	
	public static String getIntermediateResourceName(Task from, Task to){
		return "intermRes" + from.getName() + "2" + to.getName();
	}
	public static String getIntermediateResourcePath(CodegenContext context, String intermediateResourceName){
		return "/temp/" + context.getModelRootName() + "/" + intermediateResourceName;
	}
	public static String getIntermediateResourcePath(CodegenContext context,Task from, Task to){
		return getIntermediateResourcePath(context, getIntermediateResourceName(from,to));
	}
	public static String getReducerInnerClassName(org.mdbda.model.Resource p){
		return p.getName() + "Reducer";
	}
	
	public static String getPartitonerInnerClassName(org.mdbda.model.Resource p){
		return p.getName() + "Partitoner";
	}
	
	public static String getMapReduceTestClassNameFromPattern(org.mdbda.model.Resource p){
		return getMapReduceClassNameFromPattern(p) + "Test";
	}
	
	public static String getMapReduceClassNameFromPattern(org.mdbda.model.Resource p){
		return fixClassName(p.getName()) + "MapReduce";
	}
	public static String getStormClassNameFromPattern(org.mdbda.model.Resource p){
		return fixClassName(p.getName()) + "Storm";		
	}
	
	public static void doGenerate(String modelUri, String targetUri, MDBDACodegenerator gen, String selectedCodeStyle){
		
		Resource resource = getResource(modelUri);
		
	    JavaIoFileSystemAccess fsa = new JavaIoFileSystemAccess();
	    fsa.setOutputPath(targetUri + "/mdbda-gen/");
	    
	    
	//DOFUQ?? http://www.eclipse.org/forums/index.php/t/628292/	
	    Guice.createInjector(new AbstractGenericModule() {
	    	
			public Class<? extends IEncodingProvider> bindIEncodingProvider() {
				return IEncodingProvider.Runtime.class;
			}
			
		}).injectMembers(fsa);

		gen.doGenerate(resource, fsa, selectedCodeStyle);
	}

	private static Resource getResource(String modelUri) {
		ModelPackage.eINSTANCE.eClass();//init
				
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		
	    Map m = reg.getExtensionToFactoryMap();
	    m.put("mdbdamodel", new XMIResourceFactoryImpl());

	    // Obtain a new resource set
	    ResourceSetImpl resSet = new ResourceSetImpl();

	    // Get the resource
	    Resource resource = resSet.getResource(URI.createURI(modelUri), true);
		return resource;
	}
	public static String fixInputString(String value) {
		if(value == null || value.equals("null")) return value;
		if(!value.startsWith("\"")){
			value = "\"" + value;
		}
		if(!value.endsWith("\"")){
			value = value + "\"";
		}
		
		return value;
	}
	public static String genWriterConstructorCall(String type, String value) {
	
		switch (type) {
		case "NullWritable":
			return "NullWritable.get()";
		case "Text":
			value = fixInputString(value);
			if(value.equals("null")) return "new Text(  )";
			return "new Text( " + value + " )";
		default:
			return "new " + type + "( " + value + " )";
		}
	}

	public static HashSet<String> getTypeIds(String modelUri) {
		Resource emfResource = getResource(modelUri);
		
		HashSet<String> typeIds = new HashSet<String>();
		
		TreeIterator<EObject> iter = emfResource.getAllContents();
		
		while (iter.hasNext()) {
			EObject el = iter.next();
			if(el instanceof org.mdbda.model.Resource){
				org.mdbda.model.Resource mdbdaResource = (org.mdbda.model.Resource) el;
				if(mdbdaResource.getTypeId() != null && mdbdaResource.getTypeId() != ""){
					typeIds.add(mdbdaResource.getTypeId());
				}
			}
		}
		
		return typeIds;
	}
}
