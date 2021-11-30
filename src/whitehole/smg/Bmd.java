package whitehole.smg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import whitehole.fileio.FileBase;
import whitehole.rendering.Helper;
import whitehole.vectors.Color4;
import whitehole.vectors.Matrix4;
import whitehole.vectors.Vector2;
import whitehole.vectors.Vector3;

public class Bmd {
  private FileBase file;
  
  public Vector3 bboxMin;
  
  public Vector3 bboxMax;
  
  public int numVertices;
  
  public List<SceneGraphNode> sceneGraph;
  
  public int arrayMask;
  
  public Vector3[] positionArray;
  
  public Vector3[] normalArray;
  
  public Color4[][] colorArray;
  
  public Vector2[][] texcoordArray;
  
  public Batch[] batches;
  
  public MultiMatrix[] multiMatrix;
  
  public MatrixType[] matrixTypes;
  
  public Joint[] joints;
  
  public Material[] materials;
  
  public Texture[] textures;
  
  public Bmd(FileBase _file) throws IOException {
    this.file = _file;
    this.file.setBigEndian(true);
    this.file.position(12L);
    int numsections = this.file.readInt();
    this.file.skip(16L);
    for (int i = 0; i < numsections; i++) {
      int sectiontag = this.file.readInt();
      switch (sectiontag) {
        case 1229866545:
          readINF1();
          break;
        case 1448368177:
          readVTX1();
          break;
        case 1163284529:
          readEVP1();
          break;
        case 1146246961:
          readDRW1();
          break;
        case 1246647345:
          readJNT1();
          break;
        case 1397248049:
          readSHP1();
          break;
        case 1296127027:
          readMAT3();
          break;
        case 1296321587:
          readMDL3();
          break;
        case 1413830705:
          readTEX1();
          break;
        default:
          throw new IOException(String.format("Unsupported BMD section 0x%1$08X", new Object[] { Integer.valueOf(sectiontag) }));
      } 
    } 
    this.bboxMin = new Vector3(0.0F, 0.0F, 0.0F);
    this.bboxMax = new Vector3(0.0F, 0.0F, 0.0F);
    for (Vector3 vec : this.positionArray) {
      if (vec.x < this.bboxMin.x)
        this.bboxMin.x = vec.x; 
      if (vec.y < this.bboxMin.y)
        this.bboxMin.y = vec.y; 
      if (vec.z < this.bboxMin.z)
        this.bboxMin.z = vec.z; 
      if (vec.x > this.bboxMax.x)
        this.bboxMax.x = vec.x; 
      if (vec.y > this.bboxMax.y)
        this.bboxMax.y = vec.y; 
      if (vec.z > this.bboxMax.z)
        this.bboxMax.z = vec.z; 
    } 
  }
  
  public void save() throws IOException {
    this.file.save();
  }
  
  public void close() throws IOException {
    this.file.close();
  }
  
  private float readArrayValue_s16(int fixedpoint) throws IOException {
    short val = this.file.readShort();
    return val / (1 << fixedpoint);
  }
  
  private float readArrayValue_f32() throws IOException {
    return this.file.readFloat();
  }
  
  private float readArrayValue(int type, int fixedpoint) throws IOException {
    switch (type) {
      case 3:
        return readArrayValue_s16(fixedpoint);
      case 4:
        return readArrayValue_f32();
    } 
    return 0.0F;
  }
  
  private Color4 readColorValue_RGBA8() throws IOException {
    int r = this.file.readByte() & 0xFF;
    int g = this.file.readByte() & 0xFF;
    int b = this.file.readByte() & 0xFF;
    int a = this.file.readByte() & 0xFF;
    return new Color4(r / 255.0F, g / 255.0F, b / 255.0F, a / 255.0F);
  }
  
  private Color4 readColorValue_RGBX8() throws IOException {
    int r = this.file.readByte() & 0xFF;
    int g = this.file.readByte() & 0xFF;
    int b = this.file.readByte() & 0xFF;
    this.file.readByte();
    return new Color4(r / 255.0F, g / 255.0F, b / 255.0F, 1.0F);
  }
  
  private Color4 readColorValue(int type) throws IOException {
    switch (type) {
      case 1:
        return readColorValue_RGBX8();
      case 2:
        return readColorValue_RGBX8();
      case 5:
        return readColorValue_RGBA8();
    } 
    return null;
  }
  
  private void readINF1() throws IOException {
    long sectionstart = this.file.position() - 4L;
    int sectionsize = this.file.readInt();
    this.sceneGraph = new ArrayList<>();
    Stack<Integer> matstack = new Stack<>();
    Stack<Integer> nodestack = new Stack<>();
    matstack.push(Integer.valueOf(65535));
    nodestack.push(Integer.valueOf(-1));
    this.file.skip(8L);
    this.numVertices = this.file.readInt();
    int datastart = this.file.readInt();
    this.file.skip((datastart - 24));
    short curtype = 0;
    while ((curtype = this.file.readShort()) != 0) {
      int parentnode;
      SceneGraphNode node;
      int arg = this.file.readShort();
      switch (curtype) {
        case 1:
          matstack.push(matstack.peek());
          nodestack.push(Integer.valueOf(this.sceneGraph.size() - 1));
        case 2:
          matstack.pop();
          nodestack.pop();
        case 17:
          matstack.pop();
          matstack.push(Integer.valueOf(arg));
        case 16:
        case 18:
          parentnode = ((Integer)nodestack.peek()).intValue();
          node = new SceneGraphNode();
          node.materialID = (short)((Integer)matstack.peek()).intValue();
          node.nodeID = (short)arg;
          node.nodeType = (curtype == 18) ? 0 : 1;
          node.parentIndex = parentnode;
          this.sceneGraph.add(node);
      } 
    } 
    this.file.position(sectionstart + sectionsize);
  }
  
