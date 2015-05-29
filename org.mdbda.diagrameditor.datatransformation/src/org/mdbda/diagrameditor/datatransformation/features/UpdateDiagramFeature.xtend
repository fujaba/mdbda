package org.mdbda.diagrameditor.datatransformation.features

import org.eclipse.graphiti.features.IFeatureProvider
import org.eclipse.graphiti.features.context.IUpdateContext
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature
import org.eclipse.graphiti.mm.pictograms.Diagram
import org.mdbda.model.Task
import org.mdbda.model.DataObject
import org.eclipse.graphiti.features.IReason
import org.eclipse.graphiti.features.impl.Reason
import org.eclipse.graphiti.features.context.impl.AddContext

class UpdateDiagramFeature extends AbstractUpdateFeature{
	
	new(IFeatureProvider fp) {
		super(fp)
	}
	
	override canUpdate(IUpdateContext context) {
		if(context.pictogramElement instanceof Diagram){
			val bo = context.pictogramElement.businessObjectForPictogramElement
			if(bo instanceof Task){
				return true				
			}
		}
		return false
	}
	
	def boolean isDataObjectNotOnTheDiagram(Task task, DataObject dao){
		val allPictogramElementsForDataObject = featureProvider.getAllPictogramElementsForBusinessObject(dao)
		if (allPictogramElementsForDataObject.empty) {
			return true // there is NO graphical representation
		} else {
			var boolean graphicalRepresentationOnDiagram = false
			for (pe : allPictogramElementsForDataObject) {
				if (pe.eContainer instanceof Diagram) { // parent pe should be a diagram 
					val parentDiagram = pe.eContainer as Diagram
					val parentDiaBo = parentDiagram.businessObjectForPictogramElement
					if (parentDiaBo instanceof Task && parentDiaBo.equals(task)) {
						graphicalRepresentationOnDiagram = true;
					}
				}
			}
			if (!graphicalRepresentationOnDiagram) {
				return true // there are graphical representations but not on this diagram
			}
		}
		
		return false
	}
	
	override update(IUpdateContext context) {
		var somethingChanged = false;
		if(context.pictogramElement instanceof Diagram){
			val bo = context.pictogramElement.businessObjectForPictogramElement
			if(bo instanceof Task){
				val task = bo as Task
				
				var inputCount = 0
				var outputCount = 0
				
				
				for(dao : task.dataObjects){
					if(isDataObjectNotOnTheDiagram(task, dao)){
						var x = 42
						var y = 120
						
						if(dao.linkedInputResource != null){
							y = y * inputCount + 23
							inputCount++
						}else if(dao.linkedOutputResource != null){
							y = y * outputCount + 23
							outputCount++
							x+= 400
						}
						
						val addContext = new AddContext
						addContext.newObject = dao
						addContext.setLocation(x,y)
						addContext.targetContainer = context.pictogramElement as Diagram
						
						val addFeature = new AddDataObject(featureProvider)
						addFeature.add(addContext)
						
						somethingChanged = true
					}
				}
			}
		}
		return somethingChanged;
	}
	
	override updateNeeded(IUpdateContext context) {
		if(context.pictogramElement instanceof Diagram){
			val bo = context.pictogramElement.businessObjectForPictogramElement
			if(bo instanceof Task){
				val task = bo as Task
				
				for(dao : task.dataObjects){
					if(isDataObjectNotOnTheDiagram(task, dao)){
						return Reason.createTrueReason();
					}
				}	
			}
		}
	}
	
}