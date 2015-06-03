package org.mdbda.codegen

import java.util.HashMap
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.core.runtime.IExtensionRegistry
import org.eclipse.core.runtime.Platform
import org.eclipse.core.runtime.IConfigurationElement
import java.util.HashSet
import org.mdbda.codegen.styles.ICodeGen
import org.mdbda.codegen.styles.storm.StormCodeGen
import org.mdbda.codegen.styles.hadoop.HadoopCodeGen

class MDBDACodegenerator {

	var jobResourcesGeneratedName = new HashMap<String, String>();

	def addJobResource(org.mdbda.model.Resource res, String genName) {
		jobResourcesGeneratedName.put(res.name, genName)
	}

	def getJobResourceGenName(org.mdbda.model.Resource res, String genName) {
		jobResourcesGeneratedName.put(res.name, genName)
	}

	public static def addTemplate(String patternId, ITemplate template) {
		CodeGeneratorRegistry.get.registerCodeGenerator(template.codeStyle, patternId, template)
	}

	val activeCodeStyles = new HashSet<ICodeGen>

	public def init() {

		val IExtensionRegistry reg = Platform.getExtensionRegistry();
		var IConfigurationElement[] elements = reg.getConfigurationElementsFor("org.mdbda.codegen.plugin");

		for (IConfigurationElement cEl : elements) {
			val typeId = cEl.getAttribute("typeId")
			val codeStyle = cEl.getAttribute("CodeStyle")
			val clazzName = cEl.getAttribute("TemplateClass")
			if (clazzName != null) {
				var Class<ITemplate> clazz = null
				try {
					clazz = Platform.getBundle(cEl.getContributor().getName()).loadClass(clazzName) as Class<ITemplate>
				} catch (ClassNotFoundException e) {
					clazz = Platform.getBundle("org.mdbda.codegen").loadClass(clazzName) as Class<ITemplate>
				}
				val template = clazz.newInstance
				if (template.codeStyle.equals(codeStyle)) {
					addTemplate(typeId, template)
				} else {
					// configuration error
					// TODO add logging
					System.err.println("configuration error: " + template );
				}
			} else {
				System.err.println("configuration error: " + cEl);
			}
		}
		activeCodeStyles.add(new StormCodeGen)
		activeCodeStyles.add(new HadoopCodeGen)

//		addPatternTemplate(SummatizationPatternTemplateConstatns.NumericalSummarization, new NumericalSummarizationPattern)
//		addPatternTemplate(SummatizationPatternTemplateConstatns.CustomCalculation, new DefaultPatternTemplate)
	// addPatternTemplate(DataOrganizationPatternTemplateConstatns.Binning, new DefaultPatternTemplate)
//		addPatternTemplate(DataOrganizationPatternTemplateConstatns.Partitioning, new DefaultPatternTemplate)
//		addPatternTemplate(DataOrganizationPatternTemplateConstatns.Shuffling, new DefaultPatternTemplate)
//		addPatternTemplate(DataOrganizationPatternTemplateConstatns.StructuredToHierachical, new DefaultPatternTemplate)
//		addPatternTemplate(DataOrganizationPatternTemplateConstatns.TotalOrderSorting, new DefaultPatternTemplate)
//		addPatternTemplate(FilteringPatternTemplateConstatns.BloomFiltering, new DefaultPatternTemplate)
//		addPatternTemplate(FilteringPatternTemplateConstatns.Distinct, new DefaultPatternTemplate)
//		addPatternTemplate(FilteringPatternTemplateConstatns.TopTen, new DefaultPatternTemplate)
//		addPatternTemplate(JoinPatternTemplateConstatns.CartesianProduct, new MultipleInputTemplate)
//		addPatternTemplate(JoinPatternTemplateConstatns.CompositeJoin, new MultipleInputTemplate)
//		addPatternTemplate(JoinPatternTemplateConstatns.ReduceSideJoin, new MultipleInputTemplate)
//		addPatternTemplate(JoinPatternTemplateConstatns.ReplicatedJoin, new MultipleInputTemplate)
	}

	def doGenerate(Resource emfInputResource, IFileSystemAccess fsa, String codeStyle) {
		activeCodeStyles.forEach[doGenerate(emfInputResource, fsa, codeStyle, this)]
	}

	def genFile(CharSequence content, CodegenContext context) {
		var sb = new StringBuilder();
		if (context.packageName != "") {
			sb.append("package ")
			sb.append(context.packageName)
			sb.append('\n')
			sb.append('\n')
		}

		for (String imp : context.imports) {
			sb.append("import ")
			sb.append(imp)
			sb.append(';')
			sb.append('\n')
		}

		sb.append('\n')
		sb.append(content)

		context.fileSystemAccess.generateFile(context.modelRootName + context.fileSuffix, sb.toString);
	}
}
	