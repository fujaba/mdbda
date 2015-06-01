package org.mdbda.codegen.action;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mdbda.codegen.MDBDACodegenerator;
import org.mdbda.codegen.dialog.CodegenDialog;
import org.mdbda.codegen.dialog.CodegenDialog.CodegenDialogResult;
import org.mdbda.codegen.helper.CodeGenHelper;

public class GeneratorAction implements IObjectActionDelegate{
	private Shell shell;
	private IStructuredSelection selection;
	@Override
	public void run(IAction action) {
		MDBDACodegenerator gen = new MDBDACodegenerator();
	    gen.init();
		IFile file = (IFile) selection.getFirstElement();
		
		CodegenDialog genDia = new CodegenDialog(shell, SWT.TITLE | SWT.BORDER | SWT.CLOSE);
		
		
		CodegenDialogResult result = genDia.open(CodeGenHelper.getTypeIds(file.getLocationURI().toASCIIString()));
		
		if(result == null){//canceled
			return;
		}
		
		String selectedCodeStyle = result.getCodeStyle();
		
		CodeGenHelper.doGenerate(file.getLocationURI().toASCIIString(), file.getProject().getLocation().toOSString(), gen ,selectedCodeStyle);
	

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
