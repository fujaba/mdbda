package org.mdbda.diagrameditor.test;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses;
import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.graphiti.examples.common.FileService;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.impl.ContainerShapeImpl;
import org.eclipse.graphiti.ui.internal.parts.ContainerShapeEditPart;
import org.eclipse.graphiti.ui.internal.parts.DiagramEditPart;
import org.eclipse.graphiti.ui.internal.services.GraphitiUiInternal;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swtbot.eclipse.finder.SWTBotEclipseTestCase;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.matchers.AbstractMatcher;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredList;
import org.mdbda.model.MDBDADiagram;
import org.mdbda.model.ModelFactory;
import org.mdbda.model.Workflow;
import org.hamcrest.Description;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mdbda.diagrameditor.features.CreateWorkflowFeature;
import org.mdbda.diagrameditor.features.pattern.dataorganization.CreateBinningFeature;
import org.mdbda.diagrameditor.features.pattern.dataorganization.CreatePartitoningFeature;
import org.mdbda.diagrameditor.features.pattern.dataorganization.CreateShufflingFeature;
import org.mdbda.diagrameditor.features.pattern.dataorganization.CreateStructuredToHierachicalFeature;
import org.mdbda.diagrameditor.features.pattern.dataorganization.CreateTotalOrderSortingFeature;
import org.mdbda.diagrameditor.features.pattern.filtering.CreateDistinctFeature;
import org.mdbda.diagrameditor.features.pattern.filtering.CreateSimpleMatcherFeature;
import org.mdbda.diagrameditor.features.pattern.filtering.CreateTopTenFeature;
import org.mdbda.diagrameditor.features.pattern.join.CreateCartesianProductFeature;
import org.mdbda.diagrameditor.features.pattern.join.CreateReduceSideJoinFeature;
import org.mdbda.diagrameditor.features.pattern.join.CreateReplicatedJoinFeature;
import org.mdbda.diagrameditor.features.pattern.summatization.CreateCustomCalculationFeature;
import org.mdbda.diagrameditor.features.pattern.summatization.CreateNumericalSummarizationFeature;
import org.mdbda.diagrameditor.features.resources.CreateCassandraResourceFeature;
import org.mdbda.diagrameditor.features.resources.CreateGenericResourceFeature;
import org.mdbda.diagrameditor.features.resources.CreateHDFSResourceFeature;
import org.mdbda.diagrameditor.features.resources.CreateHazelcastResourceFeature;
import org.mdbda.diagrameditor.features.resources.CreateNeo4jResourceFeature;
import org.mdbda.diagrameditor.utils.DiagramUtils;
import org.mdbda.diagrameditor.wizard.NewMDBDADiagramWizard;


@RunWith(SWTBotJunit4ClassRunner.class)
public class MDBDADiagrameditorTest extends SWTBotEclipseTestCase{
	private static final String PACKAGE_EXPLORER = "Package Explorer";
	static SWTGefBot	bot	= new SWTGefBot();
	final static String PROJECT_NAME = "junit Test Project";
	final static String DIAGRAM_NAME = "junitTestDiagram";

	final static String DIAGRAM_NAME2 = "junitTestDiagram2";

	final int _PATTERN_WIDTH_CONST = 110;
	
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
	
