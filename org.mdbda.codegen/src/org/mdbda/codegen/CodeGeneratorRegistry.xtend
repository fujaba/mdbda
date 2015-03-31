package org.mdbda.codegen

import java.util.HashMap
import java.util.HashSet
import java.util.Set
class CodeGeneratorRegistry {
	
	private new() {
	}
	
	static var CodeGeneratorRegistry instance
	static def get(){
		if(instance == null){
			instance = new CodeGeneratorRegistry()
		}
	}
	
	
	val HashSet<String> codeStyles = new HashSet()
	val HashSet<String> knownTypes = new HashSet()
	val HashMap<String,String> typeToCodestyle = new HashMap

	val HashMap <String, HashMap<String,ITemplate>>  templateStore = new HashMap
	
	
	def registerCodeGenerator(String codeStyle, String typeId, ITemplate generator){
		codeStyles.add(codeStyle)
		knownTypes.add(typeId)
		typeToCodestyle.put(typeId,codeStyle)
		
		if(!templateStore.containsKey(typeId)){
			templateStore.put(typeId,new HashMap<String,ITemplate>)
		}
		templateStore.get(typeId).put(codeStyle,generator)
	}
	
	def boolean existGenerator(String codeStyle, String typeId){
		templateStore.containsKey(typeId) && templateStore.get(typeId).containsKey(codeStyle)
	}
	
	def getGenerator(String codeStyle, String typeId){
		if(templateStore.containsKey(typeId)){
			return templateStore.get(typeId).get(codeStyle)
		}
	}
	
	def getIncompatibilityList(String style, Set<String> typeIds){
		val HashSet<String> incompatibilityList = new HashSet 
		for(String typeId : typeIds){
			if(templateStore.containsKey(typeId)){
				if(!templateStore.get(typeId).containsKey(style)){					
					incompatibilityList.add(typeId)
				}
			}else{
				incompatibilityList.add(typeId)
			}
		}
		return incompatibilityList
	}
	
	def double getCompatibilityScore(String style, Set<String> typeIds){
		val incompatibleSize = getIncompatibilityList(style,typeIds).size
		if((typeIds.size - incompatibleSize ) == 0 ){
			return 0.0		
		}
		return typeIds.size / (typeIds.size - incompatibleSize )
	
	}
}