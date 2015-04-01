package org.mdbda.hdfs.codegen;

import org.mdbda.codegen.IResourceTemplate;
import org.mdbda.model.Resource;
import org.mdbda.model.Task;

@SuppressWarnings("all")
public class HDFSResourceHadoop implements IResourceTemplate {
  public String getCodeStyle() {
    return "Hadoop";
  }
  
  public CharSequence generareInputResouce(final Resource res, final Task pattern, final CharSequence controledJobName, final /* CodegenContext */Object context) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field MDBDAConfiguration is undefined for the type HDFSResourceHadoop"
      + "\naddImport cannot be resolved"
      + "\nreadConfigString cannot be resolved"
      + "\ngetHDFSPath cannot be resolved"
      + "\naddImport cannot be resolved"
      + "\naddImport cannot be resolved"
      + "\ngetCounter cannot be resolved"
      + "\nsetCounter cannot be resolved"
      + "\n+ cannot be resolved"
      + "\naddImport cannot be resolved");
  }
  
  public CharSequence generareOutputResouce(final Resource res, final CharSequence controledJobName, final /* CodegenContext */Object context) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field MDBDAConfiguration is undefined for the type HDFSResourceHadoop"
      + "\naddImport cannot be resolved"
      + "\nreadConfigString cannot be resolved"
      + "\ngetHDFSPath cannot be resolved"
      + "\naddImport cannot be resolved");
  }
}
