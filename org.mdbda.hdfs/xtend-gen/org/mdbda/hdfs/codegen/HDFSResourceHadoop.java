package org.mdbda.hdfs.codegen;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.mdbda.codegen.AbstractResourceTemplate;
import org.mdbda.codegen.CodegenContext;
import org.mdbda.codegen.helper.CodeGenHelper;
import org.mdbda.codegen.helper.MDBDAConfiguration;
import org.mdbda.model.Resource;
import org.mdbda.model.Task;

@SuppressWarnings("all")
public class HDFSResourceHadoop extends AbstractResourceTemplate {
  @Override
  public String getCodeStyle() {
    return "Hadoop";
  }
  
  @Override
  public CharSequence generareInputResouce(final Resource res, final Task pattern, final CharSequence controledJobName, final CodegenContext context) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    context.addImport("org.apache.hadoop.fs.Path");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("Path inputPath = new Path(\"");
    String _configurationString = res.getConfigurationString();
    MDBDAConfiguration _readConfigString = MDBDAConfiguration.readConfigString(_configurationString);
    Object _hDFSPath = _readConfigString.getHDFSPath();
    _builder.append(_hDFSPath, "\t");
    _builder.append("\");");
    _builder.newLineIfNotEmpty();
    {
      EList<Resource> _inputResources = pattern.getInputResources();
      int _size = _inputResources.size();
      boolean _greaterThan = (_size > 1);
      if (_greaterThan) {
        _builder.append("\t");
        _builder.append("//MULTI INPUT");
        _builder.newLine();
        _builder.append("\t");
        context.addImport("org.apache.hadoop.mapreduce.lib.input.TextInputFormat");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        context.addImport("org.apache.hadoop.mapreduce.lib.input.MultipleInputs");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        String _string = pattern.toString();
        String _plus = ("MULTIINPUT" + _string);
        Long fooCount = context.getCounter(_plus);
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        String _string_1 = pattern.toString();
        String _plus_1 = ("MULTIINPUT" + _string_1);
        context.setCounter(_plus_1, Long.valueOf(((fooCount).longValue() + 1)));
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        String _mapReduceClassNameFromPattern = CodeGenHelper.getMapReduceClassNameFromPattern(pattern);
        String _plus_2 = (_mapReduceClassNameFromPattern + ".");
        String _mapperInnderClassName = CodeGenHelper.getMapperInnderClassName(pattern);
        String _plus_3 = (_plus_2 + _mapperInnderClassName);
        final String mapperClass = (_plus_3 + fooCount);
        _builder.append(" ");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("MultipleInputs.addInputPath(");
        _builder.append(controledJobName, "\t");
        _builder.append(".getJob(),inputPath,TextInputFormat.class,");
        _builder.append(mapperClass, "\t");
        _builder.append(".class);");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("\t");
        context.addImport("org.apache.hadoop.mapreduce.lib.input.TextInputFormat");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append(controledJobName, "\t");
        _builder.append(".getJob().setInputFormatClass(TextInputFormat.class);");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("TextInputFormat.setInputPaths(");
        _builder.append(controledJobName, "\t");
        _builder.append(".getJob(), inputPath);");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  @Override
  public CharSequence generareOutputResouce(final Resource res, final CharSequence controledJobName, final CodegenContext context) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    context.addImport("org.apache.hadoop.fs.Path");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("Path outputPath = new Path(\"");
    String _configurationString = res.getConfigurationString();
    MDBDAConfiguration _readConfigString = MDBDAConfiguration.readConfigString(_configurationString);
    Object _hDFSPath = _readConfigString.getHDFSPath();
    _builder.append(_hDFSPath, "\t");
    _builder.append("\");");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    context.addImport("org.apache.hadoop.mapreduce.lib.output.TextOutputFormat");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append(controledJobName, "\t");
    _builder.append(".getJob().setOutputFormatClass(TextOutputFormat.class);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("TextOutputFormat.setOutputPath(");
    _builder.append(controledJobName, "\t");
    _builder.append(".getJob(), outputPath);");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
