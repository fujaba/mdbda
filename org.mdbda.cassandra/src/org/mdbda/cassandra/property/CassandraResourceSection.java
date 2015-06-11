package org.mdbda.cassandra.property;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.mdbda.cassandra.CreateCassandraResourceFeature;
import org.mdbda.cassandra.helper.CassandraConfigReader;
import org.mdbda.model.ModelPackage;
import org.mdbda.model.Resource;

public class CassandraResourceSection extends GFPropertySection implements ITabbedPropertyConstants {
	private Text LiveServerIP;
	private Text TestServerIP;
	private Text ColumnName;
	private Text ColumnFamily;
	private Text Keyspace;

	private static int  LABEL_WITH = 100;

	ModifyListener modify = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			Resource res = getCassandraResource();
			if(res != null) {
				boolean somethingChanged = false;
				CassandraConfigReader conf = new CassandraConfigReader(res.getConfigurationString());
				if(e.widget.equals(LiveServerIP) ){
					if(!LiveServerIP.getText().equals(conf.getLiveServerIP())){
						conf.setLiveServerIP(LiveServerIP.getText());
						somethingChanged = true;
					}
				}
				if(e.widget.equals(TestServerIP) ){
					if(!TestServerIP.getText().equals(conf.getTestServerIP())){
						conf.setTestServerIP(TestServerIP.getText());
						somethingChanged = true;
					}
				}
				if(e.widget.equals(ColumnName) ){
					if(!ColumnName.getText().equals(conf.getCassandraColumnName())){
						conf.setCassandraColumnName(ColumnName.getText());
						somethingChanged = true;
					}
				}
				if(e.widget.equals(ColumnFamily) ){
					if(!ColumnFamily.getText().equals(conf.getCassandraResourceColumnFamily())){
						conf.setCassandraResourceColumnFamily(ColumnFamily.getText());
						somethingChanged = true;
					}
				}
				if(e.widget.equals(Keyspace) ){
					if(!Keyspace.getText().equals(conf.getCassandraResourceKeyspace())){
						conf.setCassandraResourceKeyspace(Keyspace.getText());
						somethingChanged = true;
					}
				}
				
				if(somethingChanged){//http://stackoverflow.com/questions/19244684/emf-gmf-papyrus-set-excplicit-an-elementimpl-property-out-of-code
					TransactionalEditingDomain ted = (TransactionalEditingDomain)AdapterFactoryEditingDomain.getEditingDomainFor(res);
					ted.getCommandStack().execute(new SetCommand(ted,res, ModelPackage.Literals.RESOURCE__CONFIGURATION_STRING,conf.writeConfigString()));
				}
			}
		}
	};

	private void addElement(Text element,Text element_above,Composite composite, String label){
		FormData data;
		TabbedPropertySheetWidgetFactory factory = getWidgetFactory();

		element.addModifyListener(modify);

		data = new FormData();
		data.left = new FormAttachment(0, LABEL_WITH);
		data.right = new FormAttachment(100, 0);
		if(element_above == null){
			data.top = new FormAttachment(0, VSPACE);
		}else{
			data.top = new FormAttachment(element_above, VSPACE);

		}
		element.setLayoutData(data);

		CLabel valueLabel = factory.createCLabel(composite,  label);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(element, -HSPACE);
		data.top = new FormAttachment(element, 0, SWT.CENTER);
		valueLabel.setLayoutData(data);
	}

	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		TabbedPropertySheetWidgetFactory factory = getWidgetFactory();
		Composite composite = factory.createFlatFormComposite(parent);

		LiveServerIP = factory.createText(composite, "");
		TestServerIP = factory.createText(composite, "");
		ColumnName = factory.createText(composite, "");
		ColumnFamily = factory.createText(composite, "");
		Keyspace = factory.createText(composite, "");

		addElement(LiveServerIP,null,composite,"Live Server:");
		addElement(TestServerIP,LiveServerIP,composite,"Test Server:");
		addElement(ColumnName,TestServerIP,composite,"Column Name:");
		addElement(ColumnFamily,ColumnName,composite,"Column Family:");
		addElement(Keyspace,ColumnFamily,composite,"Keyspace:");
	}


	private Resource getCassandraResource(){
		PictogramElement pe = getSelectedPictogramElement();
		if(pe != null) {
			EObject eObj = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
			if(eObj instanceof Resource) {
				Resource res = (Resource) eObj;			
				if(CreateCassandraResourceFeature.TYPE_ID.equals(res.getTypeId())) {
					return res;
				}
			}
		}
		return null;
	}

	@Override
	public void refresh() {
		Resource res = getCassandraResource();
		if(res != null) {
			CassandraConfigReader conf = new CassandraConfigReader(res.getConfigurationString());

			LiveServerIP.setText(conf.getLiveServerIP() + "");
			TestServerIP.setText(conf.getTestServerIP() + "");
			ColumnName.setText(conf.getCassandraColumnName() + "");
			ColumnFamily.setText(conf.getCassandraResourceColumnFamily() + "");
			Keyspace.setText(conf.getCassandraResourceKeyspace() + "");
		}
	}
}
