package whitehole.vectors;

public class Matrix4 {
  public float[] m;
  
  public Matrix4() {
    this.m = new float[16];
    this.m[0] = 1.0F;
    this.m[1] = 0.0F;
    this.m[2] = 0.0F;
    this.m[3] = 0.0F;
    this.m[4] = 0.0F;
    this.m[5] = 1.0F;
    this.m[6] = 0.0F;
    this.m[7] = 0.0F;
    this.m[8] = 0.0F;
    this.m[9] = 0.0F;
    this.m[10] = 1.0F;
    this.m[11] = 0.0F;
    this.m[12] = 0.0F;
    this.m[13] = 0.0F;
    this.m[14] = 0.0F;
    this.m[15] = 1.0F;
  }
  
  public Matrix4(float m0, float m1, float m2, float m3, float m4, float m5, float m6, float m7, float m8, float m9, float m10, float m11, float m12, float m13, float m14, float m15) {
    this.m = new float[16];
    this.m[0] = m0;
    this.m[1] = m1;
    this.m[2] = m2;
    this.m[3] = m3;
    this.m[4] = m4;
    this.m[5] = m5;
    this.m[6] = m6;
    this.m[7] = m7;
    this.m[8] = m8;
    this.m[9] = m9;
    this.m[10] = m10;
    this.m[11] = m11;
    this.m[12] = m12;
    this.m[13] = m13;
    this.m[14] = m14;
    this.m[15] = m15;
  }
  
  public static Matrix4 scale(float factor) {
    return new Matrix4(factor, 0.0F, 0.0F, 0.0F, 0.0F, factor, 0.0F, 0.0F, 0.0F, 0.0F, factor, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public static Matrix4 scale(Vector3 factor) {
    return new Matrix4(factor.x, 0.0F, 0.0F, 0.0F, 0.0F, factor.y, 0.0F, 0.0F, 0.0F, 0.0F, factor.z, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public static Matrix4 createRotationX(float angle) {
    float cos = (float)Math.cos(angle);
    float sin = (float)Math.sin(angle);
    return new Matrix4(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, cos, sin, 0.0F, 0.0F, -sin, cos, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public static Matrix4 createRotationY(float angle) {
    float cos = (float)Math.cos(angle);
    float sin = (float)Math.sin(angle);
    return new Matrix4(cos, 0.0F, -sin, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, sin, 0.0F, cos, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public static Matrix4 createRotationZ(float angle) {
    float cos = (float)Math.cos(angle);
    float sin = (float)Math.sin(angle);
    return new Matrix4(cos, sin, 0.0F, 0.0F, -sin, cos, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
  }
  
  public static Matrix4 createTranslation(Vector3 trans) {
    return new Matrix4(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, trans.x, trans.y, trans.z, 1.0F);
  }
  
  public static void mult(Matrix4 left, Matrix4 right, Matrix4 out) {
    float m0 = left.m[0] * right.m[0] + left.m[1] * right.m[4] + left.m[2] * right.m[8] + left.m[3] * right.m[12];
    float m1 = left.m[0] * right.m[1] + left.m[1] * right.m[5] + left.m[2] * right.m[9] + left.m[3] * right.m[13];
    float m2 = left.m[0] * right.m[2] + left.m[1] * right.m[6] + left.m[2] * right.m[10] + left.m[3] * right.m[14];
    float m3 = left.m[0] * right.m[3] + left.m[1] * right.m[7] + left.m[2] * right.m[11] + left.m[3] * right.m[15];
    float m4 = left.m[4] * right.m[0] + left.m[5] * right.m[4] + left.m[6] * right.m[8] + left.m[7] * right.m[12];
    float m5 = left.m[4] * right.m[1] + left.m[5] * right.m[5] + left.m[6] * right.m[9] + left.m[7] * right.m[13];
    float m6 = left.m[4] * right.m[2] + left.m[5] * right.m[6] + left.m[6] * right.m[10] + left.m[7] * right.m[14];
    float m7 = left.m[4] * right.m[3] + left.m[5] * right.m[7] + left.m[6] * right.m[11] + left.m[7] * right.m[15];
    float m8 = left.m[8] * right.m[0] + left.m[9] * right.m[4] + left.m[10] * right.m[8] + left.m[11] * right.m[12];
    float m9 = left.m[8] * right.m[1] + left.m[9] * right.m[5] + left.m[10] * right.m[9] + left.m[11] * right.m[13];
    float m10 = left.m[8] * right.m[2] + left.m[9] * right.m[6] + left.m[10] * right.m[10] + left.m[11] * right.m[14];
    float m11 = left.m[8] * right.m[3] + left.m[9] * right.m[7] + left.m[10] * right.m[11] + left.m[11] * right.m[15];
    float m12 = left.m[12] * right.m[0] + left.m[13] * right.m[4] + left.m[14] * right.m[8] + left.m[15] * right.m[12];
    float m13 = left.m[12] * right.m[1] + left.m[13] * right.m[5] + left.m[14] * right.m[9] + left.m[15] * right.m[13];
    float m14 = left.m[12] * right.m[2] + left.m[13] * right.m[6] + left.m[14] * right.m[10] + left.m[15] * right.m[14];
    float m15 = left.m[12] * right.m[3] + left.m[13] * right.m[7] + left.m[14] * right.m[11] + left.m[15] * right.m[15];
    out.m[0] = m0;
    out.m[1] = m1;
    out.m[2] = m2;
    out.m[3] = m3;
    out.m[4] = m4;
    out.m[5] = m5;
    out.m[6] = m6;
    out.m[7] = m7;
    out.m[8] = m8;
    out.m[9] = m9;
    out.m[10] = m10;
    out.m[11] = m11;
    out.m[12] = m12;
    out.m[13] = m13;
    out.m[14] = m14;
    out.m[15] = m15;
  }
  
  public static Matrix4 lookAt(Vector3 eye, Vector3 target, Vector3 up) {
    Vector3 z = new Vector3();
    Vector3.subtract(eye, target, z);
    Vector3.normalize(z, z);
    Vector3 x = new Vector3();
    Vector3.cross(up, z, x);
    Vector3.normalize(x, x);
    Vector3 y = new Vector3();
    Vector3.cross(z, x, y);
    Vector3.normalize(y, y);
    Matrix4 rot = new Matrix4(x.x, y.x, z.x, 0.0F, x.y, y.y, z.y, 0.0F, x.z, y.z, z.z, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
    Matrix4 trans = createTranslation(new Vector3(-eye.x, -eye.y, -eye.z));
    mult(trans, rot, trans);
    return trans;
  }
}
