package org.hahnpro.mdbda.diagrameditor.pictogramelements;

import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.pictograms.impl.ContainerShapeImpl;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;
import org.hahnpro.mdbda.diagrameditor.features.AddWorkflowFeature;

public abstract class AbstractMDBDAShape extends ContainerShapeImpl {
	public static final int[] OUTPUTPOLYGON = new int[] {1,1,  10,1,  20,5,  10,10,  1,10, 1,1};
	public static final int[] INPUTPOLYGON = new int[] {1,1,  20,1,  20,10,  1,10,  10,5,  1,1};
	public static final int INVISIBLE_RECT_SIDE = 10;
	public static final IColorConstant PATTERN_TEXT_FOREGROUND = IColorConstant.BLACK;
	public static final IColorConstant PATTERN_FOREGROUND = new ColorConstant(148, 131, 167);
	public static final IColorConstant PATTERN_BACKGROUND = new ColorConstant(237, 218, 247);
	public static final IColorConstant WORKFLOW_TEXT_FOREGROUND = IColorConstant.BLACK;

	public static final IColorConstant ROOTWORKFLOW_FOREGROUND = new ColorConstant(
			98, 131, 167);

	public static final IColorConstant ROOTWORKFLOW_BACKGROUND = new ColorConstant(
			187, 218, 247);

	public static final IColorConstant WORKFLOW_FOREGROUND = new ColorConstant(
			80, 222, 99);

	public static final IColorConstant WORKFLOW_BACKGROUND = new ColorConstant(
			83, 230, 100);


	public static final IColorConstant RESOURCE_TEXT_FOREGROUND =
            IColorConstant.BLACK;
     
	public static final IColorConstant RESOURCE_FOREGROUND = IColorConstant.BLUE; 
//            new ColorConstant(148, 131, 167);

	public static final IColorConstant  RESOURCE_BACKGROUND = IColorConstant.LIGHT_BLUE;
//            new ColorConstant(237, 218, 247);
	
	public AbstractMDBDAShape(IAddContext context, AddWorkflowFeature feature){
		setVisible(true);
		setActive(active);
		setContainer(context.getTargetContainer());
	}
}
