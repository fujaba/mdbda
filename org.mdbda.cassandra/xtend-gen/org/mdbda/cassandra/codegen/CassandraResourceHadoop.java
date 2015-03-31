package org.mdbda.cassandra.codegen;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.mdbda.codegen.CodegenContext;
import org.mdbda.codegen.IResourceTemplate;
import org.mdbda.codegen.helper.MDBDAConfiguration;
import org.mdbda.model.Resource;
import org.mdbda.model.Task;

@SuppressWarnings("all")
public class CassandraResourceHadoop implements IResourceTemplate {
  @Override
  public CharSequence generareInputResouce(final Resource res, final Task pattern, final CharSequence controledJobName, final CodegenContext context) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    String _configurationString = res.getConfigurationString();
    final MDBDAConfiguration conf = MDBDAConfiguration.readConfigString(_configurationString);
    _builder.append("\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t ");
    context.addImport("org.apache.cassandra.hadoop.ColumnFamilyInputFormat");
    _builder.newLineIfNotEmpty();
    _builder.append("\t ");
    _builder.append(controledJobName, "\t ");
    _builder.append(".getJob().setInputFormatClass(ColumnFamilyInputFormat.class);\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t ");
    context.addImport("org.apache.cassandra.hadoop.ConfigHelper");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("ConfigHelper.setInputColumnFamily(");
    _builder.append(controledJobName, "\t");
    _builder.append(".getJob().getConfiguration(), \"");
    Object _cassandraResourceKeyspace = conf.getCassandraResourceKeyspace();
    _builder.append(_cassandraResourceKeyspace, "\t");
    _builder.append("\", \"");
    Object _cassandraResourceColumnFamily = conf.getCassandraResourceColumnFamily();
    _builder.append(_cassandraResourceColumnFamily, "\t");
    _builder.append("\");\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t ");
    context.addImport("org.apache.cassandra.thrift.SlicePredicate");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("SlicePredicate predicate = new SlicePredicate();");
    _builder.newLine();
    _builder.append("\t ");
    context.addImport("java.nio.ByteBuffer");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("predicate.addToColumn_names(ByteBuffer.wrap(\"");
    Object _cassandraColumnName = conf.getCassandraColumnName();
    _builder.append(_cassandraColumnName, "\t");
    _builder.append("\".getBytes()));\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("ConfigHelper.setInputSlicePredicate(");
    _builder.append(controledJobName, "\t");
    _builder.append(".getJob().getConfiguration(), predicate);");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  @Override
  public CharSequence generareOutputResouce(final Resource res, final CharSequence controledJobName, final CodegenContext context) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t ");
    String _configurationString = res.getConfigurationString();
    final MDBDAConfiguration conf = MDBDAConfiguration.readConfigString(_configurationString);
    _builder.newLineIfNotEmpty();
    _builder.append("\t ");
    context.addImport("org.apache.cassandra.hadoop.ColumnFamilyOutputFormat");
    _builder.newLineIfNotEmpty();
    _builder.append("\t ");
    _builder.append(controledJobName, "\t ");
    _builder.append(".getJob().setOutputFormatClass(ColumnFamilyOutputFormat.class);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t ");
    context.addImport("org.apache.cassandra.hadoop.ConfigHelper");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("ConfigHelper.setOutputColumnFamily(");
    _builder.append(controledJobName, "\t");
    _builder.append(".getJob().getConfiguration(), \"");
    Object _cassandraResourceKeyspace = conf.getCassandraResourceKeyspace();
    _builder.append(_cassandraResourceKeyspace, "\t");
    _builder.append("\" , \"");
    Object _cassandraResourceColumnFamily = conf.getCassandraResourceColumnFamily();
    _builder.append(_cassandraResourceColumnFamily, "\t");
    _builder.append("\");");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  @Override
  public String getCodeStyle() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field HadoopStyle is undefined for the type CassandraResourceHadoop"
      + "\ngetInstance cannot be resolved");
  }
}
