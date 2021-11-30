package whitehole.smg;

import whitehole.PropertyPanel;
import whitehole.vectors.Vector3;

public class GeneralObject extends LevelObject {
  public GeneralObject(ZoneArchive zone, String filepath, Bcsv.Entry entry) {
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
  
  public GeneralObject(ZoneArchive zone, String filepath, int game, String objname, Vector3 pos) {
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
    this.data.put("Obj_arg0", Integer.valueOf(-1));
    this.data.put("Obj_arg1", Integer.valueOf(-1));
    this.data.put("Obj_arg2", Integer.valueOf(-1));
    this.data.put("Obj_arg3", Integer.valueOf(-1));
    this.data.put("Obj_arg4", Integer.valueOf(-1));
    this.data.put("Obj_arg5", Integer.valueOf(-1));
    this.data.put("Obj_arg6", Integer.valueOf(-1));
    this.data.put("Obj_arg7", Integer.valueOf(-1));
    this.data.put("l_id", Integer.valueOf(0));
    this.data.put("CameraSetId", Integer.valueOf(-1));
    this.data.put("SW_APPEAR", Integer.valueOf(-1));
    this.data.put("SW_DEAD", Integer.valueOf(-1));
    this.data.put("SW_A", Integer.valueOf(-1));
    this.data.put("SW_B", Integer.valueOf(-1));
    if (game == 2) {
      this.data.put("SW_AWAKE", Integer.valueOf(-1));
      this.data.put("SW_PARAM", Integer.valueOf(-1));
      this.data.put("ParamScale", Float.valueOf(1.0F));
    } else {
      this.data.put("SW_SLEEP", Integer.valueOf(-1));
    } 
    this.data.put("CastId", Integer.valueOf(-1));
    this.data.put("ViewGroupId", Integer.valueOf(-1));
    this.data.put("ShapeModelNo", Short.valueOf((short)-1));
    this.data.put("CommonPath_ID", Short.valueOf((short)-1));
    this.data.put("ClippingGroupId", Short.valueOf((short)-1));
    this.data.put("GroupId", Short.valueOf((short)-1));
    this.data.put("DemoGroupId", Short.valueOf((short)-1));
    this.data.put("MapParts_ID", Short.valueOf((short)-1));
    if (game == 2)
      this.data.put("Obj_ID", Short.valueOf((short)-1)); 
    this.data.put("MessageId", Integer.valueOf(-1));
    if (game == 2)
      this.data.put("GeneratorID", Short.valueOf((short)-1)); 
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
    if (this.zone.gameMask == 2)
      panel.addField("ParamScale", "ParamScale", "float", null, this.data.get("ParamScale")); 
    panel.addCategory("obj_args", "Object arguments");
    panel.addField("Obj_arg0", "Obj_arg0", "int", null, this.data.get("Obj_arg0"));
    panel.addField("Obj_arg1", "Obj_arg1", "int", null, this.data.get("Obj_arg1"));
    panel.addField("Obj_arg2", "Obj_arg2", "int", null, this.data.get("Obj_arg2"));
    panel.addField("Obj_arg3", "Obj_arg3", "int", null, this.data.get("Obj_arg3"));
    panel.addField("Obj_arg4", "Obj_arg4", "int", null, this.data.get("Obj_arg4"));
    panel.addField("Obj_arg5", "Obj_arg5", "int", null, this.data.get("Obj_arg5"));
    panel.addField("Obj_arg6", "Obj_arg6", "int", null, this.data.get("Obj_arg6"));
    panel.addField("Obj_arg7", "Obj_arg7", "int", null, this.data.get("Obj_arg7"));
    panel.addCategory("obj_eventinfo", "Event IDs");
    panel.addField("SW_APPEAR", "SW_APPEAR", "int", null, this.data.get("SW_APPEAR"));
    panel.addField("SW_DEAD", "SW_DEAD", "int", null, this.data.get("SW_DEAD"));
    panel.addField("SW_A", "SW_A", "int", null, this.data.get("SW_A"));
    panel.addField("SW_B", "SW_B", "int", null, this.data.get("SW_B"));
    if (this.zone.gameMask == 2) {
      panel.addField("SW_AWAKE", "SW_AWAKE", "int", null, this.data.get("SW_AWAKE"));
      panel.addField("SW_PARAM", "SW_PARAM", "int", null, this.data.get("SW_PARAM"));
    } else {
      panel.addField("SW_SLEEP", "SW_SLEEP", "int", null, this.data.get("SW_SLEEP"));
    } 
    panel.addCategory("obj_objinfo", "Object settings");
    panel.addField("l_id", "Object ID", "int", null, this.data.get("l_id"));
    panel.addField("MessageId", "Message ID", "int", null, this.data.get("MessageId"));
    if (this.zone.gameMask == 2)
      panel.addField("GeneratorID", "Generator ID", "int", null, this.data.get("GeneratorID")); 
    panel.addField("ViewGroupId", "View group ID", "int", null, this.data.get("ViewGroupId"));
    panel.addField("CommonPath_ID", "Path ID", "int", null, this.data.get("CommonPath_ID"));
    panel.addField("ClippingGroupId", "Clipping group ID", "int", null, this.data.get("ClippingGroupId"));
    panel.addField("GroupId", "Group ID", "int", null, this.data.get("GroupId"));
    panel.addField("DemoGroupId", "Demo group ID", "int", null, this.data.get("DemoGroupId"));
    panel.addCategory("obj_misc", "Misc. settings");
    panel.addField("CameraSetId", "CameraSetId", "int", null, this.data.get("CameraSetId"));
    panel.addField("CastId", "CastId", "int", null, this.data.get("CastId"));
    panel.addField("ShapeModelNo", "ShapeModelNo", "int", null, this.data.get("ShapeModelNo"));
    panel.addField("MapParts_ID", "MapParts_ID", "int", null, this.data.get("MapParts_ID"));
    if (this.zone.gameMask == 2)
      panel.addField("Obj_ID", "Obj_ID", "int", null, this.data.get("Obj_ID")); 
  }
  
  public String toString() {
    String l = this.layer.equals("common") ? "Common" : ("Layer" + this.layer.substring(5).toUpperCase());
    return this.dbInfo.name + " [" + l + "]";
  }
}
