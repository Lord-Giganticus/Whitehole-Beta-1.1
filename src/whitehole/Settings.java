package whitehole;

import java.util.prefs.Preferences;

public class Settings {
  public static boolean objectDBUpdate;
  
  public static boolean useShaders;
  
  public static boolean fastDrag;
  
  public static boolean reverseRot;
  
  public static void initialize() {
    Preferences prefs = Preferences.userRoot();
    objectDBUpdate = prefs.getBoolean("ObjectDBUpdate", true);
    useShaders = prefs.getBoolean("UseShaders", true);
    fastDrag = prefs.getBoolean("FastDrag", false);
    reverseRot = prefs.getBoolean("ReverseRot", false);
  }
  
  public static void save() {
    Preferences prefs = Preferences.userRoot();
    prefs.putBoolean("ObjectDBUpdate", objectDBUpdate);
    prefs.putBoolean("UseShaders", useShaders);
    prefs.putBoolean("FastDrag", fastDrag);
    prefs.putBoolean("ReverseRot", reverseRot);
  }
}
