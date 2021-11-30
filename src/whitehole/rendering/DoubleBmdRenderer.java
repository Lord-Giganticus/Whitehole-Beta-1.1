package whitehole.rendering;

import java.io.IOException;
import javax.media.opengl.GL2;
import javax.media.opengl.GLException;
import whitehole.vectors.Vector3;

public class DoubleBmdRenderer extends GLRenderer {
  private BmdRenderer rend1;
  
  private BmdRenderer rend2;
  
  private Vector3 position1;
  
  private Vector3 position2;
  
  public DoubleBmdRenderer(GLRenderer.RenderInfo info, String model1, Vector3 pos1, String model2, Vector3 pos2) throws IOException {
    this.rend1 = new BmdRenderer(info, model1);
    this.position1 = pos1;
    this.rend2 = new BmdRenderer(info, model2);
    this.position2 = pos2;
  }
  
  public void close(GLRenderer.RenderInfo info) throws GLException {
    this.rend1.close(info);
    this.rend2.close(info);
  }
  
  public boolean gottaRender(GLRenderer.RenderInfo info) throws GLException {
    return (this.rend1.gottaRender(info) || this.rend2.gottaRender(info));
  }
  
  public void render(GLRenderer.RenderInfo info) throws GLException {
    GL2 gl = info.drawable.getGL().getGL2();
    if (this.rend1.gottaRender(info)) {
      gl.glTranslatef(this.position1.x, this.position1.y, this.position1.z);
      this.rend1.render(info);
    } 
    if (this.rend2.gottaRender(info)) {
      gl.glTranslatef(this.position2.x, this.position2.y, this.position2.z);
      this.rend2.render(info);
    } 
  }
}
