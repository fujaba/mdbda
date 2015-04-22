package org.mdbda.diagrameditor.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.graphiti.examples.common.FileService;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.BoxRelativeAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramsFactory;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.graphiti.ui.internal.parts.ContainerShapeEditPart;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.mdbda.diagrameditor.Activator;
import org.mdbda.diagrameditor.diagram.MDBDADiagramTypeProvider;
import org.mdbda.diagrameditor.features.AddWorkflowFeature;
import org.mdbda.diagrameditor.features.CreateLinkFeature;
import org.mdbda.diagrameditor.wizard.NewMDBDADiagramWizard;
import org.mdbda.model.MDBDAModelRoot;
import org.mdbda.model.ModelFactory;
import org.mdbda.model.Workflow;

public class DiagramUtils {

	public static final String SELECT_DIAGRAM_TITEL = "Select Diagram";

	public static Collection<Diagram> getDiagrams(Diagram diagram) {
		return getDiagrams(diagram, ".*");
	}

	public static Collection<Diagram> getDiagrams(Diagram diagram,
			String fitlerDiagramTypeRegex) {
		Collection<Diagram> result = Collections.emptyList();
		Resource resource = diagram.eResource();
		URI uri = resource.getURI();
		URI uriTrimmed = uri.trimFragment();
		if (uriTrimmed.isPlatformResource()) {
			String platformString = uriTrimmed.toPlatformString(true);
			IResource fileResource = ResourcesPlugin.getWorkspace().getRoot()
					.findMember(platformString);
			if (fileResource != null) {
				IProject project = fileResource.getProject();
				result = DiagramUtils.getDiagrams(project,
						fitlerDiagramTypeRegex);
			}
		}
		return result;
	}

	public static Collection<Diagram> getDiagrams(IProject p) {
		return getDiagrams(p, ".*");
	}

	public static Collection<Diagram> getDiagrams(IProject p,
			String fitlerDiagramTypeRegex) {
		final List<IFile> files = getDiagramFiles(p);
		final List<Diagram> diagramList = new ArrayList<Diagram>();
		final ResourceSet rSet = new ResourceSetImpl();
		for (final IFile file : files) {
			final Diagram diagram = getDiagramFromFile(file, rSet);
			if (diagram != null) {
				if (diagram.getDiagramTypeId().matches(fitlerDiagramTypeRegex)) {
					diagramList.add(diagram);
				}
			}
		}
		return diagramList;
	}

	private static List<IFile> getDiagramFiles(IContainer folder) {
		final List<IFile> ret = new ArrayList<IFile>();
		try {
			final IResource[] members = folder.members();
			for (final IResource resource : members) {
				if (resource instanceof IContainer) {
					// ((IContainer) resource).
					IContainer con = (IContainer) resource;
					if (con.getName().startsWith("."))
						continue;
					if ("bin".equals(con.getName()))
						continue;
					ret.addAll(getDiagramFiles((IContainer) resource));
				} else if (resource instanceof IFile) {
					final IFile file = (IFile) resource;
					if (file.getName().endsWith(".diagram")) {
						ret.add(file);
					}
				}
			}
		} catch (final CoreException e) {
			e.printStackTrace();
		}
		return ret;
	}

