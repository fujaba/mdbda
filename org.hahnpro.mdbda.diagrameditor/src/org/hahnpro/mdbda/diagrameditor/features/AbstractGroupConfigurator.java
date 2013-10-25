package org.hahnpro.mdbda.diagrameditor.features;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.palette.impl.ConnectionCreationToolEntry;
import org.eclipse.graphiti.palette.impl.ObjectCreationToolEntry;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.eclipse.graphiti.palette.impl.StackEntry;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;
import org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization.DataOrganizationPatternGroupConfigurator;
import org.hahnpro.mdbda.diagrameditor.features.pattern.filtering.FilteringPatternGroupConfigurator;
import org.hahnpro.mdbda.diagrameditor.features.pattern.join.JoinPatternGroupConfigurator;
import org.hahnpro.mdbda.diagrameditor.features.pattern.summatization.SummatizationPatternGroupConfigurator;
import org.hahnpro.mdbda.diagrameditor.features.resources.ResourceGroupConfigurator;

public abstract class AbstractGroupConfigurator {
	public static LinkedList<AbstractGroupConfigurator> groupConfigurators = new LinkedList<>();
	
	static{
		groupConfigurators.add(new DataOrganizationPatternGroupConfigurator());
		groupConfigurators.add(new FilteringPatternGroupConfigurator());
		groupConfigurators.add(new JoinPatternGroupConfigurator());
		groupConfigurators.add(new SummatizationPatternGroupConfigurator());
		groupConfigurators.add(new ResourceGroupConfigurator());
				
	}
	
	public static <T> T getElByClass(Class<?> clazz, T... list){
		for (T object : list) {
			if(clazz.equals(object.getClass())) return object;
		}
		
		return null;
	}
	
	public abstract PaletteCompartmentEntry getPalette(IFeatureProvider iFeatureProvider) ;
	public abstract List<ICreateFeature> getCreateFeatures(IFeatureProvider fp) ;
	public abstract IAddFeature getAddFeature(IAddContext context,IFeatureProvider fp) ;

	public static void addPaletteElementToCompartmentAndLinkCreateFeatureFromCreateFeatureClass(
			PaletteCompartmentEntry compartmentEntry, IFeatureProvider fp, Class<?> clazz) {
					ICreateFeature cf = getElByClass(clazz,	fp.getCreateFeatures());
					if(cf != null){
					ObjectCreationToolEntry objectCreationToolEntry = new ObjectCreationToolEntry(
							cf.getCreateName(), cf.getCreateDescription(),
							cf.getCreateImageId(), cf.getCreateLargeImageId(), cf);

					compartmentEntry.addToolEntry(objectCreationToolEntry);
					}else{
						
						ICreateConnectionFeature ccf = getElByClass(clazz,	fp.getCreateConnectionFeatures());
						ConnectionCreationToolEntry connectionCreationToolEntry = new ConnectionCreationToolEntry(ccf.getName(), ccf.getDescription(),
								ccf.getCreateImageId(), ccf.getCreateLargeImageId());

						compartmentEntry.addToolEntry(connectionCreationToolEntry);
					}
					
					
				
				
			
			}
}
