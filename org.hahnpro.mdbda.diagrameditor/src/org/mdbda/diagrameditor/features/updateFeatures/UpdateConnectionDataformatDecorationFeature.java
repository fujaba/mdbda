package org.mdbda.diagrameditor.features.updateFeatures;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.mdbda.diagrameditor.utils.DataformatUtils;
import org.mdbda.model.Resource;

public class UpdateConnectionDataformatDecorationFeature extends
		AbstractConnectionUpdateFeature {

	public UpdateConnectionDataformatDecorationFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		Connection conn = (Connection)  context.getPictogramElement();
	
		
//		Resource startResource = (Resource) getBusinessObjectForPictogramElement( conn.getStart().getParent() );
		
		String soll = DataformatUtils.getConnectionTextDecoration(conn, this);
		
		for(ConnectionDecorator connDec : conn.getConnectionDecorators()){
			if(connDec instanceof Text){
				Text text = (Text) connDec.getGraphicsAlgorithm();
				if(!soll.equals(text.getValue())){
					return Reason.createTrueReason(soll + " != " + text.getValue());
				}
			}
		}
		
		return Reason.createFalseReason();
		
	}

	@Override
	public boolean update(IUpdateContext context) {
		Connection conn = (Connection)  context.getPictogramElement();
		String soll = DataformatUtils.getConnectionTextDecoration(conn, this);
		for(ConnectionDecorator connDec : conn.getConnectionDecorators()){
			if(connDec instanceof Text){
				Text text = (Text) connDec.getGraphicsAlgorithm();
				if(!soll.equals(text.getValue())){
					text.setValue(soll);
					return true;
				}
			}
		}
		return false;
	}

}