  private void readVTX1() throws IOException {
    long sectionstart = this.file.position() - 4L;
    int sectionsize = this.file.readInt();
    this.arrayMask = 0;
    this.colorArray = new Color4[2][];
    this.texcoordArray = new Vector2[8][];
    List<Integer> arrayoffsets = new ArrayList<>();
    int arraydefoffset = this.file.readInt();
    int i;
    for (i = 0; i < 13; i++) {
      this.file.position(sectionstart + 12L + (i * 4));
      int dataoffset = this.file.readInt();
      if (dataoffset != 0)
        arrayoffsets.add(Integer.valueOf(dataoffset)); 
    } 
    for (i = 0; i < arrayoffsets.size(); i++) {
      int j, cid, tid, k;
      this.file.position(sectionstart + arraydefoffset + (i * 16));
      int arraytype = this.file.readInt();
      int compsize = this.file.readInt();
      int datatype = this.file.readInt();
      int fp = this.file.readByte() & 0xFF;
      int arraysize = 0;
      if (i == arrayoffsets.size() - 1) {
        arraysize = sectionsize - ((Integer)arrayoffsets.get(i)).intValue();
      } else {
        arraysize = ((Integer)arrayoffsets.get(i + 1)).intValue() - ((Integer)arrayoffsets.get(i)).intValue();
      } 
      if (arraytype == 11 || arraytype == 12) {
        if ((((datatype < 3) ? 1 : 0) ^ ((compsize == 0) ? 1 : 0)) != 0)
          throw new IOException(String.format("Bmd: component count mismatch in color array; DataType=%1$d, CompSize=%2$d", new Object[] { Integer.valueOf(datatype), Integer.valueOf(compsize) })); 
        switch (datatype) {
          case 1:
          case 2:
          case 5:
            arraysize /= 4;
            break;
          default:
            throw new IOException(String.format("Bmd: unsupported color DataType %1$d", new Object[] { Integer.valueOf(datatype) }));
        } 
      } else {
        switch (datatype) {
          case 3:
            arraysize /= 2;
            break;
          case 4:
            arraysize /= 4;
            break;
          default:
            throw new IOException(String.format("Bmd: unsupported DataType %1$d", new Object[] { Integer.valueOf(datatype) }));
        } 
      } 
      this.file.position(sectionstart + ((Integer)arrayoffsets.get(i)).intValue());
      this.arrayMask |= 1 << arraytype;
      switch (arraytype) {
        case 9:
          switch (compsize) {
            case 0:
              this.positionArray = new Vector3[arraysize / 2];
              for (j = 0; j < arraysize / 2; ) {
                this.positionArray[j] = new Vector3(readArrayValue(datatype, fp), readArrayValue(datatype, fp), 0.0F);
                j++;
              } 
              break;
            case 1:
              this.positionArray = new Vector3[arraysize / 3];
              for (j = 0; j < arraysize / 3; ) {
                this.positionArray[j] = new Vector3(readArrayValue(datatype, fp), readArrayValue(datatype, fp), readArrayValue(datatype, fp));
                j++;
              } 
              break;
          } 
          throw new IOException(String.format("Bmd: unsupported position CompSize %1$d", new Object[] { Integer.valueOf(compsize) }));
        case 10:
          switch (compsize) {
            case 0:
              this.normalArray = new Vector3[arraysize / 3];
              for (j = 0; j < arraysize / 3; ) {
                this.normalArray[j] = new Vector3(readArrayValue(datatype, fp), readArrayValue(datatype, fp), readArrayValue(datatype, fp));
                j++;
              } 
              break;
          } 
          throw new IOException(String.format("Bmd: unsupported normal CompSize %1$d", new Object[] { Integer.valueOf(compsize) }));
        case 11:
        case 12:
          cid = arraytype - 11;
          this.colorArray[cid] = new Color4[arraysize];
          for (k = 0; k < arraysize; ) {
            this.colorArray[cid][k] = readColorValue(datatype);
            k++;
          } 
          break;
        case 13:
        case 14:
        case 15:
        case 16:
        case 17:
        case 18:
        case 19:
        case 20:
          tid = arraytype - 13;
          switch (compsize) {
            case 0:
              this.texcoordArray[tid] = new Vector2[arraysize];
              for (k = 0; k < arraysize; ) {
                this.texcoordArray[tid][k] = new Vector2(readArrayValue(datatype, fp), 0.0F);
                k++;
              } 
              break;
            case 1:
              this.texcoordArray[tid] = new Vector2[arraysize / 2];
              for (k = 0; k < arraysize / 2; ) {
                this.texcoordArray[tid][k] = new Vector2(readArrayValue(datatype, fp), readArrayValue(datatype, fp));
                k++;
              } 
              break;
          } 
          throw new IOException(String.format("Bmd: unsupported texcoord CompSize %1$d", new Object[] { Integer.valueOf(compsize) }));
        default:
          throw new IOException(String.format("Bmd: unsupported ArrayType %1$d", new Object[] { Integer.valueOf(arraytype) }));
      } 
    } 
    this.file.position(sectionstart + sectionsize);
  }
  
