package whitehole.fileio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class RarcFilesystem implements FilesystemBase {
  private FileBase file;
  
  private int unk38;
  
  private LinkedHashMap<String, FileEntry> fileEntries;
  
  private LinkedHashMap<String, DirEntry> dirEntries;
  
  public RarcFilesystem(FileBase _file) throws IOException {
    this.file = new Yaz0File(_file);
    this.file.setBigEndian(true);
    this.file.position(0L);
    int tag = this.file.readInt();
    if (tag != 1380012611)
      throw new IOException(String.format("File isn't a RARC (tag 0x%1$08X, expected 0x52415243)", new Object[] { Integer.valueOf(tag) })); 
    this.file.position(12L);
    int fileDataOffset = this.file.readInt() + 32;
    this.file.position(32L);
    int numDirNodes = this.file.readInt();
    int dirNodesOffset = this.file.readInt() + 32;
    int numFileEntries = this.file.readInt();
    int fileEntriesOffset = this.file.readInt() + 32;
    this.file.skip(4L);
    int stringTableOffset = this.file.readInt() + 32;
    this.unk38 = this.file.readInt();
    this.dirEntries = new LinkedHashMap<>(numDirNodes);
    this.fileEntries = new LinkedHashMap<>(numFileEntries);
    DirEntry root = new DirEntry();
    root.parentDir = null;
    this.file.position((dirNodesOffset + 6));
    int rnoffset = this.file.readShort();
    this.file.position((stringTableOffset + rnoffset));
    root.name = this.file.readString("ASCII", 0);
    root.fullName = "/" + root.name;
    root.tempID = 0;
    this.dirEntries.put("/", root);
    for (int i = 0; i < numDirNodes; i++) {
      DirEntry parentdir = null;
      for (DirEntry de : this.dirEntries.values()) {
        if (de.tempID == i) {
          parentdir = de;
          break;
        } 
      } 
      this.file.position((dirNodesOffset + i * 16 + 10));
      short numentries = this.file.readShort();
      int firstentry = this.file.readInt();
      for (int j = 0; j < numentries; j++) {
        int entryoffset = fileEntriesOffset + (j + firstentry) * 20;
        this.file.position(entryoffset);
        this.file.skip(4L);
        int entrytype = this.file.readShort() & 0xFFFF;
        int nameoffset = this.file.readShort() & 0xFFFF;
        int dataoffset = this.file.readInt();
        int datasize = this.file.readInt();
        this.file.position((stringTableOffset + nameoffset));
        String name = this.file.readString("ASCII", 0);
        if (!name.equals(".") && !name.equals("..")) {
          String fullname = parentdir.fullName + "/" + name;
          if (entrytype == 512) {
            DirEntry d = new DirEntry();
            d.parentDir = parentdir;
            d.name = name;
            d.fullName = fullname;
            d.tempID = dataoffset;
            this.dirEntries.put(pathToKey(fullname), d);
            parentdir.childrenDirs.add(d);
          } else {
            FileEntry f = new FileEntry();
            f.parentDir = parentdir;
            f.dataOffset = fileDataOffset + dataoffset;
            f.dataSize = datasize;
            f.name = name;
            f.fullName = fullname;
            f.data = null;
            this.fileEntries.put(pathToKey(fullname), f);
            parentdir.childrenFiles.add(f);
          } 
        } 
      } 
    } 
  }
  
  private String pathToKey(String path) {
    String ret = path.toLowerCase();
    ret = ret.substring(1);
    if (ret.indexOf("/") == -1)
      return "/"; 
    ret = ret.substring(ret.indexOf("/"));
    return ret;
  }
  
  private int align32(int val) {
    return val + 31 & 0xFFFFFFE0;
  }
  
  private int dirMagic(String name) {
    String uppername = name.toUpperCase();
    int ret = 0;
    for (int i = 0; i < 4; i++) {
      ret <<= 8;
      if (i >= uppername.length()) {
        ret += 32;
      } else {
        ret += uppername.charAt(i);
      } 
    } 
    return ret;
  }
  
  private short nameHash(String name) {
    short ret = 0;
    for (char ch : name.toCharArray()) {
      ret = (short)(ret * 3);
      ret = (short)(ret + ch);
    } 
    return ret;
  }
  
  public void save() throws IOException {
    for (FileEntry fe : this.fileEntries.values()) {
      if (fe.data != null)
        continue; 
      this.file.position(fe.dataOffset);
      fe.data = this.file.readBytes(fe.dataSize);
    } 
    int dirOffset = 64;
    int fileOffset = dirOffset + align32(this.dirEntries.size() * 16);
    int stringOffset = fileOffset + align32((this.fileEntries.size() + this.dirEntries.size() * 3 - 1) * 20);
    int dataOffset = stringOffset;
    int dataLength = 0;
    for (DirEntry de : this.dirEntries.values())
      dataOffset += de.name.length() + 1; 
    for (FileEntry fe : this.fileEntries.values()) {
      dataOffset += fe.name.length() + 1;
      dataLength += align32(fe.dataSize);
    } 
    dataOffset += 5;
    dataOffset = align32(dataOffset);
    int dirSubOffset = 0;
    int fileSubOffset = 0;
    int stringSubOffset = 0;
    int dataSubOffset = 0;
    this.file.setLength((dataOffset + dataLength));
    this.file.position(0L);
    this.file.writeInt(1380012611);
    this.file.writeInt(dataOffset + dataLength);
    this.file.writeInt(32);
    this.file.writeInt(dataOffset - 32);
    this.file.writeInt(dataLength);
    this.file.writeInt(dataLength);
    this.file.writeInt(0);
    this.file.writeInt(0);
    this.file.writeInt(this.dirEntries.size());
    this.file.writeInt(dirOffset - 32);
    this.file.writeInt(this.fileEntries.size() + this.dirEntries.size() * 3 - 1);
    this.file.writeInt(fileOffset - 32);
    this.file.writeInt(dataOffset - stringOffset);
    this.file.writeInt(stringOffset - 32);
    this.file.writeInt(this.unk38);
    this.file.writeInt(0);
    this.file.position(stringOffset);
    this.file.writeString("ASCII", ".", 0);
    this.file.writeString("ASCII", "..", 0);
    stringSubOffset += 5;
    Stack<Iterator<DirEntry>> dirstack = new Stack<>();
    Object[] entriesarray = this.dirEntries.values().toArray();
    DirEntry curdir = (DirEntry)entriesarray[0];
    int c = 1;
    for (; curdir.parentDir != null; curdir = (DirEntry)entriesarray[c++]);
    short fileid = 0;
    while (true) {
      curdir.tempID = dirSubOffset / 16;
      this.file.position((dirOffset + dirSubOffset));
      this.file.writeInt((curdir.tempID == 0) ? 1380929364 : dirMagic(curdir.name));
      this.file.writeInt(stringSubOffset);
      this.file.writeShort(nameHash(curdir.name));
      this.file.writeShort((short)(2 + curdir.childrenDirs.size() + curdir.childrenFiles.size()));
      this.file.writeInt(fileSubOffset / 20);
      dirSubOffset += 16;
      if (curdir.tempID > 0) {
        this.file.position(curdir.tempNameOffset);
        this.file.writeShort((short)stringSubOffset);
        this.file.writeInt(curdir.tempID);
      } 
      this.file.position((stringOffset + stringSubOffset));
      stringSubOffset += this.file.writeString("ASCII", curdir.name, 0);
      this.file.position((fileOffset + fileSubOffset));
      for (DirEntry cde : curdir.childrenDirs) {
        this.file.writeShort((short)-1);
        this.file.writeShort(nameHash(cde.name));
        this.file.writeShort((short)512);
        cde.tempNameOffset = (int)this.file.position();
        this.file.skip(6L);
        this.file.writeInt(16);
        this.file.writeInt(0);
        fileSubOffset += 20;
      } 
      for (FileEntry cfe : curdir.childrenFiles) {
        this.file.position((fileOffset + fileSubOffset));
        this.file.writeShort(fileid);
        this.file.writeShort(nameHash(cfe.name));
        this.file.writeShort((short)4352);
        this.file.writeShort((short)stringSubOffset);
        this.file.writeInt(dataSubOffset);
        this.file.writeInt(cfe.dataSize);
        this.file.writeInt(0);
        fileSubOffset += 20;
        fileid = (short)(fileid + 1);
        this.file.position((stringOffset + stringSubOffset));
        stringSubOffset += this.file.writeString("ASCII", cfe.name, 0);
        this.file.position((dataOffset + dataSubOffset));
        cfe.dataOffset = (int)this.file.position();
        byte[] thedata = Arrays.copyOf(cfe.data, cfe.dataSize);
        this.file.writeBytes(thedata);
        dataSubOffset += align32(cfe.dataSize);
        cfe.data = null;
      } 
      this.file.position((fileOffset + fileSubOffset));
      this.file.writeShort((short)-1);
      this.file.writeShort((short)46);
      this.file.writeShort((short)512);
      this.file.writeShort((short)0);
      this.file.writeInt(curdir.tempID);
      this.file.writeInt(16);
      this.file.writeInt(0);
      this.file.writeShort((short)-1);
      this.file.writeShort((short)184);
      this.file.writeShort((short)512);
      this.file.writeShort((short)2);
      this.file.writeInt((curdir.parentDir != null) ? curdir.parentDir.tempID : -1);
      this.file.writeInt(16);
      this.file.writeInt(0);
      fileSubOffset += 40;
      if (!curdir.childrenDirs.isEmpty()) {
        dirstack.push(curdir.childrenDirs.iterator());
        curdir = ((Iterator<DirEntry>)dirstack.peek()).next();
        continue;
      } 
      curdir = null;
      while (curdir == null) {
        if (dirstack.empty())
          break; 
        Iterator<DirEntry> it = dirstack.peek();
        if (it.hasNext()) {
          curdir = it.next();
          continue;
        } 
        dirstack.pop();
      } 
      if (curdir == null)
        break; 
    } 
    this.file.save();
  }
  
  public void close() throws IOException {
    this.file.close();
  }
  
  public boolean directoryExists(String directory) {
    return this.dirEntries.containsKey(pathToKey(directory));
  }
  
  public List<String> getDirectories(String directory) {
    if (!this.dirEntries.containsKey(pathToKey(directory)))
      return null; 
    DirEntry dir = this.dirEntries.get(pathToKey(directory));
    List<String> ret = new ArrayList<>();
    for (DirEntry de : dir.childrenDirs)
      ret.add(de.name); 
    return ret;
  }
  
  public boolean fileExists(String filename) {
    return this.fileEntries.containsKey(pathToKey(filename));
  }
  
  public List<String> getFiles(String directory) {
    if (!this.dirEntries.containsKey(pathToKey(directory)))
      return null; 
    DirEntry dir = this.dirEntries.get(pathToKey(directory));
    List<String> ret = new ArrayList<>();
    for (FileEntry fe : dir.childrenFiles)
      ret.add(fe.name); 
    return ret;
  }
  
  public FileBase openFile(String filename) throws FileNotFoundException {
    if (!this.fileEntries.containsKey(pathToKey(filename)))
      throw new FileNotFoundException(filename + " not found in RARC"); 
    try {
      return new RarcFile(this, filename);
    } catch (IOException ex) {
      throw new FileNotFoundException("got IOException");
    } 
  }
  
  public void createFile(String parent, String newfile) {
    String parentkey = pathToKey(parent);
    String fnkey = pathToKey(parent + "/" + newfile);
    if (!this.dirEntries.containsKey(parentkey))
      return; 
    if (this.fileEntries.containsKey(fnkey))
      return; 
    if (this.dirEntries.containsKey(fnkey))
      return; 
    DirEntry parentdir = this.dirEntries.get(parentkey);
    FileEntry fe = new FileEntry();
    fe.data = new byte[0];
    fe.dataSize = fe.data.length;
    fe.fullName = parent + "/" + newfile;
    fe.name = newfile;
    fe.parentDir = parentdir;
    parentdir.childrenFiles.add(fe);
    this.fileEntries.put(pathToKey(fe.fullName), fe);
  }
  
  public void renameFile(String file, String newname) {
    file = pathToKey(file);
    if (!this.fileEntries.containsKey(file))
      return; 
    FileEntry fe = this.fileEntries.get(file);
    DirEntry parent = fe.parentDir;
    String parentkey = pathToKey(parent.fullName + "/" + newname);
    if (this.fileEntries.containsKey(parentkey) || this.dirEntries.containsKey(parentkey))
      return; 
    String fnkey = pathToKey(fe.fullName);
    this.fileEntries.remove(fnkey);
    fe.name = newname;
    fe.fullName = parent.fullName + "/" + newname;
    this.fileEntries.put(fnkey, fe);
  }
  
  public void deleteFile(String file) {
    file = pathToKey(file);
    if (!this.fileEntries.containsKey(file))
      return; 
    FileEntry fe = this.fileEntries.get(file);
    DirEntry parent = fe.parentDir;
    parent.childrenFiles.remove(fe);
    this.fileEntries.remove(file);
  }
  
  public byte[] getFileContents(String fullname) throws IOException {
    FileEntry fe = this.fileEntries.get(pathToKey(fullname));
    this.file.position(fe.dataOffset);
    return this.file.readBytes(fe.dataSize);
  }
  
  public void reinsertFile(RarcFile _file) throws IOException {
    FileEntry fe = this.fileEntries.get(pathToKey(_file.fileName));
    fe.data = _file.getContents();
    fe.dataSize = (int)_file.getLength();
  }
  
  private class FileEntry {
    public int dataOffset;
    
    public int dataSize;
    
    public RarcFilesystem.DirEntry parentDir;
    
    public String name;
    
    public String fullName;
    
    public byte[] data = null;
  }
  
  private class DirEntry {
    public DirEntry parentDir;
    
    public LinkedList<DirEntry> childrenDirs = new LinkedList<>();
    
    public LinkedList<RarcFilesystem.FileEntry> childrenFiles = new LinkedList<>();
    
    public String name;
    
    public String fullName;
    
    public int tempID;
    
    public int tempNameOffset;
  }
}