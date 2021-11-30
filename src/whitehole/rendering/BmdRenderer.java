package whitehole.rendering;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import javax.media.opengl.GL2;
import javax.media.opengl.GLException;
import whitehole.Settings;
import whitehole.SuperFastHash;
import whitehole.Whitehole;
import whitehole.fileio.RarcFilesystem;
import whitehole.smg.Bmd;
import whitehole.smg.Bva;
import whitehole.vectors.Color4;
import whitehole.vectors.Matrix4;
import whitehole.vectors.Vector2;
import whitehole.vectors.Vector3;

public class BmdRenderer extends GLRenderer {
  private RarcFilesystem container;
  
  protected Bmd model;
  
  protected int[] textures;
  
  protected boolean hasShaders;
  
  protected Shader[] shaders;
  
  protected Bva bva;
  
  private void uploadTexture(GL2 gl, int id) {
    int ifmt, fmt;
    Bmd.Texture tex = this.model.textures[id];
    int hash = 0;
    for (int i = 0; i < tex.mipmapCount; i++)
      hash = (int)SuperFastHash.calculate(tex.image[i], hash, 0, (tex.image[i]).length); 
    this.textures[id] = hash;
    if (TextureCache.containsEntry(Integer.valueOf(hash))) {
      TextureCache.getEntry(Integer.valueOf(hash));
      return;
    } 
    int[] wrapmodes = { 33071, 10497, 33648 };
    int[] minfilters = { 9728, 9729, 9984, 9985, 9986, 9987 };
    int[] magfilters = { 9728, 9729, 9728, 9729, 9728, 9729 };
    int[] texids = new int[1];
    gl.glGenTextures(1, texids, 0);
    int texid = texids[0];
    TextureCache.addEntry(Integer.valueOf(hash), texid);
    gl.glBindTexture(3553, texid);
    gl.glTexParameteri(3553, 33085, tex.mipmapCount - 1);
    gl.glTexParameteri(3553, 10241, minfilters[tex.minFilter]);
    gl.glTexParameteri(3553, 10240, magfilters[tex.magFilter]);
    gl.glTexParameteri(3553, 10242, wrapmodes[tex.wrapS]);
    gl.glTexParameteri(3553, 10243, wrapmodes[tex.wrapT]);
    switch (tex.format) {
      case 0:
      case 1:
        ifmt = 32841;
        fmt = 6409;
        break;
      case 2:
      case 3:
        ifmt = 32837;
        fmt = 6410;
        break;
      default:
        ifmt = 4;
        fmt = 32993;
        break;
    } 
    int width = tex.width, height = tex.height;
    for (int mip = 0; mip < tex.mipmapCount; mip++) {
      gl.glTexImage2D(3553, mip, ifmt, width, height, 0, fmt, 5121, ByteBuffer.wrap(tex.image[mip]));
      width /= 2;
      height /= 2;
    } 
  }
  
