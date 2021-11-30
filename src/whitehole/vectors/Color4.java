package whitehole.vectors;

public class Color4 {
  public float r;
  
  public float g;
  
  public float b;
  
  public float a;
  
  public Color4() {
    this.r = this.g = this.b = this.a = 0.0F;
  }
  
  public Color4(float r, float g, float b) {
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = 1.0F;
  }
  
  public Color4(float r, float g, float b, float a) {
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = a;
  }
}
