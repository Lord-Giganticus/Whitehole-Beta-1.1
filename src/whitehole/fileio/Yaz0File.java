package whitehole.fileio;

import java.io.IOException;

public class Yaz0File extends MemoryFile {
  protected FileBase backend;
  
  public Yaz0File(FileBase backendfile) throws IOException {
    super(backendfile.getContents());
    this.buffer = Yaz0.decompress(this.buffer);
    this.logicalSize = this.buffer.length;
    this.backend = backendfile;
    this.backend.releaseStorage();
  }
  
  public void save() throws IOException {
    byte[] compbuffer = this.buffer;
    if (this.backend != null) {
      this.backend.setContents(compbuffer);
      this.backend.save();
      this.backend.releaseStorage();
    } 
  }
  
  public void close() throws IOException {
    if (this.backend != null)
      this.backend.close(); 
  }
}