  private void readEVP1() throws IOException {
    long sectionstart = this.file.position() - 4L;
    int sectionsize = this.file.readInt();
    short count = this.file.readShort();
    this.file.skip(2L);
    this.multiMatrix = new MultiMatrix[count];
    int offset0 = this.file.readInt();
    int offset1 = this.file.readInt();
    int offset2 = this.file.readInt();
    int offset3 = this.file.readInt();
    int position1 = 0, position2 = 0;
    for (int i = 0; i < count; i++) {
      this.file.position(sectionstart + offset0 + i);
      byte subcount = this.file.readByte();
      MultiMatrix mm = new MultiMatrix();
      this.multiMatrix[i] = mm;
      mm.numMatrices = subcount;
      mm.matrixIndices = new short[subcount];
      mm.matrices = new Matrix4[subcount];
      mm.matrixWeights = new float[subcount];
      for (int j = 0; j < subcount; j++) {
        this.file.position(sectionstart + offset1 + position1);
        mm.matrixIndices[j] = this.file.readShort();
        position1 += 2;
        this.file.position(sectionstart + offset2 + position2);
        mm.matrixWeights[j] = this.file.readFloat();
        position2 += 4;
        this.file.position(sectionstart + offset3 + (mm.matrixIndices[j] * 48));
        mm.matrices[j] = new Matrix4();
        (mm.matrices[j]).m[0] = this.file.readFloat();
        (mm.matrices[j]).m[1] = this.file.readFloat();
        (mm.matrices[j]).m[2] = this.file.readFloat();
        (mm.matrices[j]).m[3] = this.file.readFloat();
        (mm.matrices[j]).m[4] = this.file.readFloat();
        (mm.matrices[j]).m[5] = this.file.readFloat();
        (mm.matrices[j]).m[6] = this.file.readFloat();
        (mm.matrices[j]).m[7] = this.file.readFloat();
        (mm.matrices[j]).m[8] = this.file.readFloat();
        (mm.matrices[j]).m[9] = this.file.readFloat();
        (mm.matrices[j]).m[10] = this.file.readFloat();
        (mm.matrices[j]).m[11] = this.file.readFloat();
      } 
    } 
    this.file.position(sectionstart + sectionsize);
  }
  
  private void readDRW1() throws IOException {
    long sectionstart = this.file.position() - 4L;
    int sectionsize = this.file.readInt();
    short count = this.file.readShort();
    this.file.skip(2L);
    this.matrixTypes = new MatrixType[count];
    int offset0 = this.file.readInt();
    int offset1 = this.file.readInt();
    for (int i = 0; i < count; i++) {
      MatrixType mt = new MatrixType();
      this.matrixTypes[i] = mt;
      this.file.position(sectionstart + offset0 + i);
      mt.isWeighted = (this.file.readByte() != 0);
      this.file.position(sectionstart + offset1 + (i * 2));
      mt.index = this.file.readShort();
    } 
    this.file.position(sectionstart + sectionsize);
  }
  
  private void readJNT1() throws IOException {
    long sectionstart = this.file.position() - 4L;
    int sectionsize = this.file.readInt();
    short numjoints = this.file.readShort();
    this.file.skip(2L);
    this.joints = new Joint[numjoints];
    int jointsoffset = this.file.readInt();
    int unkoffset = this.file.readInt();
    int stringsoffset = this.file.readInt();
    for (int i = 0; i < numjoints; i++) {
      this.file.position(sectionstart + jointsoffset + (i * 64));
      Joint jnt = new Joint();
      this.joints[i] = jnt;
      jnt.unk1 = this.file.readShort();
      jnt.unk2 = this.file.readByte();
      this.file.skip(1L);
      jnt.scale = new Vector3(this.file.readFloat(), this.file.readFloat(), this.file.readFloat());
      jnt.rotation = new Vector3((float)(this.file.readShort() * Math.PI / 32768.0D), (float)(this.file.readShort() * Math.PI / 32768.0D), (float)(this.file.readShort() * Math.PI / 32768.0D));
      this.file.skip(2L);
      jnt.translation = new Vector3(this.file.readFloat(), this.file.readFloat(), this.file.readFloat());
      jnt.matrix = Helper.SRTToMatrix(jnt.scale, jnt.rotation, jnt.translation);
      for (SceneGraphNode node : this.sceneGraph) {
        if (node.nodeType != 1 || 
          node.nodeID != i)
          continue; 
        SceneGraphNode parentnode = node;
        do {
          if (parentnode.parentIndex == -1) {
            parentnode = null;
            break;
          } 
          parentnode = this.sceneGraph.get(parentnode.parentIndex);
        } while (parentnode.nodeType != 1);
        if (parentnode != null) {
          jnt.finalMatrix = new Matrix4();
          Matrix4.mult(jnt.matrix, (this.joints[parentnode.nodeID]).finalMatrix, jnt.finalMatrix);
          break;
        } 
        jnt.finalMatrix = jnt.matrix;
      } 
    } 
    this.file.position(sectionstart + sectionsize);
  }
  
