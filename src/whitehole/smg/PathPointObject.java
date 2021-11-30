package whitehole.smg;

import javax.media.opengl.GL2;
import whitehole.rendering.ColorCubeRenderer;
import whitehole.rendering.GLRenderer;
import whitehole.vectors.Color4;
import whitehole.vectors.Vector3;

public class PathPointObject {
  public PathObject path;
  
  public Bcsv.Entry data;
  
  public int uniqueID;
  
  public int index;
  
  public Vector3 point0;
  
  public Vector3 point1;
  
  public Vector3 point2;
  
  public int[] displayLists;
  
  public PathPointObject(PathObject path, Bcsv.Entry entry) {
    this.path = path;
    this.data = entry;
    this.uniqueID = -1;
    this.index = ((Short)this.data.get("id")).shortValue();
    this.point0 = new Vector3(((Float)this.data.get("pnt0_x")).floatValue(), ((Float)this.data.get("pnt0_y")).floatValue(), ((Float)this.data.get("pnt0_z")).floatValue());
    this.point1 = new Vector3(((Float)this.data.get("pnt1_x")).floatValue(), ((Float)this.data.get("pnt1_y")).floatValue(), ((Float)this.data.get("pnt1_z")).floatValue());
    this.point2 = new Vector3(((Float)this.data.get("pnt2_x")).floatValue(), ((Float)this.data.get("pnt2_y")).floatValue(), ((Float)this.data.get("pnt2_z")).floatValue());
    this.displayLists = null;
  }
  
  public void save() {
    this.data.put("id", Short.valueOf((short)this.index));
    this.data.put("pnt0_x", Float.valueOf(this.point0.x));
    this.data.put("pnt0_y", Float.valueOf(this.point0.y));
    this.data.put("pnt0_z", Float.valueOf(this.point0.z));
    this.data.put("pnt1_x", Float.valueOf(this.point1.x));
    this.data.put("pnt1_y", Float.valueOf(this.point1.y));
    this.data.put("pnt1_z", Float.valueOf(this.point1.z));
    this.data.put("pnt2_x", Float.valueOf(this.point2.x));
    this.data.put("pnt2_y", Float.valueOf(this.point2.y));
    this.data.put("pnt2_z", Float.valueOf(this.point2.z));
  }
  
  public void render(GLRenderer.RenderInfo info, Color4 color, int what) {
    Vector3 pt;
    if (info.renderMode == GLRenderer.RenderMode.TRANSLUCENT)
      return; 
    GL2 gl = info.drawable.getGL().getGL2();
    if (what == 0) {
      pt = this.point0;
    } else if (what == 1) {
      pt = this.point1;
    } else {
      pt = this.point2;
    } 
    if (info.renderMode == GLRenderer.RenderMode.PICKING) {
      int uniqueid = this.uniqueID + what;
      gl.glColor4ub((byte)(uniqueid >>> 16), (byte)(uniqueid >>> 8), (byte)uniqueid, (byte)-1);
    } 
    gl.glPushMatrix();
    gl.glTranslatef(pt.x, pt.y, pt.z);
    ColorCubeRenderer cube = new ColorCubeRenderer((what == 0) ? 100.0F : 50.0F, new Color4(1.0F, 1.0F, 1.0F, 1.0F), color, false);
    cube.render(info);
    gl.glPopMatrix();
  }
  
  public String toString() {
    return String.format("Point %1$d", new Object[] { Integer.valueOf(this.index) });
  }
}