	static void botSelectProject(){
		SWTBotView packageExplorerView = bot.viewByTitle(PACKAGE_EXPLORER);
		packageExplorerView.show();
		Composite packageExplorerComposite = (Composite) packageExplorerView.getWidget();
		
		
		
		Tree swtTree = (Tree) bot.widget(widgetOfType(Tree.class) , packageExplorerComposite);

		SWTBotTree tree = new SWTBotTree(swtTree);

		tree.select(PROJECT_NAME);
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
	
	
	public static void newMDBDAProject(){
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

	
	@Test
	public void testWorkflowInWorkflow(){

		SWTBotGefEditor editor = bot.gefEditor(DIAGRAM_NAME);
		
		/*assertNull(getRootWorkflowFromDiagram());//is empty
		editor.activateTool(CreateWorkflowFeature.name);
		editor.drag(50, 50, 100, 100);
		*/

		
		SWTBotGefEditPart rootWorkflowEditPart = getRootWorkflowEditPart(editor);
		//bot.sleep(500);
		//rootWorkflowEditPart.
		
		assertTrue(rootWorkflowEditPart.children().size() == 0);
		

		//new workflow		
		editor.activateTool(CreateWorkflowFeature.name);
		editor.drag(20, 60 , 40, 80);
		
		SWTBotShell shell = bot.shell(CreateWorkflowFeature.DIALOG_TITLE);		
		shell.activate();		
		bot.button("new").click();
		
		shell = bot.shell(NewMDBDADiagramWizard.PAGE_NAME_NEW_MDBDA_DIAGRAM_FILE);		
		shell.activate();	
		
		bot.textWithLabel("File name:").setText(DIAGRAM_NAME2);
		bot.button("Finish").click();
		
		assertTrue(rootWorkflowEditPart.children().size() == 1);
		
		

		//open workflow
		editor.activateTool(CreateWorkflowFeature.name);
		editor.drag(20 + _PATTERN_WIDTH_CONST, 60 , 40 + _PATTERN_WIDTH_CONST, 80);
		
		shell = bot.shell(CreateWorkflowFeature.DIALOG_TITLE);		
		shell.activate();		
		bot.button("open").click();
		
		shell = bot.shell(DiagramUtils.SELECT_DIAGRAM_TITEL);		
		shell.activate();	
		
		//bot.list().select(DIAGRAM_NAME2);
		bot.table().select(DIAGRAM_NAME2);
		bot.button("OK").click();
		
		assertTrue(rootWorkflowEditPart.children().size() == 2);
		
		
		
		assertNotNull(getRootWorkflowFromDiagram());//is not empty
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
	
	@Test
	public void testAddSummationPattern(){
		//assertNull(getRootWorkflowFromDiagram());//is empty
		SWTBotGefEditor editor = bot.gefEditor(DIAGRAM_NAME);
		//editor.activateTool(CreateWorkflowFeature.name);
		//editor.drag(50, 50, 100, 100);
		
		SWTBotGefEditPart rootWorkflowEditPart = getRootWorkflowEditPart(editor);
		//bot.sleep(500);
		//rootWorkflowEditPart.
		
		assertTrue(rootWorkflowEditPart.children().size() == 0);
		
		editor.activateTool(CreateNumericalSummarizationFeature.name);
		editor.drag(20, 100 , 40, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 1);
		
		
		editor.activateTool(CreateCustomCalculationFeature.name);
		editor.drag(20 + _PATTERN_WIDTH_CONST, 100 , 40 + _PATTERN_WIDTH_CONST, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 2);
		
		assertNotNull(getRootWorkflowFromDiagram());//is not empty
	}
	
	@Test
	public void testCreateLink() throws InterruptedException{
		//assertNull(getRootWorkflowFromDiagram());//is empty
		final SWTBotGefEditor editor = bot.gefEditor(DIAGRAM_NAME);
		//editor.activateTool(CreateWorkflowFeature.name);
		//editor.drag(50, 50, 100, 100);
		
		SWTBotGefEditPart rootWorkflowEditPart = getRootWorkflowEditPart(editor);
		//bot.sleep(500);
		//rootWorkflowEditPart.
		
		assertTrue(rootWorkflowEditPart.children().size() == 0);
		
		editor.activateTool(CreateNumericalSummarizationFeature.name);
		editor.drag(20, 100 , 100, 150);		
		assertTrue(rootWorkflowEditPart.children().size() == 1);
		final SWTBotGefEditPart numericalSummarizationEditPart = rootWorkflowEditPart.children().get(0);
		
		editor.activateTool(CreateCustomCalculationFeature.name);
		editor.drag(_PATTERN_WIDTH_CONST * 4, 100 , 100 + _PATTERN_WIDTH_CONST * 4 , 150);		
		assertTrue(rootWorkflowEditPart.children().size() == 2);

		final SWTBotGefEditPart customCalculationEditPart = rootWorkflowEditPart.children().get(1);
		
//		final TransactionalEditingDomain editingDomain = GraphitiUiInternal.getEmfService().createResourceSetAndEditingDomain();//TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();
//				editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
//					@Override
//					protected void doExecute() {
		//final TransactionalEditingDomain editingDomain = GraphitiUiInternal.getEmfService()
		//		.createResourceSetAndEditingDomain();
		
//		editor.getReference().
//		final TransactionalEditingDomain editingDomain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();
		
		
		Object bo = ((ContainerShapeEditPart)numericalSummarizationEditPart.part()).getFeatureProvider().getBusinessObjectForPictogramElement(((ContainerShapeEditPart)numericalSummarizationEditPart.part()).getPictogramElement());
		
		final TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(bo);
		
		final CommandStack commandStack = editingDomain.getCommandStack();

		commandStack.execute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				DiagramUtils.createLink((ContainerShapeEditPart)numericalSummarizationEditPart.part(),
										(ContainerShapeEditPart)customCalculationEditPart.part());
			}
		});
		
		//editingDomain.dispose();
				//					}
//				});
				
		assertNotNull(getRootWorkflowFromDiagram());//is not empty
	}

	@Test
	public void testAddJoinPattern(){
		//assertNull(getRootWorkflowFromDiagram());//is empty
		SWTBotGefEditor editor = bot.gefEditor(DIAGRAM_NAME);
		//editor.activateTool(CreateWorkflowFeature.name);
		//editor.drag(50, 50, 100, 100);
		
		SWTBotGefEditPart rootWorkflowEditPart = getRootWorkflowEditPart(editor);
		//bot.sleep(500);
		//rootWorkflowEditPart.
		
		assertTrue(rootWorkflowEditPart.children().size() == 0);
		
		editor.activateTool(CreateCartesianProductFeature.name);
		editor.drag(20, 100 , 40, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 1);
		

		editor.activateTool(CreateReplicatedJoinFeature.name);
		editor.drag(20 + _PATTERN_WIDTH_CONST, 100 , 40 + _PATTERN_WIDTH_CONST, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 2);
		
		editor.activateTool(CreateReduceSideJoinFeature.name);
		editor.drag(20 + _PATTERN_WIDTH_CONST*2, 100 , 40 + _PATTERN_WIDTH_CONST*2, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 3);
		
		
		
		assertNotNull(getRootWorkflowFromDiagram());//is not empty
	}

