package org.hahnpro.mdbda.diagrameditor.features;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.util.IColorConstant;
import org.hahnpro.mdbda.diagrameditor.pictogramelements.WorkflowShape;
import org.hahnpro.mdbda.diagrameditor.utils.DiagramUtils;
import org.hahnpro.mdbda.model.Workflow;

public class AddWorkflowFeature extends AbstactMDBDAAddFeature implements
		IAddFeature {

	public AddWorkflowFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public Diagram getDiagram() {
		return super.getDiagram();
	}

	@Override
	public void link(PictogramElement pe, Object businessObject) {
		super.link(pe, businessObject);
	}

	@Override
	public Color manageColor(IColorConstant colorConstant) {
		return super.manageColor(colorConstant);
	}
	
	@Override
	public boolean canAdd(IAddContext context) {
		if (context.getNewObject() instanceof Workflow && context.getTargetContainer() instanceof Diagram) {//root wf
		
				return true;
		} else if(context.getNewObject() instanceof Diagram && DiagramUtils.getMDBDADiagram((Diagram) context.getNewObject()).getRootWorkflow() != null ) {// workflow in workflow
				
				return true;
			
		}
		return false;
	}

	@Override
	public PictogramElement add(IAddContext context) {		
		return WorkflowShape.getWorkflowShape(context, this);
	}
}
