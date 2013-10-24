package org.hahnpro.mdbda.diagrameditor.test;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.graphiti.mm.Property;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.mm.pictograms.impl.ContainerShapeImpl;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.internal.parts.ContainerShapeEditPart;
import org.eclipse.graphiti.ui.internal.parts.DiagramEditPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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
import org.hahnpro.mdbda.diagrameditor.features.CreateWorkflowFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization.CreateBinningFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization.CreatePartitoningFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization.CreateShufflingFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization.CreateStructuredToHierachicalFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization.CreateTotalOrderSortingFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.filtering.CreateBloomFilteringFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.filtering.CreateDistinctFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.filtering.CreateTopTenFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.join.CreateCartesianProductFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.join.CreateCompositeJoinFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.join.CreateReduceSideJoinFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.join.CreateReplicatedJoinFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.summatization.CreateCountingWithCountersFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.summatization.CreateInvertedIndexSummarizationFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.summatization.CreateNumericalSummarizationFeature;
import org.hahnpro.mdbda.model.workflow.Workflow;
import org.hamcrest.Description;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses;



@RunWith(SWTBotJunit4ClassRunner.class)
public class MDBDADiagrameditorTest extends SWTBotEclipseTestCase{
	private static final String PACKAGE_EXPLORER = "Package Explorer";
	static SWTGefBot	bot	= new SWTGefBot();
	final static String PROJECT_NAME = "junit Test Project";
	final static String DIAGRAM_NAME = "junitTestDiagram";
	
	static IProject project;
	