	@Test
	public void testAddFilteringPattern(){
		//assertNull(getRootWorkflowFromDiagram());//is empty
		SWTBotGefEditor editor = bot.gefEditor(DIAGRAM_NAME);
		//editor.activateTool(CreateWorkflowFeature.name);
		//editor.drag(50, 50, 100, 100);
		
		SWTBotGefEditPart rootWorkflowEditPart = getRootWorkflowEditPart(editor);
		//bot.sleep(500);
		//rootWorkflowEditPart.
		
		assertTrue(rootWorkflowEditPart.children().size() == 0);
		
		editor.activateTool(CreateTopTenFeature.name);
		editor.drag(20, 100 , 40, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 1);
		
		
		editor.activateTool(CreateDistinctFeature.name);
		editor.drag(20 + _PATTERN_WIDTH_CONST, 100 , 40 + _PATTERN_WIDTH_CONST, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 2);
		
		editor.activateTool(CreateSimpleMatcherFeature.name);
		editor.drag(20 + _PATTERN_WIDTH_CONST*2, 100 , 40 + _PATTERN_WIDTH_CONST*2, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 3);
		
		
		assertNotNull(getRootWorkflowFromDiagram());//is not empty
	}
	
	@Test
	public void testAddDataorganizationPattern(){
		//assertNull(getRootWorkflowFromDiagram());//is empty
		SWTBotGefEditor editor = bot.gefEditor(DIAGRAM_NAME);
		//editor.activateTool(CreateWorkflowFeature.name);
		//editor.drag(50, 50, 100, 100);
		
		SWTBotGefEditPart rootWorkflowEditPart = getRootWorkflowEditPart(editor);
		//bot.sleep(500);
		//rootWorkflowEditPart.
		
		assertTrue(rootWorkflowEditPart.children().size() == 0);
		
		editor.activateTool(CreateBinningFeature.name);
		editor.drag(20, 100 , 40, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 1);

		
		editor.activateTool(CreatePartitoningFeature.name);
		editor.drag(20 + _PATTERN_WIDTH_CONST, 100 , 40 + _PATTERN_WIDTH_CONST, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 2);
		
		editor.activateTool(CreateShufflingFeature.name);
		editor.drag(20 + _PATTERN_WIDTH_CONST*2, 100 , 40 + _PATTERN_WIDTH_CONST*2, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 3);
		
		editor.activateTool(CreateStructuredToHierachicalFeature.name);
		editor.drag(20 + _PATTERN_WIDTH_CONST*3, 100 , 40 + _PATTERN_WIDTH_CONST*3, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 4);
		
		editor.activateTool(CreateTotalOrderSortingFeature.name);
		editor.drag(20 + _PATTERN_WIDTH_CONST*4, 100 , 40 + _PATTERN_WIDTH_CONST*4, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 5);
		
		
		assertNotNull(getRootWorkflowFromDiagram());//is not empty
	}
	
	@Test
	public void testAddRessourcenPattern(){
		//assertNull(getRootWorkflowFromDiagram());//is empty
		SWTBotGefEditor editor = bot.gefEditor(DIAGRAM_NAME);
		//editor.activateTool(CreateWorkflowFeature.name);
		//editor.drag(50, 50, 100, 100);
		
		SWTBotGefEditPart rootWorkflowEditPart = getRootWorkflowEditPart(editor);
		//bot.sleep(500);
		//rootWorkflowEditPart.
		
		assertTrue(rootWorkflowEditPart.children().size() == 0);
		
		editor.activateTool(CreateCassandraResourceFeature.name);
		editor.drag(20, 100 , 40, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 1);

		
		editor.activateTool(CreateHDFSResourceFeature.name);
		editor.drag(20 + _PATTERN_WIDTH_CONST, 100 , 40 + _PATTERN_WIDTH_CONST, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 2);
		
		editor.activateTool(CreateNeo4jResourceFeature.name);
		editor.drag(20 + _PATTERN_WIDTH_CONST*2, 100 , 40 + _PATTERN_WIDTH_CONST*2, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 3);
		
		editor.activateTool(CreateHazelcastResourceFeature.name);
		editor.drag(20 + _PATTERN_WIDTH_CONST*3, 100 , 40 + _PATTERN_WIDTH_CONST*3, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 4);
		
		editor.activateTool(CreateGenericResourceFeature.name);
		editor.drag(20 + _PATTERN_WIDTH_CONST*4, 100 , 40 + _PATTERN_WIDTH_CONST*4, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 5);
		
		
		assertNotNull(getRootWorkflowFromDiagram());//is not empty
	}
	
	
	@SuppressWarnings("restriction")
	DiagramEditPart getDiagramEditPart(){
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
	
	Workflow getRootWorkflowFromDiagram(){
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