  private int shaderHash(int matid) {
    byte[] sigarray = new byte[200];
    ByteBuffer sig = ByteBuffer.wrap(sigarray);
    Bmd.Material mat = this.model.materials[matid];
    sig.put((byte)mat.numTexgens);
    int i;
    for (i = 0; i < mat.numTexgens; i++)
      sig.put((mat.texGen[i]).src); 
    for (i = 0; i < 4; i++) {
      sig.putShort((short)(mat.colorS10[i]).r);
      sig.putShort((short)(mat.colorS10[i]).g);
      sig.putShort((short)(mat.colorS10[i]).b);
      sig.putShort((short)(mat.colorS10[i]).a);
    } 
    for (i = 0; i < 4; i++) {
      sig.put((byte)(mat.constColors[i]).r);
      sig.put((byte)(mat.constColors[i]).g);
      sig.put((byte)(mat.constColors[i]).b);
      sig.put((byte)(mat.constColors[i]).a);
    } 
    sig.put((byte)mat.numTevStages);
    for (i = 0; i < mat.numTevStages; i++) {
      sig.put(mat.constColorSel[i]);
      sig.put(mat.constAlphaSel[i]);
      sig.put((mat.tevOrder[i]).texMap);
      sig.put((mat.tevOrder[i]).texcoordID);
      sig.put((mat.tevStage[i]).colorOp);
      sig.put((mat.tevStage[i]).colorRegID);
      sig.put((mat.tevStage[i]).colorIn[0]);
      sig.put((mat.tevStage[i]).colorIn[1]);
      sig.put((mat.tevStage[i]).colorIn[2]);
      sig.put((mat.tevStage[i]).colorIn[3]);
      if ((mat.tevStage[i]).colorOp < 2) {
        sig.put((mat.tevStage[i]).colorBias);
        sig.put((mat.tevStage[i]).colorScale);
      } 
      sig.put((mat.tevStage[i]).alphaOp);
      sig.put((mat.tevStage[i]).alphaRegID);
      sig.put((mat.tevStage[i]).alphaIn[0]);
      sig.put((mat.tevStage[i]).alphaIn[1]);
      sig.put((mat.tevStage[i]).alphaIn[2]);
      sig.put((mat.tevStage[i]).alphaIn[3]);
      if ((mat.tevStage[i]).alphaOp < 2) {
        sig.put((mat.tevStage[i]).alphaBias);
        sig.put((mat.tevStage[i]).alphaScale);
      } 
    } 
    if (mat.alphaComp.mergeFunc == 1 && (mat.alphaComp.func0 == 7 || mat.alphaComp.func1 == 7)) {
      sig.put((byte)119);
    } else if (mat.alphaComp.mergeFunc == 0 && (mat.alphaComp.func0 == 0 || mat.alphaComp.func1 == 0)) {
      sig.put((byte)0);
    } else {
      int b2 = 3;
      if (mat.alphaComp.mergeFunc == 1) {
        if (mat.alphaComp.func0 == 0) {
          b2 = 2;
        } else if (mat.alphaComp.func1 == 0) {
          b2 = 1;
        } 
      } else if (mat.alphaComp.mergeFunc == 0) {
        if (mat.alphaComp.func0 == 7) {
          b2 = 2;
        } else if (mat.alphaComp.func1 == 7) {
          b2 = 1;
        } 
      } 
      if ((b2 & 0x1) != 0) {
        sig.put((byte)1);
        sig.put(mat.alphaComp.func0);
        sig.put((byte)mat.alphaComp.ref0);
      } 
      if ((b2 & 0x2) != 0) {
        sig.put((byte)2);
        sig.put(mat.alphaComp.func1);
        sig.put((byte)mat.alphaComp.ref1);
      } 
      if (b2 == 3)
        sig.put(mat.alphaComp.mergeFunc); 
    } 
    return (int)SuperFastHash.calculate(sigarray, 0L, 0, sig.position());
  }
  
