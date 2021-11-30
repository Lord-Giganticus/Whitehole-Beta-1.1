package whitehole.vectors;

public class Vector2 {
  public float x;
  
  public float y;
  
  public Vector2() {
    this.x = this.y = 0.0F;
  }
  
  public Vector2(float x, float y) {
    this.x = x;
    this.y = y;
  }
  
  public Vector2(Vector2 copy) {
    this.x = copy.x;
    this.y = copy.y;
  }
  
  public static boolean roughlyEqual(Vector2 a, Vector2 b) {
    float epsilon = 1.0E-5F;
    if (Math.abs(a.x - b.x) > epsilon)
      return false; 
    if (Math.abs(a.y - b.y) > epsilon)
      return false; 
    return true;
  }
  
  public float length() {
    return (float)Math.sqrt((this.x * this.x + this.y * this.y));
  }
  
  public static void normalize(Vector2 v, Vector2 out) {
    float len = v.length();
    if (len < 1.0E-6F)
      len = 1.0F; 
    float x = v.x / len;
    float y = v.y / len;
    out.x = x;
    out.y = y;
  }
  
  public static void add(Vector2 a, Vector2 b, Vector2 out) {
    a.x += b.x;
    a.y += b.y;
  }
  
  public static void subtract(Vector2 a, Vector2 b, Vector2 out) {
    a.x -= b.x;
    a.y -= b.y;
  }
}
