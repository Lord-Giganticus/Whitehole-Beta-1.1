package whitehole.smg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import whitehole.fileio.FilesystemBase;

public class GameArchive {
  public FilesystemBase filesystem;
  
  public GameArchive(FilesystemBase fs) {
    this.filesystem = fs;
  }
  
  public void close() {
    try {
      this.filesystem.close();
    } catch (IOException ex) {}
  }
  
  public boolean galaxyExists(String name) {
    return this.filesystem.fileExists(String.format("/StageData/%1$s/%1$sScenario.arc", new Object[] { name }));
  }
  
  public List<String> getGalaxies() {
    List<String> ret = new ArrayList<>();
    List<String> stages = this.filesystem.getDirectories("/StageData");
    for (String stage : stages) {
      if (!galaxyExists(stage))
        continue; 
      ret.add(stage);
    } 
    return ret;
  }
  
  public GalaxyArchive openGalaxy(String name) throws IOException {
    if (!galaxyExists(name))
      return null; 
    return new GalaxyArchive(this, name);
  }
}
