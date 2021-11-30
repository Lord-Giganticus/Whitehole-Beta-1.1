package whitehole.fileio;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

public class ExternalFile implements FileBase {
  protected RandomAccessFile file;
  
  private boolean bigEndian;
  
  public ExternalFile(String path) throws FileNotFoundException {
    this.file = new RandomAccessFile(path, "rw");
    this.bigEndian = false;
  }
  
  public void save() throws IOException {
    this.file.getChannel().force(true);
  }
  
  public void close() throws IOException {
    this.file.close();
  }
  
  public void releaseStorage() {}
  
  public void setBigEndian(boolean bigendian) {
    this.bigEndian = bigendian;
  }
  
  public long getLength() throws IOException {
    return this.file.length();
  }
  
  public void setLength(long length) throws IOException {
    this.file.setLength(length);
  }
  
  public long position() throws IOException {
    return this.file.getFilePointer();
  }
  
  public void position(long newpos) throws IOException {
    this.file.seek(newpos);
  }
  
  public void skip(long nbytes) throws IOException {
    this.file.seek(this.file.getFilePointer() + nbytes);
  }
  
  public byte readByte() throws IOException {
    try {
      return this.file.readByte();
    } catch (EOFException ex) {
      return 0;
    } 
  }
  
  public short readShort() throws IOException {
    try {
      short ret = this.file.readShort();
      if (!this.bigEndian)
        ret = (short)((ret & 0xFF00) >>> 8 | (ret & 0xFF) << 8); 
      return ret;
    } catch (EOFException ex) {
      return 0;
    } 
  }
  
  public int readInt() throws IOException {
    try {
      int ret = this.file.readInt();
      if (!this.bigEndian)
        ret = (ret & 0xFF000000) >>> 24 | (ret & 0xFF0000) >>> 8 | (ret & 0xFF00) << 8 | (ret & 0xFF) << 24; 
      return ret;
    } catch (EOFException ex) {
      return 0;
    } 
  }
  
  public float readFloat() throws IOException {
    return Float.intBitsToFloat(readInt());
  }
  
  public String readString(String encoding, int length) throws IOException {
    if (!Charset.isSupported(encoding))
      encoding = "ASCII"; 
    Charset charset = Charset.forName(encoding);
    CharsetDecoder dec = charset.newDecoder();
    ByteBuffer bin = ByteBuffer.allocate(8);
    CharBuffer bout = CharBuffer.allocate(1);
    String ret = "";
    for (int i = 0; length == 0 || i < length; i++) {
      for (int j = 0; j < 8; j++) {
        try {
          bin.put(this.file.readByte());
        } catch (EOFException ex) {
          bin.put((byte)0);
        } 
      } 
      bin.rewind();
      CoderResult res = dec.decode(bin, bout, false);
      if (res == CoderResult.UNDERFLOW)
        break; 
      if (res != CoderResult.OVERFLOW)
        throw new IOException("Error while reading string: " + res); 
      skip(-bin.remaining());
      char ch = bout.get(0);
      if (ch == '\000')
        break; 
      bin.clear();
      bout.clear();
      ret = ret + ch;
    } 
    return ret;
  }
  
  public byte[] readBytes(int length) throws IOException {
    byte[] ret = new byte[length];
    this.file.read(ret);
    return ret;
  }
  
  public void writeByte(byte val) throws IOException {
    this.file.writeByte(val);
  }
  
  public void writeShort(short val) throws IOException {
    if (!this.bigEndian)
      val = (short)((val & 0xFF00) >>> 8 | (val & 0xFF) << 8); 
    this.file.writeShort(val);
  }
  
  public void writeInt(int val) throws IOException {
    if (!this.bigEndian)
      val = (val & 0xFF000000) >>> 24 | (val & 0xFF0000) >>> 8 | (val & 0xFF00) << 8 | (val & 0xFF) << 24; 
    this.file.writeInt(val);
  }
  
  public void writeFloat(float val) throws IOException {
    writeInt(Float.floatToIntBits(val));
  }
  
  public int writeString(String encoding, String val, int length) throws IOException {
    if (!Charset.isSupported(encoding))
      encoding = "ASCII"; 
    Charset charset = Charset.forName(encoding);
    CharsetEncoder enc = charset.newEncoder();
    CharBuffer bin = CharBuffer.allocate(1);
    ByteBuffer bout = ByteBuffer.allocate(8);
    int len = 0;
    for (int i = 0; i < ((length > 0) ? length : val.length()); i++) {
      bin.put(val.charAt(i));
      bin.rewind();
      CoderResult res = enc.encode(bin, bout, false);
      if (res != CoderResult.UNDERFLOW)
        throw new IOException("Error while writing string"); 
      int bytesize = bout.position();
      len += bytesize;
      for (int j = 0; j < bytesize; j++)
        this.file.writeByte(bout.get(j)); 
      bin.clear();
      bout.clear();
    } 
    if (length == 0 || val.length() < length) {
      bin.put(Boolean.toString(false));
      bin.rewind();
      CoderResult res = enc.encode(bin, bout, false);
      if (res != CoderResult.UNDERFLOW)
        throw new IOException("Error while writing string"); 
      int bytesize = bout.position();
      len += bytesize;
      for (int j = 0; j < bytesize; j++)
        this.file.writeByte(bout.get(j)); 
    } 
    return len;
  }
  
  public void writeBytes(byte[] stuff) throws IOException {
    this.file.write(stuff);
  }
  
  public byte[] getContents() throws IOException {
    byte[] ret = new byte[(int)this.file.length()];
    long oldpos = this.file.getFilePointer();
    this.file.seek(0L);
    this.file.read(ret);
    this.file.seek(oldpos);
    return ret;
  }
  
  public void setContents(byte[] buf) throws IOException {
    long oldpos = this.file.getFilePointer();
    this.file.setLength(buf.length);
    this.file.seek(0L);
    this.file.write(buf);
    this.file.seek(oldpos);
  }
}