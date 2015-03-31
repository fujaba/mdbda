package org.mdbda.diagrameditor.pictogramelements;

import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.pictograms.impl.ContainerShapeImpl;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;
import org.mdbda.diagrameditor.features.AddWorkflowFeature;

public abstract class AbstractMDBDAShape extends ContainerShapeImpl {
	public static final int[] OUTPUTPOLYGON = new int[] {0,0,  4,0,  14,5,  4,10,  0,10, 1,5, 0,0};
	public static final int[] INPUTPOLYGON = new int[] {1,0,  15,0, 16,5,  15,10,  1,10, 0,9, 10,5,  0,1, 1,0};
	public static final int INVISIBLE_RECT_SIDE = 10;
	public static final IColorConstant PATTERN_TEXT_FOREGROUND = IColorConstant.BLACK;
//	public static final IColorConstant PATTERN_FOREGROUND = 
//	public static final IColorConstant PATTERN_BACKGROUND = ;
	public static final IColorConstant WORKFLOW_TEXT_FOREGROUND = IColorConstant.BLACK;

	public static final IColorConstant WORKFLOW_FOREGROUND = IColorConstant.LIGHT_GRAY;//new ColorConstant(80, 222, 99);

	public static final IColorConstant WORKFLOW_BACKGROUND = IColorConstant.LIGHT_LIGHT_GRAY;// new ColorConstant(	83, 230, 100);


	public static final IColorConstant RESOURCE_TEXT_FOREGROUND =
            IColorConstant.BLACK;
     
//	public static final IColorConstant RESOURCE_FOREGROUND = ; 
//            new ColorConstant(148, 131, 167);

//	public static final IColorConstant  RESOURCE_BACKGROUND = 
//            new ColorConstant(237, 218, 247);
	
//	public AbstractMDBDAShape(IAddContext context, AddWorkflowFeature feature){
//		setVisible(true);
//		setActive(active);
//		setContainer(context.getTargetContainer());
//	}
}
