package whitehole.rendering;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.media.opengl.GL2;
import javax.media.opengl.GLException;
import whitehole.Whitehole;
import whitehole.fileio.RarcFilesystem;
import whitehole.smg.Bti;
import whitehole.vectors.Vector3;

public class BtiRenderer extends GLRenderer {
  private RarcFilesystem container;
  
  private Bti bti;
  
  private int texID;
  
  private Vector3 pt1;
  
  private Vector3 pt2;
  
  private boolean vertical;
  
  private void uploadTexture(GL2 gl) {
    int ifmt, fmt, wrapmodes[] = { 33071, 10497, 33648 };
    int[] minfilters = { 9728, 9729, 9984, 9985, 9986, 9987 };
    int[] magfilters = { 9728, 9729, 9728, 9729, 9728, 9729 };
    int[] texids = new int[1];
    gl.glGenTextures(1, texids, 0);
    this.texID = texids[0];
    gl.glBindTexture(3553, this.texID);
    gl.glTexParameteri(3553, 33085, this.bti.mipmapCount - 1);
    gl.glTexParameteri(3553, 10241, minfilters[this.bti.minFilter]);
    gl.glTexParameteri(3553, 10240, magfilters[this.bti.magFilter]);
    gl.glTexParameteri(3553, 10242, wrapmodes[this.bti.wrapS]);
    gl.glTexParameteri(3553, 10243, wrapmodes[this.bti.wrapT]);
    switch (this.bti.format) {
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
    int width = this.bti.width, height = this.bti.height;
    for (int mip = 0; mip < this.bti.mipmapCount; mip++) {
      gl.glTexImage2D(3553, mip, ifmt, width, height, 0, fmt, 5121, ByteBuffer.wrap(this.bti.image[mip]));
      width /= 2;
      height /= 2;
    } 
  }
  
  public BtiRenderer(GLRenderer.RenderInfo info, String name, Vector3 _pt1, Vector3 _pt2, boolean vert) {
    GL2 gl = info.drawable.getGL().getGL2();
    this.container = null;
    this.bti = null;
    try {
      this.container = new RarcFilesystem(Whitehole.game.filesystem.openFile("/ObjectData/" + name + ".arc"));
      if (this.container.fileExists("/" + name + "/" + name + ".bti")) {
        this.bti = new Bti(this.container.openFile("/" + name + "/" + name + ".bti"));
      } else {
        throw new IOException("No suitable texture file inside RARC");
      } 
    } catch (IOException ex) {
      if (this.container != null)
        try {
          this.container.close();
        } catch (IOException ex2) {} 
      throw new GLException("Failed to load texture " + name + ": " + ex.getMessage());
    } 
    this.pt1 = _pt1;
    this.pt2 = _pt2;
    this.vertical = vert;
    uploadTexture(gl);
  }
  
  public void close(GLRenderer.RenderInfo info) throws GLException {
    GL2 gl = info.drawable.getGL().getGL2();
    gl.glDeleteTextures(1, new int[] { this.texID }, 0);
    if (this.bti != null)
      try {
        this.bti.close();
        this.container.close();
      } catch (IOException ex) {} 
  }
  
  public void releaseStorage() {
    if (this.bti != null) {
      try {
        this.bti.close();
        this.container.close();
      } catch (IOException ex) {}
      this.bti = null;
      this.container = null;
    } 
  }
  
  public boolean gottaRender(GLRenderer.RenderInfo info) throws GLException {
    return (info.renderMode != GLRenderer.RenderMode.TRANSLUCENT);
  }
  
  public void render(GLRenderer.RenderInfo info) throws GLException {
    if (info.renderMode == GLRenderer.RenderMode.TRANSLUCENT)
      return; 
    GL2 gl = info.drawable.getGL().getGL2();
    if (info.renderMode != GLRenderer.RenderMode.PICKING) {
      for (int i = 0; i < 8; i++) {
        try {
          gl.glActiveTexture(33984 + i);
          gl.glDisable(3553);
        } catch (GLException ex) {}
      } 
      gl.glDisable(3553);
      gl.glDepthFunc(515);
      gl.glDepthMask(true);
      gl.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      gl.glDisable(2896);
      gl.glDisable(3042);
      gl.glDisable(3058);
      gl.glDisable(3008);
      try {
        gl.glUseProgram(0);
      } catch (GLException ex) {}
      try {
        gl.glActiveTexture(33984);
      } catch (GLException ex) {}
      gl.glEnable(3553);
      gl.glBindTexture(3553, this.texID);
    } 
    gl.glDisable(2884);
    gl.glBegin(5);
    if (this.vertical) {
      gl.glTexCoord2f(0.0F, 0.0F);
      gl.glVertex3f(this.pt1.x, this.pt1.y, this.pt1.z);
      gl.glTexCoord2f(1.0F, 0.0F);
      gl.glVertex3f(this.pt2.x, this.pt1.y, this.pt2.z);
      gl.glTexCoord2f(0.0F, 1.0F);
      gl.glVertex3f(this.pt1.x, this.pt2.y, this.pt1.z);
      gl.glTexCoord2f(1.0F, 1.0F);
      gl.glVertex3f(this.pt2.x, this.pt2.y, this.pt2.z);
    } else {
      gl.glTexCoord2f(0.0F, 0.0F);
      gl.glVertex3f(this.pt1.x, this.pt1.y, this.pt1.z);
      gl.glTexCoord2f(1.0F, 0.0F);
      gl.glVertex3f(this.pt1.x, this.pt1.y, this.pt2.z);
      gl.glTexCoord2f(0.0F, 1.0F);
      gl.glVertex3f(this.pt2.x, this.pt2.y, this.pt1.z);
      gl.glTexCoord2f(1.0F, 1.0F);
      gl.glVertex3f(this.pt2.x, this.pt2.y, this.pt2.z);
    } 
    gl.glEnd();
  }
}
