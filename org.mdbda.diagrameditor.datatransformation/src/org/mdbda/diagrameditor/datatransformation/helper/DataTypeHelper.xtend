package org.mdbda.diagrameditor.datatransformation.helper

import org.mdbda.model.MDBDAModelRoot
import org.mdbda.model.ModelFactory
import org.mdbda.model.DataType

class DataTypeHelper {
	static def DataType getDefaultType(MDBDAModelRoot modelRoot){
		val types = modelRoot.getDataTypes();
		val name = "Object"
		val pName = "java.lang"
		if(types.empty){
			val defaultType = ModelFactory.eINSTANCE.createDataType
			defaultType.typeName = name
			defaultType.packageName = pName
			types.add(defaultType)
			
			
		}
		
		modelRoot.searchType(name,pName)
	}
	
	static def DataType searchType(MDBDAModelRoot modelRoot, String name, String packageName){
		for(t : modelRoot.getDataTypes()){
			if(t.typeName == name && t.packageName == packageName){
				return t
			}
		}
	}
}