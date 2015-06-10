package org.mdbda.cassandra.property;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.AbstractPropertySectionFilter;
import org.eclipse.jface.viewers.IFilter;
import org.mdbda.cassandra.CreateCassandraResourceFeature;
import org.mdbda.model.Resource;

public class CassandraResourceFilter extends AbstractPropertySectionFilter  {

	@Override
	protected boolean accept(PictogramElement pictogramElement) {
		EObject eObj = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pictogramElement);
		if(eObj instanceof Resource) {
			Resource res = (Resource) eObj;			
			if(CreateCassandraResourceFeature.TYPE_ID.equals(res.getTypeId())) {
				return true;
			}
		}
		return false;
	}



}
