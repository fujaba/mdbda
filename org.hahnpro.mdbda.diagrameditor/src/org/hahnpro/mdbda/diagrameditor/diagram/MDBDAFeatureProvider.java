package org.hahnpro.mdbda.diagrameditor.diagram;

import java.util.ArrayList;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;
import org.hahnpro.mdbda.diagrameditor.features.AddDomainObjectConnectionConnectionFeature;
import org.hahnpro.mdbda.diagrameditor.features.AddWorkflowFeature;
import org.hahnpro.mdbda.diagrameditor.features.CreateDomainObjectConnectionConnectionFeature;
import org.hahnpro.mdbda.diagrameditor.features.CreateWorkflowFeature;
import org.hahnpro.mdbda.diagrameditor.features.LayoutDomainObjectFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.AbstractPatternGroupConfigurator;
import org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization.AddBinningFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization.AddPartitoningFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization.AddShufflingFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization.AddStructuredToHierachicalFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization.AddTotalOrderSortingFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization.CreateBinningFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization.CreatePartitoningFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization.CreateShufflingFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization.CreateStructuredToHierachicalFeature;
import org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization.CreateTotalOrderSortingFeature;
import org.hahnpro.mdbda.model.pattern.dataorganization.Binning;
import org.hahnpro.mdbda.model.pattern.dataorganization.Partitioning;
import org.hahnpro.mdbda.model.pattern.dataorganization.Shuffling;
import org.hahnpro.mdbda.model.pattern.dataorganization.StructuredToHierachical;
import org.hahnpro.mdbda.model.pattern.dataorganization.TotalOrderSorting;
import org.hahnpro.mdbda.model.workflow.Workflow;


public class MDBDAFeatureProvider extends DefaultFeatureProvider {

	public MDBDAFeatureProvider(IDiagramTypeProvider dtp) {
		super(dtp);
	}

	@Override
	public ICreateFeature[] getCreateFeatures() {
		ArrayList<ICreateFeature> cf = new ArrayList();
		cf.add( new CreateWorkflowFeature(this));
		
		for(AbstractPatternGroupConfigurator conf : AbstractPatternGroupConfigurator.groupConfigurators){
			cf.addAll(conf.getCreateFeatures(this));
		}
		
		return (ICreateFeature[]) cf.toArray(new ICreateFeature[cf.size()]) ;
	}
	
	@Override
	public ICreateConnectionFeature[] getCreateConnectionFeatures() {
		return new ICreateConnectionFeature[] {new CreateDomainObjectConnectionConnectionFeature(this)};
	}
	
	@Override
	public IAddFeature getAddFeature(IAddContext context) {
		// TODO: check for right domain object instances below
		if (context instanceof IAddConnectionContext /* && context.getNewObject() instanceof <DomainObject> */) {
			return new AddDomainObjectConnectionConnectionFeature(this);
		} else if (context instanceof IAddContext  && context.getNewObject() instanceof Workflow ) {
			return new AddWorkflowFeature(this);
		} 
		
		for(AbstractPatternGroupConfigurator conf : AbstractPatternGroupConfigurator.groupConfigurators){
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
