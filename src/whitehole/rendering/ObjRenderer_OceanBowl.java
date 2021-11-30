package whitehole.rendering;

import java.io.IOException;
import javax.media.opengl.GL2;
import javax.media.opengl.GLException;

public class ObjRenderer_OceanBowl extends BmdRenderer {
  public ObjRenderer_OceanBowl(GLRenderer.RenderInfo info) throws IOException {
    super(info, "WaterBowlObject");
  }
  
  public void close(GLRenderer.RenderInfo info) throws GLException {
    super.close(info);
  }
  
  public void render(GLRenderer.RenderInfo info) throws GLException {
    float factor = 15.0F;
    GL2 gl = info.drawable.getGL().getGL2();
    gl.glScalef(1.0F / factor, 1.0F / factor, 1.0F / factor);
    super.render(info);
  }
}
