package whitehole;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import whitehole.smg.LevelObject;
import whitehole.smg.PathObject;
import whitehole.smg.PathPointObject;

public class ObjListTreeNode extends ObjTreeNode {
  LinkedHashMap<Integer, TreeNode> children;
  
  public ObjListTreeNode() {
    this.children = new LinkedHashMap<>();
    this.object = null;
  }
  
  public ObjListTreeNode(PathObject obj) {
    this.object = obj;
    this.uniqueID = obj.uniqueID;
    this.children = new LinkedHashMap<>();
    for (PathPointObject ptobj : obj.points.values())
      addObject(ptobj); 
  }
  
  public void insert(MutableTreeNode child, int index) {
    throw new UnsupportedOperationException("This is not how you add nodes to this kind of TreeNode.");
  }
  
  public void remove(int index) {
    int key = ((Integer)this.children.keySet().toArray()[index]).intValue();
    this.children.remove(Integer.valueOf(key));
  }
  
  public void remove(MutableTreeNode node) {
    this.children.remove(Integer.valueOf(((ObjTreeNode)node).uniqueID));
  }
  
  public void removeFromParent() {
    this.parent = null;
    System.out.println("[ObjListTreeNode] REMOVE FROM PARENT");
  }
  
  public void setParent(MutableTreeNode newParent) {
    this.parent = newParent;
  }
  
  public TreeNode getChildAt(int childIndex) {
    return (TreeNode)this.children.values().toArray()[childIndex];
  }
  
  public int getChildCount() {
    return this.children.size();
  }
  
  public TreeNode getParent() {
    return this.parent;
  }
  
  public int getIndex(TreeNode node) {
    int uid = ((ObjTreeNode)node).uniqueID;
    int i = 0;
    for (TreeNode tn : this.children.values()) {
      if (((ObjTreeNode)tn).uniqueID == uid)
        return i; 
      i++;
    } 
    return -1;
  }
  
  public boolean getAllowsChildren() {
    return true;
  }
  
  public boolean isLeaf() {
    return this.children.isEmpty();
  }
  
  public Enumeration children() {
    return new Iterator2Enumeration(this.children.values().iterator());
  }
  
  public TreeNode addObject(LevelObject obj) {
    ObjTreeNode tn = new ObjTreeNode(obj);
    this.children.put(Integer.valueOf(obj.uniqueID), tn);
    tn.setParent(this);
    return tn;
  }
  
  public TreeNode addObject(PathPointObject obj) {
    ObjTreeNode tn = new ObjTreeNode(obj);
    this.children.put(Integer.valueOf(obj.uniqueID), tn);
    tn.setParent(this);
    return tn;
  }
  
  public TreeNode addObject(PathObject obj) {
    ObjListTreeNode tn = new ObjListTreeNode(obj);
    this.children.put(Integer.valueOf(obj.uniqueID), tn);
    tn.setParent(this);
    return tn;
  }
}
