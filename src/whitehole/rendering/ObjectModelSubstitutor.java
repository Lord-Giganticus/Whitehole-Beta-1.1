package whitehole.rendering;

import java.io.IOException;
import whitehole.smg.GravityObject;
import whitehole.smg.LevelObject;
import whitehole.vectors.Color4;
import whitehole.vectors.Vector3;

public class ObjectModelSubstitutor {
  public static String substituteModelName(LevelObject obj, String modelname) {
    switch (obj.name) {
      case "BenefitItemOneUp":
        return "KinokoOneUp";
      case "SplashPieceBlock":
        return "CoinBlock";
      case "TimerCoinBlock":
        return "CoinBlock";
      case "Karikari":
        return "Karipon";
      case "SpinCloudItem":
        return "PowerUpCloud";
      case "LuigiIntrusively":
        return "Luigi";
      case "Teresa":
        return "TeresaWater";
    } 
    return modelname;
  }
  
  public static String substituteObjectKey(LevelObject obj, String objectkey) {
    switch (obj.name) {
      case "Pole":
        objectkey = objectkey + String.format("_%1$3f", new Object[] { Float.valueOf(obj.scale.y / obj.scale.x) });
        break;
      case "Kinopio":
      case "KinopioAstro":
        objectkey = String.format("object_Kinopio_%1$d", new Object[] { obj.data.get("Obj_arg1") });
        break;
      case "UFOKinoko":
        objectkey = String.format("object_UFOKinoko_%1$d", new Object[] { obj.data.get("Obj_arg0") });
        break;
      case "AstroDome":
      case "AstroDomeEntrance":
      case "AstroDomeSky":
      case "AstroStarPlate":
        objectkey = objectkey + String.format("_%1$d", new Object[] { obj.data.get("Obj_arg0") });
        break;
    } 
    return objectkey;
  }
  
  public static GLRenderer substituteRenderer(LevelObject obj, GLRenderer.RenderInfo info) {
    try {
      if (obj.getClass() == GravityObject.class)
        return new ColorCubeRenderer(100.0F, new Color4(1.0F, 0.5F, 0.5F, 1.0F), new Color4(0.8F, 0.0F, 0.0F, 1.0F), true); 
      switch (obj.name) {
        case "Patakuri":
          return new DoubleBmdRenderer(info, "Kuribo", new Vector3(), "PatakuriWing", new Vector3(0.0F, 15.0F, -25.0F));
        case "Kinopio":
        case "KinopioAstro":
          return new ObjRenderer_Kinopio(info, ((Integer)obj.data.get("Obj_arg1")).intValue());
        case "UFOKinoko":
          return new ObjRenderer_UFOKinoko(info, ((Integer)obj.data.get("Obj_arg0")).intValue());
        case "Pole":
          return new ObjRenderer_Pole(info, obj.scale);
        case "FlagKoopaA":
          return new BtiRenderer(info, "FlagKoopaA", new Vector3(0.0F, 150.0F, 0.0F), new Vector3(0.0F, -150.0F, 600.0F), true);
        case "AstroDome":
        case "AstroDomeEntrance":
        case "AstroDomeSky":
        case "AstroStarPlate":
          return new ObjRenderer_AstroPart(info, obj.name, ((Integer)obj.data.get("Obj_arg0")).intValue());
        case "RedBlueTurnBlock":
          return new DoubleBmdRenderer(info, "RedBlueTurnBlock", new Vector3(), "RedBlueTurnBlockBase", new Vector3());
      } 
    } catch (IOException ex) {}
    return null;
  }
}
