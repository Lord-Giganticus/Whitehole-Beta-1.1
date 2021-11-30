package whitehole.smg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import whitehole.fileio.FilesystemBase;
import whitehole.fileio.RarcFilesystem;

public class ZoneArchive {
  public GalaxyArchive galaxy;
  
  public GameArchive game;
  
  public FilesystemBase filesystem;
  
  public String zonefile;
  
  public RarcFilesystem archive;
  
  public String zoneName;
  
  public int gameMask;
  
  public HashMap<String, List<LevelObject>> objects;
  
  public List<PathObject> paths;
  
  public HashMap<String, List<Bcsv.Entry>> subZones;
  
  public ZoneArchive(GalaxyArchive arc, String name) throws IOException {
    this.galaxy = arc;
    this.game = arc.game;
    this.filesystem = this.game.filesystem;
    this.zoneName = name;
    if (this.filesystem.fileExists("/StageData/" + this.zoneName + "/" + this.zoneName + "Map.arc")) {
      this.gameMask = 2;
      this.zonefile = "/StageData/" + this.zoneName + "/" + this.zoneName + "Map.arc";
    } else {
      this.gameMask = 1;
      this.zonefile = "/StageData/" + this.zoneName + ".arc";
    } 
    loadZone();
  }
  
  public ZoneArchive(GameArchive game, String name) throws IOException {
    this.galaxy = null;
    this.game = game;
    this.filesystem = game.filesystem;
    this.zoneName = name;
    if (this.filesystem.fileExists("/StageData/" + this.zoneName + "/" + this.zoneName + "Map.arc")) {
      this.gameMask = 2;
      this.zonefile = "/StageData/" + this.zoneName + "/" + this.zoneName + "Map.arc";
    } else {
      this.gameMask = 1;
      this.zonefile = "/StageData/" + this.zoneName + ".arc";
    } 
    loadZone();
  }
  
  public void save() throws IOException {
    saveObjects("MapParts", "MapPartsInfo");
    saveObjects("Placement", "ObjInfo");
    saveObjects("Start", "StartInfo");
    saveObjects("Placement", "PlanetObjInfo");
    savePaths();
    this.archive.save();
  }
  
  public void close() {
    try {
      this.archive.close();
    } catch (IOException ex) {}
  }
  
  private void loadZone() throws IOException {
    this.objects = new HashMap<>();
    this.subZones = new HashMap<>();
    this.archive = new RarcFilesystem(this.filesystem.openFile(this.zonefile));
    loadObjects("MapParts", "MapPartsInfo");
    loadObjects("Placement", "ObjInfo");
    loadObjects("Start", "StartInfo");
    loadObjects("Placement", "PlanetObjInfo");
    loadPaths();
    loadSubZones();
  }
  
  private void loadObjects(String dir, String file) {
    List<String> layers = this.archive.getDirectories("/Stage/Jmp/" + dir);
    for (String layer : layers)
      addObjectsToList(dir + "/" + layer + "/" + file); 
  }
  
  private void saveObjects(String dir, String file) {
    List<String> layers = this.archive.getDirectories("/Stage/Jmp/" + dir);
    for (String layer : layers)
      saveObjectList(dir + "/" + layer + "/" + file); 
  }
  
  private void addObjectsToList(String filepath) {
    String[] stuff = filepath.split("/");
    String layer = stuff[1].toLowerCase();
    String file = stuff[2].toLowerCase();
    if (!this.objects.containsKey(layer))
      this.objects.put(layer, new ArrayList<>()); 
    try {
      Bcsv bcsv = new Bcsv(this.archive.openFile("/Stage/Jmp/" + filepath));
      switch (file) {
        case "mappartsinfo":
          for (Bcsv.Entry entry : bcsv.entries)
            ((List<LevelObject>)this.objects.get(layer)).add(new MapPartObject(this, filepath, entry)); 
          break;
        case "objinfo":
          for (Bcsv.Entry entry : bcsv.entries)
            ((List<LevelObject>)this.objects.get(layer)).add(new GeneralObject(this, filepath, entry)); 
          break;
        case "startinfo":
          for (Bcsv.Entry entry : bcsv.entries)
            ((List<LevelObject>)this.objects.get(layer)).add(new StartObject(this, filepath, entry)); 
          break;
        case "planetobjinfo":
          for (Bcsv.Entry entry : bcsv.entries)
            ((List<LevelObject>)this.objects.get(layer)).add(new GravityObject(this, filepath, entry)); 
          break;
      } 
      bcsv.close();
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    } 
  }
  
  private void saveObjectList(String filepath) {
    String[] stuff = filepath.split("/");
    String dir = stuff[0], file = stuff[2];
    String layer = stuff[1].toLowerCase();
    if (!this.objects.containsKey(layer))
      return; 
    try {
      Bcsv bcsv = new Bcsv(this.archive.openFile("/Stage/Jmp/" + filepath));
      bcsv.entries.clear();
      for (LevelObject obj : this.objects.get(layer)) {
        if (!dir.equals(obj.directory) || !file.equals(obj.file))
          continue; 
        obj.save();
        bcsv.entries.add(obj.data);
      } 
      bcsv.save();
      bcsv.close();
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    } 
  }
  
  private void loadPaths() {
    try {
      Bcsv bcsv = new Bcsv(this.archive.openFile("/Stage/jmp/Path/CommonPathInfo"));
      this.paths = new ArrayList<>(bcsv.entries.size());
      for (Bcsv.Entry entry : bcsv.entries)
        this.paths.add(new PathObject(this, entry)); 
      bcsv.close();
    } catch (IOException ex) {
      System.out.println(this.zoneName + ": Failed to load paths: " + ex.getMessage());
    } 
  }
  
  private void savePaths() {
    try {
      Bcsv bcsv = new Bcsv(this.archive.openFile("/Stage/jmp/Path/CommonPathInfo"));
      bcsv.entries.clear();
      for (PathObject pobj : this.paths) {
        pobj.save();
        bcsv.entries.add(pobj.data);
      } 
      bcsv.save();
      bcsv.close();
    } catch (IOException ex) {
      System.out.println(this.zoneName + ": Failed to load paths: " + ex.getMessage());
    } 
  }
  
  private void loadSubZones() {
    List<String> layers = this.archive.getDirectories("/Stage/Jmp/Placement");
    for (String layer : layers) {
      try {
        Bcsv bcsv = new Bcsv(this.archive.openFile("/Stage/Jmp/Placement/" + layer + "/StageObjInfo"));
        this.subZones.put(layer.toLowerCase(), bcsv.entries);
        bcsv.close();
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
        ex.printStackTrace();
      } 
    } 
  }
}
