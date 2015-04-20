package org.mdbda.diagrameditor.datatransformation.features;

import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.mdbda.model.DataObject;
import org.mdbda.model.ModelFactory;
import org.mdbda.model.Task;

public class CreateDataObject extends AbstractCreateFeature implements
ICreateFeature {

	public CreateDataObject(IFeatureProvider fp) {
		super(fp, "Data Object", "Create new Data Object");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		return context.getTargetContainer() instanceof Diagram
				&& getBusinessObjectForPictogramElement(context.getTargetContainer()) instanceof Task;
	}

	@Override
	public Object[] create(ICreateContext context) {
		Task parentTask = (Task) getBusinessObjectForPictogramElement(context.getTargetContainer());
		DataObject dob = ModelFactory.eINSTANCE.createDataObject();
		parentTask.getDataObjects().add(dob);
		
		addGraphicalRepresentation(context, dob);
		
		return new Object[] { dob };
	}
	
	

}
