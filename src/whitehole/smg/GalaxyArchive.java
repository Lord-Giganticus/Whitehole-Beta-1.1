package whitehole.smg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import whitehole.fileio.FilesystemBase;
import whitehole.fileio.RarcFilesystem;

public class GalaxyArchive {
  public GameArchive game;
  
  public FilesystemBase filesystem;
  
  public String galaxyName;
  
  public List<String> zoneList;
  
  public List<Bcsv.Entry> scenarioData;
  
  public GalaxyArchive(GameArchive arc, String name) throws IOException {
    this.game = arc;
    this.filesystem = arc.filesystem;
    this.galaxyName = name;
    this.zoneList = new ArrayList<>();
    RarcFilesystem scenario = new RarcFilesystem(this.filesystem.openFile("/StageData/" + this.galaxyName + "/" + this.galaxyName + "Scenario.arc"));
    Bcsv zonesbcsv = new Bcsv(scenario.openFile(String.format("/%1$sScenario/ZoneList.bcsv", new Object[] { this.galaxyName })));
    for (Bcsv.Entry entry : zonesbcsv.entries)
      this.zoneList.add((String)entry.get("ZoneName")); 
    zonesbcsv.close();
    Bcsv scenariobcsv = new Bcsv(scenario.openFile(String.format("/%1$sScenario/ScenarioData.bcsv", new Object[] { this.galaxyName })));
    this.scenarioData = scenariobcsv.entries;
    scenariobcsv.close();
    scenario.close();
  }
  
  public void close() {}
  
  public ZoneArchive openZone(String name) throws IOException {
    if (!this.zoneList.contains(name))
      return null; 
    return new ZoneArchive(this, name);
  }
}
