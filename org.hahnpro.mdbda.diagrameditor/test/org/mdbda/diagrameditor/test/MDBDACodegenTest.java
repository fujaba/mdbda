package org.mdbda.diagrameditor.test;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;

import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mdbda.diagrameditor.test.conditions.AtomicBooleanIsTrueCondition;
import org.mdbda.diagrameditor.test.conditions.NonSystemJobRunsCondition;

@RunWith(SWTBotJunit4ClassRunner.class)
public class MDBDACodegenTest extends MDBDASWTBotTestCase{
	
	protected static final String GenMDBDAContextLable = "Generate MDBDA Code";
	@Test
	public void testImportEMailSucheAndStartCodeGenAndValiadeTests() throws InterruptedException{

		bot.menu("File").menu("Import...").click();
		SWTBotShell shell = bot.shell("Import");
		shell.activate();
		bot.tree().expandNode("General").select("Existing Projects into Workspace");
		bot.button("Next >").click();
		bot.radio("Select archive file:").click();
		bot.comboBox(1).setText("C:\\Users\\hahn\\git\\mdbda\\org.hahnpro.mdbda.diagrameditor\\testresources\\exampleprojects\\EMailSuche.zip");
		bot.button("Refresh").click();
		bot.button("Finish").click();
		
		Thread.sleep(2000);
		
		
		//open diagram
		getPackageExplorerTree().expandNode("EMailSuche").expandNode("src").expandNode("diagrams").getNode("mailsuche.mdbdamodel").select().contextMenu(GenMDBDAContextLable).click();
		bot.waitWhile(new NonSystemJobRunsCondition(), 60 * 1000);
		
		getPackageExplorerTree().select("EMailSuche").contextMenu("Refresh").click();
		bot.waitWhile(new NonSystemJobRunsCondition(), 60 * 1000);
		
		
		final AtomicBoolean isFinishBoolean = new AtomicBoolean(false);
		
		org.eclipse.jdt.junit.JUnitCore.addTestRunListener(new TestRunListener() {
			
			@Override
			public void sessionStarted(ITestRunSession session) {
				System.out.println("sessionStarted " + session);
				super.sessionStarted(session);
			}
			
			@Override
			public void sessionFinished(ITestRunSession session) {
				System.out.println("sessionFinished " + session);
				super.sessionFinished(session);
			}
		
			@Override
			public void testCaseStarted(ITestCaseElement testCaseElement) {
				System.out.println("testCaseStarted " + testCaseElement);
				super.testCaseStarted(testCaseElement);
			}
			
			@Override
			public void testCaseFinished(ITestCaseElement testCaseElement) {
				System.out.println("testCaseFinished " + testCaseElement);
				isFinishBoolean.set(true);
				super.testCaseFinished(testCaseElement);
			}
		});
		

		getPackageExplorerTree().expandNode("EMailSuche").getNode("mdbda-gen").select().pressShortcut(SWT.ALT | SWT.SHIFT, 'x').pressShortcut(new Keystrokes().create('t'));
	
		//bot.waitWhile(new NonSystemJobRunsCondition(), 60 * 1000);
		
		bot.waitWhile(new AtomicBooleanIsTrueCondition(isFinishBoolean), 60 * 1000); //FIXME geht nicht 
		
		
	}
}
