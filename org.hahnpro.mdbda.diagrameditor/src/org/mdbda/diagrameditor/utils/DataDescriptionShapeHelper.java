package org.mdbda.diagrameditor.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.algorithms.styles.TextStyle;
import org.eclipse.graphiti.mm.algorithms.styles.TextStyleRegion;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;
import org.mdbda.diagrameditor.pictogramelements.AbstractMDBDAShape;
import org.mdbda.model.Resource;

public class DataDescriptionShapeHelper  extends AbstactMDBDAResourceShapeHelper {
	static final IColorConstant FOREGROUND_COLOR = IColorConstant.DARK_BLUE;
	
	@Override
	protected String getShapeId() {
		return "DataDescription";
	}
	
	public DataDescriptionShapeHelper(Resource resource,
			ContainerShape rootContainerShapeForResourceElement,
			IFeatureProvider fp) {
		super(resource, rootContainerShapeForResourceElement, fp);
	}


	@Override
	public IDimension addNewShapeOnContainer(int parentWidth, int parentHeight,
			int leftOffset, int topOffset) {

	
		Shape shape = createNewShapeOnRootContainer();
		IGaService gaService = Graphiti.getGaService();

		Text text = gaService.createText(shape, "Data Desciption");
		// IDimension calculateSize = gaService.calculateSize(text);
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
		
		setLocationAndSize(text, parentWidth, parentHeight, leftOffset, topOffset);
		
		text.setFont(ButtonTextFont);

		text.setFilled(true);

		updateTextColor(text, resource);

		return gaService.calculateSize(text);
	}

	private boolean checkDataDescription(){
		boolean dataDesctiptionIsOK = false;
		
		String dataDSLDesciptionURI = resource.getDataDSLDesciptionURI();
		if(dataDSLDesciptionURI != null && dataDSLDesciptionURI != ""){
			Path path = new Path(dataDSLDesciptionURI);
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);//  getFileForLocation(path) ;//getFile(path);
			
			dataDesctiptionIsOK = file.exists();
		}
		
		return dataDesctiptionIsOK;
	}
	
	private void updateTextColor(Text text, Resource  resource) {
		if (checkDataDescription()) {
			text.setForeground(manageColor(IColorConstant.BLACK));
			text.setBackground(manageColor(IColorConstant.LIGHT_GREEN));
		} else {
			text.setForeground(manageColor(IColorConstant.BLACK));
			text.setBackground(manageColor(IColorConstant.RED));
		}
	}

	@Override
	public int calculateX(int parentWidth, int parentHeight, int leftOffset,
			int topOffset) {
		return AbstractMDBDAShape.INVISIBLE_RECT_SIDE + 4;
	}

	@Override
	public int calculateY(int parentWidth, int parentHeight, int leftOffset,
			int topOffset) {
		return parentHeight - 13;
	}

	@Override
	public int calculateWidth(int parentWidth, int parentHeight,
			int leftOffset, int topOffset) {
		return parentWidth / 2 - 1;
	}

	@Override
	public int calculateHeight(int parentWidth, int parentHeight,
			int leftOffset, int topOffset) {
		return 10;
	}


	@Override
	public boolean hasChanged() {
		Text t = (Text) getGraphicsAlgorithm();
		if(t.getBackground().equals(manageColor(IColorConstant.RED)) && checkDataDescription()){
			return true;
		}
		if(t.getBackground().equals(manageColor(IColorConstant.LIGHT_GREEN)) && !checkDataDescription()){
			return true;
		}
		
		
		return false;
	}
	
	@Override
	public void update() {
		updateTextColor((Text) getGraphicsAlgorithm(), resource);
	}
	


}
