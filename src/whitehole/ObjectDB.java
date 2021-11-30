package whitehole;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class ObjectDB {
  public static boolean fallback;
  
  public static long timestamp;
  
  public static LinkedHashMap<Integer, String> categories;
  
  public static LinkedHashMap<String, Object> objects;
  
  public static void initialize() {
    fallback = true;
    timestamp = 0L;
    categories = new LinkedHashMap<>();
    objects = new LinkedHashMap<>();
    File odbfile = new File("objectdb.xml");
    if (!odbfile.exists())
      return; 
    try {
      SAXBuilder sxb = new SAXBuilder();
      Document doc = sxb.build(odbfile);
      Element root = doc.getRootElement();
      timestamp = root.getAttribute("timestamp").getLongValue();
      List<Element> catelems = root.getChild("categories").getChildren("category");
      for (Element catelem : catelems)
        categories.put(Integer.valueOf(catelem.getAttribute("id").getIntValue()), catelem.getText()); 
      List<Element> objelems = root.getChildren("object");
      for (Element objelem : objelems) {
        Object entry = new Object();
        entry.ID = objelem.getAttributeValue("id");
        entry.name = objelem.getChildText("name");
        Element flags = objelem.getChild("flags");
        entry.games = flags.getAttribute("games").getIntValue();
        entry.category = objelem.getChild("category").getAttribute("id").getIntValue();
        entry.preferredFile = objelem.getChild("preferredfile").getAttributeValue("name");
        entry.notes = objelem.getChildText("notes");
        entry.dataFiles = new ArrayList<>();
        String datafiles = objelem.getChildText("files");
        for (String datafile : datafiles.split("\n"))
          entry.dataFiles.add(datafile); 
        List<Element> fields = objelem.getChildren("field");
        entry.fields = new HashMap<>(fields.size());
        for (Element field : fields) {
          Object.Field fielddata = new Object.Field();
          fielddata.ID = field.getAttribute("id").getIntValue();
          fielddata.type = field.getAttributeValue("type");
          fielddata.name = field.getAttributeValue("name");
          fielddata.values = field.getAttributeValue("values");
          fielddata.notes = field.getAttributeValue("notes");
          entry.fields.put(Integer.valueOf(fielddata.ID), fielddata);
        } 
        objects.put(entry.ID, entry);
      } 
    } catch (Exception ex) {
      timestamp = 0L;
      return;
    } 
    fallback = false;
  }
  
  public static class Object {
    public String ID;
    
    public String name;
    
    public int games;
    
    public int category;
    
    public String preferredFile;
    
    public String notes;
    
    public List<String> dataFiles;
    
    public HashMap<Integer, Field> fields;
    
    public static class Field {
      public int ID;
      
      public String type;
      
      public String name;
      
      public String values;
      
      public String notes;
    }
  }
  
  public static class Field {
    public int ID;
    
    public String type;
    
    public String name;
    
    public String values;
    
    public String notes;
  }
}
