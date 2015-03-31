package org.mdbda.codegen.dialog;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.widgets.Button;

import swing2swt.layout.FlowLayout;

import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Table;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class CodegenDialog extends Dialog {
	private DataBindingContext m_bindingContext;
	
	
	public class CodegenDialogResult{
		String packageName = "";
		String generatorOutputDir = "";
		String codeStyle = "";
		public String getPackageName() {
			return packageName;
		}
		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}
		public String getGeneratorOutputDir() {
			return generatorOutputDir;
		}
		public void setGeneratorOutputDir(String generatorOutputDir) {
			this.generatorOutputDir = generatorOutputDir;
		}
		public String getCodeStyle() {
			return codeStyle;
		}
		public void setCodeStyle(String codeStyle) {
			this.codeStyle = codeStyle;
		}
		
		
	}

	protected CodegenDialogResult result;
	protected Shell shlGenerateMdbdaCode;
	private Text txtPackageName;
	private Text txtTargetDir;
	private Combo comboCodeStyle;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public CodegenDialog(Shell parent, int style) {
		super(parent, style);
		setText("Generate MDBDA Code");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public CodegenDialogResult open() {
		createContents();
		initData();
		shlGenerateMdbdaCode.open();
		shlGenerateMdbdaCode.layout();
		Display display = getParent().getDisplay();
		while (!shlGenerateMdbdaCode.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	private void initData(){
		comboCodeStyle.add("Hadoop");
		comboCodeStyle.add("Storm");
		comboCodeStyle.add("Spark (todo)");
		comboCodeStyle.add("MPI (todo)");
		comboCodeStyle.add("Akka (todo)");
		comboCodeStyle.add("... (todo)");
		
		
	}
	
	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlGenerateMdbdaCode = new Shell(getParent(), getStyle());
		shlGenerateMdbdaCode.setSize(600, 341);
		shlGenerateMdbdaCode.setText("Generate MDBDA Code");
		shlGenerateMdbdaCode.setLayout(new FormLayout());
		
		Label lblPackageName = new Label(shlGenerateMdbdaCode, SWT.NONE);
		FormData fd_lblPackageName = new FormData();
		fd_lblPackageName.top = new FormAttachment(0, 6);
		fd_lblPackageName.left = new FormAttachment(0, 3);
		lblPackageName.setLayoutData(fd_lblPackageName);
		lblPackageName.setText("Package Name:");
		
		txtPackageName = new Text(shlGenerateMdbdaCode, SWT.BORDER);
		FormData fd_txtPackageName = new FormData();
		fd_txtPackageName.right = new FormAttachment(100, -3);
		fd_txtPackageName.top = new FormAttachment(0, 3);
		fd_txtPackageName.left = new FormAttachment(0, 88);
		txtPackageName.setLayoutData(fd_txtPackageName);
		txtPackageName.setText("org.mdbda.gen");
		
		Label lblTargetDir = new Label(shlGenerateMdbdaCode, SWT.NONE);
		FormData fd_lblTargetDir = new FormData();
		fd_lblTargetDir.top = new FormAttachment(lblPackageName, 9);
		fd_lblTargetDir.right = new FormAttachment(lblPackageName, 0, SWT.RIGHT);
		lblTargetDir.setLayoutData(fd_lblTargetDir);
		lblTargetDir.setText("Target Dir:");
		
		txtTargetDir = new Text(shlGenerateMdbdaCode, SWT.BORDER);
		FormData fd_txtTargetDir = new FormData();
		fd_txtTargetDir.right = new FormAttachment(txtPackageName, 0, SWT.RIGHT);
		fd_txtTargetDir.left = new FormAttachment(lblTargetDir, 3);
		fd_txtTargetDir.top = new FormAttachment(lblPackageName, 6);
		txtTargetDir.setLayoutData(fd_txtTargetDir);
		txtTargetDir.setText("mdbda-gen/");
		
		Label lblCodeStyle = new Label(shlGenerateMdbdaCode, SWT.NONE);
		FormData fd_lblCodeStyle = new FormData();
		fd_lblCodeStyle.top = new FormAttachment(lblTargetDir, 12);
		fd_lblCodeStyle.right = new FormAttachment(lblPackageName, 0, SWT.RIGHT);
		lblCodeStyle.setLayoutData(fd_lblCodeStyle);
		lblCodeStyle.setText("Code Style:");
		
		comboCodeStyle = new Combo(shlGenerateMdbdaCode, SWT.NONE);
		FormData fd_comboCodeStyle = new FormData();
		fd_comboCodeStyle.right = new FormAttachment(txtPackageName, 0, SWT.RIGHT);
		fd_comboCodeStyle.top = new FormAttachment(txtTargetDir, 6);
		fd_comboCodeStyle.left = new FormAttachment(lblCodeStyle, 3);
		comboCodeStyle.setLayoutData(fd_comboCodeStyle);
		comboCodeStyle.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println(comboCodeStyle.getText());
			};
		});
		
		Button btnCancel = new Button(shlGenerateMdbdaCode, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlGenerateMdbdaCode.dispose();
			}
		});
		
		FormData fd_btnCancel = new FormData();
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");
		
		Button btnGenerateCode = new Button(shlGenerateMdbdaCode, SWT.NONE);
		fd_btnCancel.top = new FormAttachment(btnGenerateCode, 0, SWT.TOP);
		fd_btnCancel.right = new FormAttachment(btnGenerateCode, -6);
		FormData fd_btnGenerateCode = new FormData();
		fd_btnGenerateCode.top = new FormAttachment(comboCodeStyle, 207);
		fd_btnGenerateCode.right = new FormAttachment(100, -10);
		btnGenerateCode.setLayoutData(fd_btnGenerateCode);
		btnGenerateCode.setText("Generate Code");
		
		ProgressBar progressBar = new ProgressBar(shlGenerateMdbdaCode, SWT.NONE);
		FormData fd_progressBar = new FormData();
		fd_progressBar.right = new FormAttachment(100, -3);
		fd_progressBar.top = new FormAttachment(comboCodeStyle, 33);
		progressBar.setLayoutData(fd_progressBar);
		
		Label lblCodeStyleCoverage = new Label(shlGenerateMdbdaCode, SWT.NONE);
		fd_progressBar.left = new FormAttachment(lblCodeStyleCoverage, 6);
		FormData fd_lblCodeStyleCoverage = new FormData();
		fd_lblCodeStyleCoverage.top = new FormAttachment(comboCodeStyle, 35);
		lblCodeStyleCoverage.setLayoutData(fd_lblCodeStyleCoverage);
		lblCodeStyleCoverage.setText("Code style coverage:");
		
		Label lblNotSupportedElements = new Label(shlGenerateMdbdaCode, SWT.NONE);
		fd_lblCodeStyleCoverage.bottom = new FormAttachment(lblNotSupportedElements, -6);
		fd_lblCodeStyleCoverage.right = new FormAttachment(lblNotSupportedElements, 0, SWT.RIGHT);
		FormData fd_lblNotSupportedElements = new FormData();
		fd_lblNotSupportedElements.top = new FormAttachment(0, 133);
		fd_lblNotSupportedElements.left = new FormAttachment(lblPackageName, 0, SWT.LEFT);
		lblNotSupportedElements.setLayoutData(fd_lblNotSupportedElements);
		lblNotSupportedElements.setText("Not supported Elements:");
		
		Label seperator = new Label(shlGenerateMdbdaCode, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_seperator = new FormData();
		fd_seperator.bottom = new FormAttachment(comboCodeStyle, 16, SWT.BOTTOM);
		fd_seperator.right = new FormAttachment(0, 594);
		fd_seperator.top = new FormAttachment(comboCodeStyle, 14);
		fd_seperator.left = new FormAttachment(0);
		seperator.setLayoutData(fd_seperator);
		
		List list = new List(shlGenerateMdbdaCode, SWT.BORDER);
		FormData fd_list = new FormData();
		fd_list.top = new FormAttachment(progressBar, 6);
		fd_list.bottom = new FormAttachment(btnCancel, -6);
		fd_list.left = new FormAttachment(progressBar, 0, SWT.LEFT);
		fd_list.right = new FormAttachment(100, -3);
		list.setLayoutData(fd_list);
		m_bindingContext = initDataBindings();

	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue codeStyleResultObserveValue = PojoProperties.value("codeStyle").observe(result);
		IObservableValue observeSelectionComboCodeStyleObserveWidget_1 = WidgetProperties.selection().observe(comboCodeStyle);
		bindingContext.bindValue(codeStyleResultObserveValue, observeSelectionComboCodeStyleObserveWidget_1, null, null);
		//
		return bindingContext;
	}
}