  private void readSHP1() throws IOException {
    long sectionstart = this.file.position() - 4L;
    int sectionsize = this.file.readInt();
    short numbatches = this.file.readShort();
    this.file.skip(2L);
    int batchesoffset = this.file.readInt();
    this.file.skip(8L);
    int batchattribsoffset = this.file.readInt();
    int mtxtableoffset = this.file.readInt();
    int dataoffset = this.file.readInt();
    int mtxdataoffset = this.file.readInt();
    int pktlocationsoffset = this.file.readInt();
    this.batches = new Batch[numbatches];
    for (int i = 0; i < numbatches; i++) {
      Batch batch = new Batch();
      this.batches[i] = batch;
      this.file.position(sectionstart + batchesoffset + (i * 40));
      batch.matrixType = this.file.readByte();
      this.file.skip(1L);
      int numpackets = this.file.readShort() & 0xFFFF;
      int attribsoffset = this.file.readShort() & 0xFFFF;
      int firstmtxindex = this.file.readShort() & 0xFFFF;
      int firstpktindex = this.file.readShort() & 0xFFFF;
      this.file.skip(2L);
      batch.unk = this.file.readFloat();
      List<Integer> attribs = new ArrayList<>();
      this.file.position(sectionstart + batchattribsoffset + attribsoffset);
      int arraymask = 0;
      while (true) {
        int arraytype = this.file.readInt();
        int datatype = this.file.readInt();
        if (arraytype == 255)
          break; 
        int attrib = arraytype & 0xFF | (datatype & 0xFF) << 8;
        attribs.add(Integer.valueOf(attrib));
        arraymask |= 1 << arraytype;
      } 
      batch.packets = new Batch.Packet[numpackets];
      for (int j = 0; j < numpackets; j++) {
        batch.getClass();
        Batch.Packet packet = new Batch().new Packet();
        packet.primitives = new ArrayList<>();
        batch.packets[j] = packet;
        this.file.position(sectionstart + mtxdataoffset + ((firstmtxindex + j) * 8));
        this.file.skip(2L);
        short mtxtablesize = this.file.readShort();
        int mtxtablefirstindex = this.file.readInt();
        packet.matrixTable = new short[mtxtablesize];
        this.file.position(sectionstart + mtxtableoffset + (mtxtablefirstindex * 2));
        for (int k = 0; k < mtxtablesize; k++)
          packet.matrixTable[k] = this.file.readShort(); 
        this.file.position(sectionstart + pktlocationsoffset + ((firstpktindex + j) * 8));
        int pktsize = this.file.readInt();
        int pktoffset = this.file.readInt();
        this.file.position(sectionstart + dataoffset + pktoffset);
        long packetend = this.file.position() + pktsize;
        while (this.file.position() < packetend) {
          int primtype = this.file.readByte() & 0xFF;
          if (primtype == 0)
            break; 
          short numvertices = this.file.readShort();
          packet.getClass();
          Batch.Packet.Primitive prim = new Batch().new Packet().new Primitive();
          packet.primitives.add(prim);
          prim.colorIndices = new int[2][];
          prim.texcoordIndices = new int[8][];
          prim.arrayMask = arraymask;
          prim.numIndices = numvertices;
          if ((arraymask & 0x1) != 0)
            prim.posMatrixIndices = new int[numvertices]; 
          if ((arraymask & 0x200) != 0)
            prim.positionIndices = new int[numvertices]; 
          if ((arraymask & 0x400) != 0)
            prim.normalIndices = new int[numvertices]; 
          if ((arraymask & 0x800) != 0)
            prim.colorIndices[0] = new int[numvertices]; 
          if ((arraymask & 0x1000) != 0)
            prim.colorIndices[1] = new int[numvertices]; 
          if ((arraymask & 0x2000) != 0)
            prim.texcoordIndices[0] = new int[numvertices]; 
          if ((arraymask & 0x4000) != 0)
            prim.texcoordIndices[1] = new int[numvertices]; 
          if ((arraymask & 0x8000) != 0)
            prim.texcoordIndices[2] = new int[numvertices]; 
          if ((arraymask & 0x10000) != 0)
            prim.texcoordIndices[3] = new int[numvertices]; 
          if ((arraymask & 0x20000) != 0)
            prim.texcoordIndices[4] = new int[numvertices]; 
          if ((arraymask & 0x40000) != 0)
            prim.texcoordIndices[5] = new int[numvertices]; 
          if ((arraymask & 0x80000) != 0)
            prim.texcoordIndices[6] = new int[numvertices]; 
          if ((arraymask & 0x100000) != 0)
            prim.texcoordIndices[7] = new int[numvertices]; 
          prim.primitiveType = primtype;
          for (int m = 0; m < numvertices; m++) {
            for (Iterator<Integer> i$ = attribs.iterator(); i$.hasNext(); ) {
              int attrib = ((Integer)i$.next()).intValue();
              int val = 0;
              switch (attrib & 0xFF00) {
                case 0:
                case 256:
                  val = this.file.readByte() & 0xFF;
                  break;
                case 512:
                case 768:
                  val = this.file.readShort() & 0xFFFF;
                  break;
                default:
                  throw new IOException(String.format("Bmd: unsupported index attrib %1$04X", new Object[] { Integer.valueOf(attrib) }));
              } 
              switch (attrib & 0xFF) {
                case 0:
                  prim.posMatrixIndices[m] = val / 3;
                  continue;
                case 9:
                  prim.positionIndices[m] = val;
                  continue;
                case 10:
                  prim.normalIndices[m] = val;
                  continue;
                case 11:
                case 12:
                  prim.colorIndices[(attrib & 0xFF) - 11][m] = val;
                  continue;
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                  prim.texcoordIndices[(attrib & 0xFF) - 13][m] = val;
                  continue;
              } 
              throw new IOException(String.format("Bmd: unsupported index attrib %1$04X", new Object[] { Integer.valueOf(attrib) }));
            } 
          } 
        } 
      } 
    } 
    this.file.position(sectionstart + sectionsize);
  }
  
