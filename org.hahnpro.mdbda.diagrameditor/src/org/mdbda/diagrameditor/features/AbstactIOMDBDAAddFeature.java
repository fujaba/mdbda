package org.mdbda.diagrameditor.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.BoxRelativeAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.mdbda.diagrameditor.pictogramelements.AbstractMDBDAShape;
import org.mdbda.model.Resource;

public abstract class AbstactIOMDBDAAddFeature extends AbstractSimpleMDBDAAddFeature {

	public AbstactIOMDBDAAddFeature(IFeatureProvider fp) {
		super(fp);
	}

	public static void addOutputAnchor(Resource resource,
			ContainerShape rootContainerShapeForResourceElement, double relativHeight, Diagram dia, IFeatureProvider fp) {
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		IGaService gaService = Graphiti.getGaService();
		RoundedRectangle roundedRectangle = null;
		if(rootContainerShapeForResourceElement.getGraphicsAlgorithm() instanceof RoundedRectangle){ 
			roundedRectangle = (RoundedRectangle) rootContainerShapeForResourceElement.getGraphicsAlgorithm();
		}else{
			for(GraphicsAlgorithm ga : rootContainerShapeForResourceElement.getGraphicsAlgorithm().getGraphicsAlgorithmChildren()){
				if(ga instanceof RoundedRectangle){
					roundedRectangle = (RoundedRectangle) ga;
					break;
				}
			}
		}
		
		
		BoxRelativeAnchor boxAnchor = peCreateService.createBoxRelativeAnchor(rootContainerShapeForResourceElement);
    	boxAnchor.setUseAnchorLocationAsConnectionEndpoint(false);
    	boxAnchor.setRelativeWidth(1.0);
    	boxAnchor.setRelativeHeight(relativHeight);
    	boxAnchor.setReferencedGraphicsAlgorithm(roundedRectangle);
    	
    	fp.link(boxAnchor,resource);
    	
    	Polygon poly = gaService.createPolygon(boxAnchor, AbstractMDBDAShape.OUTPUTPOLYGON);


		poly.setBackground(gaService.manageColor(dia, new ColorConstant("FFEEEE")));
		poly.setForeground(gaService.manageColor(dia, new ColorConstant("880000")));//IColorConstant.DARK_ORANGE));
		
    	poly.setLineWidth(1);
    	gaService.setLocationAndSize(poly, -4, -5, 12, 12);
	}
	
	public static void addInputAnchor(Resource resource,
			ContainerShape rootContainerShapeForResourceElement,
			 double relativHeight, Diagram dia, IFeatureProvider fp) {
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		IGaService gaService = Graphiti.getGaService();
		
		RoundedRectangle roundedRectangle = null;
		if(rootContainerShapeForResourceElement.getGraphicsAlgorithm() instanceof RoundedRectangle){
			roundedRectangle = (RoundedRectangle) rootContainerShapeForResourceElement.getGraphicsAlgorithm();
		}else{
			for(GraphicsAlgorithm ga : rootContainerShapeForResourceElement.getGraphicsAlgorithm().getGraphicsAlgorithmChildren()){
				if(ga instanceof RoundedRectangle){
					roundedRectangle = (RoundedRectangle) ga;
					break;
				}
			}
		}
		BoxRelativeAnchor boxAnchor = peCreateService.createBoxRelativeAnchor(rootContainerShapeForResourceElement);
		boxAnchor.setUseAnchorLocationAsConnectionEndpoint(true);
		
		boxAnchor.setRelativeWidth(0.0);
		boxAnchor.setRelativeHeight(relativHeight);
		boxAnchor.setReferencedGraphicsAlgorithm(roundedRectangle);
		fp.link(boxAnchor,resource);
		
		Polygon poly = gaService.createPolygon(boxAnchor, AbstractMDBDAShape.INPUTPOLYGON);
		

		poly.setBackground(gaService.manageColor(dia, new ColorConstant("FFEEEE")));
		poly.setForeground(gaService.manageColor(dia, new ColorConstant("880000")));
		poly.setLineWidth(1);
		gaService.setLocationAndSize(poly, -10, -5, 12, 12);
	}
}