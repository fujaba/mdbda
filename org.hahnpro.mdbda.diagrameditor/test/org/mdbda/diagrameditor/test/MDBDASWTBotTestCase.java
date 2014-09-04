package org.mdbda.diagrameditor.test;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.impl.ContainerShapeImpl;
import org.eclipse.graphiti.ui.internal.parts.ContainerShapeEditPart;
import org.eclipse.graphiti.ui.internal.parts.DiagramEditPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swtbot.eclipse.finder.SWTBotEclipseTestCase;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.matchers.AbstractMatcher;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.ui.PlatformUI;
import org.hamcrest.Description;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mdbda.model.Workflow;

public abstract class MDBDASWTBotTestCase extends SWTBotEclipseTestCase {

	protected static final String PACKAGE_EXPLORER = "Package Explorer";
	protected static SWTGefBot bot = new SWTGefBot();
	static final String PROJECT_NAME = "junit Test Project";
	protected static final String DIAGRAM_NAME = "junitTestDiagram";
	protected static final String DIAGRAM_NAME2 = "junitTestDiagram2";
	protected final int _PATTERN_WIDTH_CONST = 110;
	static IProject project;
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		
		Thread.sleep(1000);
	
		// Force activation of current shell to avoid "no widget found"
		// exceptions
		// see http://www.eclipse.org/forums/index.php?t=msg&goto=484090&
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().forceActive();
			}
		});
		
		//PROJECT_NAME = PROJECT_NAME + " " + System.currentTimeMillis();
	
	
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		project = root.getProject(PROJECT_NAME);
		//project.create(null);
		//project.open(null);
		//TODO: was soll das hier?
		//IProjectDescription desc = project.getDescription();
		//desc.setNatureIds(new String[] { ExampleProjectNature.NATURE_ID });
		//project.setDescription(desc, null);
	
		
		bot.viewByTitle("Welcome").close();
		
		newMDBDAProject();
		
	}
	static SWTBotTree botSelectProject() {
		return botSelectProject(PROJECT_NAME);
	}
	static SWTBotTree getPackageExplorerTree() {
		SWTBotView packageExplorerView = bot.viewByTitle(PACKAGE_EXPLORER);
		packageExplorerView.show();
		Composite packageExplorerComposite = (Composite) packageExplorerView.getWidget();

		Tree swtTree = (Tree) bot.widget(widgetOfType(Tree.class) , packageExplorerComposite);
	
		return new SWTBotTree(swtTree);
	}
	static SWTBotTree botSelectProject(String projectName) {
		return getPackageExplorerTree().select(projectName);
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		botSelectProject();
	
		bot.menu("Edit").menu("Delete").click();
	
		// the project deletion confirmation dialog
		SWTBotShell shell = bot.shell("Delete Resources");
		shell.activate();
		bot.checkBox("Delete project contents on disk (cannot be undone)").select();
		bot.button("OK").click();
		bot.waitUntil(shellCloses(shell));
	
	}

	public static void newMDBDAProject() {
		bot.perspectiveByLabel("Graphiti").activate();
		
		bot.menu("File").menu("New").menu("Project...").click();
		
		SWTBotShell shell = bot.shell("New Project");
		shell.activate();
		//bot.tree().expandNode("Examples").expandNode("Graphiti").select("Graphiti Sample Project");
		//Luna update:
		bot.tree().expandNode("Other").select("Graphiti Example Project");
		
		bot.button("Next >").click();
		
		bot.textWithLabel("Project name:").setText(PROJECT_NAME);
		bot.button("Finish").click();
		
		try {
			bot.sleep(500);
			//wait(1000);//TODO: dynamisch pr�fen ob fertig
			project.open(null);
			//wait(1000);//TODO 
			assertTrue(project.isOpen());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
		
	}

	public MDBDASWTBotTestCase() {
		super();
	}

	@Before
	public void newDiagram() {
		SWTBotShell shell;
		botSelectProject();
		bot.menu("File").menu("New").menu("Other...").click();
		
		shell = bot.shell("New");
		shell.activate();
		bot.tree().expandNode("Other").select("New MDBDA Diagram");
		bot.button("Next >").click();
		
		//bot.textWithLabel("Enter or select the patent folder").setText(PROJECT_NAME);
		
		
		bot.textWithLabel("File name:").setText(DIAGRAM_NAME);
		bot.button("Finish").click();
	}

	@After
	public void closeDiagramAndDelete() {
		bot.gefEditor(DIAGRAM_NAME).close();//  saveAndClose();
		
		bot.viewByTitle(PACKAGE_EXPLORER);
		try {
			IFile file = project.getFile(DIAGRAM_NAME);
			//assertTrue(project.exists(file.getLocation()));
			file.delete(true, null);
			//assertFalse(project.exists(file.getLocation()));
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	protected SWTBotGefEditPart getRootWorkflowEditPart(SWTBotGefEditor editor) {
		List<SWTBotGefEditPart> editParts = editor.editParts(new AbstractMatcher<EditPart>(){
			@SuppressWarnings("restriction")
			@Override
			protected boolean doMatch(Object item) {
				// TODO Auto-generated method stub
				if(item instanceof ContainerShapeEditPart && ((ContainerShapeEditPart) item).getModel() instanceof ContainerShapeImpl){
					ContainerShapeImpl container = (ContainerShapeImpl) ((ContainerShapeEditPart) item).getModel();			
					if(container == null || container.getLink() == null ) return false;
					EList<EObject> businessObjects = container.getLink().getBusinessObjects();
					
					for(EObject obj : businessObjects){
						if(obj instanceof Workflow){
							Workflow w = (Workflow) obj;
							if(w.getWorkflow() != null){
								return false;//ist nicht root wf
							}		
							return true;
						}
					}
					
					return false;//hat kein bo vom typ Workflow
				}
				return false;
			}
	
			@Override
			public void describeTo(Description description) {
				// TODO Auto-generated method stub
				
			}
		});
		
		assertTrue(editParts.size() == 1);//es gibt ein root Workflow
		
		SWTBotGefEditPart rootWorkflowEditPart = editParts.get(0);
		return rootWorkflowEditPart;
	}

	@SuppressWarnings("restriction")
	DiagramEditPart getDiagramEditPart() {
		SWTBotGefEditor editor = bot.gefEditor(DIAGRAM_NAME);
		SWTBotGefEditPart rootEditPart = editor.rootEditPart();
		List children = rootEditPart.part().getChildren();
		for(Object p :  children){
			if(p instanceof DiagramEditPart){
				return (DiagramEditPart) p;
			}
		}
		
		return null;
	}

	protected Workflow getRootWorkflowFromDiagram() {
		Diagram diagram = (Diagram) getDiagramEditPart().getModel();
		if(diagram.getChildren().size() > 0){
			EList<EObject> businessObjects = diagram.getChildren().get(0).getLink().getBusinessObjects();
			for(EObject bo : businessObjects){
				if(bo instanceof Workflow){
					return (Workflow) bo;
				}
			}
			
		}
		
		return null;
	}

}