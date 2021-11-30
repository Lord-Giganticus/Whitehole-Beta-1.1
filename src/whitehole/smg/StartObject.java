package whitehole.smg;

import whitehole.PropertyPanel;
import whitehole.vectors.Vector3;

public class StartObject extends LevelObject {
  public StartObject(ZoneArchive zone, String filepath, Bcsv.Entry entry) {
    this.zone = zone;
    String[] stuff = filepath.split("/");
    this.directory = stuff[0];
    this.layer = stuff[1].toLowerCase();
    this.file = stuff[2];
    this.data = entry;
    this.name = (String)this.data.get("name");
    if (!this.name.equals("Mario"))
      System.out.println("NON-MARIO START OBJECT -- WHAT THE HELL (" + this.name + ")"); 
    loadDBInfo();
    this.renderer = null;
    this.uniqueID = -1;
    this.position = new Vector3(((Float)this.data.get("pos_x")).floatValue(), ((Float)this.data.get("pos_y")).floatValue(), ((Float)this.data.get("pos_z")).floatValue());
    this.rotation = new Vector3(((Float)this.data.get("dir_x")).floatValue(), ((Float)this.data.get("dir_y")).floatValue(), ((Float)this.data.get("dir_z")).floatValue());
    this.scale = new Vector3(((Float)this.data.get("scale_x")).floatValue(), ((Float)this.data.get("scale_y")).floatValue(), ((Float)this.data.get("scale_z")).floatValue());
  }
  
  public StartObject(ZoneArchive zone, String filepath, int game, Vector3 pos) {
    this.zone = zone;
    String[] stuff = filepath.split("/");
    this.directory = stuff[0];
    this.layer = stuff[1].toLowerCase();
    this.file = stuff[2];
    this.data = new Bcsv.Entry();
    this.name = "Mario";
    loadDBInfo();
    this.renderer = null;
    this.uniqueID = -1;
    this.position = pos;
    this.rotation = new Vector3(0.0F, 0.0F, 0.0F);
    this.scale = new Vector3(1.0F, 1.0F, 1.0F);
    this.data.put("name", this.name);
    this.data.put("pos_x", Float.valueOf(this.position.x));
    this.data.put("pos_y", Float.valueOf(this.position.y));
    this.data.put("pos_z", Float.valueOf(this.position.z));
    this.data.put("dir_x", Float.valueOf(this.rotation.x));
    this.data.put("dir_y", Float.valueOf(this.rotation.y));
    this.data.put("dir_z", Float.valueOf(this.rotation.z));
    this.data.put("scale_x", Float.valueOf(this.scale.x));
    this.data.put("scale_y", Float.valueOf(this.scale.y));
    this.data.put("scale_z", Float.valueOf(this.scale.z));
    this.data.put("Obj_arg0", Integer.valueOf(-1));
    this.data.put("MarioNo", Integer.valueOf(0));
    this.data.put("Camera_id", Integer.valueOf(-1));
  }
  
  public void save() {
    this.data.put("name", this.name);
    this.data.put("pos_x", Float.valueOf(this.position.x));
    this.data.put("pos_y", Float.valueOf(this.position.y));
    this.data.put("pos_z", Float.valueOf(this.position.z));
    this.data.put("dir_x", Float.valueOf(this.rotation.x));
    this.data.put("dir_y", Float.valueOf(this.rotation.y));
    this.data.put("dir_z", Float.valueOf(this.rotation.z));
    this.data.put("scale_x", Float.valueOf(this.scale.x));
    this.data.put("scale_y", Float.valueOf(this.scale.y));
    this.data.put("scale_z", Float.valueOf(this.scale.z));
  }
  
  public void getProperties(PropertyPanel panel) {
    panel.addCategory("obj_position", "Position");
    panel.addField("pos_x", "X position", "float", null, Float.valueOf(this.position.x));
    panel.addField("pos_y", "Y position", "float", null, Float.valueOf(this.position.y));
    panel.addField("pos_z", "Z position", "float", null, Float.valueOf(this.position.z));
    panel.addField("dir_x", "X rotation", "float", null, Float.valueOf(this.rotation.x));
    panel.addField("dir_y", "Y rotation", "float", null, Float.valueOf(this.rotation.y));
    panel.addField("dir_z", "Z rotation", "float", null, Float.valueOf(this.rotation.z));
    panel.addField("scale_x", "X scale", "float", null, Float.valueOf(this.scale.x));
    panel.addField("scale_y", "Y scale", "float", null, Float.valueOf(this.scale.y));
    panel.addField("scale_z", "Z scale", "float", null, Float.valueOf(this.scale.z));
    panel.addCategory("obj_args", "Object arguments");
    panel.addField("Obj_arg0", "Obj_arg0", "int", null, this.data.get("Obj_arg0"));
    panel.addCategory("obj_objinfo", "Object settings");
    panel.addField("MarioNo", "Mario ID", "int", null, this.data.get("MarioNo"));
    panel.addField("Camera_id", "Camera ID", "int", null, this.data.get("Camera_id"));
  }
  
  public String toString() {
    String l = this.layer.equals("common") ? "Common" : ("Layer" + this.layer.substring(5).toUpperCase());
    return String.format("Starting point %1$d [%2$s]", new Object[] { Integer.valueOf(((Integer)this.data.get("MarioNo")).intValue()), l });
  }
}
