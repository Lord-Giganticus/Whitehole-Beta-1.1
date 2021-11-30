package whitehole;

import java.nio.charset.Charset;
import java.util.prefs.Preferences;
import javax.media.opengl.GLProfile;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import whitehole.rendering.RendererCache;
import whitehole.rendering.ShaderCache;
import whitehole.rendering.TextureCache;
import whitehole.smg.Bcsv;
import whitehole.smg.GameArchive;

public class Whitehole {
  public static final String name = "Whitehole";
  
  public static final String version = "v1.1 PRIVATE BETA";
  
  public static String fullName = "Whitehole v1.1 PRIVATE BETA";
  
  public static final String websiteURL = "http://kuribo64.net/";
  
  public static GameArchive game;
  
  public class UncaughtExceptionHandler {
    public void handle(Throwable throwable) {
      System.out.println(throwable.getMessage());
    }
  }
  
  public static void dorun() {
    if (!Charset.isSupported("SJIS"))
      if (!Preferences.userRoot().getBoolean("charset-alreadyWarned", false)) {
        JOptionPane.showMessageDialog(null, "Shift-JIS encoding isn't supported.\nWhitehole will default to ASCII, which may cause certain strings to look corrupted.\n\nThis message appears only once.", "Whitehole", 2);
        Preferences.userRoot().putBoolean("charset-alreadyWarned", true);
      }  
    Settings.initialize();
    Bcsv.populateHashTable();
    ObjectDB.initialize();
    TextureCache.initialize();
    ShaderCache.initialize();
    RendererCache.initialize();
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ex) {}
    SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            GLProfile.initSingleton();
            (new MainFrame()).setVisible(true);
          }
        });
  }
  
  public static void main(String[] args) {
    boolean catchemall = true;
    if (catchemall) {
      ThreadGroup strictgroup = new StrictThreadGroup();
      (new Thread(strictgroup, "CATCH 'EM ALL") {
          public void run() {
            Whitehole.dorun();
          }
        }).start();
    } else {
      dorun();
    } 
  }
}
