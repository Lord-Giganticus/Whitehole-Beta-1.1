package whitehole.smg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import whitehole.Whitehole;
import whitehole.fileio.FileBase;

public class Bcsv {
  private FileBase file;
  
  public LinkedHashMap<Integer, Field> fields;
  
  public List<Entry> entries;
  
  public static HashMap<Integer, String> hashTable;
  
  public Bcsv(FileBase file) throws IOException {
    this.file = file;
    file.setBigEndian(true);
    if (file.getLength() == 0L) {
      this.fields = new LinkedHashMap<>();
      this.entries = new ArrayList<>();
      return;
    } 
    file.position(0L);
    int entrycount = file.readInt();
    int fieldcount = file.readInt();
    int dataoffset = file.readInt();
    int entrydatasize = file.readInt();
    this.fields = new LinkedHashMap<>(fieldcount);
    this.entries = new ArrayList<>(entrycount);
    int stringtableoffset = dataoffset + entrycount * entrydatasize;
    int i;
    for (i = 0; i < fieldcount; i++) {
      Field field = new Field();
      file.position((16 + 12 * i));
      field.nameHash = file.readInt();
      field.mask = file.readInt();
      field.entryOffset = file.readShort();
      field.shiftAmount = file.readByte();
      field.type = file.readByte();
      String fieldname = hashToFieldName(field.nameHash);
      field.name = fieldname;
      this.fields.put(Integer.valueOf(field.nameHash), field);
    } 
    for (i = 0; i < entrycount; i++) {
      Entry entry = new Entry();
      for (Field field : this.fields.values()) {
        int str_offset;
        file.position((dataoffset + i * entrydatasize + field.entryOffset));
        Object val = null;
        switch (field.type) {
          case 0:
          case 3:
            val = Integer.valueOf((file.readInt() & field.mask) >>> field.shiftAmount);
            break;
          case 4:
            val = Short.valueOf((short)((file.readShort() & field.mask) >>> field.shiftAmount));
            break;
          case 5:
            val = Byte.valueOf((byte)((file.readByte() & field.mask) >>> field.shiftAmount));
            break;
          case 2:
            val = Float.valueOf(file.readFloat());
            break;
          case 6:
            str_offset = file.readInt();
            file.position((stringtableoffset + str_offset));
            val = file.readString("SJIS", 0);
            break;
          default:
            throw new IOException(String.format("Bcsv: unsupported data type %1$02X", new Object[] { Byte.valueOf(field.type) }));
        } 
        entry.put(Integer.valueOf(field.nameHash), val);
      } 
      this.entries.add(entry);
    } 
  }
  
  public void save() throws IOException {
    int[] datasizes = { 4, -1, 4, 4, 2, 1, 4 };
    int entrysize = 0;
    for (Field field : this.fields.values()) {
      short fieldend = (short)(field.entryOffset + datasizes[field.type]);
      if (fieldend > entrysize)
        entrysize = fieldend; 
    } 
    entrysize = entrysize + 3 & 0xFFFFFFFC;
    int dataoffset = 16 + 12 * this.fields.size();
    int stringtableoffset = dataoffset + this.entries.size() * entrysize;
    int curstring = 0;
    this.file.setLength(stringtableoffset);
    this.file.position(0L);
    this.file.writeInt(this.entries.size());
    this.file.writeInt(this.fields.size());
    this.file.writeInt(dataoffset);
    this.file.writeInt(entrysize);
    for (Field field : this.fields.values()) {
      this.file.writeInt(field.nameHash);
      this.file.writeInt(field.mask);
      this.file.writeShort(field.entryOffset);
      this.file.writeByte(field.shiftAmount);
      this.file.writeByte(field.type);
    } 
    int i = 0;
    HashMap<String, Integer> stringoffsets = new HashMap<>();
    for (Entry entry : this.entries) {
      for (Field field : this.fields.values()) {
        int j;
        short s;
        byte b;
        String val;
        int valoffset = dataoffset + i * entrysize + field.entryOffset;
        this.file.position(valoffset);
        switch (field.type) {
          case 0:
          case 3:
            j = this.file.readInt();
            j &= field.mask ^ 0xFFFFFFFF;
            j |= ((Integer)entry.get(Integer.valueOf(field.nameHash))).intValue() << field.shiftAmount & field.mask;
            this.file.position(valoffset);
            this.file.writeInt(j);
          case 4:
            s = this.file.readShort();
            s = (short)(s & (short)(field.mask ^ 0xFFFFFFFF));
            s = (short)(s | (short)(((Short)entry.get(Integer.valueOf(field.nameHash))).shortValue() << field.shiftAmount & field.mask));
            this.file.position(valoffset);
            this.file.writeShort(s);
          case 5:
            b = this.file.readByte();
            b = (byte)(b & (byte)(field.mask ^ 0xFFFFFFFF));
            b = (byte)(b | (byte)(((Byte)entry.get(Integer.valueOf(field.nameHash))).byteValue() << field.shiftAmount & field.mask));
            this.file.position(valoffset);
            this.file.writeByte(b);
          case 2:
            this.file.writeFloat(((Float)entry.get(Integer.valueOf(field.nameHash))).floatValue());
          case 6:
            val = (String)entry.get(Integer.valueOf(field.nameHash));
            if (stringoffsets.containsKey(val)) {
              this.file.writeInt(((Integer)stringoffsets.get(val)).intValue());
              continue;
            } 
            stringoffsets.put(val, Integer.valueOf(curstring));
            this.file.writeInt(curstring);
            this.file.position((stringtableoffset + curstring));
            curstring += this.file.writeString("SJIS", val, 0);
        } 
      } 
      i++;
    } 
    i = (int)this.file.getLength();
    this.file.position(i);
    int aligned_end = i + 31 & 0xFFFFFFE0;
    for (; i < aligned_end; i++)
      this.file.writeByte((byte)64); 
    this.file.save();
  }
  
