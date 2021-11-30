package whitehole;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.zip.CRC32;
import javax.swing.JLabel;

public class ObjectDBUpdater extends Thread {
  private JLabel statusLabel;
  
  public ObjectDBUpdater(JLabel status) {
    this.statusLabel = status;
  }
  
  public void run() {
    try {
      long l;
      String ts = String.format("&ts=%1$d", new Object[] { Long.valueOf(ObjectDB.timestamp) });
      URL url = new URL("http://kuribo64.net/whitehole/objectdb.php?whitehole" + ts);
      URLConnection conn = url.openConnection();
      DataInputStream dis = new DataInputStream(conn.getInputStream());
      int length = conn.getContentLength();
      if (length < 8) {
        this.statusLabel.setText("Failed to update object database: received invalid data.");
        return;
      } 
      byte[] data = new byte[length];
      for (int i = 0; i < data.length; i++)
        data[i] = dis.readByte(); 
      Charset charset = Charset.forName("UTF-8");
      CharsetDecoder dec = charset.newDecoder();
      String strdata = dec.decode(ByteBuffer.wrap(data, 0, 8)).toString();
      if (strdata.equals("noupdate")) {
        this.statusLabel.setText("Object database already up-to-date.");
        return;
      } 
      if (data.length < 10) {
        this.statusLabel.setText("Failed to update object database: received invalid data.");
        return;
      } 
      CRC32 crc = new CRC32();
      crc.update(data, 9, data.length - 9);
      try {
        l = Long.parseLong(strdata, 16);
      } catch (NumberFormatException ex) {
        l = -1L;
      } 
      if (crc.getValue() != l) {
        this.statusLabel.setText("Failed to update object database: received invalid data.");
        return;
      } 
      File odbbkp = new File("objectdb.xml.bak");
      File odb = new File("objectdb.xml");
      try {
        if (odb.exists()) {
          odb.renameTo(odbbkp);
          odb.delete();
        } 
        odb.createNewFile();
        FileOutputStream odbstream = new FileOutputStream(odb);
        odbstream.write(data, 9, data.length - 9);
        odbstream.flush();
        odbstream.close();
        if (odbbkp.exists())
          odbbkp.delete(); 
      } catch (IOException ex) {
        this.statusLabel.setText("Failed to save new object database.");
        if (odbbkp.exists())
          odbbkp.renameTo(odb); 
        return;
      } 
      this.statusLabel.setText("Object database updated.");
      ObjectDB.initialize();
    } catch (MalformedURLException ex) {
      this.statusLabel.setText("Failed to connect to update server.");
    } catch (IOException ex) {
      this.statusLabel.setText("Failed to save new object database.");
    } 
  }
}
