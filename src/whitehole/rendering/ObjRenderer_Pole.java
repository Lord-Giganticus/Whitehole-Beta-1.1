package whitehole.rendering;

import java.io.IOException;
import javax.media.opengl.GL2;
import javax.media.opengl.GLException;
import whitehole.vectors.Vector3;

public class ObjRenderer_Pole extends BmdRenderer {
  private Vector3 myscale;
  
  public ObjRenderer_Pole(GLRenderer.RenderInfo info, Vector3 scale) throws IOException {
    super(info, "Pole");
    this.myscale = scale;
    (this.model.joints[1]).finalMatrix.m[13] = 100.0F * scale.y / scale.x;
  }
  
  public boolean isScaled() {
    return false;
  }
  
  public boolean hasSpecialScaling() {
    return true;
  }
  
  public void render(GLRenderer.RenderInfo info) throws GLException {
    GL2 gl = info.drawable.getGL().getGL2();
    gl.glScalef(this.myscale.x, this.myscale.x, this.myscale.x);
    super.render(info);
  }
}
