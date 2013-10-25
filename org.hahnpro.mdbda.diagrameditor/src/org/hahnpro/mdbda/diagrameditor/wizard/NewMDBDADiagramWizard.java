package org.hahnpro.mdbda.diagrameditor.wizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.examples.common.FileService;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.hahnpro.mdbda.diagrameditor.Activator;
import org.hahnpro.mdbda.diagrameditor.utils.DiagramUtils;

public class NewMDBDADiagramWizard extends Wizard implements INewWizard {

	private static final String PAGE_NAME_NEW_MDBDA_DIAGRAM_FILE = "New MDBDA Diagram File";

    protected IStructuredSelection selection;


	public NewMDBDADiagramWizard() {
		setWindowTitle(PAGE_NAME_NEW_MDBDA_DIAGRAM_FILE);
	}
	
	private Diagram diagram;

	@Override
	public void addPages() {	
		super.addPages();
		addPage(new NewMDBDADiagramWizardPage(PAGE_NAME_NEW_MDBDA_DIAGRAM_FILE, selection));
		
	}
	
	@Override
	public boolean performFinish() {
		NewMDBDADiagramWizardPage filePage = (NewMDBDADiagramWizardPage) getPage(PAGE_NAME_NEW_MDBDA_DIAGRAM_FILE);
		final String fileName = filePage.getFileName();
		Object firstElement = selection.getFirstElement();
		IProject project = null;
		if(firstElement instanceof IFile){
			IFile file = (IFile) firstElement;
			project = file.getProject();;
		}else if(firstElement instanceof IProject){
			project = (IProject) firstElement;
		}
		

		this.diagram =  DiagramUtils.newDiagram(project,fileName);
		return true;
	}

//	private IStructuredSelection getSelection() {
//		return this.selection;
//	}

//	@Override
//	public void init(IWorkbench workbench, IStructuredSelection selection) {		
//		this.selection = selection;
//		setWindowTitle(WIZART_TITEL_NEW_MDBDA_DIAGRAM);
//	}

	public Diagram getDiagram() {
		return diagram;
	}
	IWorkbench workbench;
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		this.workbench = workbench;
	}
	

}
