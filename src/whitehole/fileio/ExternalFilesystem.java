package whitehole.fileio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExternalFilesystem implements FilesystemBase {
  private File baseDirectory;
  
  public ExternalFilesystem(String basedir) throws IOException {
    if (basedir.endsWith("/") || basedir.endsWith("\\"))
      basedir = basedir.substring(0, basedir.length() - 1); 
    this.baseDirectory = new File(basedir);
    if (!this.baseDirectory.exists())
      throw new IOException("Directory '" + basedir + "' doesn't exist"); 
    if (!this.baseDirectory.isDirectory())
      throw new IOException(basedir + " isn't a directory"); 
  }
  
  public void save() {}
  
  public void close() {}
  
  public List<String> getDirectories(String directory) {
    directory = directory.substring(1);
    File[] files = (new File(this.baseDirectory, directory)).listFiles();
    List<String> ret = new ArrayList<>();
    for (File file : files) {
      if (file.isDirectory())
        ret.add(file.getName()); 
    } 
    return ret;
  }
  
  public boolean directoryExists(String directory) {
    directory = directory.substring(1);
    File dir = new File(this.baseDirectory, directory);
    return (dir.exists() && dir.isDirectory());
  }
  
  public List<String> getFiles(String directory) {
    directory = directory.substring(1);
    File[] files = (new File(this.baseDirectory, directory)).listFiles();
    List<String> ret = new ArrayList<>();
    for (File file : files) {
      if (file.isFile())
        ret.add(file.getName()); 
    } 
    return ret;
  }
  
  public boolean fileExists(String filename) {
    filename = filename.substring(1);
    File file = new File(this.baseDirectory, filename);
    return (file.exists() && file.isFile());
  }
  
  public FileBase openFile(String filename) throws FileNotFoundException {
    if (!fileExists(filename))
      throw new FileNotFoundException("File " + filename + "doesn't exist"); 
    return new ExternalFile(this.baseDirectory.getPath() + filename);
  }
  
  public void createFile(String parent, String newfile) {
    throw new UnsupportedOperationException("not done lol");
  }
  
  public void renameFile(String file, String newname) {
    throw new UnsupportedOperationException("not done lol");
  }
  
  public void deleteFile(String file) {
    throw new UnsupportedOperationException("not done lol");
  }
}
