package org.mdbda.diagrameditor.features;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.mdbda.model.Resource;

public abstract class AbstractCreateMDBDAFeature extends AbstractCreateFeature {

	public AbstractCreateMDBDAFeature(IFeatureProvider fp, String name,
			String description) {
		super(fp, name, description);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	public abstract Object[] create(ICreateContext context);
	
	public abstract URL getDefaultConfigJSONFileLocation();

	public static long patternCounter = 0;
	
	public void initPattern(Resource eInst, String typeId){
		eInst.setTypeId(typeId);
		eInst.setConfigurationString(getDefaultConfigurationString());
		eInst.setName("Unnamed" + typeId + (++patternCounter));
	}

	public String getDefaultConfigurationString() {
		//Platform.get
		URL fileURL = getDefaultConfigJSONFileLocation();
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



}
