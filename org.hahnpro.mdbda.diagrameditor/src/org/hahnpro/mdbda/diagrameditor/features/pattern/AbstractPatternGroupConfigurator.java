package org.hahnpro.mdbda.diagrameditor.features.pattern;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.palette.impl.ObjectCreationToolEntry;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.eclipse.graphiti.palette.impl.StackEntry;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;
import org.hahnpro.mdbda.diagrameditor.features.pattern.dataorganization.DataOrganizationPatternGroupConfigurator;
import org.hahnpro.mdbda.diagrameditor.features.pattern.filtering.FilteringPatternGroupConfigurator;
import org.hahnpro.mdbda.diagrameditor.features.pattern.join.JoinPatternGroupConfigurator;
import org.hahnpro.mdbda.diagrameditor.features.pattern.summatization.SummatizationPatternGroupConfigurator;

public abstract class AbstractPatternGroupConfigurator {
	public static LinkedList<AbstractPatternGroupConfigurator> groupConfigurators = new LinkedList<>();
	
	static{
		groupConfigurators.add(new DataOrganizationPatternGroupConfigurator());
		groupConfigurators.add(new FilteringPatternGroupConfigurator());
		groupConfigurators.add(new JoinPatternGroupConfigurator());
		groupConfigurators.add(new SummatizationPatternGroupConfigurator());
				
	}
	
	public static <T> T getElByClass(Class<T> clazz, T... list){
		for (T object : list) {
			if(clazz.equals(object.getClass())) return object;
		}
		
		return null;
	}
	
	public abstract PaletteCompartmentEntry getPalette(IFeatureProvider iFeatureProvider) ;
	public abstract List<ICreateFeature> getCreateFeatures(IFeatureProvider fp) ;
	public abstract IAddFeature getAddFeature(IAddContext context,IFeatureProvider fp) ;

	public static void addPaletteElementToCompartmentAndLinkCreateFeatureFromCreateFeatureClass(
			PaletteCompartmentEntry compartmentEntry, IFeatureProvider fp, Class<ICreateFeature> clazz) {
				ICreateFeature cf = getElByClass(clazz,	fp.getCreateFeatures());
				ObjectCreationToolEntry objectCreationToolEntry = new ObjectCreationToolEntry(
						cf.getCreateName(), cf.getCreateDescription(),
						cf.getCreateImageId(), cf.getCreateLargeImageId(), cf);
				
				
				compartmentEntry.addToolEntry(objectCreationToolEntry);
			
			}
}
