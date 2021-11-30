package whitehole.fileio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface FilesystemBase {
  void save() throws IOException;
  
  void close() throws IOException;
  
  List<String> getDirectories(String paramString);
  
  boolean directoryExists(String paramString);
  
  List<String> getFiles(String paramString);
  
  boolean fileExists(String paramString);
  
  FileBase openFile(String paramString) throws FileNotFoundException;
  
  void createFile(String paramString1, String paramString2);
  
  void renameFile(String paramString1, String paramString2);
  
  void deleteFile(String paramString);
}