  private void generateShaders(GL2 gl, int matid) throws GLException {
    this.shaders[matid] = new Shader();
    int hash = shaderHash(matid);
    (this.shaders[matid]).cacheKey = hash;
    if (ShaderCache.containsEntry(Integer.valueOf(hash))) {
      ShaderCache.CacheEntry entry = ShaderCache.getEntry(Integer.valueOf(hash));
      (this.shaders[matid]).vertexShader = entry.vertexID;
      (this.shaders[matid]).fragmentShader = entry.fragmentID;
      (this.shaders[matid]).program = entry.programID;
      return;
    } 
    Locale usa = new Locale("en-US");
    String[] texgensrc = { 
        "normalize(gl_Vertex)", "vec4(gl_Normal,1.0)", "argh", "argh", "gl_MultiTexCoord0", "gl_MultiTexCoord1", "gl_MultiTexCoord2", "gl_MultiTexCoord3", "gl_MultiTexCoord4", "gl_MultiTexCoord5", 
        "gl_MultiTexCoord6", "gl_MultiTexCoord7" };
    String[] outputregs = { "rprev", "r0", "r1", "r2" };
    String[] c_inputregs = { 
        "truncc3(rprev.rgb)", "truncc3(rprev.aaa)", "truncc3(r0.rgb)", "truncc3(r0.aaa)", "truncc3(r1.rgb)", "truncc3(r1.aaa)", "truncc3(r2.rgb)", "truncc3(r2.aaa)", "texcolor.rgb", "texcolor.aaa", 
        "rascolor.rgb", "rascolor.aaa", "vec3(1.0,1.0,1.0)", "vec3(0.5,0.5,0.5)", "konst.rgb", "vec3(0.0,0.0,0.0)" };
    String[] c_inputregsD = { 
        "rprev.rgb", "rprev.aaa", "r0.rgb", "r0.aaa", "r1.rgb", "r1.aaa", "r2.rgb", "r2.aaa", "texcolor.rgb", "texcolor.aaa", 
        "rascolor.rgb", "rascolor.aaa", "vec3(1.0,1.0,1.0)", "vec3(0.5,0.5,0.5)", "konst.rgb", "vec3(0.0,0.0,0.0)" };
    String[] c_konstsel = { 
        "vec3(1.0,1.0,1.0)", "vec3(0.875,0.875,0.875)", "vec3(0.75,0.75,0.75)", "vec3(0.625,0.625,0.625)", "vec3(0.5,0.5,0.5)", "vec3(0.375,0.375,0.375)", "vec3(0.25,0.25,0.25)", "vec3(0.125,0.125,0.125)", "", "", 
        "", "", "k0.rgb", "k1.rgb", "k2.rgb", "k3.rgb", "k0.rrr", "k1.rrr", "k2.rrr", "k3.rrr", 
        "k0.ggg", "k1.ggg", "k2.ggg", "k3.ggg", "k0.bbb", "k1.bbb", "k2.bbb", "k3.bbb", "k0.aaa", "k1.aaa", 
        "k2.aaa", "k3.aaa" };
    String[] a_inputregs = { "truncc1(rprev.a)", "truncc1(r0.a)", "truncc1(r1.a)", "truncc1(r2.a)", "texcolor.a", "rascolor.a", "konst.a", "0.0" };
    String[] a_inputregsD = { "rprev.a", "r0.a", "r1.a", "r2.a", "texcolor.a", "rascolor.a", "konst.a", "0.0" };
    String[] a_konstsel = { 
        "1.0", "0.875", "0.75", "0.625", "0.5", "0.375", "0.25", "0.125", "", "", 
        "", "", "", "", "", "", "k0.r", "k1.r", "k2.r", "k3.r", 
        "k0.g", "k1.g", "k2.g", "k3.g", "k0.b", "k1.b", "k2.b", "k3.b", "k0.a", "k1.a", 
        "k2.a", "k3.a" };
    String[] tevbias = { "0.0", "0.5", "-0.5", "## ILLEGAL TEV BIAS ##" };
    String[] tevscale = { "1.0", "2.0", "4.0", "0.5" };
    String[] alphacompare = { "0 == 1", "%1$s < %2$f", "%1$s == %2$f", "%1$s <= %2$f", "%1$s > %2$f", "%1$s != %2$f", "%1$s >= %2$f", "1 == 1" };
    String[] alphacombine = { "(%1$s) && (%2$s)", "(%1$s) || (%2$s)", "((%1$s) && (!(%2$s))) || ((!(%1$s)) && (%2$s))", "((%1$s) && (%2$s)) || ((!(%1$s)) && (!(%2$s)))" };
    Bmd.Material mat = this.model.materials[matid];
    StringBuilder vert = new StringBuilder();
    vert.append("#version 120\n");
    vert.append("\n");
    vert.append("void main()\n");
    vert.append("{\n");
    vert.append("    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n");
    vert.append("    gl_FrontColor = gl_Color;\n");
    vert.append("    gl_FrontSecondaryColor = gl_SecondaryColor;\n");
    for (int i = 0; i < mat.numTexgens; i++) {
      int mtxid = (mat.texGen[i]).matrix;
      String thematrix = "";
      vert.append(String.format("    gl_TexCoord[%1$d] = %2$s;// %3$s;\n", new Object[] { Integer.valueOf(i), texgensrc[(mat.texGen[i]).src], thematrix }));
    } 
    vert.append("}\n");
    int vertid = gl.glCreateShader(35633);
    (this.shaders[matid]).vertexShader = vertid;
    gl.glShaderSource(vertid, 1, new String[] { vert.toString() }, new int[] { vert.length() }, 0);
    gl.glCompileShader(vertid);
    int[] sillyarray = new int[1];
    gl.glGetShaderiv(vertid, 35713, sillyarray, 0);
    int success = sillyarray[0];
    if (success == 0) {
      CharBuffer charBuffer;
      gl.glGetShaderiv(vertid, 35716, sillyarray, 0);
      int loglength = sillyarray[0];
      byte[] _log = new byte[loglength];
      gl.glGetShaderInfoLog(vertid, loglength, sillyarray, 0, _log, 0);
      try {
        charBuffer = Charset.forName("ASCII").newDecoder().decode(ByteBuffer.wrap(_log));
      } catch (Exception ex) {
        charBuffer = CharBuffer.wrap("lolfail");
      } 
      throw new GLException("!Failed to compile vertex shader: " + charBuffer.toString() + "\n" + vert.toString());
    } 
    StringBuilder frag = new StringBuilder();
    frag.append("#version 120\n");
    frag.append("\n");
    int j;
    for (j = 0; j < 8; j++) {
      if (mat.texStages[j] != -1)
        frag.append(String.format("uniform sampler2D texture%1$d;\n", new Object[] { Integer.valueOf(j) })); 
    } 
    frag.append("\n");
    frag.append("float truncc1(float c)\n");
    frag.append("{\n");
    frag.append("    return (c == 0.0) ? 0.0 : ((fract(c) == 0.0) ? 1.0 : fract(c));\n");
    frag.append("}\n");
    frag.append("\n");
    frag.append("vec3 truncc3(vec3 c)\n");
    frag.append("{\n");
    frag.append("    return vec3(truncc1(c.r), truncc1(c.g), truncc1(c.b));\n");
    frag.append("}\n");
    frag.append("\n");
    frag.append("void main()\n");
    frag.append("{\n");
    for (j = 0; j < 4; j++) {
      int _i = (j == 0) ? 3 : (j - 1);
      frag.append(String.format(usa, "    vec4 %1$s = vec4(%2$f, %3$f, %4$f, %5$f);\n", new Object[] { outputregs[j], Float.valueOf((mat.colorS10[_i]).r / 255.0F), Float.valueOf((mat.colorS10[_i]).g / 255.0F), Float.valueOf((mat.colorS10[_i]).b / 255.0F), Float.valueOf((mat.colorS10[_i]).a / 255.0F) }));
    } 
    for (j = 0; j < 4; j++) {
      frag.append(String.format(usa, "    vec4 k%1$d = vec4(%2$f, %3$f, %4$f, %5$f);\n", new Object[] { Integer.valueOf(j), Float.valueOf((mat.constColors[j]).r / 255.0F), Float.valueOf((mat.constColors[j]).g / 255.0F), Float.valueOf((mat.constColors[j]).b / 255.0F), Float.valueOf((mat.constColors[j]).a / 255.0F) }));
    } 
    frag.append("    vec4 texcolor, rascolor, konst;\n");
    for (j = 0; j < mat.numTevStages; j++) {
      frag.append(String.format("\n    // TEV stage %1$d\n", new Object[] { Integer.valueOf(j) }));
      if (mat.constColorSel[j] != -1)
        frag.append("    konst.rgb = " + c_konstsel[mat.constColorSel[j]] + ";\n"); 
      if (mat.constAlphaSel[j] != -1)
        frag.append("    konst.a = " + a_konstsel[mat.constAlphaSel[j]] + ";\n"); 
      if ((mat.tevOrder[j]).texMap != -1 && (mat.tevOrder[j]).texcoordID != -1)
        frag.append(String.format("    texcolor = texture2D(texture%1$d, gl_TexCoord[%2$d].st);\n", new Object[] { Byte.valueOf((mat.tevOrder[j]).texMap), Byte.valueOf((mat.tevOrder[j]).texcoordID) })); 
      frag.append("    rascolor = gl_Color;\n");
      String rout = outputregs[(mat.tevStage[j]).colorRegID] + ".rgb";
      String a = c_inputregs[(mat.tevStage[j]).colorIn[0]];
      String b = c_inputregs[(mat.tevStage[j]).colorIn[1]];
      String c = c_inputregs[(mat.tevStage[j]).colorIn[2]];
      String d = c_inputregsD[(mat.tevStage[j]).colorIn[3]];
      String operation;
      switch ((mat.tevStage[j]).colorOp) {
        case 0:
          operation = "    %1$s = (%5$s + mix(%2$s,%3$s,%4$s) + vec3(%6$s,%6$s,%6$s)) * vec3(%7$s,%7$s,%7$s);\n";
          if ((mat.tevStage[j]).colorClamp != 0)
            operation = operation + "    %1$s = clamp(%1$s, vec3(0.0,0.0,0.0), vec3(1.0,1.0,1.0));\n"; 
          break;
        case 1:
          operation = "    %1$s = (%5$s - mix(%2$s,%3$s,%4$s) + vec3(%6$s,%6$s,%6$s)) * vec3(%7$s,%7$s,%7$s);\n";
          if ((mat.tevStage[j]).colorClamp != 0)
            operation = operation + "    %1$s = clamp(%1$s, vec3(0.0,0.0,0.0), vec3(1.0,1.0,1.0));\n"; 
          break;
        case 8:
          operation = "    %1$s = %5$s + (((%2$s).r > (%3$s).r) ? %4$s : vec3(0.0,0.0,0.0));\n";
          break;
        default:
          operation = "    %1$s = vec3(1.0,0.0,1.0);\n";
          System.out.println("COLOROP ARGH");
          System.out.println((mat.tevStage[j]).colorOp);
          throw new GLException(String.format("!colorop %1$d", new Object[] { Byte.valueOf((mat.tevStage[j]).colorOp) }));
      } 
      operation = String.format(operation, new Object[] { rout, a, b, c, d, tevbias[(mat.tevStage[j]).colorBias], tevscale[(mat.tevStage[j]).colorScale] });
      frag.append(operation);
      rout = outputregs[(mat.tevStage[j]).alphaRegID] + ".a";
      a = a_inputregs[(mat.tevStage[j]).alphaIn[0]];
      b = a_inputregs[(mat.tevStage[j]).alphaIn[1]];
      c = a_inputregs[(mat.tevStage[j]).alphaIn[2]];
      d = a_inputregsD[(mat.tevStage[j]).alphaIn[3]];
      switch ((mat.tevStage[j]).alphaOp) {
        case 0:
          operation = "    %1$s = (%5$s + mix(%2$s,%3$s,%4$s) + %6$s) * %7$s;\n";
          if ((mat.tevStage[j]).alphaClamp != 0)
            operation = operation + "   %1$s = clamp(%1$s, 0.0, 1.0);\n"; 
          break;
        case 1:
          operation = "    %1$s = (%5$s - mix(%2$s,%3$s,%4$s) + %6$s) * %7$s;\n";
          if ((mat.tevStage[j]).alphaClamp != 0)
            operation = operation + "   %1$s = clamp(%1$s, 0.0, 1.0);\n"; 
          break;
        default:
          operation = "    %1$s = 1.0;";
          System.out.println("ALPHAOP ARGH");
          System.out.println((mat.tevStage[j]).alphaOp);
          throw new GLException(String.format("!alphaop %1$d", new Object[] { Byte.valueOf((mat.tevStage[j]).alphaOp) }));
      } 
      operation = String.format(operation, new Object[] { rout, a, b, c, d, tevbias[(mat.tevStage[j]).alphaBias], tevscale[(mat.tevStage[j]).alphaScale] });
      frag.append(operation);
    } 
    frag.append("\n");
    frag.append("   gl_FragColor.rgb = truncc3(rprev.rgb);\n");
    frag.append("   gl_FragColor.a = truncc1(rprev.a);\n");
    frag.append("\n");
    frag.append("    // Alpha test\n");
    if (mat.alphaComp.mergeFunc != 1 || (mat.alphaComp.func0 != 7 && mat.alphaComp.func1 != 7))
      if (mat.alphaComp.mergeFunc == 0 && (mat.alphaComp.func0 == 0 || mat.alphaComp.func1 == 0)) {
        frag.append("    discard;\n");
      } else {
        String compare0 = String.format(usa, alphacompare[mat.alphaComp.func0], new Object[] { "gl_FragColor.a", Float.valueOf(mat.alphaComp.ref0 / 255.0F) });
        String compare1 = String.format(usa, alphacompare[mat.alphaComp.func1], new Object[] { "gl_FragColor.a", Float.valueOf(mat.alphaComp.ref1 / 255.0F) });
        String fullcompare = "";
        if (mat.alphaComp.mergeFunc == 1) {
          if (mat.alphaComp.func0 == 0) {
            fullcompare = compare1;
          } else if (mat.alphaComp.func1 == 0) {
            fullcompare = compare0;
          } 
        } else if (mat.alphaComp.mergeFunc == 0) {
          if (mat.alphaComp.func0 == 7) {
            fullcompare = compare1;
          } else if (mat.alphaComp.func1 == 7) {
            fullcompare = compare0;
          } 
        } 
        if (fullcompare.equals(""))
          fullcompare = String.format(alphacombine[mat.alphaComp.mergeFunc], new Object[] { compare0, compare1 }); 
        frag.append("    if (!(" + fullcompare + ")) discard;\n");
      }  
    frag.append("}\n");
    int fragid = gl.glCreateShader(35632);
    (this.shaders[matid]).fragmentShader = fragid;
    String lol = frag.toString();
    gl.glShaderSource(fragid, 1, new String[] { frag.toString() }, new int[] { frag.length() }, 0);
    gl.glCompileShader(fragid);
    gl.glGetShaderiv(fragid, 35713, sillyarray, 0);
    success = sillyarray[0];
    if (success == 0) {
      CharBuffer charBuffer;
      gl.glGetShaderiv(fragid, 35716, sillyarray, 0);
      int loglength = sillyarray[0];
      byte[] _log = new byte[loglength];
      gl.glGetShaderInfoLog(fragid, loglength, sillyarray, 0, _log, 0);
      try {
        charBuffer = Charset.forName("ASCII").newDecoder().decode(ByteBuffer.wrap(_log));
      } catch (Exception ex) {
        charBuffer = CharBuffer.wrap("lolfail");
      } 
      throw new GLException("!Failed to compile fragment shader: " + charBuffer.toString() + "\n" + frag.toString());
    } 
    int sid = gl.glCreateProgram();
    (this.shaders[matid]).program = sid;
    gl.glAttachShader(sid, vertid);
    gl.glAttachShader(sid, fragid);
    gl.glLinkProgram(sid);
    gl.glGetProgramiv(sid, 35714, sillyarray, 0);
    success = sillyarray[0];
    if (success == 0) {
      String log = "TODO: port this shit from C#";
      throw new GLException("!Failed to link shader program: " + log);
    } 
    ShaderCache.addEntry(Integer.valueOf(hash), vertid, fragid, sid);
  }
  
