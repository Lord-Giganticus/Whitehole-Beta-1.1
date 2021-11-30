package whitehole.fileio;

import java.io.IOException;

public interface FileBase {
  void save() throws IOException;
  
  void close() throws IOException;
  
  void releaseStorage();
  
  void setBigEndian(boolean paramBoolean);
  
  long getLength() throws IOException;
  
  void setLength(long paramLong) throws IOException;
  
  long position() throws IOException;
  
  void position(long paramLong) throws IOException;
  
  void skip(long paramLong) throws IOException;
  
  byte readByte() throws IOException;
  
  short readShort() throws IOException;
  
  int readInt() throws IOException;
  
  float readFloat() throws IOException;
  
  String readString(String paramString, int paramInt) throws IOException;
  
  byte[] readBytes(int paramInt) throws IOException;
  
  void writeByte(byte paramByte) throws IOException;
  
  void writeShort(short paramShort) throws IOException;
  
  void writeInt(int paramInt) throws IOException;
  
  void writeFloat(float paramFloat) throws IOException;
  
  int writeString(String paramString1, String paramString2, int paramInt) throws IOException;
  
  void writeBytes(byte[] paramArrayOfbyte) throws IOException;
  
  byte[] getContents() throws IOException;
  
  void setContents(byte[] paramArrayOfbyte) throws IOException;
}