package org.mdbda.diagrameditor.action;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mdbda.codegen.MDBDACodegenerator;
import org.mdbda.codegen.helper.CodeGenHelper;

public class GeneratorAction implements IObjectActionDelegate{
	private Shell shell;
	private IStructuredSelection selection;
	@Override
	public void run(IAction action) {
		
		IFile file = (IFile) selection.getFirstElement();
		
		CodeGenHelper.doGenerate(file.getLocationURI().toASCIIString() , file.getProject().getFullPath().toString());
		
		//CodeGenHelper.doGenerate
		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = (IStructuredSelection) selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

}