  public BmdRenderer() {
    this.container = null;
    this.model = null;
  }
  
  public BmdRenderer(GLRenderer.RenderInfo info, String modelname) throws GLException {
    ctor_loadModel(info, modelname);
    ctor_uploadData(info);
  }
  
  protected final void ctor_loadModel(GLRenderer.RenderInfo info, String modelname) throws GLException {
    GL2 gl = info.drawable.getGL().getGL2();
    this.container = null;
    this.model = null;
    try {
      this.container = new RarcFilesystem(Whitehole.game.filesystem.openFile("/ObjectData/" + modelname + ".arc"));
      if (this.container.fileExists("/" + modelname + "/" + modelname + ".bdl")) {
        this.model = new Bmd(this.container.openFile("/" + modelname + "/" + modelname + ".bdl"));
      } else if (this.container.fileExists("/" + modelname + "/" + modelname + ".bmd")) {
        this.model = new Bmd(this.container.openFile("/" + modelname + "/" + modelname + ".bmd"));
      } else {
        throw new IOException("No suitable model file inside RARC");
      } 
    } catch (IOException ex) {
      if (this.container != null)
        try {
          this.container.close();
        } catch (IOException ex2) {} 
      throw new GLException("Failed to load model " + modelname + ": " + ex.getMessage());
    } 
    this.bva = null;
    try {
      if (this.container.fileExists("/" + modelname + "/Wait.bva")) {
        this.bva = new Bva(this.container.openFile("/" + modelname + "/Wait.bva"));
      } else if (this.container.fileExists("/" + modelname + "/Normal.bva")) {
        this.bva = new Bva(this.container.openFile("/" + modelname + "/Normal.bva"));
      } 
    } catch (IOException ex) {}
  }
  
