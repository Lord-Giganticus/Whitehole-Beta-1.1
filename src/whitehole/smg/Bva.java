package whitehole.smg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import whitehole.fileio.FileBase;

public class Bva {
  private FileBase file;
  
  public List<List<Boolean>> animData;
  
  public Bva(FileBase file) throws IOException {
    this.file = file;
    this.file.setBigEndian(true);
    readData();
  }
  
  public void save() throws IOException {
    this.file.save();
  }
  
  public void close() throws IOException {
    this.file.close();
  }
  
  private void readData() throws IOException {
    this.file.position(44L);
    short nbatches = this.file.readShort();
    this.file.skip(2L);
    int sec1offset = 32 + this.file.readInt();
    int sec2offset = 32 + this.file.readInt();
    this.animData = new ArrayList<>(nbatches);
    for (int b = 0; b < nbatches; b++) {
      this.file.position((sec1offset + b * 4));
      short bsize = this.file.readShort();
      short bstart = this.file.readShort();
      List<Boolean> thislist = new ArrayList<>(bsize);
      this.animData.add(thislist);
      this.file.position((sec2offset + bstart));
      for (int i = 0; i < bsize; i++) {
        byte val = this.file.readByte();
        thislist.add(Boolean.valueOf((val == 1)));
      } 
    } 
  }
}
