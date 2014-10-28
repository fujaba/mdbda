package org.mdbda.diagrameditor.test;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.internal.parts.ContainerShapeEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
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
import org.mdbda.diagrameditor.utils.DiagramUtils;
import org.mdbda.diagrameditor.wizard.NewMDBDADiagramWizard;


@RunWith(SWTBotJunit4ClassRunner.class)
public class MDBDADiagrameditorTest extends MDBDASWTBotTestCase{
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
		
		editor.activateTool("Neo4j Resource"); //ausgelagert in ein plugin
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
 
}
