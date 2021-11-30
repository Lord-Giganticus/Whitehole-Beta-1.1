package whitehole.rendering;

public class ObjRenderer_AstroPart extends BmdRenderer {
  public ObjRenderer_AstroPart(GLRenderer.RenderInfo info, String objname, int arg0) {
    String[] parts = { "Observatory", "Well", "Kitchen", "BedRoom", "Machine", "Tower" };
    if (arg0 < 1 || arg0 > 6)
      arg0 = 1; 
    ctor_loadModel(info, objname + parts[arg0 - 1]);
    ctor_uploadData(info);
  }
  
  public boolean boundToObjArg(int arg) {
    if (arg == 0)
      return true; 
    return false;
  }
}
