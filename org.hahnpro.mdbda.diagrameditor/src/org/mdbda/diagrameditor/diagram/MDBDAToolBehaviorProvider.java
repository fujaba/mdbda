package org.mdbda.diagrameditor.diagram;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.palette.impl.ObjectCreationToolEntry;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;
import org.mdbda.diagrameditor.features.CreateLinkFeature;
import org.mdbda.diagrameditor.features.CreateWorkflowFeature;

public class MDBDAToolBehaviorProvider extends DefaultToolBehaviorProvider {

	MDBDADiagramTypeProvider diagramTypeProvider;
	public MDBDAToolBehaviorProvider(MDBDADiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
		this.diagramTypeProvider = diagramTypeProvider;
	}

	@Override
	public IPaletteCompartmentEntry[] getPalette() {
		List<IPaletteCompartmentEntry> ret = new ArrayList<IPaletteCompartmentEntry>();

		// // add compartments from super class
		// IPaletteCompartmentEntry[] superCompartments =
		// super.getPalette();
		// for (int i = 0; i < superCompartments.length; i++)
		// ret.add(superCompartments[i]);
		//
		// // add new compartment at the end of the existing compartments
		// PaletteCompartmentEntry compartmentEntry =
		// new PaletteCompartmentEntry("Stacked", null);
		// ret.add(compartmentEntry);
		//
		// // add new stack entry to new compartment
		// StackEntry stackEntry = new StackEntry("EObject", "EObject", null);
		// compartmentEntry.addToolEntry(stackEntry);

		PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry(
				"Main", MDBDAPlugableImageProvider.MAIN_ICON); // TODO iconid

		ret.add(compartmentEntry);
//		for (Class<ICreateFeature> clazz : new Class[] { CreateWorkflowFeature.class, CreateLinkFeature.class }) {
//			AbstractGroupConfigurator
//					.addPaletteElementToCompartmentAndLinkCreateFeatureFromCreateFeatureClass(
//							compartmentEntry, getFeatureProvider(), clazz);
//		}

		for(String categoryGroup : diagramTypeProvider.mapPaletteCategory2CreateFeatureClasses.keySet()){
			
			PaletteCompartmentEntry paletteGroup = new PaletteCompartmentEntry(	categoryGroup, getIconIdForCategory(categoryGroup));
			ret.add(paletteGroup);
			for(Class<ICreateFeature> cfClazz : diagramTypeProvider.mapPaletteCategory2CreateFeatureClasses.get(categoryGroup) ){
				try {
					ICreateFeature cf = cfClazz.getConstructor(IFeatureProvider.class).newInstance(getFeatureProvider());
					ObjectCreationToolEntry objectCreationToolEntry = new ObjectCreationToolEntry(
							cf.getCreateName(), cf.getCreateDescription(),
							cf.getCreateImageId(), cf.getCreateLargeImageId(), cf);
					
					
					paletteGroup.addToolEntry(objectCreationToolEntry);
				} catch (InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}
		
//		for (AbstractGroupConfigurator conf : AbstractGroupConfigurator.groupConfigurators) {
//			ret.add(conf.getPalette(getFeatureProvider()));
//		}

		// // add all create-features to the new stack-entry
		// IFeatureProvider featureProvider = getFeatureProvider();
		// ICreateFeature[] createFeatures =
		// featureProvider.getCreateFeatures();
		//
		// for (ICreateFeature cf : createFeatures) {
		// ObjectCreationToolEntry objectCreationToolEntry =
		// new ObjectCreationToolEntry(cf.getCreateName(),
		// cf.getCreateDescription(), cf.getCreateImageId(),
		// cf.getCreateLargeImageId(), cf);
		// stackEntry.addCreationToolEntry(objectCreationToolEntry);
		// }
		//
		// // add all create-connection-features to the new stack-entry
		// ICreateConnectionFeature[] createConnectionFeatures =
		// featureProvider.getCreateConnectionFeatures();
		// for (ICreateConnectionFeature cf : createConnectionFeatures) {
		// ConnectionCreationToolEntry connectionCreationToolEntry =
		// new ConnectionCreationToolEntry(cf.getCreateName(), cf
		// .getCreateDescription(), cf.getCreateImageId(),
		// cf.getCreateLargeImageId());
		// connectionCreationToolEntry.addCreateConnectionFeature(cf);
		// stackEntry.addCreationToolEntry(connectionCreationToolEntry);
		// }

		return ret.toArray(new IPaletteCompartmentEntry[ret.size()]);
	}

	private String getIconIdForCategory(String categoryGroup) {
		switch (categoryGroup) {
		case "Filter":
			return  MDBDAPlugableImageProvider.FILTER_ICON;
		case "Join":
			return  MDBDAPlugableImageProvider.JOIN_ICON;
		case "Resources":
			return  MDBDAPlugableImageProvider.RESSOURCE_ICON;
		case "Summarization":
			return  MDBDAPlugableImageProvider.SUMMARIZATION_ICON;
		case "Dataorganization":
			return  MDBDAPlugableImageProvider.STRUCTURE_ICON;

		default:
			return  MDBDAPlugableImageProvider.MAIN_ICON;
		}
		
	}

}
