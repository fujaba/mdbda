package org.hahnpro.mdbda.diagrameditor.features;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.BoxRelativeAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.hahnpro.mdbda.diagrameditor.pictogramelements.AbstractMDBDAShape;
import org.hahnpro.mdbda.diagrameditor.pictogramelements.WorkflowShape;
import org.hahnpro.mdbda.diagrameditor.utils.DiagramUtils;
import org.hahnpro.mdbda.model.resources.Resource;
import org.hahnpro.mdbda.model.workflow.Workflow;

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
		} else if(context.getNewObject() instanceof Diagram && DiagramUtils.getMDBDADiagram((Diagram) context.getNewObject()).getWorkflow() != null ) {// workflow in workflow
				
				return true;
			
		}
		return false;
	}

	@Override
	public PictogramElement add(IAddContext context) {		
		return new WorkflowShape(context, this);
	}
}
