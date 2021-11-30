package whitehole;

import java.io.File;
import java.io.PrintStream;
import javax.swing.JOptionPane;

public class StrictThreadGroup extends ThreadGroup {
  public StrictThreadGroup() {
    super("StrictThreadGroup");
  }
  
  public void uncaughtException(Thread t, Throwable e) {
    if (e.getMessage().contains("Method 'gl") && e.getMessage().contains("' not available")) {
      JOptionPane.showMessageDialog(null, e.getMessage() + "\n\n" + "This error is likely caused by an outdated video driver. Update it if possible.", "Whitehole", 0);
      return;
    } 
    JOptionPane.showMessageDialog(null, "An unhandled exception has occured: " + e.getMessage() + "\n" + "Whitehole may be unstable. It is recommended that you close it now. You can try to save your unsaved work before doing so, but at your own risks.\n\n" + "You should report this crash at Kuribo64 (" + "http://kuribo64.net/" + "), providing the detailed report found in whiteholeCrash.txt.", "Whitehole", 0);
    try {
      File report = new File("whiteholeCrash.txt");
      if (report.exists())
        report.delete(); 
      report.createNewFile();
      PrintStream ps = new PrintStream(report);
      ps.append(Whitehole.fullName + " crash report\r\n");
      ps.append("Please report this at Kuribo64 (http://kuribo64.net/) with all the details below\r\n");
      ps.append("--------------------------------------------------------------------------------\r\n\r\n");
      e.printStackTrace(ps);
      ps.close();
    } catch (Exception ex) {}
  }
}