  private void readMAT3() throws IOException {
    long sectionstart = this.file.position() - 4L;
    int sectionsize = this.file.readInt();
    short nummaterials = this.file.readShort();
    this.file.skip(2L);
    this.materials = new Material[nummaterials];
    int[] offsets = new int[30];
    int i;
    for (i = 0; i < 30; ) {
      offsets[i] = this.file.readInt();
      i++;
    } 
    for (i = 0; i < nummaterials; i++) {
      Material mat = new Material();
      this.materials[i] = mat;
      this.file.position(sectionstart + offsets[2] + 4L + (i * 4) + 2L);
      short nameoffset = this.file.readShort();
      this.file.position(sectionstart + offsets[2] + nameoffset);
      mat.name = this.file.readString("ASCII", 0);
      this.file.position(sectionstart + offsets[1] + (i * 2));
      short matindex = this.file.readShort();
      this.file.position(sectionstart + offsets[0] + (matindex * 332));
      mat.drawFlag = this.file.readByte();
      byte cull_id = this.file.readByte();
      byte numchans_id = this.file.readByte();
      byte numtexgens_id = this.file.readByte();
      byte numtev_id = this.file.readByte();
      this.file.skip(1L);
      byte zmode_id = this.file.readByte();
      this.file.skip(1L);
      this.file.skip(4L);
      this.file.skip(8L);
      this.file.skip(4L);
      this.file.skip(16L);
      short[] texgen_id = new short[8];
      for (int j = 0; j < 8; ) {
        texgen_id[j] = this.file.readShort();
        j++;
      } 
      this.file.skip(16L);
      short[] texmtx_id = new short[10];
      for (int k = 0; k < 10; ) {
        texmtx_id[k] = this.file.readShort();
        k++;
      } 
      this.file.skip(40L);
      short[] texstage_id = new short[8];
      for (int m = 0; m < 8; ) {
        texstage_id[m] = this.file.readShort();
        m++;
      } 
      short[] constcolor_id = new short[4];
      int n;
      for (n = 0; n < 4; ) {
        constcolor_id[n] = this.file.readShort();
        n++;
      } 
      mat.constColorSel = new byte[16];
      for (n = 0; n < 16; ) {
        mat.constColorSel[n] = this.file.readByte();
        n++;
      } 
      mat.constAlphaSel = new byte[16];
      for (n = 0; n < 16; ) {
        mat.constAlphaSel[n] = this.file.readByte();
        n++;
      } 
      short[] tevorder_id = new short[16];
      for (int i1 = 0; i1 < 16; ) {
        tevorder_id[i1] = this.file.readShort();
        i1++;
      } 
      short[] colors10_id = new short[4];
      for (int i2 = 0; i2 < 4; ) {
        colors10_id[i2] = this.file.readShort();
        i2++;
      } 
      short[] tevstage_id = new short[16];
      for (int i3 = 0; i3 < 16; ) {
        tevstage_id[i3] = this.file.readShort();
        i3++;
      } 
      short[] tevswap_id = new short[16];
      for (int i4 = 0; i4 < 16; ) {
        tevswap_id[i4] = this.file.readShort();
        i4++;
      } 
      short[] tevswaptbl_id = new short[4];
      for (int i5 = 0; i5 < 4; ) {
        tevswaptbl_id[i5] = this.file.readShort();
        i5++;
      } 
      this.file.skip(24L);
      short fog_id = this.file.readShort();
      short alphacomp_id = this.file.readShort();
      short blendmode_id = this.file.readShort();
      this.file.position(sectionstart + offsets[4] + (cull_id * 4));
      mat.cullMode = (byte)this.file.readInt();
      this.file.position(sectionstart + offsets[6] + numchans_id);
      mat.numChans = this.file.readByte();
      this.file.position(sectionstart + offsets[10] + numtexgens_id);
      mat.numTexgens = this.file.readByte();
      this.file.position(sectionstart + offsets[19] + numtev_id);
      mat.numTevStages = this.file.readByte();
      this.file.position(sectionstart + offsets[26] + (zmode_id * 4));
      mat.getClass();
      mat.zMode = new Material().new ZModeInfo();
      mat.zMode.enableZTest = (this.file.readByte() != 0);
      mat.zMode.func = this.file.readByte();
      mat.zMode.enableZWrite = (this.file.readByte() != 0);
      mat.texGen = new Material.TexGenInfo[mat.numTexgens];
      int i6;
      for (i6 = 0; i6 < mat.numTexgens; i6++) {
        mat.getClass();
        mat.texGen[i6] = new Material().new TexGenInfo();
        this.file.position(sectionstart + offsets[11] + (texgen_id[i6] * 4));
        (mat.texGen[i6]).type = this.file.readByte();
        (mat.texGen[i6]).src = this.file.readByte();
        (mat.texGen[i6]).matrix = this.file.readByte();
      } 
      mat.texMtx = new Material.TexMtxInfo[10];
      for (i6 = 0; i6 < 10; i6++) {
        mat.getClass();
        mat.texMtx[i6] = new Material().new TexMtxInfo();
        if (texmtx_id[i6] == -1) {
          (mat.texMtx[i6]).finalMatrix = new Matrix4();
        } else {
          this.file.position(sectionstart + offsets[13] + (texmtx_id[i6] * 100));
          (mat.texMtx[i6]).unks1 = this.file.readShort();
          (mat.texMtx[i6]).unks2 = this.file.readShort();
          (mat.texMtx[i6]).unkf1 = new float[5];
          int i7;
          for (i7 = 0; i7 < 5; ) {
            (mat.texMtx[i6]).unkf1[i7] = this.file.readFloat();
            i7++;
          } 
          (mat.texMtx[i6]).unks3 = this.file.readShort();
          (mat.texMtx[i6]).unks4 = this.file.readShort();
          (mat.texMtx[i6]).unkf2 = new float[2];
          for (i7 = 0; i7 < 2; ) {
            (mat.texMtx[i6]).unkf2[i7] = this.file.readFloat();
            i7++;
          } 
          (mat.texMtx[i6]).unkf3 = new float[16];
          for (i7 = 0; i7 < 16; ) {
            (mat.texMtx[i6]).unkf3[i7] = this.file.readFloat();
            i7++;
          } 
          (mat.texMtx[i6]).finalMatrix = new Matrix4();
          (mat.texMtx[i6]).finalMatrix.m = (mat.texMtx[i6]).unkf3;
        } 
      } 
      mat.texStages = new short[8];
      for (i6 = 0; i6 < 8; i6++) {
        if (texstage_id[i6] == -1) {
          mat.texStages[i6] = -1;
        } else {
          this.file.position(sectionstart + offsets[15] + (texstage_id[i6] * 2));
          mat.texStages[i6] = this.file.readShort();
        } 
      } 
      mat.constColors = new Material.ColorInfo[4];
      for (i6 = 0; i6 < 4; i6++) {
        mat.getClass();
        mat.constColors[i6] = new Material().new ColorInfo();
        if (constcolor_id[i6] == -1) {
          (mat.constColors[i6]).r = 0;
          (mat.constColors[i6]).g = 0;
          (mat.constColors[i6]).b = 0;
          (mat.constColors[i6]).a = 0;
        } else {
          this.file.position(sectionstart + offsets[18] + (constcolor_id[i6] * 4));
          (mat.constColors[i6]).r = this.file.readByte() & 0xFF;
          (mat.constColors[i6]).g = this.file.readByte() & 0xFF;
          (mat.constColors[i6]).b = this.file.readByte() & 0xFF;
          (mat.constColors[i6]).a = this.file.readByte() & 0xFF;
        } 
      } 
      mat.tevOrder = new Material.TevOrderInfo[mat.numTevStages];
      for (i6 = 0; i6 < mat.numTevStages; i6++) {
        mat.getClass();
        mat.tevOrder[i6] = new Material().new TevOrderInfo();
        this.file.position(sectionstart + offsets[16] + (tevorder_id[i6] * 4));
        (mat.tevOrder[i6]).texcoordID = this.file.readByte();
        (mat.tevOrder[i6]).texMap = this.file.readByte();
        (mat.tevOrder[i6]).chanID = this.file.readByte();
      } 
      mat.colorS10 = new Material.ColorInfo[4];
      for (i6 = 0; i6 < 4; i6++) {
        mat.getClass();
        mat.colorS10[i6] = new Material().new ColorInfo();
        if (colors10_id[i6] == -1) {
          (mat.colorS10[i6]).r = 255;
          (mat.colorS10[i6]).g = 0;
          (mat.colorS10[i6]).b = 255;
          (mat.colorS10[i6]).a = 255;
        } else {
          this.file.position(sectionstart + offsets[17] + (colors10_id[i6] * 8));
          (mat.colorS10[i6]).r = this.file.readShort();
          (mat.colorS10[i6]).g = this.file.readShort();
          (mat.colorS10[i6]).b = this.file.readShort();
          (mat.colorS10[i6]).a = this.file.readShort();
        } 
      } 
      mat.tevStage = new Material.TevStageInfo[mat.numTevStages];
      for (i6 = 0; i6 < mat.numTevStages; i6++) {
        mat.getClass();
        mat.tevStage[i6] = new Material().new TevStageInfo();
        this.file.position(sectionstart + offsets[20] + (tevstage_id[i6] * 20) + 1L);
        (mat.tevStage[i6]).colorIn = new byte[4];
        int i7;
        for (i7 = 0; i7 < 4; ) {
          (mat.tevStage[i6]).colorIn[i7] = this.file.readByte();
          i7++;
        } 
        (mat.tevStage[i6]).colorOp = this.file.readByte();
        (mat.tevStage[i6]).colorBias = this.file.readByte();
        (mat.tevStage[i6]).colorScale = this.file.readByte();
        (mat.tevStage[i6]).colorClamp = this.file.readByte();
        (mat.tevStage[i6]).colorRegID = this.file.readByte();
        (mat.tevStage[i6]).alphaIn = new byte[4];
        for (i7 = 0; i7 < 4; ) {
          (mat.tevStage[i6]).alphaIn[i7] = this.file.readByte();
          i7++;
        } 
        (mat.tevStage[i6]).alphaOp = this.file.readByte();
        (mat.tevStage[i6]).alphaBias = this.file.readByte();
        (mat.tevStage[i6]).alphaScale = this.file.readByte();
        (mat.tevStage[i6]).alphaClamp = this.file.readByte();
        (mat.tevStage[i6]).alphaRegID = this.file.readByte();
      } 
      mat.tevSwapMode = new Material.TevSwapModeInfo[mat.numTevStages];
      for (i6 = 0; i6 < mat.numTevStages; i6++) {
        mat.getClass();
        mat.tevSwapMode[i6] = new Material().new TevSwapModeInfo();
        if (tevswap_id[i6] == -1) {
          (mat.tevSwapMode[i6]).rasSel = 0;
          (mat.tevSwapMode[i6]).texSel = 0;
        } else {
          this.file.position(sectionstart + offsets[21] + (tevswap_id[i6] * 4));
          (mat.tevSwapMode[i6]).rasSel = this.file.readByte();
          (mat.tevSwapMode[i6]).texSel = this.file.readByte();
        } 
      } 
      mat.tevSwapTable = new Material.TevSwapModeTable[4];
      for (i6 = 0; i6 < 4; i6++) {
        mat.getClass();
        mat.tevSwapTable[i6] = new Material().new TevSwapModeTable();
        if (tevswaptbl_id[i6] != -1) {
          this.file.position(sectionstart + offsets[22] + (tevswaptbl_id[i6] * 4));
          (mat.tevSwapTable[i6]).r = this.file.readByte();
          (mat.tevSwapTable[i6]).g = this.file.readByte();
          (mat.tevSwapTable[i6]).b = this.file.readByte();
          (mat.tevSwapTable[i6]).a = this.file.readByte();
        } 
      } 
      this.file.position(sectionstart + offsets[24] + (alphacomp_id * 8));
      mat.getClass();
      mat.alphaComp = new Material().new AlphaCompInfo();
      mat.alphaComp.func0 = this.file.readByte();
      mat.alphaComp.ref0 = this.file.readByte() & 0xFF;
      mat.alphaComp.mergeFunc = this.file.readByte();
      mat.alphaComp.func1 = this.file.readByte();
      mat.alphaComp.ref1 = this.file.readByte() & 0xFF;
      this.file.position(sectionstart + offsets[25] + (blendmode_id * 4));
      mat.getClass();
      mat.blendMode = new Material().new BlendModeInfo();
      mat.blendMode.blendMode = this.file.readByte();
      mat.blendMode.srcFactor = this.file.readByte();
      mat.blendMode.dstFactor = this.file.readByte();
      mat.blendMode.blendOp = this.file.readByte();
      if (mat.drawFlag != 1 && mat.drawFlag != 4)
        throw new IOException(String.format("Unknown DrawFlag %1$d for material %2$s", new Object[] { Byte.valueOf(mat.drawFlag), mat.name })); 
    } 
    this.file.position(sectionstart + sectionsize);
  }
  
