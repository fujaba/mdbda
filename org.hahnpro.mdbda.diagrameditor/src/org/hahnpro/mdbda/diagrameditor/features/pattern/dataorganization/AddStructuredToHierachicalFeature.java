package org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.hahnpro.mdbda.diagrameditor.features.pattern.AddPatternFeature;
import org.hahnpro.mdbda.model.workflow.Workflow;


public class AddStructuredToHierachicalFeature extends AddPatternFeature implements
		IAddFeature {

	public AddStructuredToHierachicalFeature(IFeatureProvider fp) {
		super(fp);
		this.typeName = "Structured to Hierachical";
	}


}