  protected final void ctor_uploadData(GLRenderer.RenderInfo info) throws GLException {
    GL2 gl = info.drawable.getGL().getGL2();
    String extensions = gl.glGetString(7939);
    this.hasShaders = (extensions.contains("GL_ARB_shading_language_100") && extensions.contains("GL_ARB_shader_objects") && extensions.contains("GL_ARB_vertex_shader") && extensions.contains("GL_ARB_fragment_shader"));
    this.hasShaders = (this.hasShaders && Settings.useShaders);
    this.textures = new int[this.model.textures.length];
    int i;
    for (i = 0; i < this.model.textures.length; i++)
      uploadTexture(gl, i); 
    if (this.hasShaders) {
      this.shaders = new Shader[this.model.materials.length];
      for (i = 0; i < this.model.materials.length; i++) {
        try {
          generateShaders(gl, i);
        } catch (GLException ex) {
          if (ex.getMessage().charAt(0) == '!')
            throw ex; 
          (this.shaders[i]).program = 0;
        } 
      } 
    } 
  }
  
  public void close(GLRenderer.RenderInfo info) throws GLException {
    GL2 gl = info.drawable.getGL().getGL2();
    if (this.hasShaders)
      for (Shader shader : this.shaders) {
        if (ShaderCache.removeEntry(Integer.valueOf(shader.cacheKey))) {
          if (shader.vertexShader > 0) {
            gl.glDetachShader(shader.program, shader.vertexShader);
            gl.glDeleteShader(shader.vertexShader);
          } 
          if (shader.fragmentShader > 0) {
            gl.glDetachShader(shader.program, shader.fragmentShader);
            gl.glDeleteShader(shader.fragmentShader);
          } 
          if (shader.program > 0)
            gl.glDeleteProgram(shader.program); 
        } 
      }  
    for (int tex : this.textures) {
      int theid = TextureCache.getTextureID(Integer.valueOf(tex));
      if (TextureCache.removeEntry(Integer.valueOf(tex)))
        gl.glDeleteTextures(1, new int[] { theid }, 0); 
    } 
    if (this.model != null)
      try {
        this.model.close();
        this.container.close();
      } catch (IOException ex) {} 
  }
  