  private void readMDL3() throws IOException {
    long sectionstart = this.file.position() - 4L;
    int sectionsize = this.file.readInt();
    this.file.position(sectionstart + sectionsize);
  }
  
  private void readTEX1() throws IOException {
    long sectionstart = this.file.position() - 4L;
    int sectionsize = this.file.readInt();
    short numtextures = this.file.readShort();
    this.file.skip(2L);
    this.textures = new Texture[numtextures];
    int entriesoffset = this.file.readInt();
    for (int i = 0; i < numtextures; i++) {
      Texture tex = new Texture();
      this.textures[i] = tex;
      this.file.position(sectionstart + entriesoffset + (i * 32));
      tex.format = this.file.readByte();
      this.file.skip(1L);
      tex.width = this.file.readShort();
      tex.height = this.file.readShort();
      tex.wrapS = this.file.readByte();
      tex.wrapT = this.file.readByte();
      this.file.skip(1L);
      tex.paletteFormat = this.file.readByte();
      short palnumentries = this.file.readShort();
      int paloffset = this.file.readInt();
      this.file.skip(4L);
      tex.minFilter = this.file.readByte();
      tex.magFilter = this.file.readByte();
      this.file.skip(2L);
      tex.mipmapCount = this.file.readByte();
      this.file.skip(3L);
      int dataoffset = this.file.readInt();
      tex.image = DataHelper.decodeTextureData(this.file, sectionstart + dataoffset + 32L + (32 * i), tex.mipmapCount, tex.format, tex.width, tex.height);
    } 
    this.file.position(sectionstart + sectionsize);
  }
  
