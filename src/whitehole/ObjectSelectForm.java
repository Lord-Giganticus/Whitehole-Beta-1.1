package whitehole;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class ObjectSelectForm extends JDialog {
  private int game;
  
  public String selectedObject;
  
  public String selectedZone;
  
  public String selectedLayer;
  
  private DefaultMutableTreeNode objList;
  
  private DefaultMutableTreeNode searchList;
  
  private JButton btnSelect;
  
  private JComboBox cbxLayer;
  
  private JComboBox cbxZone;
  
  private JEditorPane epObjDescription;
  
  private JLabel jLabel1;
  
  private JPanel jPanel1;
  
  private JPanel jPanel2;
  
  private JScrollPane jScrollPane1;
  
  private JScrollPane jScrollPane3;
  
  private JSplitPane jSplitPane1;
  
  private JToolBar jToolBar1;
  
  private JToolBar jToolBar2;
  
  private JLabel lblLayer;
  
  private JLabel lblZone;
  
  private JToolBar.Separator sepSelect;
  
  private JTree tvObjectList;
  
  private JTextField txtSearch;
  
  public ObjectSelectForm(Frame parent, int game, String selobj) {
    super(parent, true);
    initComponents();
    setIconImage(Toolkit.getDefaultToolkit().createImage(Whitehole.class.getResource("/Resources/icon.png")));
    this.game = game;
    this.selectedObject = selobj;
    if (selobj != null) {
      this.lblZone.setVisible(false);
      this.cbxZone.setVisible(false);
      this.lblLayer.setVisible(false);
      this.cbxLayer.setVisible(false);
      this.sepSelect.setVisible(false);
      this.selectedZone = "#lolz#";
      this.selectedLayer = "#lolz#";
    } else {
      GalaxyEditorForm gal_parent = (GalaxyEditorForm)parent;
      this.lblZone.setVisible(false);
      this.cbxZone.setVisible(false);
      this.lblLayer.setText("Add to layer: ");
      this.selectedZone = "#lolz#";
      DefaultComboBoxModel<String> layerlist = (DefaultComboBoxModel)this.cbxLayer.getModel();
      layerlist.addElement("Common");
      for (int l = 0; l < 26; l++) {
        String ls = String.format("Layer%1$c", new Object[] { Integer.valueOf(65 + l) });
        if (gal_parent.curZoneArc.objects.containsKey(ls.toLowerCase()))
          layerlist.addElement(ls); 
      } 
      this.selectedLayer = "Common";
      this.cbxLayer.setSelectedItem(this.selectedLayer);
      this.selectedObject = "";
    } 
  }
  
  private void initComponents() {
    this.jSplitPane1 = new JSplitPane();
    this.jPanel1 = new JPanel();
    this.jToolBar1 = new JToolBar();
    this.jLabel1 = new JLabel();
    this.txtSearch = new JTextField();
    this.jScrollPane1 = new JScrollPane();
    this.tvObjectList = new JTree();
    this.jPanel2 = new JPanel();
    this.jScrollPane3 = new JScrollPane();
    this.epObjDescription = new JEditorPane();
    this.jToolBar2 = new JToolBar();
    this.lblZone = new JLabel();
    this.cbxZone = new JComboBox();
    this.lblLayer = new JLabel();
    this.cbxLayer = new JComboBox();
    this.sepSelect = new JToolBar.Separator();
    this.btnSelect = new JButton();
    setDefaultCloseOperation(2);
    setTitle("Select object");
    addWindowListener(new WindowAdapter() {
          public void windowOpened(WindowEvent evt) {
            ObjectSelectForm.this.formWindowOpened(evt);
          }
        });
    this.jSplitPane1.setDividerLocation(320);
    this.jSplitPane1.setOrientation(0);
    this.jSplitPane1.setResizeWeight(1.0D);
    this.jSplitPane1.setFocusable(false);
    this.jSplitPane1.setLastDividerLocation(320);
    this.jPanel1.setLayout(new BorderLayout());
    this.jToolBar1.setFloatable(false);
    this.jToolBar1.setRollover(true);
    this.jLabel1.setText("Search: ");
    this.jLabel1.setToolTipText("");
    this.jToolBar1.add(this.jLabel1);
    this.txtSearch.addKeyListener(new KeyAdapter() {
          public void keyReleased(KeyEvent evt) {
            ObjectSelectForm.this.txtSearchKeyReleased(evt);
          }
        });
    this.jToolBar1.add(this.txtSearch);
    this.jPanel1.add(this.jToolBar1, "First");
    DefaultMutableTreeNode treeNode1 = new DefaultMutableTreeNode("root");
    this.tvObjectList.setModel(new DefaultTreeModel(treeNode1));
    this.tvObjectList.setShowsRootHandles(true);
    this.tvObjectList.addMouseListener(new MouseAdapter() {
          public void mouseClicked(MouseEvent evt) {
            ObjectSelectForm.this.tvObjectListMouseClicked(evt);
          }
        });
    this.tvObjectList.addTreeSelectionListener(new TreeSelectionListener() {
          public void valueChanged(TreeSelectionEvent evt) {
            ObjectSelectForm.this.tvObjectListValueChanged(evt);
          }
        });
    this.jScrollPane1.setViewportView(this.tvObjectList);
    this.jPanel1.add(this.jScrollPane1, "Center");
    this.jSplitPane1.setTopComponent(this.jPanel1);
    this.jPanel2.setLayout(new BorderLayout());
    this.epObjDescription.setContentType("text/html");
    this.jScrollPane3.setViewportView(this.epObjDescription);
    this.jPanel2.add(this.jScrollPane3, "Center");
    this.jToolBar2.setFloatable(false);
    this.jToolBar2.setRollover(true);
    this.lblZone.setText("Add to zone: ");
    this.jToolBar2.add(this.lblZone);
    this.jToolBar2.add(this.cbxZone);
    this.lblLayer.setText(" Layer: ");
    this.jToolBar2.add(this.lblLayer);
    this.jToolBar2.add(this.cbxLayer);
    this.jToolBar2.add(this.sepSelect);
    this.btnSelect.setText("Select");
    this.btnSelect.setEnabled(false);
    this.btnSelect.setFocusable(false);
    this.btnSelect.setHorizontalTextPosition(0);
    this.btnSelect.setVerticalTextPosition(3);
    this.btnSelect.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            ObjectSelectForm.this.btnSelectActionPerformed(evt);
          }
        });
    this.jToolBar2.add(this.btnSelect);
    this.jPanel2.add(this.jToolBar2, "Last");
    this.jSplitPane1.setRightComponent(this.jPanel2);
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jSplitPane1, -1, 400, 32767));
    layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jSplitPane1, -1, 537, 32767));
    pack();
  }
  
  private void formWindowOpened(WindowEvent evt) {
    DefaultTreeModel thelist = (DefaultTreeModel)this.tvObjectList.getModel();
    this.objList = new DefaultMutableTreeNode((this.game == 2) ? "SMG2 objects" : "SMG1 objects");
    thelist.setRoot(this.objList);
    LinkedHashMap<Integer, DefaultMutableTreeNode> catlist = new LinkedHashMap<>(ObjectDB.categories.size());
    for (Map.Entry<Integer, String> cat : ObjectDB.categories.entrySet()) {
      DefaultMutableTreeNode tn = new DefaultMutableTreeNode(cat.getValue());
      catlist.put(cat.getKey(), tn);
    } 
    LinkedHashMap<String, MyObjTreeNode> tempnodelist = new LinkedHashMap<>(ObjectDB.objects.size());
    for (ObjectDB.Object obj : ObjectDB.objects.values()) {
      if ((obj.games & this.game) == 0)
        continue; 
      DefaultMutableTreeNode tn = catlist.get(Integer.valueOf(obj.category));
      MyObjTreeNode objnode = new MyObjTreeNode(obj.ID);
      tn.add(objnode);
      tempnodelist.put(obj.ID, objnode);
    } 
    for (DefaultMutableTreeNode catnode : catlist.values()) {
      if (catnode.getChildCount() == 0)
        continue; 
      this.objList.add(catnode);
    } 
    if (!this.selectedObject.isEmpty() && ObjectDB.objects.containsKey(this.selectedObject)) {
      TreePath path = new TreePath((Object[])((DefaultTreeModel)this.tvObjectList.getModel()).getPathToRoot(tempnodelist.get(this.selectedObject)));
      this.tvObjectList.setSelectionPath(path);
      this.tvObjectList.scrollPathToVisible(path);
    } else {
      TreePath path = new TreePath((Object[])((DefaultTreeModel)this.tvObjectList.getModel()).getPathToRoot(this.objList.getChildAt(0)));
      this.tvObjectList.scrollPathToVisible(path);
    } 
    this.searchList = new DefaultMutableTreeNode("Search results");
  }
  
  private void tvObjectListValueChanged(TreeSelectionEvent evt) {
    if (this.tvObjectList.getSelectionPath() == null) {
      this.epObjDescription.setText("");
      this.btnSelect.setEnabled(false);
      return;
    } 
    MutableTreeNode tn = (MutableTreeNode)this.tvObjectList.getSelectionPath().getLastPathComponent();
    if (tn.getClass() != MyObjTreeNode.class) {
      this.epObjDescription.setText("");
      this.btnSelect.setEnabled(false);
      return;
    } 
    ObjectDB.Object dbinfo = ObjectDB.objects.get(((MyObjTreeNode)tn).objectID);
    this.epObjDescription.setText(String.format("<b>%1$s</b> (%2$s)<br><br>%3$s", new Object[] { dbinfo.name, dbinfo.ID, dbinfo.notes }));
    this.btnSelect.setEnabled(true);
  }
  
  private void btnSelectActionPerformed(ActionEvent evt) {
    MutableTreeNode tn = (MutableTreeNode)this.tvObjectList.getSelectionPath().getLastPathComponent();
    if (tn.getClass() != MyObjTreeNode.class)
      throw new NullPointerException("oops"); 
    this.selectedObject = ((MyObjTreeNode)tn).objectID;
    if (!this.selectedZone.equals("#lolz#"))
      this.selectedZone = (String)this.cbxZone.getSelectedItem(); 
    if (!this.selectedLayer.equals("#lolz#"))
      this.selectedLayer = (String)this.cbxLayer.getSelectedItem(); 
    dispose();
  }
  
  private void txtSearchKeyReleased(KeyEvent evt) {
    String search = this.txtSearch.getText().toLowerCase();
    if (search.isEmpty()) {
      ((DefaultTreeModel)this.tvObjectList.getModel()).setRoot(this.objList);
    } else {
      this.searchList.removeAllChildren();
      for (ObjectDB.Object obj : ObjectDB.objects.values()) {
        if ((obj.games & this.game) == 0 || (
          !obj.ID.toLowerCase().contains(search) && !obj.name.toLowerCase().contains(search)))
          continue; 
        MyObjTreeNode objnode = new MyObjTreeNode(obj.ID);
        this.searchList.add(objnode);
      } 
      if (this.searchList.getChildCount() == 0)
        this.searchList.add(new DefaultMutableTreeNode("(no results)")); 
      ((DefaultTreeModel)this.tvObjectList.getModel()).setRoot(this.searchList);
    } 
  }
  
  private void tvObjectListMouseClicked(MouseEvent evt) {
    if (this.tvObjectList.getSelectionPath() == null)
      return; 
    MutableTreeNode tn = (MutableTreeNode)this.tvObjectList.getSelectionPath().getLastPathComponent();
    if (tn.getClass() != MyObjTreeNode.class)
      return; 
    if (evt.getClickCount() < 2)
      return; 
    this.selectedObject = ((MyObjTreeNode)tn).objectID;
    dispose();
  }
  
  public class MyObjTreeNode implements MutableTreeNode {
    public TreeNode parent;
    
    public String objectID;
    
    public MyObjTreeNode(String objid) {
      this.parent = null;
      this.objectID = objid;
    }
    
    public void insert(MutableTreeNode child, int index) {}
    
    public void remove(int index) {}
    
    public void remove(MutableTreeNode node) {}
    
    public void setUserObject(Object object) {}
    
    public void removeFromParent() {
      this.parent = null;
      System.out.println("[MyObjTreeNode] REMOVE FROM PARENT");
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
      ObjectDB.Object dbinfo = ObjectDB.objects.get(this.objectID);
      return dbinfo.name + " (" + dbinfo.ID + ")";
    }
  }
}
