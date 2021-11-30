package whitehole.rendering;

import javax.media.opengl.GLException;

public class PlanetRenderer extends GLRenderer {
  private BmdRenderer rendMain;
  
  private BmdRenderer rendWater;
  
  public PlanetRenderer(GLRenderer.RenderInfo info, String planet) throws GLException {
    this.rendMain = new BmdRenderer(info, planet);
    try {
      this.rendWater = new BmdRenderer(info, planet + "Water");
    } catch (GLException ex) {
      this.rendWater = null;
    } 
  }
  
  public void close(GLRenderer.RenderInfo info) throws GLException {
    this.rendMain.close(info);
    if (this.rendWater != null)
      this.rendWater.close(info); 
  }
  
  public boolean gottaRender(GLRenderer.RenderInfo info) throws GLException {
    boolean render = this.rendMain.gottaRender(info);
    if (this.rendWater != null)
      render = (render || this.rendWater.gottaRender(info)); 
    return render;
  }
  
  public void render(GLRenderer.RenderInfo info) throws GLException {
    if (this.rendMain.gottaRender(info))
      this.rendMain.render(info); 
    if (this.rendWater != null && this.rendWater.gottaRender(info))
      this.rendWater.render(info); 
  }
}