  public class SceneGraphNode {
    public short materialID;
    
    public int parentIndex;
    
    public int nodeType;
    
    public short nodeID;
  }
  
  public class Batch {
    public byte matrixType;
    
    public Packet[] packets;
    
    public float unk;
    
    public class Packet {
      public List<Primitive> primitives;
      
      public short[] matrixTable;
      
      public class Primitive {
        public int numIndices;
        
        public int primitiveType;
        
        public int arrayMask;
        
        public int[] posMatrixIndices;
        
        public int[] positionIndices;
        
        public int[] normalIndices;
        
        public int[][] colorIndices;
        
        public int[][] texcoordIndices;
      }
    }
  }
  
  public class Packet {
    public List<Primitive> primitives;
    
    public short[] matrixTable;
    
    public class Primitive {
      public int numIndices;
      
      public int primitiveType;
      
      public int arrayMask;
      
      public int[] posMatrixIndices;
      
      public int[] positionIndices;
      
      public int[] normalIndices;
      
      public int[][] colorIndices;
      
      public int[][] texcoordIndices;
    }
  }
  
  public class Primitive {
    public int numIndices;
    
    public int primitiveType;
    
    public int arrayMask;
    
    public int[] posMatrixIndices;
    
    public int[] positionIndices;
    
    public int[] normalIndices;
    
