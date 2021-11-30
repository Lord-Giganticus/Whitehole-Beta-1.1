package whitehole.rendering;

public class ObjRenderer_UFOKinoko extends BmdRenderer {
  public ObjRenderer_UFOKinoko(GLRenderer.RenderInfo info, int color) {
    ctor_loadModel(info, "UFOKinoko");
    switch (color) {
      case 1:
        ((this.model.materials[5]).colorS10[0]).r = 30;
        ((this.model.materials[5]).colorS10[0]).g = 220;
        ((this.model.materials[5]).colorS10[0]).b = 30;
        ((this.model.materials[5]).colorS10[1]).r = 32;
        ((this.model.materials[5]).colorS10[1]).g = 121;
        ((this.model.materials[5]).colorS10[1]).b = 32;
        break;
      case 2:
        ((this.model.materials[5]).colorS10[0]).r = 220;
        ((this.model.materials[5]).colorS10[0]).g = 220;
        ((this.model.materials[5]).colorS10[0]).b = 30;
        ((this.model.materials[5]).colorS10[1]).r = 121;
        ((this.model.materials[5]).colorS10[1]).g = 121;
        ((this.model.materials[5]).colorS10[1]).b = 32;
        break;
      case 3:
        ((this.model.materials[5]).colorS10[0]).r = 30;
        ((this.model.materials[5]).colorS10[0]).g = 30;
        ((this.model.materials[5]).colorS10[0]).b = 220;
        ((this.model.materials[5]).colorS10[1]).r = 32;
        ((this.model.materials[5]).colorS10[1]).g = 32;
        ((this.model.materials[5]).colorS10[1]).b = 121;
        break;
      case 4:
        ((this.model.materials[5]).colorS10[0]).r = 220;
        ((this.model.materials[5]).colorS10[0]).g = 30;
        ((this.model.materials[5]).colorS10[0]).b = 220;
        ((this.model.materials[5]).colorS10[1]).r = 121;
        ((this.model.materials[5]).colorS10[1]).g = 32;
        ((this.model.materials[5]).colorS10[1]).b = 121;
        break;
    } 
    ctor_uploadData(info);
  }
  
  public boolean boundToObjArg(int arg) {
    if (arg == 0)
      return true; 
    return false;
  }
}
