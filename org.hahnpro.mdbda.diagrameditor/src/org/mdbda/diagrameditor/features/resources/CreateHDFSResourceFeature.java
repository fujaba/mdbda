package org.mdbda.diagrameditor.features.resources;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.mdbda.model.ModelFactory;
import org.mdbda.model.Resource;
import org.mdbda.model.ResourcesTemplateConstatns;

public class CreateHDFSResourceFeature extends CreateResourceFeature {

	public CreateHDFSResourceFeature(IFeatureProvider fp) {
		super(fp, name, description);
	}
	public static String name = "HDFS Resource";
	public static String description = "Creates a new HDFS Resource";
    
    


	@Override
	public Object[] create(ICreateContext context) {		
		Resource eInst = ModelFactory.eINSTANCE.createResource();
		eInst.setTypeId(ResourcesTemplateConstatns.RESOURCETYPE_HDFS);
		addToTargetBO(context,eInst);
		
		addGraphicalRepresentation(context, eInst);
		return new Object[] { eInst };
	}
}