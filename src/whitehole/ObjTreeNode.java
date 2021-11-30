package whitehole;

import java.util.Enumeration;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import whitehole.smg.LevelObject;
import whitehole.smg.PathPointObject;

public class ObjTreeNode implements MutableTreeNode {
  public TreeNode parent;
  
  public Object object;
  
  public Object userObject;
  
  public int uniqueID;
  
  public ObjTreeNode() {
    this.parent = null;
    this.object = null;
    this.userObject = null;
    this.uniqueID = -1;
  }
  
  public ObjTreeNode(LevelObject obj) {
    this.parent = null;
    this.object = obj;
    this.userObject = null;
    this.uniqueID = obj.uniqueID;
  }
  
  public ObjTreeNode(PathPointObject obj) {
    this.parent = null;
    this.object = obj;
    this.userObject = null;
    this.uniqueID = obj.uniqueID;
  }
  
  public void insert(MutableTreeNode child, int index) {}
  
  public void remove(int index) {}
  
  public void remove(MutableTreeNode node) {}
  
  public void setUserObject(Object object) {
    this.userObject = object;
  }
  
  public void removeFromParent() {
    this.parent = null;
    System.out.println("[ObjTreeNode] REMOVE FROM PARENT");
  }
  
  public void setParent(MutableTreeNode newParent) {
    this.parent = newParent;
  }
  
  public TreeNode getChildAt(int childIndex) {
    return null;
  }
  
  public int getChildCount() {
    return 0;
  }
  
  public TreeNode getParent() {
    return this.parent;
  }
  
  public int getIndex(TreeNode node) {
    return -1;
  }
  
  public boolean getAllowsChildren() {
    return false;
  }
  
  public boolean isLeaf() {
    return true;
  }
  
  public Enumeration children() {
    return null;
  }
  
  public String toString() {
    if (this.userObject != null)
      return this.userObject.toString(); 
    if (this.object != null)
      return this.object.toString(); 
    return "unknown node lol";
  }
}
