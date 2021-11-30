package whitehole.rendering;

import whitehole.vectors.Matrix4;
import whitehole.vectors.Vector3;

public class Helper {
  public static Matrix4 SRTToMatrix(Vector3 scale, Vector3 rot, Vector3 trans) {
    Matrix4 ret = new Matrix4();
    Matrix4 mscale = Matrix4.scale(scale);
    Matrix4 mxrot = Matrix4.createRotationX(rot.x);
    Matrix4 myrot = Matrix4.createRotationY(rot.y);
    Matrix4 mzrot = Matrix4.createRotationZ(rot.z);
    Matrix4 mtrans = Matrix4.createTranslation(trans);
    Matrix4.mult(ret, mscale, ret);
    Matrix4.mult(ret, mxrot, ret);
    Matrix4.mult(ret, myrot, ret);
    Matrix4.mult(ret, mzrot, ret);
    Matrix4.mult(ret, mtrans, ret);
    return ret;
  }
}
