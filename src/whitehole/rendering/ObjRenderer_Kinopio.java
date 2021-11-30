package whitehole.rendering;

public class ObjRenderer_Kinopio extends BmdRenderer {
  public ObjRenderer_Kinopio(GLRenderer.RenderInfo info, int color) {
    ctor_loadModel(info, "Kinopio");
    switch (color) {
      case 0:
        ((this.model.materials[0]).colorS10[0]).r = -103;
        ((this.model.materials[0]).colorS10[0]).g = -103;
        ((this.model.materials[0]).colorS10[0]).b = 211;
        break;
      case 1:
        ((this.model.materials[0]).colorS10[0]).r = -103;
        ((this.model.materials[0]).colorS10[0]).g = 211;
        ((this.model.materials[0]).colorS10[0]).b = -103;
        break;
      case 2:
        ((this.model.materials[0]).colorS10[0]).r = 211;
        ((this.model.materials[0]).colorS10[0]).g = -103;
        ((this.model.materials[0]).colorS10[0]).b = 211;
        break;
      case 4:
        ((this.model.materials[0]).colorS10[0]).r = 211;
        ((this.model.materials[0]).colorS10[0]).g = 211;
        ((this.model.materials[0]).colorS10[0]).b = -103;
        break;
    } 
    ctor_uploadData(info);
  }
  
  public boolean boundToObjArg(int arg) {
    if (arg == 1)
      return true; 
    return false;
  }
}
