package org.mdbda.codegen.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import org.mdbda.runtime.annotations.Matcher;

public class PatternConfiguratorHelper {

	public static String getMatcherMethod(String configutarionClassName) throws ClassNotFoundException{
		
		Class clazz = Class.forName(configutarionClassName);
		
		for(Method m : clazz.getDeclaredMethods()){
			if(m.isAnnotationPresent(Matcher.class)){
				return m.getName();	
			}
   			if ( m.getAnnotation(Matcher.class) != null ){ //geht iwie nicht
				return m.getName();	
			}
		}
		
		return null;
	}
	
//	public static Class loadConfiguration(String classLocations){
//		ArrayList<URL> urls = new ArrayList<>();
//		
////		ClassLoader
////		
////		for(String u : URI){
////		
////		}
//		
//		URLClassLoader classLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]), PatternConfiguratorHelper.class.getClassLoader());
//		return null;
//	}
}
