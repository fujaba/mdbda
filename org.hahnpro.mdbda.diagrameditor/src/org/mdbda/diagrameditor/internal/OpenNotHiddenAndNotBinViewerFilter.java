package org.mdbda.diagrameditor.internal;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class OpenNotHiddenAndNotBinViewerFilter extends ViewerFilter {

	@Override
	public boolean select(Viewer viewer, Object parentElement,
			Object element) {
		IResource resource = null;
		if (element instanceof IResource) {
			resource = (IResource) element;
		} else if (element instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) element;
			resource = (IResource) adaptable.getAdapter(IResource.class);
		}
		
		if(resource != null){
			if(resource.getName().startsWith(".") || resource.isHidden()) return false;
			
			if(resource instanceof IProject){
				IProject pro = (IProject) resource;
				return pro.isOpen();
			}else if(resource instanceof IFolder){
				IFolder folder = (IFolder) resource;
				if("bin".equals(folder.getName()) && parentElement instanceof IProject) return false;
			}
			
			
			return true;
		}
		return false;
	}

}
