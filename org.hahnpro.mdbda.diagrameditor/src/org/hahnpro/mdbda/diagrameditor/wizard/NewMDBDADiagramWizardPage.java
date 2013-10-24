package org.hahnpro.mdbda.diagrameditor.wizard;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class NewMDBDADiagramWizardPage extends WizardNewFileCreationPage  {

	public NewMDBDADiagramWizardPage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
		setTitle("MDBDA Diagram");
		setDescription("Create a new MDBDA Diagram");
		setFileExtension("diagram");
		
	}
	
	

}
 