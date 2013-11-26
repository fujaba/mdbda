package org.mdbda.diagrameditor.diagram;

import org.eclipse.graphiti.dt.AbstractDiagramTypeProvider;
import org.eclipse.graphiti.tb.IToolBehaviorProvider;

public class MDBDADiagramTypeProvider extends AbstractDiagramTypeProvider {
	private IToolBehaviorProvider[] toolBehaviorProviders;
	
	public MDBDADiagramTypeProvider() {
		super();
		setFeatureProvider(new MDBDAFeatureProvider(this));
	}
	
	@Override
	public IToolBehaviorProvider[] getAvailableToolBehaviorProviders() {
		if(toolBehaviorProviders == null){
			toolBehaviorProviders = new IToolBehaviorProvider[] { new MDBDAToolBehaviorProvider(this)};
		}
		
		return toolBehaviorProviders;
	}
	
	
}
