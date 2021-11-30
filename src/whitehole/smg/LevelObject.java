package whitehole.smg;

import javax.media.opengl.GL2;
import whitehole.ObjectDB;
import whitehole.PropertyPanel;
import whitehole.rendering.GLRenderer;
import whitehole.rendering.RendererCache;
import whitehole.vectors.Vector3;

public class LevelObject {
  public ZoneArchive zone;
  
  public String directory;
  
  public String layer;
  
  public String file;
  
  public String name;
  
  public Bcsv.Entry data;
  
  public ObjectDB.Object dbInfo;
  
  public GLRenderer renderer;
  
  public int uniqueID;
  
  public Vector3 position;
  
  public Vector3 rotation;
  
  public Vector3 scale;
  
  public void save() {}
  
  public final void loadDBInfo() {
    if (ObjectDB.objects.containsKey(this.name)) {
      this.dbInfo = (ObjectDB.Object)ObjectDB.objects.get(this.name);
    } else {
      this.dbInfo = new ObjectDB.Object();
      this.dbInfo.ID = this.name;
      this.dbInfo.name = "(" + this.name + ")";
      this.dbInfo.category = 0;
      this.dbInfo.games = 3;
    } 
  }
  
  public void initRenderer(GLRenderer.RenderInfo info) {
    if (this.renderer != null)
      return; 
    this.renderer = RendererCache.getObjectRenderer(info, this);
    this.renderer.compileDisplayLists(info);
    this.renderer.releaseStorage();
  }
  
  public void closeRenderer(GLRenderer.RenderInfo info) {
    if (this.renderer == null)
      return; 
    RendererCache.closeObjectRenderer(info, this);
    this.renderer = null;
  }
  
  public void render(GLRenderer.RenderInfo info) {
    GL2 gl = info.drawable.getGL().getGL2();
    gl.glPushMatrix();
    gl.glTranslatef(this.position.x, this.position.y, this.position.z);
    gl.glRotatef(this.rotation.z, 0.0F, 0.0F, 1.0F);
    gl.glRotatef(this.rotation.y, 0.0F, 1.0F, 0.0F);
    gl.glRotatef(this.rotation.x, 1.0F, 0.0F, 0.0F);
    if (this.renderer.isScaled())
      gl.glScalef(this.scale.x, this.scale.y, this.scale.z); 
    int dlid = -1;
    switch (info.renderMode) {
      case PICKING:
        dlid = 0;
        break;
      case OPAQUE:
        dlid = 1;
        break;
      case TRANSLUCENT:
        dlid = 2;
        break;
    } 
    gl.glCallList(this.renderer.displayLists[dlid]);
    gl.glPopMatrix();
  }
  
  public void getProperties(PropertyPanel panel) {}
  
  public String toString() {
    return "LevelObject (did someone forget to override this?)";
  }
}
