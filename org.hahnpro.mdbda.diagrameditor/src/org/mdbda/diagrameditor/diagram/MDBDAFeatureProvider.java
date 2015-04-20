package org.mdbda.diagrameditor.diagram;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;
import org.mdbda.model.RemoteWorkflow;
import org.mdbda.model.Task;
import org.mdbda.model.Resource;
import org.mdbda.model.Workflow;
import org.mdbda.diagrameditor.features.AddLinkFeature;
import org.mdbda.diagrameditor.features.AddWorkflowFeature;
import org.mdbda.diagrameditor.features.CreateLinkFeature;
import org.mdbda.diagrameditor.features.CreateWorkflowFeature;
import org.mdbda.diagrameditor.features.RemoteWorkflowDrillDownFeature;
import org.mdbda.diagrameditor.features.ResizeWorkflowFeature;
import org.mdbda.diagrameditor.features.UpdateWorkflowFeature;
import org.mdbda.diagrameditor.features.resources.DirectEditingResourceFeature;
import org.mdbda.diagrameditor.features.resources.ResizeResourceFeature;
import org.mdbda.diagrameditor.features.resources.ResourceDataDSLDrillDownFeature;
import org.mdbda.diagrameditor.features.resources.UpdateResourceFeature;
import org.mdbda.diagrameditor.features.task.DirectEditingTaskFeature;
import org.mdbda.diagrameditor.features.task.ResizeTaskFeature;
import org.mdbda.diagrameditor.features.task.UpdateTaskFeature;
import org.mdbda.diagrameditor.features.updateFeatures.UpdateConnectionDataformatDecorationFeature;
import org.mdbda.diagrameditor.utils.DiagramUtils;
import org.mdbda.diagrameditor.utils.LayoutDomainObjectFeature;


public class MDBDAFeatureProvider extends DefaultFeatureProvider {
	MDBDADiagramTypeProvider diagramTypeProvider;
	
	public MDBDAFeatureProvider(MDBDADiagramTypeProvider dtp) {
		super(dtp);
		diagramTypeProvider = dtp;
	}

	@Override
	public ICreateFeature[] getCreateFeatures() {
		ArrayList<ICreateFeature> cf = new ArrayList();
		cf.add( new CreateWorkflowFeature(this));
		
		for(String groupKey : diagramTypeProvider.mapPaletteCategory2CreateFeatureClasses.keySet()){
			
			HashSet<Class<ICreateFeature>> cFeatureClazzes = diagramTypeProvider.mapPaletteCategory2CreateFeatureClasses.get(groupKey);
			
			for(Class<ICreateFeature> fClazz : cFeatureClazzes){
				try {
					cf.add(fClazz.getConstructor(IFeatureProvider.class).newInstance(this));
				} catch (InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
//		
//		for(AbstractGroupConfigurator conf : AbstractGroupConfigurator.groupConfigurators){
//			cf.addAll(conf.getCreateFeatures(this));
//		}
				
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
		} else if (context instanceof IAddContext  && (context.getNewObject() instanceof Workflow || context.getNewObject() instanceof RemoteWorkflow || (context.getNewObject() instanceof Diagram && DiagramUtils.getMDBDAModelRoot((Diagram) context.getNewObject()).getRootWorkflow() != null ) )) {
			return new AddWorkflowFeature(this);
		} 
		
//		for(AbstractGroupConfigurator conf : AbstractGroupConfigurator.groupConfigurators){
//			IAddFeature af = conf.getAddFeature(context, this);
//			if(af != null) return af;
//		}
		
		if(context instanceof IAddContext && context.getNewObject() instanceof Resource){
			Resource res = (Resource) context.getNewObject();
			Class<IAddFeature> addClazz = diagramTypeProvider.resourceTypeIdAddFreature.get(res.getTypeId());
			
			try {
				return addClazz.getConstructor(IFeatureProvider.class).newInstance(this);
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	@Override
	public IResizeShapeFeature getResizeShapeFeature(IResizeShapeContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getPictogramElement());
		if(bo instanceof Workflow  || bo instanceof RemoteWorkflow){
			return new ResizeWorkflowFeature(this);
		}else if(bo instanceof Task){
			return new ResizeTaskFeature(this);
		}else if(bo instanceof Resource){
			return new ResizeResourceFeature(this);
		}
		return super.getResizeShapeFeature(context);
	}
	
	@Override
	public IUpdateFeature getUpdateFeature(IUpdateContext context) {
		  PictogramElement pictogramElement = context.getPictogramElement();
		   if (pictogramElement  instanceof ContainerShape) {
		       Object bo = getBusinessObjectForPictogramElement(pictogramElement);
		       if (bo instanceof Workflow || bo instanceof RemoteWorkflow) {
		           return new UpdateWorkflowFeature(this); 
		       }else if (bo instanceof Task) {
		           return new UpdateTaskFeature(this);
		       }else if (bo instanceof Resource) {
		           return new UpdateResourceFeature(this);
		       }
		   }else if(pictogramElement instanceof Connection) {
	           return new UpdateConnectionDataformatDecorationFeature(this);
		   }
		return super.getUpdateFeature(context);
	}
	
	@Override
	public IDirectEditingFeature getDirectEditingFeature(
			IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
        Object bo = getBusinessObjectForPictogramElement(pe);
        
        if(bo instanceof Task){
        	return new DirectEditingTaskFeature(this);
        }else if(bo instanceof Resource){
        	return new DirectEditingResourceFeature(this);
        }
		return super.getDirectEditingFeature(context);
	}
	
	
	
	@Override
	public ICustomFeature[] getCustomFeatures(ICustomContext context) {
		return new ICustomFeature[]{new RemoteWorkflowDrillDownFeature(this), new ResourceDataDSLDrillDownFeature(this)	};
	}
	
	
}
