package org.mdbda.diagrameditor.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.util.ColorConstant;
import org.mdbda.diagrameditor.internal.PublicGetBusinessObjectForPictogramElementInterface;
import org.mdbda.diagrameditor.utils.AbstractSimpleMDBDAShapeHelper;
import org.mdbda.diagrameditor.utils.DataDescriptionShapeHelper;
import org.mdbda.diagrameditor.utils.LiveStatusShapeHelper;
import org.mdbda.diagrameditor.utils.NameShapeHelper;
import org.mdbda.diagrameditor.utils.SampleDataShapeHelper;
import org.mdbda.diagrameditor.utils.TestStatusShapeHelper;
import org.mdbda.diagrameditor.utils.TypeIdShapeHelper;
import org.mdbda.model.RemoteWorkflow;
import org.mdbda.model.Resource;
import org.mdbda.model.Task;
import org.mdbda.model.Workflow;

public abstract class AbstractSimpleMDBDAAddFeature extends AbstractAddFeature implements PublicGetBusinessObjectForPictogramElementInterface{

	protected String typeName = "Abstract Type";
	protected static final String ROUNDED_RECTANGLE_ID = "RoundedRecangle";
	
	public static ContainerShape getRootContainerShapeForResourceElement(Resource resource,
			IFeatureProvider fp) {
				PictogramElement[] allPictogramElementsForBusinessObject = fp.getAllPictogramElementsForBusinessObject(resource);
				
				for(PictogramElement pe : allPictogramElementsForBusinessObject){
					if(pe instanceof ContainerShape){
						ContainerShape cs = (ContainerShape) pe;
										
						Shape nameShape = new NameShapeHelper(resource, cs, fp).getShape();				
						Shape typeShape = new TypeIdShapeHelper(resource, cs, fp).getShape();
						Shape dataDescriptionShape = new DataDescriptionShapeHelper(resource, cs, fp).getShape();
						Shape exampleDataShape = new SampleDataShapeHelper(resource, cs, fp).getShape();
						Shape liveShape = new LiveStatusShapeHelper(resource, cs, fp).getShape();
						Shape testShape = new TestStatusShapeHelper(resource, cs, fp).getShape();
						
						if(	nameShape != null ){
							if(resource instanceof Workflow || resource instanceof RemoteWorkflow){
								//is a Workflow Shape
								return cs;
							}else if(typeShape != null 
									&& dataDescriptionShape != null 
									&& exampleDataShape != null ){
								if(resource instanceof Task){
									//is a Task Shape
									return cs;
								}else if( liveShape != null && testShape != null){
									//is a Resource Shape
									return cs;
								}
							}			
						}	
					}
				}
				return null;
			}

	public Object getBusinessObjectForPictogramElement(PictogramElement pe) {
		return super.getBusinessObjectForPictogramElement(pe);
	}

	public static RoundedRectangle getResourceRoundedRectangle(ContainerShape resourceContainer, IFeatureProvider fp) {
		if(resourceContainer == null) return null;
		for(GraphicsAlgorithm child : resourceContainer.getGraphicsAlgorithm().getGraphicsAlgorithmChildren()){
			if(ROUNDED_RECTANGLE_ID.equals( Graphiti.getPeService()
	           .getPropertyValue(child, AbstractSimpleMDBDAShapeHelper.SHAPE_KEY))){
				if(child instanceof RoundedRectangle){
					return (RoundedRectangle) child;
				}
			}
		}
		if(resourceContainer.getGraphicsAlgorithm() instanceof RoundedRectangle
				&& ROUNDED_RECTANGLE_ID.equals( Graphiti.getPeService()
				           .getPropertyValue(resourceContainer.getGraphicsAlgorithm(), AbstractSimpleMDBDAShapeHelper.SHAPE_KEY))){
			
			return (RoundedRectangle) resourceContainer.getGraphicsAlgorithm();
		}
		return null;
	}

	protected ColorConstant getBackgroundColor() {
		return new ColorConstant("B0DCF7");// manageColor(new ColorConstant(176, 220, 247));//B0DCF7
	}

	protected ColorConstant getForegroundColor() {
		return new ColorConstant("083755");// manageColor(new ColorConstant(8, 55, 85));//083755
	}

	public AbstractSimpleMDBDAAddFeature(IFeatureProvider fp) {
		super(fp);
	}

}