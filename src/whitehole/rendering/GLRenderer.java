package whitehole.rendering;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLException;

public class GLRenderer {
  public int[] displayLists = null;
  
  public void close(RenderInfo info) throws GLException {
    GL2 gl = info.drawable.getGL().getGL2();
    if (this.displayLists != null) {
      gl.glDeleteLists(this.displayLists[0], 1);
      gl.glDeleteLists(this.displayLists[1], 1);
      gl.glDeleteLists(this.displayLists[2], 1);
      this.displayLists = null;
    } 
  }
  
  public void releaseStorage() {}
  
  public boolean isScaled() {
    return true;
  }
  
  public boolean hasSpecialScaling() {
    return false;
  }
  
  public boolean boundToObjArg(int arg) {
    return false;
  }
  
  public boolean gottaRender(RenderInfo info) throws GLException {
    return false;
  }
  
  public void render(RenderInfo info) throws GLException {}
  
  public void compileDisplayLists(RenderInfo info) throws GLException {
    if (this.displayLists != null)
      return; 
    GL2 gl = info.drawable.getGL().getGL2();
    RenderInfo info2 = new RenderInfo();
    info2.drawable = info.drawable;
    this.displayLists = new int[3];
    info2.renderMode = RenderMode.PICKING;
    if (gottaRender(info2)) {
      this.displayLists[0] = gl.glGenLists(1);
      gl.glNewList(this.displayLists[0], 4864);
      render(info2);
      gl.glEndList();
    } else {
      this.displayLists[0] = 0;
    } 
    info2.renderMode = RenderMode.OPAQUE;
    if (gottaRender(info2)) {
      this.displayLists[1] = gl.glGenLists(1);
      gl.glNewList(this.displayLists[1], 4864);
      render(info2);
      gl.glEndList();
    } else {
      this.displayLists[1] = 0;
    } 
    info2.renderMode = RenderMode.TRANSLUCENT;
    if (gottaRender(info2)) {
      this.displayLists[2] = gl.glGenLists(1);
      gl.glNewList(this.displayLists[2], 4864);
      render(info2);
      gl.glEndList();
    } else {
      this.displayLists[2] = 0;
    } 
  }
  
  public enum RenderMode {
    PICKING, OPAQUE, TRANSLUCENT;
  }
  
  public static class RenderInfo {
    public GLAutoDrawable drawable;
    
    public GLRenderer.RenderMode renderMode;
  }
}