  public void close() throws IOException {
    this.file.close();
  }
  
  public Field addField(String name, int offset, int type, int mask, int shift, Object defaultval) {
    int[] datasizes = { 4, -1, 4, 4, 2, 1, 4 };
    addHash(name);
    if (type == 2 || type == 6) {
      mask = -1;
      shift = 0;
    } 
    if (offset == -1)
      for (Field field : this.fields.values()) {
        short fieldend = (short)(field.entryOffset + datasizes[field.type]);
        if (fieldend > offset)
          offset = fieldend; 
      }  
    Field newfield = new Field();
    newfield.name = name;
    newfield.nameHash = fieldNameToHash(name);
    newfield.mask = mask;
    newfield.shiftAmount = (byte)shift;
    newfield.type = (byte)type;
    newfield.entryOffset = (short)offset;
    this.fields.put(Integer.valueOf(newfield.nameHash), newfield);
    for (Entry entry : this.entries)
      entry.put(name, defaultval); 
    return newfield;
  }
  
  public void removeField(String name) {
    int hash = fieldNameToHash(name);
    this.fields.remove(Integer.valueOf(hash));
    for (Entry entry : this.entries)
      entry.remove(Integer.valueOf(hash)); 
  }
  
  public static class Field {
    public int nameHash;
    
    public int mask;
    
    public short entryOffset;
    
    public byte shiftAmount;
    
    public byte type;
    
    public String name;
  }
  
  public static class Entry extends LinkedHashMap<Integer, Object> {
    public Object get(String key) {
      return get(Integer.valueOf(Bcsv.fieldNameToHash(key)));
    }
    
    public void put(String key, Object val) {
      put(Integer.valueOf(Bcsv.fieldNameToHash(key)), val);
    }
    
    public boolean containsKey(String key) {
      return containsKey(Integer.valueOf(Bcsv.fieldNameToHash(key)));
    }
  }
  
  public static int fieldNameToHash(String field) {
    int ret = 0;
    for (char ch : field.toCharArray()) {
      ret *= 31;
      ret += ch;
    } 
    return ret;
  }
  
  public static String hashToFieldName(int hash) {
    if (!hashTable.containsKey(Integer.valueOf(hash)))
      return String.format("[%1$08X]", new Object[] { Integer.valueOf(hash) }); 
    return hashTable.get(Integer.valueOf(hash));
  }
  
  public static void addHash(String field) {
    int hash = fieldNameToHash(field);
    if (!hashTable.containsKey(Integer.valueOf(hash)))
      hashTable.put(Integer.valueOf(hash), field); 
  }
  
  public static void populateHashTable() {
    hashTable = new HashMap<>();
    try {
      InputStream strm = Whitehole.class.getResourceAsStream("/Resources/KnownFieldNames.txt");
      BufferedReader reader = new BufferedReader(new InputStreamReader(strm));
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (line.length() == 0 || 
          line.charAt(0) == '#')
          continue; 
        addHash(line);
      } 
      strm.close();
    } catch (IOException ex) {}
  }
}
