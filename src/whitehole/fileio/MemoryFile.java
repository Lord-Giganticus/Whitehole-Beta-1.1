package whitehole.fileio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

public class MemoryFile implements FileBase {
  protected byte[] buffer;
  
  private int curPosition;
  
  private boolean bigEndian;
  
  protected int logicalSize;
  
  public MemoryFile(byte[] buf) throws IOException {
    this.buffer = buf;
    this.curPosition = 0;
    this.bigEndian = false;
    this.logicalSize = this.buffer.length;
  }
  
  public void save() throws IOException {}
  
  public void close() throws IOException {}
  
  public void releaseStorage() {
    this.buffer = null;
  }
  
  public void setBigEndian(boolean bigendian) {
    this.bigEndian = bigendian;
  }
  
  public long getLength() throws IOException {
    return this.logicalSize;
  }
  
  public void setLength(long length) throws IOException {
    if (length >= 2147483648L)
      throw new IOException("hey calm down, you're gonna eat all the RAM"); 
    resizeBuffer((int)length);
    this.logicalSize = (int)length;
  }
  
  public long position() throws IOException {
    return this.curPosition;
  }
  
  public void position(long newpos) throws IOException {
    if (newpos >= 2147483648L)
      throw new IOException("hey calm down, you're gonna eat all the RAM"); 
    this.curPosition = (int)newpos;
  }
  
  public void skip(long nbytes) throws IOException {
    this.curPosition = (int)(this.curPosition + nbytes);
    if (this.curPosition >= 2147483648L)
      throw new IOException("hey calm down, you're gonna eat all the RAM"); 
  }
  
  public byte readByte() throws IOException {
    if (this.curPosition + 1 > this.logicalSize)
      return 0; 
    return this.buffer[this.curPosition++];
  }
  
  public short readShort() throws IOException {
    if (this.curPosition + 2 > this.logicalSize)
      return 0; 
    if (this.bigEndian)
      return (short)((this.buffer[this.curPosition++] & 0xFF) << 8 | this.buffer[this.curPosition++] & 0xFF); 
    return (short)(this.buffer[this.curPosition++] & 0xFF | (this.buffer[this.curPosition++] & 0xFF) << 8);
  }
  
  public int readInt() throws IOException {
    if (this.curPosition + 4 > this.logicalSize)
      return 0; 
    if (this.bigEndian)
      return (this.buffer[this.curPosition++] & 0xFF) << 24 | (this.buffer[this.curPosition++] & 0xFF) << 16 | (this.buffer[this.curPosition++] & 0xFF) << 8 | this.buffer[this.curPosition++] & 0xFF; 
    return this.buffer[this.curPosition++] & 0xFF | (this.buffer[this.curPosition++] & 0xFF) << 8 | (this.buffer[this.curPosition++] & 0xFF) << 16 | (this.buffer[this.curPosition++] & 0xFF) << 24;
  }
  
  public float readFloat() throws IOException {
    return Float.intBitsToFloat(readInt());
  }
  
  public String readString(String encoding, int length) throws IOException {
    if (!Charset.isSupported(encoding))
      encoding = "ASCII"; 
    Charset charset = Charset.forName(encoding);
    CharsetDecoder dec = charset.newDecoder();
    ByteBuffer bin = ByteBuffer.wrap(this.buffer);
    CharBuffer bout = CharBuffer.allocate(1);
    String ret = "";
    bin.position(this.curPosition);
    for (int i = 0; length == 0 || i < length; i++) {
      CoderResult res = dec.decode(bin, bout, false);
      if (res != CoderResult.OVERFLOW)
        break; 
      char ch = bout.get(0);
      if (ch == '\000')
        break; 
      bout.clear();
      ret = ret + ch;
    } 
    return ret;
  }
  
  public byte[] readBytes(int length) throws IOException {
    byte[] ret = new byte[length];
    for (int i = 0; i < length; i++)
      ret[i] = this.buffer[this.curPosition++]; 
    return ret;
  }
  
  public void writeByte(byte val) throws IOException {
    autoExpand(this.curPosition + 1);
    this.buffer[this.curPosition++] = val;
  }
  
  public void writeShort(short val) throws IOException {
    autoExpand(this.curPosition + 2);
    if (this.bigEndian) {
      this.buffer[this.curPosition++] = (byte)(val >>> 8 & 0xFF);
      this.buffer[this.curPosition++] = (byte)(val & 0xFF);
    } else {
      this.buffer[this.curPosition++] = (byte)(val & 0xFF);
      this.buffer[this.curPosition++] = (byte)(val >>> 8 & 0xFF);
    } 
  }
  
  public void writeInt(int val) throws IOException {
    autoExpand(this.curPosition + 4);
    if (this.bigEndian) {
      this.buffer[this.curPosition++] = (byte)(val >>> 24 & 0xFF);
      this.buffer[this.curPosition++] = (byte)(val >>> 16 & 0xFF);
      this.buffer[this.curPosition++] = (byte)(val >>> 8 & 0xFF);
      this.buffer[this.curPosition++] = (byte)(val & 0xFF);
    } else {
      this.buffer[this.curPosition++] = (byte)(val & 0xFF);
      this.buffer[this.curPosition++] = (byte)(val >>> 8 & 0xFF);
      this.buffer[this.curPosition++] = (byte)(val >>> 16 & 0xFF);
      this.buffer[this.curPosition++] = (byte)(val >>> 24 & 0xFF);
    } 
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
      autoExpand(this.curPosition + bytesize);
      for (int j = 0; j < bytesize; j++)
        this.buffer[this.curPosition++] = bout.get(j); 
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
      autoExpand(this.curPosition + bytesize);
      for (int j = 0; j < bytesize; j++)
        this.buffer[this.curPosition++] = bout.get(j); 
    } 
    return len;
  }
  
  public void writeBytes(byte[] stuff) throws IOException {
    autoExpand(this.curPosition + stuff.length);
    for (byte b : stuff)
      this.buffer[this.curPosition++] = b; 
  }
  
  public byte[] getContents() throws IOException {
    return this.buffer;
  }
  
  public void setContents(byte[] buf) throws IOException {
    this.buffer = buf;
  }
  
  private void resizeBuffer(int newsize) {
    byte[] newbuf = new byte[newsize];
    if (newsize > 0 && this.buffer.length > 0)
      System.arraycopy(this.buffer, 0, newbuf, 0, Math.min(newsize, this.buffer.length)); 
    this.buffer = newbuf;
  }
  
  private void autoExpand(int newend) {
    if (this.logicalSize < newend)
      this.logicalSize = newend; 
    if (this.buffer.length < newend)
      resizeBuffer((this.buffer.length > 0) ? (this.buffer.length * 2) : newend); 
  }
}