package whitehole.vectors;

public class Vector3 {
  public float x;
  
  public float y;
  
  public float z;
  
  public Vector3() {
    this.x = this.y = this.z = 0.0F;
  }
  
  public Vector3(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public Vector3(Vector3 copy) {
    this.x = copy.x;
    this.y = copy.y;
    this.z = copy.z;
  }
  
  public static boolean roughlyEqual(Vector3 a, Vector3 b) {
    float epsilon = 1.0E-5F;
    if (Math.abs(a.x - b.x) > epsilon)
      return false; 
    if (Math.abs(a.y - b.y) > epsilon)
      return false; 
    if (Math.abs(a.z - b.z) > epsilon)
      return false; 
    return true;
  }
  
  public static void transform(Vector3 v, Matrix4 m, Vector3 out) {
    float x = v.x * m.m[0] + v.y * m.m[4] + v.z * m.m[8] + m.m[12];
    float y = v.x * m.m[1] + v.y * m.m[5] + v.z * m.m[9] + m.m[13];
    float z = v.x * m.m[2] + v.y * m.m[6] + v.z * m.m[10] + m.m[14];
    out.x = x;
    out.y = y;
    out.z = z;
  }
  
  public float length() {
    return (float)Math.sqrt((this.x * this.x + this.y * this.y + this.z * this.z));
  }
  
  public static void normalize(Vector3 v, Vector3 out) {
    float len = v.length();
    if (len < 1.0E-6F)
      len = 1.0F; 
    float x = v.x / len;
    float y = v.y / len;
    float z = v.z / len;
    out.x = x;
    out.y = y;
    out.z = z;
  }
  
  public static void add(Vector3 a, Vector3 b, Vector3 out) {
    a.x += b.x;
    a.y += b.y;
    a.z += b.z;
  }
  
  public static void subtract(Vector3 a, Vector3 b, Vector3 out) {
    a.x -= b.x;
    a.y -= b.y;
    a.z -= b.z;
  }
  
  public static void cross(Vector3 a, Vector3 b, Vector3 out) {
    float x = a.y * b.z - a.z * b.y;
    float y = a.z * b.x - a.x * b.z;
    float z = a.x * b.y - a.y * b.x;
    out.x = x;
    out.y = y;
    out.z = z;
  }
}
