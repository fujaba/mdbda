package org.mdbda.diagrameditor.features;

import java.util.Collection;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.AbstractDrillDownFeature;
import org.mdbda.diagrameditor.utils.DiagramUtils;
import org.mdbda.model.RemoteWorkflow;

public class DataTransformationDrillDownFeature extends AbstractDrillDownFeature {

	public DataTransformationDrillDownFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public String getName() {
		return "Open remote workflow diagram";
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			Object bo = getBusinessObjectForPictogramElement(pes[0]);
			if (bo instanceof RemoteWorkflow ) {
				RemoteWorkflow rwf = ((RemoteWorkflow)bo);
				if(rwf.getRemoteDiagramURI() != null && rwf.getRemoteDiagramURI() != ""){
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void execute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			RemoteWorkflow rwf = (RemoteWorkflow) getBusinessObjectForPictogramElement(pes[0]);

			Collection<Diagram> diagrams = DiagramUtils
					.getDiagrams(getDiagram(), DiagramUtils.MDBDA_DIAGRAM_TYPEID);

			for (Diagram dia : diagrams) {
				if (dia.eResource().getURI().toPlatformString(true)
						.equals(rwf.getRemoteDiagramURI())) {
					openDiagramEditor(dia);
				}
			}
		}
	}

	@Override
	protected Collection<Diagram> getDiagrams() {
		return DiagramUtils.getDiagrams(getDiagram(), DiagramUtils.MDBDA_DIAGRAM_TYPEID);
	}

}
