package org.mdbda.cassandra.property;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.mdbda.cassandra.CreateCassandraResourceFeature;
import org.mdbda.cassandra.helper.CassandraConfigReader;
import org.mdbda.model.Resource;

public class CassandraResourceSection extends GFPropertySection implements ITabbedPropertyConstants {
	private Text LiveServerIP;
	private Text TestServerIP;
	private Text ColumnName;
	private Text ColumnFamily;
	private Text Keyspace;

	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);

		TabbedPropertySheetWidgetFactory factory = getWidgetFactory();
		Composite composite = factory.createComposite(parent);
		FormData data;

		LiveServerIP = factory.createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, VSPACE);
		LiveServerIP.setLayoutData(data);

		CLabel valueLabel = factory.createCLabel(composite,  "Live Server:");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(LiveServerIP, -HSPACE);
		data.top = new FormAttachment(LiveServerIP, 0, SWT.CENTER);
		valueLabel.setLayoutData(data);

		
		
		TestServerIP = factory.createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, VSPACE);
		TestServerIP.setLayoutData(data);

		valueLabel = factory.createCLabel(composite,  "Test Server:");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(TestServerIP, -HSPACE);
		data.top = new FormAttachment(TestServerIP, 0, SWT.CENTER);
		valueLabel.setLayoutData(data);
		
		
		
		ColumnName = factory.createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, VSPACE);
		ColumnName.setLayoutData(data);

		valueLabel = factory.createCLabel(composite,  "Column Name:");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(ColumnName, -HSPACE);
		data.top = new FormAttachment(ColumnName, 0, SWT.CENTER);
		valueLabel.setLayoutData(data);
		
		
		
		ColumnFamily = factory.createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, VSPACE);
		ColumnFamily.setLayoutData(data);

		valueLabel = factory.createCLabel(composite,  "Column Family:");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(ColumnFamily, -HSPACE);
		data.top = new FormAttachment(ColumnFamily, 0, SWT.CENTER);
		valueLabel.setLayoutData(data);
		
		
		
		Keyspace = factory.createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, VSPACE);
		Keyspace.setLayoutData(data);

		valueLabel = factory.createCLabel(composite,  "Keyspace:");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(Keyspace, -HSPACE);
		data.top = new FormAttachment(Keyspace, 0, SWT.CENTER);
		valueLabel.setLayoutData(data);
	}
	
	@Override
	public void refresh() {
		PictogramElement pe = getSelectedPictogramElement();
		if(pe != null) {
			EObject eObj = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
			if(eObj instanceof Resource) {
				Resource res = (Resource) eObj;			
				if(CreateCassandraResourceFeature.TYPE_ID.equals(res.getTypeId())) {
					CassandraConfigReader conf = new CassandraConfigReader(res.getConfigurationString());
					
					LiveServerIP.setText(conf.getLiveServerIP() + "");
					TestServerIP.setText(conf.getTestServerIP() + "");
					ColumnName.setText(conf.getCassandraColumnName() + "");
					ColumnFamily.setText(conf.getCassandraResourceColumnFamily() + "");
					Keyspace.setText(conf.getCassandraResourceKeyspace() + "");
				}
			}
		}
	}
	
	
}
