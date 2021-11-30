package whitehole.rendering;

import javax.media.opengl.GL2;
import javax.media.opengl.GLException;
import whitehole.vectors.Color4;

public class ColorCubeRenderer extends GLRenderer {
  private float cubeSize;
  
  private Color4 borderColor;
  
  private Color4 fillColor;
  
  private boolean showAxes;
  
  public ColorCubeRenderer(float size, Color4 border, Color4 fill, boolean axes) {
    this.cubeSize = size;
    this.borderColor = border;
    this.fillColor = fill;
    this.showAxes = axes;
  }
  
  public void close(GLRenderer.RenderInfo info) throws GLException {}
  
  public boolean isScaled() {
    return false;
  }
  
  public boolean gottaRender(GLRenderer.RenderInfo info) throws GLException {
    return (info.renderMode != GLRenderer.RenderMode.TRANSLUCENT);
  }
  
  public void render(GLRenderer.RenderInfo info) throws GLException {
    if (info.renderMode == GLRenderer.RenderMode.TRANSLUCENT)
      return; 
    float s = this.cubeSize / 2.0F;
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
      gl.glColor4f(this.fillColor.r, this.fillColor.g, this.fillColor.b, this.fillColor.a);
      gl.glDisable(2896);
      gl.glDisable(3042);
      gl.glDisable(3058);
      gl.glDisable(3008);
      try {
        gl.glUseProgram(0);
      } catch (GLException ex) {}
    } 
    gl.glEnable(2884);
    gl.glCullFace(1028);
    gl.glBegin(5);
    gl.glVertex3f(-s, -s, -s);
    gl.glVertex3f(-s, s, -s);
    gl.glVertex3f(s, -s, -s);
    gl.glVertex3f(s, s, -s);
    gl.glVertex3f(s, -s, s);
    gl.glVertex3f(s, s, s);
    gl.glVertex3f(-s, -s, s);
    gl.glVertex3f(-s, s, s);
    gl.glVertex3f(-s, -s, -s);
    gl.glVertex3f(-s, s, -s);
    gl.glEnd();
    gl.glBegin(5);
    gl.glVertex3f(-s, s, -s);
    gl.glVertex3f(-s, s, s);
    gl.glVertex3f(s, s, -s);
    gl.glVertex3f(s, s, s);
    gl.glEnd();
    gl.glBegin(5);
    gl.glVertex3f(-s, -s, -s);
    gl.glVertex3f(s, -s, -s);
    gl.glVertex3f(-s, -s, s);
    gl.glVertex3f(s, -s, s);
    gl.glEnd();
    if (info.renderMode != GLRenderer.RenderMode.PICKING) {
      gl.glLineWidth(1.5F);
      gl.glColor4f(this.borderColor.r, this.borderColor.g, this.borderColor.b, this.borderColor.a);
      gl.glBegin(3);
      gl.glVertex3f(s, s, s);
      gl.glVertex3f(-s, s, s);
      gl.glVertex3f(-s, s, -s);
      gl.glVertex3f(s, s, -s);
      gl.glVertex3f(s, s, s);
      gl.glVertex3f(s, -s, s);
      gl.glVertex3f(-s, -s, s);
      gl.glVertex3f(-s, -s, -s);
      gl.glVertex3f(s, -s, -s);
      gl.glVertex3f(s, -s, s);
      gl.glEnd();
      gl.glBegin(1);
      gl.glVertex3f(-s, s, s);
      gl.glVertex3f(-s, -s, s);
      gl.glVertex3f(-s, s, -s);
      gl.glVertex3f(-s, -s, -s);
      gl.glVertex3f(s, s, -s);
      gl.glVertex3f(s, -s, -s);
      gl.glEnd();
      if (this.showAxes) {
        gl.glBegin(1);
        gl.glColor3f(1.0F, 0.0F, 0.0F);
        gl.glVertex3f(0.0F, 0.0F, 0.0F);
        gl.glColor3f(1.0F, 0.0F, 0.0F);
        gl.glVertex3f(s * 2.0F, 0.0F, 0.0F);
        gl.glColor3f(0.0F, 1.0F, 0.0F);
        gl.glVertex3f(0.0F, 0.0F, 0.0F);
        gl.glColor3f(0.0F, 1.0F, 0.0F);
        gl.glVertex3f(0.0F, s * 2.0F, 0.0F);
        gl.glColor3f(0.0F, 0.0F, 1.0F);
        gl.glVertex3f(0.0F, 0.0F, 0.0F);
        gl.glColor3f(0.0F, 0.0F, 1.0F);
        gl.glVertex3f(0.0F, 0.0F, s * 2.0F);
        gl.glEnd();
      } 
    } 
  }
}
