package whitehole.rendering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLException;
import whitehole.Whitehole;
import whitehole.fileio.RarcFilesystem;
import whitehole.smg.Bcsv;
import whitehole.smg.LevelObject;
import whitehole.vectors.Color4;

public class RendererCache {
  public static HashMap<String, CacheEntry> cache;
  
  public static GLContext refContext;
  
  public static int contextCount;
  
  private static List<String> planetList;
  
  public static void initialize() {
    cache = new HashMap<>();
    refContext = null;
    contextCount = 0;
    planetList = null;
  }
  
  public static void loadPlanetList() {
    if (planetList != null)
      return; 
    try {
      RarcFilesystem arc = new RarcFilesystem(Whitehole.game.filesystem.openFile("/ObjectData/PlanetMapDataTable.arc"));
      Bcsv planetmap = new Bcsv(arc.openFile("/PlanetMapDataTable/PlanetMapDataTable.bcsv"));
      planetList = new ArrayList<>(planetmap.entries.size());
      for (Bcsv.Entry entry : planetmap.entries)
        planetList.add((String)entry.get("PlanetName")); 
      planetmap.close();
      arc.close();
    } catch (IOException ex) {
      planetList = new ArrayList<>(0);
    } 
  }
  
  public static GLRenderer getObjectRenderer(GLRenderer.RenderInfo info, LevelObject obj) {
    loadPlanetList();
    String modelname = obj.name;
    modelname = ObjectModelSubstitutor.substituteModelName(obj, modelname);
    String key = "object_" + obj.name;
    key = ObjectModelSubstitutor.substituteObjectKey(obj, key);
    if (cache.containsKey(key)) {
      CacheEntry cacheEntry = cache.get(key);
      cacheEntry.refCount++;
      return cacheEntry.renderer;
    } 
    CacheEntry entry = new CacheEntry();
    entry.refCount = 1;
    entry.renderer = ObjectModelSubstitutor.substituteRenderer(obj, info);
    if (entry.renderer == null)
      try {
        if (planetList.contains(obj.name)) {
          entry.renderer = new PlanetRenderer(info, obj.name);
        } else {
          entry.renderer = new BmdRenderer(info, modelname);
        } 
      } catch (GLException ex) {
        if (!ex.getMessage().contains("doesn't exist") && !ex.getMessage().contains("No suitable model file inside RARC"))
          ex.printStackTrace(); 
        entry.renderer = null;
      }  
    if (entry.renderer == null)
      entry.renderer = new ColorCubeRenderer(100.0F, new Color4(0.5F, 0.5F, 1.0F, 1.0F), new Color4(0.0F, 0.0F, 0.8F, 1.0F), true); 
    cache.put(key, entry);
    return entry.renderer;
  }
  
  public static void closeObjectRenderer(GLRenderer.RenderInfo info, LevelObject obj) {
    String key = "object_" + obj.name;
    key = ObjectModelSubstitutor.substituteObjectKey(obj, key);
    if (!cache.containsKey(key))
      return; 
    CacheEntry entry = cache.get(key);
    entry.refCount--;
    if (entry.refCount > 0)
      return; 
    entry.renderer.close(info);
    cache.remove(key);
  }
  
  public static void setRefContext(GLContext ctx) {
    if (refContext == null)
      refContext = ctx; 
    contextCount++;
  }
  
  public static void clearRefContext() {
    contextCount--;
    if (contextCount < 1)
      refContext = null; 
  }
  
  public static class CacheEntry {
    public GLRenderer renderer;
    
    public int refCount;
  }
}
