package whitehole.rendering;

import java.util.HashMap;

public class TextureCache {
  public static HashMap<Object, CacheEntry> cache;
  
  public static void initialize() {
    cache = new HashMap<>();
  }
  
  public static boolean containsEntry(Object key) {
    return cache.containsKey(key);
  }
  
  public static CacheEntry getEntry(Object key) {
    CacheEntry entry = cache.get(key);
    entry.refCount++;
    return entry;
  }
  
  public static int getTextureID(Object key) {
    return ((CacheEntry)cache.get(key)).textureID;
  }
  
  public static void addEntry(Object key, int tex) {
    CacheEntry entry = new CacheEntry();
    entry.textureID = tex;
    entry.refCount = 1;
    cache.put(key, entry);
  }
  
  public static boolean removeEntry(Object key) {
    CacheEntry entry = cache.get(key);
    entry.refCount--;
    if (entry.refCount > 0)
      return false; 
    cache.remove(key);
    return true;
  }
  
  public static class CacheEntry {
    public int textureID;
    
    public int refCount;
  }
}