    public int[][] colorIndices;
    
    public int[][] texcoordIndices;
  }
  
  public class MultiMatrix {
    public int numMatrices;
    
    public short[] matrixIndices;
    
    public Matrix4[] matrices;
    
    public float[] matrixWeights;
  }
  
  public class MatrixType {
    public boolean isWeighted;
    
    public short index;
  }
  
  public class Joint {
    public short unk1;
    
    public byte unk2;
    
    public Vector3 scale;
    
    public Vector3 rotation;
    
    public Vector3 translation;
    
    public Matrix4 matrix;
    
    public Matrix4 finalMatrix;
  }
  
  public class Material {
    public String name;
    
    public byte drawFlag;
    
    public byte cullMode;
    
    public int numChans;
    
    public int numTexgens;
    
    public int numTevStages;
    
    public ZModeInfo zMode;
    
    public TexGenInfo[] texGen;
    
    public TexMtxInfo[] texMtx;
    
    public short[] texStages;
    
    public ColorInfo[] constColors;
    
    public byte[] constColorSel;
    
    public byte[] constAlphaSel;
    
    public TevOrderInfo[] tevOrder;
    
    public ColorInfo[] colorS10;
    
    public TevStageInfo[] tevStage;
    
    public TevSwapModeInfo[] tevSwapMode;
    
    public TevSwapModeTable[] tevSwapTable;
    
    public AlphaCompInfo alphaComp;
    
    public BlendModeInfo blendMode;
    
    public class ZModeInfo {
      public boolean enableZTest;
      
      public byte func;
      
      public boolean enableZWrite;
    }
    
    public class TevOrderInfo {
      public byte texcoordID;
      
      public byte texMap;
      
      public byte chanID;
    }
    
    public class ColorInfo {
      public int r;
      
      public int g;
      
      public int b;
      
      public int a;
    }
    
    public class TexGenInfo {
      public byte type;
      
      public byte src;
      
      public byte matrix;
    }
    
    public class TexMtxInfo {
      public short unks1;
      
      public short unks2;
      
      public float[] unkf1;
      
      public short unks3;
      
      public short unks4;
      
      public float[] unkf2;
      
      public float[] unkf3;
      
      public Matrix4 finalMatrix;
    }
    
    public class TevStageInfo {
      public byte[] colorIn;
      
      public byte colorOp;
      
      public byte colorBias;
      
      public byte colorScale;
      
      public byte colorClamp;
      
      public byte colorRegID;
      
      public byte[] alphaIn;
      
      public byte alphaOp;
      
      public byte alphaBias;
      
      public byte alphaScale;
      
      public byte alphaClamp;
      
      public byte alphaRegID;
    }
    
    public class TevSwapModeInfo {
      public byte rasSel;
      
      public byte texSel;
    }
    
    public class TevSwapModeTable {
      public byte r;
      
      public byte g;
      
      public byte b;
      
      public byte a;
    }
    
    public class AlphaCompInfo {
      public byte func0;
      
      public byte func1;
      
      public int ref0;
      
      public int ref1;
      
      public byte mergeFunc;
    }
    
    public class BlendModeInfo {
      public byte blendMode;
      
      public byte srcFactor;
      
      public byte dstFactor;
      
      public byte blendOp;
    }
  }
  
  public class ZModeInfo {
    public boolean enableZTest;
    
    public byte func;
    
    public boolean enableZWrite;
  }
  
  public class TevOrderInfo {
    public byte texcoordID;
    
    public byte texMap;
    
    public byte chanID;
  }
  
  public class ColorInfo {
    public int r;
    
    public int g;
    
    public int b;
    
    public int a;
  }
  
  public class TexGenInfo {
    public byte type;
    
    public byte src;
    
    public byte matrix;
  }
  
  public class TexMtxInfo {
    public short unks1;
    
    public short unks2;
    
    public float[] unkf1;
    
    public short unks3;
    
    public short unks4;
    
    public float[] unkf2;
    
    public float[] unkf3;
    
    public Matrix4 finalMatrix;
  }
  
  public class TevStageInfo {
    public byte[] colorIn;
    
    public byte colorOp;
    
    public byte colorBias;
    
    public byte colorScale;
    
    public byte colorClamp;
    
    public byte colorRegID;
    
    public byte[] alphaIn;
    
    public byte alphaOp;
    
    public byte alphaBias;
    
    public byte alphaScale;
    
    public byte alphaClamp;
    
    public byte alphaRegID;
  }
  
  public class TevSwapModeInfo {
    public byte rasSel;
    
    public byte texSel;
  }
  
  public class TevSwapModeTable {
    public byte r;
    
    public byte g;
    
    public byte b;
    
    public byte a;
  }
  
  public class AlphaCompInfo {
    public byte func0;
    
    public byte func1;
    
    public int ref0;
    
    public int ref1;
    
    public byte mergeFunc;
  }
  
  public class BlendModeInfo {
    public byte blendMode;
    
    public byte srcFactor;
    
    public byte dstFactor;
    
    public byte blendOp;
  }
  
  public class Texture {
    public byte format;
    
    public short width;
    
    public short height;
    
    public byte wrapS;
    
    public byte wrapT;
    
    public byte paletteFormat;
    
    public byte[] palette;
    
    public byte minFilter;
    
    public byte magFilter;
    
    public byte mipmapCount;
    
    public byte[][] image;
  }
}