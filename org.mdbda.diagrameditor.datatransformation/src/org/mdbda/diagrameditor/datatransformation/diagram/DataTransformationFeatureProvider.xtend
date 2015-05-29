package org.mdbda.diagrameditor.datatransformation.diagram
import org.eclipse.graphiti.dt.IDiagramTypeProvider
import org.eclipse.graphiti.features.IAddFeature
import org.eclipse.graphiti.features.ICreateConnectionFeature
import org.eclipse.graphiti.features.ICreateFeature
import org.eclipse.graphiti.features.ILayoutFeature
import org.eclipse.graphiti.features.context.IAddConnectionContext
import org.eclipse.graphiti.features.context.IAddContext
import org.eclipse.graphiti.features.context.ILayoutContext
import org.eclipse.graphiti.mm.pictograms.ContainerShape
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider
import org.mdbda.diagrameditor.datatransformation.features.LayoutDomainObjectFeature
import org.mdbda.model.DataObject
import org.mdbda.diagrameditor.datatransformation.features.AddDataObject
import org.mdbda.diagrameditor.datatransformation.features.CreateDataObject
import org.eclipse.graphiti.features.context.IDirectEditingContext
import org.mdbda.diagrameditor.datatransformation.features.DataObjectTitleDirectEditingFeature
import org.eclipse.graphiti.features.context.IUpdateContext
import org.eclipse.graphiti.mm.pictograms.Diagram
import org.mdbda.diagrameditor.datatransformation.features.UpdateDiagramFeature
import org.mdbda.model.Task

class DataTransformationFeatureProvider extends DefaultFeatureProvider {
	 new(IDiagramTypeProvider dtp){
		super(dtp)
	}
	@Override override ICreateFeature[] getCreateFeatures(){
		return #[new CreateDataObject(this)] 
	}
	@Override override ICreateConnectionFeature[] getCreateConnectionFeatures(){
		return #[]//new CreateDomainObjectConnectionConnectionFeature(this)] 
	}
	@Override override IAddFeature getAddFeature(IAddContext context){
		switch(context){
			IAddConnectionContext:	switch(context.getNewObject()){
			
			} 
			IAddContext: 			switch(context.getNewObject()){
				DataObject: return new AddDataObject(this)
			}
		}
		return super.getAddFeature(context) 
	}
		
	override getUpdateFeature(IUpdateContext context) {
		
		val bo = getBusinessObjectForPictogramElement(context.pictogramElement)
		switch(context.pictogramElement){
			ContainerShape: switch(bo){
				Task: 
				return new UpdateDiagramFeature(this)
			}
		}
	}
	
	override getDirectEditingFeature(IDirectEditingContext context) {
		switch(getBusinessObjectForPictogramElement(context.getPictogramElement())){
			DataObject:
				return new DataObjectTitleDirectEditingFeature(this)	
		}

		super.getDirectEditingFeature(context)
	}
	
}