	@BeforeClass
	public static void beforeClass() throws Exception {

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
		bot.tree().expandNode("Examples").expandNode("Graphiti").select("Graphiti Sample Project");
		bot.button("Next >").click();
		
		bot.textWithLabel("Project name:").setText(PROJECT_NAME);
		bot.button("Finish").click();
		
		try {
			bot.sleep(500);
			//wait(1000);//TODO: dynamisch prüfen ob fertig
			project.open(null);
			//wait(1000);//TODO 
			assertTrue(project.isOpen());
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
		
	}

	protected void newDiagram() {
		SWTBotShell shell;
		botSelectProject();
		bot.menu("File").menu("New").menu("Other...").click();
		
		shell = bot.shell("New");
		shell.activate();
		bot.tree().expandNode("Examples").expandNode("Graphiti").select("Graphiti Diagram");
		bot.button("Next >").click();
		
		bot.comboBoxWithLabel("Diagram Type").setSelection("MDBDAEditor");
		bot.button("Next >").click();
		
		bot.textWithLabel("Diagram Name").setText(DIAGRAM_NAME);
		bot.button("Finish").click();
	}
	
	@Test
	public void testAddWorkFlowOnDiagram(){		
		newDiagram();
		assertNull(getRootWorkflowFromDiagram());//is empty
		SWTBotGefEditor editor = bot.gefEditor(DIAGRAM_NAME);
		editor.activateTool(CreateWorkflowFeature.name);
		editor.drag(50, 50, 100, 100);
		assertNotNull(getRootWorkflowFromDiagram());//is not empty
		closeDiagramAndDelete();
	}
	
	
	private void closeDiagramAndDelete() {
		bot.gefEditor(DIAGRAM_NAME).saveAndClose();
		
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
		newDiagram();
		assertNull(getRootWorkflowFromDiagram());//is empty
		SWTBotGefEditor editor = bot.gefEditor(DIAGRAM_NAME);
		editor.activateTool(CreateWorkflowFeature.name);
		editor.drag(50, 50, 100, 100);
		
		SWTBotGefEditPart rootWorkflowEditPart = getRootWorkflowEditPart(editor);
		//bot.sleep(500);
		//rootWorkflowEditPart.
		
		assertTrue(rootWorkflowEditPart.children().size() == 0);
		
		editor.activateTool(CreateWorkflowFeature.name);
		//bot.sleep(1000);
		editor.drag(60, 60 , 80, 80);
		//bot.sleep(10000);
		
		assertTrue(rootWorkflowEditPart.children().size() == 1);
		
		
		assertNotNull(getRootWorkflowFromDiagram());//is not empty
		
		closeDiagramAndDelete();
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
					
					boolean isRootWorkflow = true;
					for(EObject obj : businessObjects){
						if(obj instanceof Workflow){
							Workflow w = (Workflow) obj;
							if(w.getWorkflow() != null){
								isRootWorkflow = false;
							}							
						}
					}
					
					return isRootWorkflow;
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
		newDiagram();
		assertNull(getRootWorkflowFromDiagram());//is empty
		SWTBotGefEditor editor = bot.gefEditor(DIAGRAM_NAME);
		editor.activateTool(CreateWorkflowFeature.name);
		editor.drag(50, 50, 100, 100);
		
		SWTBotGefEditPart rootWorkflowEditPart = getRootWorkflowEditPart(editor);
		//bot.sleep(500);
		//rootWorkflowEditPart.
		
		assertTrue(rootWorkflowEditPart.children().size() == 0);
		
		editor.activateTool(CreateCountingWithCountersFeature.name);
		editor.drag(60, 100 , 80, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 1);
		
		
		editor.activateTool(CreateInvertedIndexSummarizationFeature.name);
		editor.drag(60 + 85, 100 , 80 + 85, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 2);
		
		editor.activateTool(CreateNumericalSummarizationFeature.name);
		editor.drag(60 + 85*2, 100 , 80 + 85*2, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 3);
		
		
		assertNotNull(getRootWorkflowFromDiagram());//is not empty
		
		closeDiagramAndDelete();
	}

	@Test
	public void testAddJoinPattern(){
		newDiagram();
		assertNull(getRootWorkflowFromDiagram());//is empty
		SWTBotGefEditor editor = bot.gefEditor(DIAGRAM_NAME);
		editor.activateTool(CreateWorkflowFeature.name);
		editor.drag(50, 50, 100, 100);
		
		SWTBotGefEditPart rootWorkflowEditPart = getRootWorkflowEditPart(editor);
		//bot.sleep(500);
		//rootWorkflowEditPart.
		
		assertTrue(rootWorkflowEditPart.children().size() == 0);
		
		editor.activateTool(CreateCartesianProductFeature.name);
		editor.drag(60, 100 , 80, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 1);
		
		
		editor.activateTool(CreateCompositeJoinFeature.name);
		editor.drag(60 + 85, 100 , 80 + 85, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 2);
		
		editor.activateTool(CreateReduceSideJoinFeature.name);
		editor.drag(60 + 85*2, 100 , 80 + 85*2, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 3);
		
		editor.activateTool(CreateReplicatedJoinFeature.name);
		editor.drag(60 + 85*3, 100 , 80 + 85*3, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 4);
		
		
		assertNotNull(getRootWorkflowFromDiagram());//is not empty
		
		closeDiagramAndDelete();
	}

	@Test
	public void testAddFilteringPattern(){
		newDiagram();
		assertNull(getRootWorkflowFromDiagram());//is empty
		SWTBotGefEditor editor = bot.gefEditor(DIAGRAM_NAME);
		editor.activateTool(CreateWorkflowFeature.name);
		editor.drag(50, 50, 100, 100);
		
		SWTBotGefEditPart rootWorkflowEditPart = getRootWorkflowEditPart(editor);
		//bot.sleep(500);
		//rootWorkflowEditPart.
		
		assertTrue(rootWorkflowEditPart.children().size() == 0);
		
		editor.activateTool(CreateBloomFilteringFeature.name);
		editor.drag(60, 100 , 80, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 1);
		
		
		editor.activateTool(CreateDistinctFeature.name);
		editor.drag(60 + 85, 100 , 80 + 85, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 2);
		
		editor.activateTool(CreateTopTenFeature.name);
		editor.drag(60 + 85*2, 100 , 80 + 85*2, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 3);
		
		
		assertNotNull(getRootWorkflowFromDiagram());//is not empty
		
		closeDiagramAndDelete();
	}
	
	@Test
	public void testAddDataorganizationPattern(){
		newDiagram();
		assertNull(getRootWorkflowFromDiagram());//is empty
		SWTBotGefEditor editor = bot.gefEditor(DIAGRAM_NAME);
		editor.activateTool(CreateWorkflowFeature.name);
		editor.drag(50, 50, 100, 100);
		
		SWTBotGefEditPart rootWorkflowEditPart = getRootWorkflowEditPart(editor);
		//bot.sleep(500);
		//rootWorkflowEditPart.
		
		assertTrue(rootWorkflowEditPart.children().size() == 0);
		
		editor.activateTool(CreateBinningFeature.name);
		editor.drag(60, 100 , 80, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 1);
		
		
		editor.activateTool(CreatePartitoningFeature.name);
		editor.drag(60 + 85, 100 , 80 + 85, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 2);
		
		editor.activateTool(CreateShufflingFeature.name);
		editor.drag(60 + 85*2, 100 , 80 + 85*2, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 3);
		
		editor.activateTool(CreateStructuredToHierachicalFeature.name);
		editor.drag(60 + 85*3, 100 , 80 + 85*3, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 4);
		
		editor.activateTool(CreateTotalOrderSortingFeature.name);
		editor.drag(60 + 85*4, 100 , 80 + 85*4, 120);
		
		assertTrue(rootWorkflowEditPart.children().size() == 5);
		
		
		assertNotNull(getRootWorkflowFromDiagram());//is not empty
		
		closeDiagramAndDelete();
	}
	
	
	
//	public Object getBusinessObjectForPictogramElement(PictogramElement pictogramElement) {
//		Object ret = super.getBusinessObjectForPictogramElement(pictogramElement);
//		if (ret == null) {
//			Property linkProperty = Graphiti.getLinkService().getLinkProperty(pictogramElement);
//			if (linkProperty != null) {
//				ret = linkProperty.getValue();
//			}
//		}
//		return ret;
//	}
//	
//	@SuppressWarnings("restriction")
//	public void clearDiagram(){
//		Display.getDefault().syncExec(new Runnable() {
//			@Override
//		
//			public void run() {
//				
//				DiagramEditPart diagramEditPart = getDiagramEditPart();
//				for(Object c : diagramEditPart.getChildren()){
//		
//					diagramEditPart.deleteChildAndRefresh((EditPart) c);
//				}
//		
//
//		}});
//		bot.sleep(500);	
//	}
//	
	
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
