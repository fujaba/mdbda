package org.mdbda.diagrameditor.diagram;

import java.util.ArrayList;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;
import org.mdbda.model.Workflow;
import org.mdbda.diagrameditor.features.AbstractGroupConfigurator;
import org.mdbda.diagrameditor.features.AddLinkFeature;
import org.mdbda.diagrameditor.features.AddWorkflowFeature;
import org.mdbda.diagrameditor.features.CreateLinkFeature;
import org.mdbda.diagrameditor.features.CreateWorkflowFeature;
import org.mdbda.diagrameditor.features.LayoutDomainObjectFeature;
import org.mdbda.diagrameditor.features.pattern.dataorganization.AddBinningFeature;
import org.mdbda.diagrameditor.features.pattern.dataorganization.AddPartitoningFeature;
import org.mdbda.diagrameditor.features.pattern.dataorganization.AddShufflingFeature;
import org.mdbda.diagrameditor.features.pattern.dataorganization.AddStructuredToHierachicalFeature;
import org.mdbda.diagrameditor.features.pattern.dataorganization.AddTotalOrderSortingFeature;
import org.mdbda.diagrameditor.features.pattern.dataorganization.CreateBinningFeature;
import org.mdbda.diagrameditor.features.pattern.dataorganization.CreatePartitoningFeature;
import org.mdbda.diagrameditor.features.pattern.dataorganization.CreateShufflingFeature;
import org.mdbda.diagrameditor.features.pattern.dataorganization.CreateStructuredToHierachicalFeature;
import org.mdbda.diagrameditor.features.pattern.dataorganization.CreateTotalOrderSortingFeature;
import org.mdbda.diagrameditor.utils.DiagramUtils;


public class MDBDAFeatureProvider extends DefaultFeatureProvider {

	public MDBDAFeatureProvider(IDiagramTypeProvider dtp) {
		super(dtp);
	}

	@Override
	public ICreateFeature[] getCreateFeatures() {
		ArrayList<ICreateFeature> cf = new ArrayList();
		cf.add( new CreateWorkflowFeature(this));
		
		for(AbstractGroupConfigurator conf : AbstractGroupConfigurator.groupConfigurators){
			cf.addAll(conf.getCreateFeatures(this));
		}
		
		return (ICreateFeature[]) cf.toArray(new ICreateFeature[cf.size()]) ;
	}
	
	@Override
	public ICreateConnectionFeature[] getCreateConnectionFeatures() {
		return new ICreateConnectionFeature[] {new CreateLinkFeature(this)};
	}
	
	@Override
	public IFeature[] getDragAndDropFeatures(IPictogramElementContext context) {
		return getCreateConnectionFeatures();
	}
	
	@Override
	public IAddFeature getAddFeature(IAddContext context) {
		// TODO: check for right domain object instances below
		if (context instanceof IAddConnectionContext /* && context.getNewObject() instanceof <DomainObject> */) {
			return new AddLinkFeature(this);
		} else if (context instanceof IAddContext  && (context.getNewObject() instanceof Workflow || (context.getNewObject() instanceof Diagram && DiagramUtils.getMDBDADiagram((Diagram) context.getNewObject()).getRootWorkflow() != null ) )) {
			return new AddWorkflowFeature(this);
		} 
		
		for(AbstractGroupConfigurator conf : AbstractGroupConfigurator.groupConfigurators){
			IAddFeature af = conf.getAddFeature(context, this);
			if(af != null) return af;
		}

		return super.getAddFeature(context);
	}
	
	@Override
	public ILayoutFeature getLayoutFeature(ILayoutContext context) {
		// TODO: check for right domain object instances below
		if (context.getPictogramElement() instanceof ContainerShape /* && getBusinessObjectForPictogramElement(context.getPictogramElement()) instanceof <DomainObject> */) {
			return  new LayoutDomainObjectFeature(this);
		}
	
		return super.getLayoutFeature(context);
	}
}
