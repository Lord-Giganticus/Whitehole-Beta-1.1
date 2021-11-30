package whitehole.smg;

import java.io.IOException;
import whitehole.fileio.FileBase;

public class Bti {
  private FileBase file;
  
  public byte format;
  
  public short width;
  
  public short height;
  
  public byte wrapS;
  
  public byte wrapT;
  
  public byte paletteFormat;
  
  public byte minFilter;
  
  public byte magFilter;
  
  public byte mipmapCount;
  
  public byte[][] image;
  
  public Bti(FileBase _file) throws IOException {
    this.file = _file;
    this.file.setBigEndian(true);
    readTexture();
  }
  
  public void save() throws IOException {
    this.file.save();
  }
  
  public void close() throws IOException {
    this.file.close();
  }
  
  private void readTexture() throws IOException {
    this.format = this.file.readByte();
    this.file.skip(1L);
    this.width = this.file.readShort();
    this.height = this.file.readShort();
    this.wrapS = this.file.readByte();
    this.wrapT = this.file.readByte();
    this.file.skip(1L);
    this.paletteFormat = this.file.readByte();
    short palnumentries = this.file.readShort();
    int paloffset = this.file.readInt();
    this.file.skip(4L);
    this.minFilter = this.file.readByte();
    this.magFilter = this.file.readByte();
    this.file.skip(2L);
    this.mipmapCount = this.file.readByte();
    this.file.skip(3L);
    int dataoffset = this.file.readInt();
    this.image = DataHelper.decodeTextureData(this.file, dataoffset, this.mipmapCount, this.format, this.width, this.height);
  }
}
