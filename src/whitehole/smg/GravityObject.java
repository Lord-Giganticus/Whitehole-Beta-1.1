package whitehole.smg;

import whitehole.PropertyPanel;
import whitehole.vectors.Vector3;

public class GravityObject extends LevelObject {
  public GravityObject(ZoneArchive zone, String filepath, Bcsv.Entry entry) {
    this.zone = zone;
    String[] stuff = filepath.split("/");
    this.directory = stuff[0];
    this.layer = stuff[1].toLowerCase();
    this.file = stuff[2];
    this.data = entry;
    this.name = (String)this.data.get("name");
    loadDBInfo();
    this.renderer = null;
    this.uniqueID = -1;
    this.position = new Vector3(((Float)this.data.get("pos_x")).floatValue(), ((Float)this.data.get("pos_y")).floatValue(), ((Float)this.data.get("pos_z")).floatValue());
    this.rotation = new Vector3(((Float)this.data.get("dir_x")).floatValue(), ((Float)this.data.get("dir_y")).floatValue(), ((Float)this.data.get("dir_z")).floatValue());
    this.scale = new Vector3(((Float)this.data.get("scale_x")).floatValue(), ((Float)this.data.get("scale_y")).floatValue(), ((Float)this.data.get("scale_z")).floatValue());
  }
  
  public GravityObject(ZoneArchive zone, String filepath, int game, String objname, Vector3 pos) {
    this.zone = zone;
    String[] stuff = filepath.split("/");
    this.directory = stuff[0];
    this.layer = stuff[1].toLowerCase();
    this.file = stuff[2];
    this.data = new Bcsv.Entry();
    this.name = objname;
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
    this.data.put("Range", Float.valueOf(-1.0F));
    this.data.put("Distant", Float.valueOf(0.0F));
    this.data.put("Priority", Integer.valueOf(0));
    this.data.put("Inverse", Integer.valueOf(0));
    this.data.put("Power", "Normal");
    this.data.put("Gravity_type", "Normal");
    this.data.put("Obj_arg0", Integer.valueOf(-1));
    this.data.put("Obj_arg1", Integer.valueOf(-1));
    this.data.put("Obj_arg2", Integer.valueOf(-1));
    this.data.put("Obj_arg3", Integer.valueOf(-1));
    this.data.put("SW_APPEAR", Integer.valueOf(-1));
    this.data.put("SW_DEAD", Integer.valueOf(-1));
    this.data.put("SW_A", Integer.valueOf(-1));
    this.data.put("SW_B", Integer.valueOf(-1));
    if (game == 2) {
      this.data.put("SW_AWAKE", Integer.valueOf(-1));
    } else {
      this.data.put("SW_SLEEP", Integer.valueOf(-1));
    } 
    this.data.put("l_id", Integer.valueOf(0));
    this.data.put("FollowId", Integer.valueOf(-1));
    this.data.put("ShapeModelNo", Short.valueOf((short)-1));
    this.data.put("CommonPath_ID", Short.valueOf((short)-1));
    this.data.put("ClippingGroupId", Short.valueOf((short)-1));
    this.data.put("GroupId", Short.valueOf((short)-1));
    this.data.put("DemoGroupId", Short.valueOf((short)-1));
    this.data.put("MapParts_ID", Short.valueOf((short)-1));
    this.data.put("Obj_ID", Short.valueOf((short)-1));
    if (game == 1)
      this.data.put("ChildObjId", Short.valueOf((short)-1)); 
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
    panel.addCategory("obj_grav", "Gravity parameters");
    panel.addField("Range", "Range", "float", null, this.data.get("Range"));
    panel.addField("Distant", "Distance", "float", null, this.data.get("Distant"));
    panel.addField("Priority", "Priority", "int", null, this.data.get("Priority"));
    panel.addField("Inverse", "Inverse", "int", null, this.data.get("Inverse"));
    panel.addField("Power", "Power", "text", null, this.data.get("Power"));
    panel.addField("Gravity_type", "Type", "text", null, this.data.get("Gravity_type"));
    panel.addCategory("obj_args", "Object arguments");
    panel.addField("Obj_arg0", "Obj_arg0", "int", null, this.data.get("Obj_arg0"));
    panel.addField("Obj_arg1", "Obj_arg1", "int", null, this.data.get("Obj_arg1"));
    panel.addField("Obj_arg2", "Obj_arg2", "int", null, this.data.get("Obj_arg2"));
    panel.addField("Obj_arg3", "Obj_arg3", "int", null, this.data.get("Obj_arg3"));
    panel.addCategory("obj_eventinfo", "Event IDs");
    panel.addField("SW_APPEAR", "SW_APPEAR", "int", null, this.data.get("SW_APPEAR"));
    panel.addField("SW_DEAD", "SW_DEAD", "int", null, this.data.get("SW_DEAD"));
    panel.addField("SW_A", "SW_A", "int", null, this.data.get("SW_A"));
    panel.addField("SW_B", "SW_B", "int", null, this.data.get("SW_B"));
    if (this.zone.gameMask == 2) {
      panel.addField("SW_AWAKE", "SW_AWAKE", "int", null, this.data.get("SW_AWAKE"));
    } else {
      panel.addField("SW_SLEEP", "SW_SLEEP", "int", null, this.data.get("SW_SLEEP"));
    } 
    panel.addCategory("obj_objinfo", "Object settings");
    panel.addField("l_id", "Object ID", "int", null, this.data.get("l_id"));
    panel.addField("FollowId", "Follow ID", "int", null, this.data.get("FollowId"));
    panel.addField("CommonPath_ID", "Path ID", "int", null, this.data.get("CommonPath_ID"));
    panel.addField("ClippingGroupId", "Clipping group ID", "int", null, this.data.get("ClippingGroupId"));
    panel.addField("GroupId", "Group ID", "int", null, this.data.get("GroupId"));
    panel.addField("DemoGroupId", "Demo group ID", "int", null, this.data.get("DemoGroupId"));
    panel.addCategory("obj_misc", "Misc. settings");
    panel.addField("MapParts_ID", "MapParts_ID", "int", null, this.data.get("MapParts_ID"));
    panel.addField("Obj_ID", "Obj_ID", "int", null, this.data.get("Obj_ID"));
    if (this.zone.gameMask == 1)
      panel.addField("ChildObjId", "ChildObjId", "int", null, this.data.get("ChildObjId")); 
  }
  
  public String toString() {
    String l = this.layer.equals("common") ? "Common" : ("Layer" + this.layer.substring(5).toUpperCase());
    return this.dbInfo.name + " [" + l + "]";
  }
}
