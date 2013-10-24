package org.hahnpro.mdbda.diagrameditor.features;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.AbstractDrillDownFeature;
import org.hahnpro.mdbda.diagrameditor.utils.DiagramUtils;
import org.hahnpro.mdbda.model.workflow.Workflow;

public class WorkflowDrillDownFeature extends AbstractDrillDownFeature {

	public WorkflowDrillDownFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public boolean canExecute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
        // first check, if one EClass is selected
        if (pes != null && pes.length == 1) {
            Object bo = getBusinessObjectForPictogramElement(pes[0]);
            if (bo instanceof Workflow) {
                // then forward to super-implementation, which checks if
                // this EClass is associated with other diagrams
                return super.canExecute(context);
            }
        }
        return false;
	}

	@Override
	protected Collection<Diagram> getDiagrams() {
       return DiagramUtils.getDiagrams(getDiagram());
	}

}
