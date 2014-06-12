package org.mdbda.diagrameditor.features.pattern;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.mdbda.model.Pattern;
import org.mdbda.model.Workflow;
import org.mdbda.diagrameditor.features.AbstactMDBDAAddFeature;
import org.mdbda.diagrameditor.features.AbstractCreateMDBDAFeature;
import org.osgi.framework.Bundle;

public abstract class CreatePatternFeature extends AbstractCreateMDBDAFeature {


	public CreatePatternFeature(IFeatureProvider fp, String name,
			String description) {
		super(fp, name, description);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canCreate(ICreateContext context) {	
		return getWorkflow(context) != null;		
	}

	protected Workflow getWorkflow(ICreateContext context) {
		Object bo =  getBusinessObjectForPictogramElement(context.getTargetContainer());
		
		if(bo instanceof Workflow) return (Workflow) bo;
		return null;
		
	}
	
	public String getDefaultConfigurationString(){
		Bundle bundle = Platform.getBundle("org.mdbda.diagrameditor");
		URL fileURL = bundle.getEntry(getDefaultConfigJSONFileLocation());
		try {
		    byte[] allBytes = Files.readAllBytes( Paths.get( FileLocator.resolve(fileURL).toURI() ) );
		    return new String(allBytes);//TODO Charset??
		} catch (URISyntaxException e1) {
		    e1.printStackTrace();
		} catch (IOException e1) {
		    e1.printStackTrace();
		}
		return "";
	}
	/*{
	}
		StringBuilder configString = new StringBuilder();
		configString.append("{\n");
		configString.append("	\"mapFunction\":{\n");
		configString.append("		\"testInput\":[], \n");
		configString.append("		\"testOutput\":[],\n");
		configString.append("		\"KEYIN\":\"LongWritable\", \n");
		configString.append("		\"VALUEIN\":\"Text\", \n");
		configString.append("		\"KEYOUT\":\"Text\",\n");
		configString.append("		\"VALUEOUT\":\"IntWritable\", \n");
		configString.append("		\"function\":"
				+ getDefaultMapFunctionBody() + " \n");
		configString.append("		\n");
		configString.append("	},\n");
		configString.append("	\"reduceFunction\":{		\n");
		configString.append("		\"testInput\":[], \n");
		configString.append("		\"testOutput\":[],\n");
		configString.append("		\"KEYIN\":\"Text\", \n");
		configString.append("		\"VALUEIN\":\"IntWritable\",\n");
		configString.append("		\"KEYOUT\":\"Text\",\n");
		configString.append("		\"VALUEOUT\":\"IntWritable\", \n");
		configString.append("		\"function\":\""
				+ getDefaultReduceFunctionBody() +  " \"\n");
		configString.append("	} \n");
		configString.append("}\n");
		return null;
	}*/
	
	public abstract String getDefaultConfigJSONFileLocation();
	static long patternCounter = 0;
	public void initPattern(Pattern eInst, String typeId){
		eInst.setTypeId(typeId);
		eInst.setConfigurationString(getDefaultConfigurationString());
		eInst.setName("Unnamed" + typeId + (++patternCounter));
	}
	
	protected void addToWorkflow(Workflow wf, Pattern eInst) {
		//wf.eResource().getContents().add(eInst);
		eInst.setWorkflow(wf);
	}
}
