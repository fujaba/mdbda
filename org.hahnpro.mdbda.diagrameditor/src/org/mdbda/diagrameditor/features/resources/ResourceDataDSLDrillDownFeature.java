package org.mdbda.diagrameditor.features.resources;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.ui.CommonUIPlugin;
import org.eclipse.emf.common.ui.dialogs.WorkspaceResourceDialog;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.FileEditorInput;
import org.mdbda.diagrameditor.Activator;
import org.mdbda.diagrameditor.internal.OpenNotHiddenAndNotBinViewerFilter;
import org.mdbda.diagrameditor.utils.DiagramUtils;
import org.mdbda.model.RemoteWorkflow;
import org.mdbda.model.Resource;
import org.mdbda.model.Task;

public class ResourceDataDSLDrillDownFeature extends AbstractCustomFeature {

	public ResourceDataDSLDrillDownFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public String getName() {
		return "Open associated Data DSL";
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			Object bo = getBusinessObjectForPictogramElement(pes[0]);
			if (bo instanceof Resource && !(bo instanceof Task || bo instanceof RemoteWorkflow) ) {
				return true;				
			}
		}

		return false;
	}

	@Override
	public void execute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			Resource res = (Resource) getBusinessObjectForPictogramElement(pes[0]);
			
			if(res.getDataDSLDesciptionURI() == null || res.getDataDSLDesciptionURI() == ""){
				selectOrCreateDataDSL(res);
			}
			
			openDataDSLEditor(res);
			
		
		}
	}

	private void openDataDSLEditor(Resource res) {
		
		
		
		Path path = new Path(res.getDataDSLDesciptionURI());
		//res.eResource().getURI().
//		ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(res.eResource().getURI());
		
		
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);//  getFileForLocation(path) ;//getFile(path);
		
		
		IWorkbenchWindow window=PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		
			// Try opening the page in the editor.
			
				try {
					page.openEditor(new FileEditorInput(file), "de.wbg.DTDSL");//"uks.database.meta.Lang");
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		
	}

	public static String DataDSLSuffix = ".dtdsl";
	
	private void selectOrCreateDataDSL(Resource res) {
		Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		WorkspaceResourceDialog dialog = new WorkspaceResourceDialog(activeShell, new WorkbenchLabelProvider(), new WorkbenchContentProvider());
	    dialog.setAllowMultiple(false);
	    dialog.setTitle("Select or Create Data DSL");
	    dialog.setMessage( "Select existing or create a new MDBDA Data DSL file");
	    dialog.setShowNewFolderControl(true);
	    dialog.setShowFileControl(true);

	    dialog.addFilter(new OpenNotHiddenAndNotBinViewerFilter());
		
	    dialog.loadContents();
	    dialog.open();
	    IFile file = dialog.getFile();

	    if(file.exists() && file.getName().endsWith(DataDSLSuffix)){
	    	//existing
	    	
	    	
	    	res.setDataDSLDesciptionURI(file.getFullPath().toPortableString());//.getProjectRelativePath().toPortableString());
	    }else{
	    	//new
	    	if(!file.getName().endsWith(DataDSLSuffix)){
	    		if( file.getParent() instanceof IFolder){
	    			file = ((IFolder)file.getParent()).getFile(file.getName() + DataDSLSuffix );
	    		}else if( file.getParent() instanceof IProject){
	    			file = ((IProject)file.getParent()).getFile(file.getName() + DataDSLSuffix);
	    		}else{
	    			return;
	    		}
	    		if(file.exists()){
	    			res.setDataDSLDesciptionURI(file.getProjectRelativePath().toPortableString());
	    		}
	    	}
	    	InputStream is = new ByteArrayInputStream("//empty MDBDA Data DSL".getBytes());
	    	try {
				file.create(is, false, null);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
}
