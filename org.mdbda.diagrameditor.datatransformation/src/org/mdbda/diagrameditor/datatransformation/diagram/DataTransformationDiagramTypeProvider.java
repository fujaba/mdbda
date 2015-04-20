package org.mdbda.diagrameditor.datatransformation.diagram;

import org.eclipse.graphiti.dt.AbstractDiagramTypeProvider;

public class DataTransformationDiagramTypeProvider extends AbstractDiagramTypeProvider {

	public DataTransformationDiagramTypeProvider() {
		super();
		setFeatureProvider(new DataTransformationFeatureProvider(this));
	}
}