  public void releaseStorage() {
    if (this.model != null) {
      try {
        this.model.close();
        this.container.close();
      } catch (IOException ex) {}
      this.model = null;
      this.container = null;
    } 
  }
  
  public boolean gottaRender(GLRenderer.RenderInfo info) throws GLException {
    if (info.renderMode == GLRenderer.RenderMode.PICKING)
      return true; 
    for (Bmd.Material mat : this.model.materials) {
      if ((((mat.drawFlag == 4) ? 1 : 0) ^ ((info.renderMode == GLRenderer.RenderMode.TRANSLUCENT) ? 1 : 0)) == 0)
        return true; 
    } 
    return false;
  }
  
  public void render(GLRenderer.RenderInfo info) throws GLException {
    GL2 gl = info.drawable.getGL().getGL2();
    int[] blendsrc = { 0, 1, 1, 0, 770, 771, 772, 773, 774, 775 };
    int[] blenddst = { 0, 1, 768, 769, 770, 771, 772, 773, 774, 775 };
    int[] logicop = { 
        5376, 5377, 5378, 5379, 5380, 5381, 5382, 5383, 5384, 5385, 
        5386, 5387, 5388, 5389, 5390, 5391 };
    Matrix4[] lastmatrixtable = null;
    if (info.renderMode != GLRenderer.RenderMode.PICKING)
      gl.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); 
    for (Bmd.SceneGraphNode node : this.model.sceneGraph) {
      if (node.nodeType != 0)
        continue; 
      int shape = node.nodeID;
      if (this.bva != null)
        if (!((Boolean)((List<Boolean>)this.bva.animData.get(shape)).get(0)).booleanValue())
          continue;  
      if (node.materialID != 65535) {
        int[] cullmodes = { 1028, 1029, 1032 };
        int[] depthfuncs = { 512, 513, 514, 515, 516, 517, 518, 519 };
        Bmd.Material mat = this.model.materials[node.materialID];
        if (info.renderMode != GLRenderer.RenderMode.PICKING) {
          if ((((mat.drawFlag == 4) ? 1 : 0) ^ ((info.renderMode == GLRenderer.RenderMode.TRANSLUCENT) ? 1 : 0)) != 0)
            continue; 
          if (this.hasShaders) {
            gl.glUseProgram((this.shaders[node.materialID]).program);
            for (int i = 0; i < 8; i++) {
              gl.glActiveTexture(33984 + i);
              if (mat.texStages[i] == -1) {
                gl.glDisable(3553);
              } else {
                int loc = gl.glGetUniformLocation((this.shaders[node.materialID]).program, String.format("texture%1$d", new Object[] { Integer.valueOf(i) }));
                gl.glUniform1i(loc, i);
                int texid = TextureCache.getTextureID(Integer.valueOf(this.textures[mat.texStages[i]]));
                gl.glEnable(3553);
                gl.glBindTexture(3553, texid);
              } 
            } 
          } else {
            int[] alphafunc = { 512, 513, 514, 515, 516, 517, 518, 519 };
            try {
              gl.glActiveTexture(33984);
            } catch (GLException ex) {}
            if (mat.texStages[0] != -1) {
              int texid = TextureCache.getTextureID(Integer.valueOf(this.textures[mat.texStages[0]]));
              gl.glEnable(3553);
              gl.glBindTexture(3553, texid);
            } else {
              gl.glDisable(3553);
            } 
            if (mat.alphaComp.mergeFunc == 1 && (mat.alphaComp.func0 == 7 || mat.alphaComp.func1 == 7)) {
              gl.glDisable(3008);
            } else if (mat.alphaComp.mergeFunc == 0 && (mat.alphaComp.func0 == 0 || mat.alphaComp.func1 == 0)) {
              gl.glEnable(3008);
              gl.glAlphaFunc(512, 0.0F);
            } else {
              gl.glEnable(3008);
              if ((mat.alphaComp.mergeFunc == 1 && mat.alphaComp.func0 == 0) || (mat.alphaComp.mergeFunc == 0 && mat.alphaComp.func0 == 7)) {
                gl.glAlphaFunc(alphafunc[mat.alphaComp.func1], mat.alphaComp.ref1 / 255.0F);
              } else {
                gl.glAlphaFunc(alphafunc[mat.alphaComp.func0], mat.alphaComp.ref0 / 255.0F);
              } 
            } 
          } 
          switch (mat.blendMode.blendMode) {
            case 0:
              gl.glDisable(3042);
              gl.glDisable(3058);
              break;
            case 1:
            case 3:
              gl.glEnable(3042);
              gl.glDisable(3058);
              if (mat.blendMode.blendMode == 3) {
                gl.glBlendEquation(32778);
              } else {
                gl.glBlendEquation(32774);
              } 
              gl.glBlendFunc(blendsrc[mat.blendMode.srcFactor], blenddst[mat.blendMode.dstFactor]);
              break;
            case 2:
              gl.glDisable(3042);
              gl.glEnable(3058);
              gl.glLogicOp(logicop[mat.blendMode.blendOp]);
              break;
          } 
        } 
        if (mat.cullMode == 0) {
          gl.glDisable(2884);
        } else {
          gl.glEnable(2884);
          gl.glCullFace(cullmodes[mat.cullMode - 1]);
        } 
        if (mat.zMode.enableZTest) {
          gl.glEnable(2929);
          gl.glDepthFunc(depthfuncs[mat.zMode.func]);
        } else {
          gl.glDisable(2929);
        } 
        gl.glDepthMask(mat.zMode.enableZWrite);
      } else {
        throw new GLException(String.format("Material-less geometry node %1$d", new Object[] { Short.valueOf(node.nodeID) }));
      } 
      Bmd.Batch batch = this.model.batches[shape];
      for (Bmd.Batch.Packet packet : batch.packets) {
        Matrix4[] mtxtable = new Matrix4[packet.matrixTable.length];
        int[] mtx_debug = new int[packet.matrixTable.length];
        for (int i = 0; i < packet.matrixTable.length; i++) {
          if (packet.matrixTable[i] == -1) {
            mtxtable[i] = lastmatrixtable[i];
            mtx_debug[i] = 2;
          } else {
            Bmd.MatrixType mtxtype = this.model.matrixTypes[packet.matrixTable[i]];
            if (mtxtype.isWeighted) {
              mtxtable[i] = new Matrix4();
              mtx_debug[i] = 1;
            } else {
              mtxtable[i] = (this.model.joints[mtxtype.index]).finalMatrix;
              mtx_debug[i] = 0;
            } 
          } 
        } 
        lastmatrixtable = mtxtable;
        for (Bmd.Batch.Packet.Primitive prim : packet.primitives) {
          int[] primtypes = { 7, 0, 4, 5, 6, 1, 3, 0 };
          gl.glBegin(primtypes[(prim.primitiveType - 128) / 8]);
          if (info.renderMode != GLRenderer.RenderMode.PICKING) {
            for (int j = 0; j < prim.numIndices; j++) {
              if ((prim.arrayMask & 0x800) != 0) {
                Color4 c = this.model.colorArray[0][prim.colorIndices[0][j]];
                gl.glColor4f(c.r, c.g, c.b, c.a);
              } 
              if (this.hasShaders) {
                if ((prim.arrayMask & 0x1000) != 0) {
                  Color4 c = this.model.colorArray[1][prim.colorIndices[1][j]];
                  gl.glSecondaryColor3f(c.r, c.g, c.b);
                } 
                if ((prim.arrayMask & 0x2000) != 0) {
                  Vector2 t = this.model.texcoordArray[0][prim.texcoordIndices[0][j]];
                  gl.glMultiTexCoord2f(33984, t.x, t.y);
                } 
                if ((prim.arrayMask & 0x4000) != 0) {
                  Vector2 t = this.model.texcoordArray[1][prim.texcoordIndices[1][j]];
                  gl.glMultiTexCoord2f(33985, t.x, t.y);
                } 
                if ((prim.arrayMask & 0x8000) != 0) {
                  Vector2 t = this.model.texcoordArray[2][prim.texcoordIndices[2][j]];
                  gl.glMultiTexCoord2f(33986, t.x, t.y);
                } 
                if ((prim.arrayMask & 0x10000) != 0) {
                  Vector2 t = this.model.texcoordArray[3][prim.texcoordIndices[3][j]];
                  gl.glMultiTexCoord2f(33987, t.x, t.y);
                } 
                if ((prim.arrayMask & 0x20000) != 0) {
                  Vector2 t = this.model.texcoordArray[4][prim.texcoordIndices[4][j]];
                  gl.glMultiTexCoord2f(33988, t.x, t.y);
                } 
                if ((prim.arrayMask & 0x40000) != 0) {
                  Vector2 t = this.model.texcoordArray[5][prim.texcoordIndices[5][j]];
                  gl.glMultiTexCoord2f(33989, t.x, t.y);
                } 
                if ((prim.arrayMask & 0x80000) != 0) {
                  Vector2 t = this.model.texcoordArray[6][prim.texcoordIndices[6][j]];
                  gl.glMultiTexCoord2f(33990, t.x, t.y);
                } 
                if ((prim.arrayMask & 0x100000) != 0) {
                  Vector2 t = this.model.texcoordArray[7][prim.texcoordIndices[7][j]];
                  gl.glMultiTexCoord2f(33991, t.x, t.y);
                } 
              } else if ((prim.arrayMask & 0x2000) != 0) {
                Vector2 t = this.model.texcoordArray[0][prim.texcoordIndices[0][j]];
                gl.glTexCoord2f(t.x, t.y);
              } 
              if ((prim.arrayMask & 0x400) != 0) {
                Vector3 n = this.model.normalArray[prim.normalIndices[j]];
                gl.glNormal3f(n.x, n.y, n.z);
              } 
              Vector3 pos = new Vector3(this.model.positionArray[prim.positionIndices[j]]);
              if ((prim.arrayMask & 0x1) != 0) {
                Vector3.transform(pos, mtxtable[prim.posMatrixIndices[j]], pos);
              } else {
                Vector3.transform(pos, mtxtable[0], pos);
              } 
              gl.glVertex3f(pos.x, pos.y, pos.z);
            } 
          } else {
            for (int j = 0; j < prim.numIndices; j++) {
              Vector3 pos = new Vector3(this.model.positionArray[prim.positionIndices[j]]);
              if ((prim.arrayMask & 0x1) != 0) {
                Vector3.transform(pos, mtxtable[prim.posMatrixIndices[j]], pos);
              } else {
                Vector3.transform(pos, mtxtable[0], pos);
              } 
              gl.glVertex3f(pos.x, pos.y, pos.z);
            } 
          } 
          gl.glEnd();
        } 
      } 
    } 
  }
  
  protected class Shader {
    public int program;
    
    public int vertexShader;
    
    public int fragmentShader;
    
    public int cacheKey;
  }
}
