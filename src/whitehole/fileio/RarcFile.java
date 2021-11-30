package whitehole.fileio;

import java.io.IOException;

public class RarcFile extends MemoryFile {
  public RarcFilesystem filesystem;
  
  public String fileName;
  
  public RarcFile(RarcFilesystem fs, String fullname) throws IOException {
    super(fs.getFileContents(fullname));
    this.filesystem = fs;
    this.fileName = fullname;
  }
  
  public void save() throws IOException {
    this.filesystem.reinsertFile(this);
  }
}