	private static Diagram getDiagramFromFile(IFile file,
			ResourceSet resourceSet) {
		// Get the URI of the model file.
		final URI resourceURI = getFileURI(file, resourceSet);
		// Demand load the resource for this file.
		Resource resource;
		try {
			resource = resourceSet.getResource(resourceURI, true);
			if (resource != null) {
				resource.load(null);
				EcoreUtil.resolveAll(resourceSet);
				// does resource contain a diagram as root object?
				final EList<EObject> contents = resource.getContents();
				for (final EObject object : contents) {
					if (object instanceof Diagram) {
						return (Diagram) object;
					}
				}
			}
		} catch (final WrappedException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static URI getFileURI(IFile file, ResourceSet resourceSet) {
		final String pathName = file.getFullPath().toString();
		URI resourceURI = URI.createFileURI(pathName);
		resourceURI = resourceSet.getURIConverter().normalize(resourceURI);
		return resourceURI;
	}

	public static Diagram openDiagramDialog(Diagram parentDiagram) {
		Collection<Diagram> diagrams = DiagramUtils.getDiagrams(parentDiagram);

		ElementListSelectionDialog selectDiagramDialog = new ElementListSelectionDialog(
				new Shell(Display.getDefault()), new LabelProvider());
		HashSet<String> diagramList = new HashSet<String>();
		for (Diagram d : diagrams) {
			diagramList.add(d.getName());
		}

		selectDiagramDialog.setElements(diagramList
				.toArray(new String[diagramList.size()]));

		selectDiagramDialog.setTitle(SELECT_DIAGRAM_TITEL);
		selectDiagramDialog.setMultipleSelection(false);
		// user pressed cancel

		if (selectDiagramDialog.open() != Window.OK) {
			return null;
		}

		Object[] result = selectDiagramDialog.getResult();

		if (result.length != 1) {
			return null;
		}
		for (Diagram d : diagrams) {
			if (d.getName().equals(result[0])) {
				return d;
			}
		}

		return null;
	}

	public static Diagram newDiagramDialog() {
		// NewMDBDADiagramWizard wizard = new NewMDBDADiagramWizard();
		// WizardDialog wizardDialog = new WizardDialog(new
		// Shell(Display.getDefault()), wizard );
		DiagramEditor activeEditor = (DiagramEditor) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (activeEditor != null) {
			DiagramEditorInput input = (DiagramEditorInput) activeEditor
					.getEditorInput();

			// FileDialog fileDialog = new FileDialog(new
			// Shell(Display.getDefault()));
			// fileDialog.setText(WIZART_TITEL_NEW_MDBDA_DIAGRAM);
			// fileDialog.setFilterExtensions(new String[] {"*.diagram"});
			// fileDialog.setOverwrite(true);
			// fileDialog.open();
			//
			//
			//
			// String name = fileDialog.getFileName();

			IWizardDescriptor descriptor = PlatformUI.getWorkbench()
					.getNewWizardRegistry()
					.findWizard(NewMDBDADiagramWizard.id);

			try {
				NewMDBDADiagramWizard wizard = (NewMDBDADiagramWizard) descriptor
						.createWizard();

				IWorkbenchWindow window = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow();
				IStructuredSelection selection = (IStructuredSelection) window
						.getSelectionService().getSelection(
								"org.eclipse.jdt.ui.PackageExplorer");

				wizard.init(PlatformUI.getWorkbench(), selection);

				WizardDialog wd = new WizardDialog(Display.getDefault()
						.getActiveShell(), wizard);

				wd.open();

				String name = wizard.getFileName();
				if (name == null)
					return null;

				// Path path = new Path(input.getUri().toPlatformString(false));
				// activeEditor.getGraphicalViewer().
				// EObject model =
				// (EObject)activeEditor.getDiagramEditPart().getModel();
				IFile modelFile = WorkspaceSynchronizer
						.getFile(new ResourceImpl(input.getUri()));
				if (modelFile != null) {
					IProject project = modelFile.getProject();
					return newMDBDADiagram(project, name);
				}

			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;
	}

	public static final String MDBDA_DIAGRAM_TYPEID = "MDBDAEditor";
	public static final String DATA_TRANSFORMATION_DIAGRAM_TYPEID = "DataTransformationDiagram";

	// public static final String WIZART_TITEL_NEW_MDBDA_DIAGRAM =
	// "New MDBDA Diagram";

	public static Diagram newMDBDADiagram(IProject project, String name) {
		String editorExtension = "diagram";

		if (name.endsWith("." + editorExtension)) { // removes .diagram at the
													// end
			name = name.substring(0,
					name.length() - ("." + editorExtension).length());
		}

		if (project == null || !project.isAccessible()) {
			String error = "project == null || !project.isAccessible()";
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					error);
			ErrorDialog.openError(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					error, error,
					status);
			return null;
		}

		final Diagram diagram = Graphiti.getPeCreateService().createDiagram(
				MDBDA_DIAGRAM_TYPEID, name, true);

		IFolder diagramFolder = project.getFolder("src/diagrams/"); //$NON-NLS-1$


		final URI diagramURI = URI.createPlatformResourceURI(diagramFolder
				.getFile(name + ".diagram").getFullPath().toString(), true);
		final URI modelURI = URI.createPlatformResourceURI(diagramFolder
				.getFile(name + ".mdbdamodel").getFullPath().toString(), true);

		// final TransactionalEditingDomain editingDomain =
		// GraphitiUiInternal.getEmfService().createResourceSetAndEditingDomain();//TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();

		final MDBDAModelRoot mdbdaRootModel = ModelFactory.eINSTANCE
				.createMDBDAModelRoot();
		FileService.createEmfFileForDiagram(diagramURI, diagram);
		ResourceSet resourceSet = diagram.eResource().getResourceSet();

		Resource diagramResource = resourceSet.getResource(diagramURI, true);
		// editingDomain.getResourceSet().createResource(diagramURI);
		diagramResource.setTrackingModification(true);
		diagramResource.getContents().add(diagram);

		Resource modelResource = resourceSet.createResource(modelURI);
		modelResource.setTrackingModification(true);
		modelResource.getContents().add(mdbdaRootModel);

		diagram.setLink(PictogramsFactory.eINSTANCE.createPictogramLink());
		diagram.getLink().getBusinessObjects().add(mdbdaRootModel);

		Workflow wf = ModelFactory.eINSTANCE.createWorkflow();
		mdbdaRootModel.setRootWorkflow(wf);
		mdbdaRootModel.setName(name);
		mdbdaRootModel.setVersion(Calendar.getInstance().getTimeInMillis() + "");

		AddContext addContext = new AddContext();
		addContext.setNewObject(wf);
		addContext.setLocation(10, 10);
		addContext.setTargetContainer(diagram);

		MDBDADiagramTypeProvider typeProvider = new MDBDADiagramTypeProvider();
		typeProvider.resourceReloaded(diagram);

		AddWorkflowFeature addWFFeature = new AddWorkflowFeature(
				typeProvider.getFeatureProvider());
		addWFFeature.add(addContext);

		try {
			modelResource.save(Collections.<Resource, Map<?, ?>> emptyMap());
			diagramResource.save(Collections.<Resource, Map<?, ?>> emptyMap());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// URI modelURI = uri.trimFileExtension().appendFileExtension("model");

		String providerId = GraphitiUi.getExtensionManager()
				.getDiagramTypeProviderId(diagram.getDiagramTypeId());
		DiagramEditorInput editorInput = new DiagramEditorInput(
				EcoreUtil.getURI(diagram), providerId);

		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage()
					.openEditor(editorInput, DiagramEditor.DIAGRAM_EDITOR_ID);
		} catch (PartInitException e) {
			String error = e.getLocalizedMessage();
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					error, e);
			 ErrorDialog.openError(
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),error,
						error , status);
			return null;
		}

		return diagram;
	}

	public static Diagram newDatatransformationDiagram(Resource modelResource){
		modelResource.getResourceSet();
		
		return null;
	}
	
	public static MDBDAModelRoot getMDBDAModelRoot(Diagram refDiagram) {
		EList<EObject> businessObjects = refDiagram.getLink()
				.getBusinessObjects();

		for (EObject bo : businessObjects) {
			if (bo instanceof MDBDAModelRoot) {
				return (MDBDAModelRoot) bo;
			}
		}

		return null;
	}

	public static void createLink(ContainerShapeEditPart from,
			ContainerShapeEditPart to) {
		ContainerShape fromCS = (ContainerShape) from.getPictogramElement();
		ContainerShape toCS = (ContainerShape) to.getPictogramElement();
		BoxRelativeAnchor sourceAnchor = null;
		BoxRelativeAnchor targetAnchor = null;

		for (Anchor a : fromCS.getAnchors()) {
			if (a instanceof BoxRelativeAnchor) {
				BoxRelativeAnchor bra = (BoxRelativeAnchor) a;
				if (bra.isUseAnchorLocationAsConnectionEndpoint() == false) { // false
																				// =
																				// startpunkt
					sourceAnchor = bra;
					break;
				}
			}
		}

		for (Anchor a : toCS.getAnchors()) {
			if (a instanceof BoxRelativeAnchor) {
				BoxRelativeAnchor bra = (BoxRelativeAnchor) a;
				if (bra.isUseAnchorLocationAsConnectionEndpoint() == true) { // true
																				// =
																				// endpunkt
					targetAnchor = bra;
					break;
				}
			}
		}

		if (sourceAnchor != null && targetAnchor != null) {

			ICreateConnectionFeature[] createConnectionFeatures = from.getFeatureProvider().getCreateConnectionFeatures();

			CreateLinkFeature createLinkFeature = null;

			for (ICreateConnectionFeature feature : createConnectionFeatures) {
				if (feature instanceof CreateLinkFeature) {
					createLinkFeature = (CreateLinkFeature) feature;
				}
			}

			CreateConnectionContext createContext = new CreateConnectionContext();
			createContext.setSourcePictogramElement(fromCS);
			createContext.setTargetPictogramElement(toCS);
			createContext.setSourceAnchor(sourceAnchor);
			createContext.setTargetAnchor(targetAnchor);

			if (createLinkFeature.canStartConnection(createContext)
					&& createLinkFeature.canCreate(createContext)) {
				createLinkFeature.create(createContext);
			}
		}
	}
}
