package org.mdbda.diagrameditor.property;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.AbstractPropertySectionFilter;
import org.mdbda.model.Resource;

public class ResourceFilter extends AbstractPropertySectionFilter {

	@Override
	protected boolean accept(PictogramElement pictogramElement) {
		EObject eObject = Graphiti.getLinkService()
				.getBusinessObjectForLinkedPictogramElement(pictogramElement);
		if (eObject instanceof Resource) {
			return true;
		}
		return false;
	}

}
