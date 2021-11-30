package whitehole;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import whitehole.rendering.GLRenderer;
import whitehole.rendering.RendererCache;
import whitehole.smg.Bcsv;
import whitehole.smg.GalaxyArchive;
import whitehole.smg.GeneralObject;
import whitehole.smg.GravityObject;
import whitehole.smg.LevelObject;
import whitehole.smg.MapPartObject;
import whitehole.smg.PathObject;
import whitehole.smg.PathPointObject;
import whitehole.smg.StartObject;
import whitehole.smg.ZoneArchive;
import whitehole.vectors.Color4;
import whitehole.vectors.Matrix4;
import whitehole.vectors.Vector2;
import whitehole.vectors.Vector3;

public class GalaxyEditorForm extends JFrame {
  public final float scaledown;
  
  public boolean galaxyMode;
  
  public String galaxyName;
  
  public GalaxyEditorForm parentForm;
  
  public HashMap<String, GalaxyEditorForm> childZoneEditors;
  
  public GalaxyArchive galaxyArc;
  
  public GalaxyRenderer renderer;
  
  public HashMap<String, ZoneArchive> zoneArcs;
  
  public int curScenarioID;
  
  public Bcsv.Entry curScenario;
  
  public String curZone;
  
  public ZoneArchive curZoneArc;
  
  public int maxUniqueID;
  
  public HashMap<Integer, LevelObject> globalObjList;
  
  public HashMap<Integer, PathObject> globalPathList;
  
  public HashMap<Integer, PathPointObject> globalPathPointList;
  
  private HashMap<Integer, TreeNode> treeNodeList;
  
  public HashMap<String, SubZoneData> subZoneData;
  
  private GLCanvas glCanvas;
  
  private boolean inited;
  
  private boolean unsavedChanges;
  
  private GLRenderer.RenderInfo renderinfo;
  
  private HashMap<String, int[]> objDisplayLists;
  
  private HashMap<Integer, int[]> zoneDisplayLists;
  
  private Queue<String> rerenderTasks;
  
  private int zoneModeLayerBitmask;
  
  private Matrix4 modelViewMatrix;
  
  private float camDistance;
  
  private Vector2 camRotation;
  
  private Vector3 camPosition;
  
  private Vector3 camTarget;
  
  private boolean upsideDown;
  
  private float pixelFactorX;
  
  private float pixelFactorY;
  
  private int mouseButton;
  
  private Point lastMouseMove;
  
  private boolean isDragging;
  
  private int keyMask;
  
  private int keyDelta;
  
  private boolean pickingCapture;
  
  private IntBuffer pickingFrameBuffer;
  
  private FloatBuffer pickingDepthBuffer;
  
  private float pickingDepth;
  
  private int underCursor;
  
  private float depthUnderCursor;
  
  private int selectedVal;
  
  private LevelObject selectedObj;
  
  private PathPointObject selectedPathPoint;
  
  private int selectedSubVal;
  
  private String objectBeingAdded;
  
  private String addingOnLayer;
  
  private boolean deletingObjects;
  
  private CheckBoxList lbLayersList;
  
  private PropertyPanel pnlObjectSettings;
  
  private JPopupMenu pmnAddObjects;
  
  private JButton btnAddScenario;
  
  private JButton btnAddZone;
  
  private JButton btnDeleteScenario;
  
  private JButton btnDeleteZone;
  
  private JButton btnDeselect;
  
  private JButton btnEditScenario;
  
  private JButton btnEditZone;
  
  private JButton btnSave;
  
  private JToggleButton btnShowAllPaths;
  
  private JToggleButton btnShowFakecolor;
  
  private JLabel jLabel1;
  
  private JLabel jLabel2;
  
  private JLabel jLabel3;
  
  private JLabel jLabel4;
  
  private JPanel jPanel1;
  
  private JPanel jPanel2;
  
  private JPanel jPanel3;
  
  private JScrollPane jScrollPane1;
  
  private JScrollPane jScrollPane2;
  
  private JScrollPane jScrollPane3;
  
  private JToolBar.Separator jSeparator1;
  
  private JToolBar.Separator jSeparator2;
  
  private JToolBar.Separator jSeparator3;
  
  private JToolBar.Separator jSeparator4;
  
  private JSplitPane jSplitPane1;
  
  private JSplitPane jSplitPane4;
  
  private JToolBar jToolBar1;
  
  private JToolBar jToolBar2;
  
  private JToolBar jToolBar3;
  
  private JToolBar jToolBar4;
  
  private JToolBar jToolBar6;
  
  private JList lbScenarioList;
  
  private JLabel lbSelected;
  
  private JLabel lbStatusLabel;
  
  private JList lbZoneList;
  
  private JPanel pnlGLPanel;
  
  private JPanel pnlLayersPanel;
  
  private JSplitPane pnlScenarioZonePanel;
  
  private JScrollPane scpLayersList;
  
  private JScrollPane scpObjSettingsContainer;
  
  private JToolBar tbObjToolbar;
  
  private JToggleButton tgbAddObject;
  
  private JToggleButton tgbDeleteObject;
  
  private JToggleButton tgbReverseRot;
  
  private JTabbedPane tpLeftPanel;
  
  private JTree tvObjectList;
  
  private void initVariables() {
    this.maxUniqueID = 0;
    this.globalObjList = new HashMap<>();
    this.globalPathList = new HashMap<>();
    this.globalPathPointList = new HashMap<>();
    this.treeNodeList = new HashMap<>();
    this.unsavedChanges = false;
    this.keyMask = 0;
    this.keyDelta = 0;
  }
  
  public GalaxyEditorForm(String galaxy) {
    this.scaledown = 10000.0F;
    initComponents();
    initVariables();
    this.subZoneData = new HashMap<>();
    this.galaxyMode = true;
    this.parentForm = null;
    this.childZoneEditors = new HashMap<>();
    this.galaxyName = galaxy;
    try {
      this.galaxyArc = Whitehole.game.openGalaxy(this.galaxyName);
      this.zoneArcs = new HashMap<>(this.galaxyArc.zoneList.size());
      for (String zone : this.galaxyArc.zoneList)
        loadZone(zone); 
      ZoneArchive mainzone = this.zoneArcs.get(this.galaxyName);
      for (int i = 0; i < this.galaxyArc.scenarioData.size(); i++) {
        for (Bcsv.Entry subzone : mainzone.subZones.get("common")) {
          SubZoneData data = new SubZoneData();
          data.layer = "common";
          data.position = new Vector3(((Float)subzone.get("pos_x")).floatValue(), ((Float)subzone.get("pos_y")).floatValue(), ((Float)subzone.get("pos_z")).floatValue());
          data.rotation = new Vector3(((Float)subzone.get("dir_x")).floatValue(), ((Float)subzone.get("dir_y")).floatValue(), ((Float)subzone.get("dir_z")).floatValue());
          String key = String.format("%1$d/%2$s", new Object[] { Integer.valueOf(i), subzone.get("name") });
          if (this.subZoneData.containsKey(key))
            throw new IOException("Duplicate zone " + key); 
          this.subZoneData.put(key, data);
        } 
        int mainlayermask = ((Integer)((Bcsv.Entry)this.galaxyArc.scenarioData.get(i)).get(this.galaxyName)).intValue();
        for (int l = 0; l < 16; l++) {
          if ((mainlayermask & 1 << l) != 0) {
            String layer = "layer" + (97 + l);
            if (mainzone.subZones.containsKey(layer))
              for (Bcsv.Entry subzone : mainzone.subZones.get(layer)) {
                SubZoneData data = new SubZoneData();
                data.layer = layer;
                data.position = new Vector3(((Float)subzone.get("pos_x")).floatValue(), ((Float)subzone.get("pos_y")).floatValue(), ((Float)subzone.get("pos_z")).floatValue());
                data.rotation = new Vector3(((Float)subzone.get("dir_x")).floatValue(), ((Float)subzone.get("dir_y")).floatValue(), ((Float)subzone.get("dir_z")).floatValue());
                String key = String.format("%1$d/%2$s", new Object[] { Integer.valueOf(i), subzone.get("name") });
                if (this.subZoneData.containsKey(key))
                  throw new IOException("Duplicate zone " + key); 
                this.subZoneData.put(key, data);
              }  
          } 
        } 
      } 
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(null, "Failed to open the galaxy: " + ex.getMessage(), "Whitehole", 0);
      dispose();
      return;
    } 
    initGUI();
    this.btnAddScenario.setVisible(false);
    this.btnEditScenario.setVisible(false);
    this.btnDeleteScenario.setVisible(false);
    this.btnAddZone.setVisible(false);
    this.btnDeleteZone.setVisible(false);
    this.tpLeftPanel.remove(1);
  }
  
  public GalaxyEditorForm(GalaxyEditorForm gal_parent, ZoneArchive zone) {
    this.scaledown = 10000.0F;
    initComponents();
    initVariables();
    this.subZoneData = null;
    this.galaxyArc = null;
    this.galaxyMode = false;
    this.parentForm = gal_parent;
    this.childZoneEditors = null;
    this.galaxyName = zone.zoneName;
    try {
      this.zoneArcs = new HashMap<>(1);
      this.zoneArcs.put(this.galaxyName, zone);
      loadZone(this.galaxyName);
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(null, "Failed to open the zone: " + ex.getMessage(), "Whitehole", 0);
      dispose();
      return;
    } 
    this.curZone = this.galaxyName;
    this.curZoneArc = this.zoneArcs.get(this.curZone);
    initGUI();
    this.tpLeftPanel.remove(0);
    this.lbLayersList = new CheckBoxList();
    this.lbLayersList.setEventListener(new CheckBoxList.EventListener() {
          public void checkBoxStatusChanged(int index, boolean status) {
            GalaxyEditorForm.this.layerSelectChange(index, status);
          }
        });
    this.scpLayersList.setViewportView(this.lbLayersList);
    pack();
    this.zoneModeLayerBitmask = 1;
    JCheckBox[] cblayers = new JCheckBox[this.curZoneArc.objects.keySet().size()];
    int i = 0;
    cblayers[i] = new JCheckBox("Common");
    cblayers[i].setSelected(true);
    i++;
    for (int l = 0; l < 16; l++) {
      String ls = String.format("Layer%1$c", new Object[] { Integer.valueOf(65 + l) });
      if (this.curZoneArc.objects.containsKey(ls.toLowerCase())) {
        cblayers[i] = new JCheckBox(ls);
        if (i == 1) {
          cblayers[i].setSelected(true);
          this.zoneModeLayerBitmask |= 2 << l;
        } 
        i++;
      } 
    } 
    this.lbLayersList.setListData(cblayers);
    populateObjectList(this.zoneModeLayerBitmask);
  }
  
  private void initGUI() {
    setTitle(this.galaxyName + " - " + Whitehole.fullName);
    setIconImage(Toolkit.getDefaultToolkit().createImage(Whitehole.class.getResource("/Resources/icon.png")));
    this.tbObjToolbar.setLayout(new ToolbarFlowLayout(0, 0, 0));
    this.tbObjToolbar.validate();
    this.tgbReverseRot.setSelected(Settings.reverseRot);
    Font bigfont = this.lbStatusLabel.getFont().deriveFont(1, 12.0F);
    this.lbStatusLabel.setFont(bigfont);
    this.pmnAddObjects = new JPopupMenu();
    String[] menuitems = { "General object", "Map part", "Gravity", "Starting point", "Path", "Path point" };
    for (String item : menuitems) {
      JMenuItem mnuitem = new JMenuItem(item);
      mnuitem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              int i = 0;
              for (; !GalaxyEditorForm.this.pmnAddObjects.getComponent(i).equals(e.getSource()); i++);
              switch (i) {
                case 0:
                  GalaxyEditorForm.this.doAddObject("general");
                  break;
                case 1:
                  GalaxyEditorForm.this.doAddObject("mappart");
                  break;
                case 2:
                  GalaxyEditorForm.this.doAddObject("gravity");
                  break;
                case 3:
                  GalaxyEditorForm.this.doAddObject("start");
                  break;
                case 4:
                  GalaxyEditorForm.this.doAddObject("path");
                  break;
                case 5:
                  GalaxyEditorForm.this.doAddObject("pathpoint");
                  break;
              } 
            }
          });
      this.pmnAddObjects.add(mnuitem);
    } 
    this.pmnAddObjects.addPopupMenuListener(new PopupMenuListener() {
          public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
          
          public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            if (!GalaxyEditorForm.this.objectBeingAdded.isEmpty()) {
              GalaxyEditorForm.this.setStatusText();
            } else {
              GalaxyEditorForm.this.tgbAddObject.setSelected(false);
            } 
          }
          
          public void popupMenuCanceled(PopupMenuEvent e) {
            if (!GalaxyEditorForm.this.objectBeingAdded.isEmpty()) {
              GalaxyEditorForm.this.setStatusText();
            } else {
              GalaxyEditorForm.this.tgbAddObject.setSelected(false);
            } 
          }
        });
    this.glCanvas = new GLCanvas(null, null, RendererCache.refContext, null);
    this.glCanvas.addGLEventListener(this.renderer = new GalaxyRenderer());
    this.glCanvas.addMouseListener(this.renderer);
    this.glCanvas.addMouseMotionListener(this.renderer);
    this.glCanvas.addMouseWheelListener(this.renderer);
    this.glCanvas.addKeyListener(this.renderer);
    this.pnlGLPanel.add((Component)this.glCanvas, "Center");
    this.pnlGLPanel.validate();
    this.pnlObjectSettings = new PropertyPanel();
    this.scpObjSettingsContainer.setViewportView(this.pnlObjectSettings);
    this.scpObjSettingsContainer.getVerticalScrollBar().setUnitIncrement(16);
    this.pnlObjectSettings.setEventListener(new PropertyPanel.EventListener() {
          public void propertyChanged(String propname, Object value) {
            GalaxyEditorForm.this.propPanelPropertyChanged(propname, value);
          }
        });
    this.glCanvas.requestFocusInWindow();
  }
  
  private void loadZone(String zone) throws IOException {
    ZoneArchive arc;
    if (this.galaxyMode) {
      arc = this.galaxyArc.openZone(zone);
      this.zoneArcs.put(zone, arc);
    } else {
      arc = this.zoneArcs.get(zone);
    } 
    for (List<LevelObject> objlist : (Iterable<List<LevelObject>>)arc.objects.values()) {
      for (LevelObject obj : objlist) {
        this.globalObjList.put(Integer.valueOf(this.maxUniqueID), obj);
        obj.uniqueID = this.maxUniqueID;
        this.maxUniqueID++;
      } 
    } 
    for (PathObject obj : arc.paths) {
      this.globalPathList.put(Integer.valueOf(this.maxUniqueID), obj);
      obj.uniqueID = this.maxUniqueID;
      this.maxUniqueID++;
      for (PathPointObject pt : obj.points.values()) {
        this.globalPathPointList.put(Integer.valueOf(this.maxUniqueID), pt);
        this.globalPathPointList.put(Integer.valueOf(this.maxUniqueID + 1), pt);
        this.globalPathPointList.put(Integer.valueOf(this.maxUniqueID + 2), pt);
        pt.uniqueID = this.maxUniqueID;
        this.maxUniqueID += 3;
      } 
    } 
  }
  
  public void updateZone(String zone) {
    this.rerenderTasks.add("zone:" + zone);
    this.glCanvas.repaint();
  }
  
  private void initComponents() {
    this.jToolBar1 = new JToolBar();
    this.btnSave = new JButton();
    this.jSeparator1 = new JToolBar.Separator();
    this.jSplitPane1 = new JSplitPane();
    this.pnlGLPanel = new JPanel();
    this.jToolBar2 = new JToolBar();
    this.jLabel2 = new JLabel();
    this.lbSelected = new JLabel();
    this.jSeparator4 = new JToolBar.Separator();
    this.btnDeselect = new JButton();
    this.jSeparator2 = new JToolBar.Separator();
    this.btnShowAllPaths = new JToggleButton();
    this.btnShowFakecolor = new JToggleButton();
    this.jSeparator3 = new JToolBar.Separator();
    this.tgbReverseRot = new JToggleButton();
    this.lbStatusLabel = new JLabel();
    this.tpLeftPanel = new JTabbedPane();
    this.pnlScenarioZonePanel = new JSplitPane();
    this.jPanel1 = new JPanel();
    this.jToolBar3 = new JToolBar();
    this.jLabel3 = new JLabel();
    this.btnAddScenario = new JButton();
    this.btnEditScenario = new JButton();
    this.btnDeleteScenario = new JButton();
    this.jScrollPane1 = new JScrollPane();
    this.lbScenarioList = new JList();
    this.jPanel2 = new JPanel();
    this.jToolBar4 = new JToolBar();
    this.jLabel4 = new JLabel();
    this.btnAddZone = new JButton();
    this.btnDeleteZone = new JButton();
    this.btnEditZone = new JButton();
    this.jScrollPane2 = new JScrollPane();
    this.lbZoneList = new JList();
    this.pnlLayersPanel = new JPanel();
    this.jToolBar6 = new JToolBar();
    this.jLabel1 = new JLabel();
    this.scpLayersList = new JScrollPane();
    this.jSplitPane4 = new JSplitPane();
    this.jPanel3 = new JPanel();
    this.tbObjToolbar = new JToolBar();
    this.tgbAddObject = new JToggleButton();
    this.tgbDeleteObject = new JToggleButton();
    this.jScrollPane3 = new JScrollPane();
    this.tvObjectList = new JTree();
    this.scpObjSettingsContainer = new JScrollPane();
    setDefaultCloseOperation(2);
    setPreferredSize(new Dimension(800, 600));
    addWindowListener(new WindowAdapter() {
          public void windowOpened(WindowEvent evt) {
            GalaxyEditorForm.this.formWindowOpened(evt);
          }
          
          public void windowClosing(WindowEvent evt) {
            GalaxyEditorForm.this.formWindowClosing(evt);
          }
        });
    this.jToolBar1.setFloatable(false);
    this.jToolBar1.setRollover(true);
    this.btnSave.setText("Save");
    this.btnSave.setFocusable(false);
    this.btnSave.setHorizontalTextPosition(0);
    this.btnSave.setVerticalTextPosition(3);
    this.btnSave.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            GalaxyEditorForm.this.btnSaveActionPerformed(evt);
          }
        });
    this.jToolBar1.add(this.btnSave);
    this.jToolBar1.add(this.jSeparator1);
    getContentPane().add(this.jToolBar1, "First");
    this.jSplitPane1.setDividerLocation(300);
    this.jSplitPane1.setFocusable(false);
    this.jSplitPane1.setLastDividerLocation(300);
    this.pnlGLPanel.setMinimumSize(new Dimension(10, 30));
    this.pnlGLPanel.setLayout(new BorderLayout());
    this.jToolBar2.setFloatable(false);
    this.jToolBar2.setRollover(true);
    this.jLabel2.setText("Selected: ");
    this.jToolBar2.add(this.jLabel2);
    this.lbSelected.setText("none");
    this.jToolBar2.add(this.lbSelected);
    this.jToolBar2.add(this.jSeparator4);
    this.btnDeselect.setText("Deselect");
    this.btnDeselect.setEnabled(false);
    this.btnDeselect.setFocusable(false);
    this.btnDeselect.setHorizontalTextPosition(0);
    this.btnDeselect.setVerticalTextPosition(3);
    this.btnDeselect.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            GalaxyEditorForm.this.btnDeselectActionPerformed(evt);
          }
        });
    this.jToolBar2.add(this.btnDeselect);
    this.jToolBar2.add(this.jSeparator2);
    this.btnShowAllPaths.setText("Show all paths");
    this.btnShowAllPaths.setFocusable(false);
    this.btnShowAllPaths.setHorizontalTextPosition(0);
    this.btnShowAllPaths.setVerticalTextPosition(3);
    this.btnShowAllPaths.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            GalaxyEditorForm.this.btnShowAllPathsActionPerformed(evt);
          }
        });
    this.jToolBar2.add(this.btnShowAllPaths);
    this.btnShowFakecolor.setText("[debug: show fakecolor]");
    this.btnShowFakecolor.setFocusable(false);
    this.btnShowFakecolor.setHorizontalTextPosition(0);
    this.btnShowFakecolor.setVerticalTextPosition(3);
    this.btnShowFakecolor.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            GalaxyEditorForm.this.btnShowFakecolorActionPerformed(evt);
          }
        });
    this.jToolBar2.add(this.btnShowFakecolor);
    this.jToolBar2.add(this.jSeparator3);
    this.tgbReverseRot.setText("Reverse rotation");
    this.tgbReverseRot.setFocusable(false);
    this.tgbReverseRot.setHorizontalTextPosition(0);
    this.tgbReverseRot.setVerticalTextPosition(3);
    this.tgbReverseRot.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            GalaxyEditorForm.this.tgbReverseRotActionPerformed(evt);
          }
        });
    this.jToolBar2.add(this.tgbReverseRot);
    this.pnlGLPanel.add(this.jToolBar2, "North");
    this.lbStatusLabel.setText("status text goes here");
    this.pnlGLPanel.add(this.lbStatusLabel, "Last");
    this.jSplitPane1.setRightComponent(this.pnlGLPanel);
    this.tpLeftPanel.setMinimumSize(new Dimension(100, 5));
    this.tpLeftPanel.addChangeListener(new ChangeListener() {
          public void stateChanged(ChangeEvent evt) {
            GalaxyEditorForm.this.tpLeftPanelStateChanged(evt);
          }
        });
    this.pnlScenarioZonePanel.setDividerLocation(200);
    this.pnlScenarioZonePanel.setOrientation(0);
    this.pnlScenarioZonePanel.setLastDividerLocation(200);
    this.jPanel1.setPreferredSize(new Dimension(201, 200));
    this.jPanel1.setLayout(new BorderLayout());
    this.jToolBar3.setFloatable(false);
    this.jToolBar3.setRollover(true);
    this.jLabel3.setText("Scenarios:");
    this.jToolBar3.add(this.jLabel3);
    this.btnAddScenario.setText("Add");
    this.btnAddScenario.setFocusable(false);
    this.btnAddScenario.setHorizontalTextPosition(0);
    this.btnAddScenario.setVerticalTextPosition(3);
    this.jToolBar3.add(this.btnAddScenario);
    this.btnEditScenario.setText("Edit");
    this.btnEditScenario.setFocusable(false);
    this.btnEditScenario.setHorizontalTextPosition(0);
    this.btnEditScenario.setVerticalTextPosition(3);
    this.jToolBar3.add(this.btnEditScenario);
    this.btnDeleteScenario.setText("Delete");
    this.btnDeleteScenario.setFocusable(false);
    this.btnDeleteScenario.setHorizontalTextPosition(0);
    this.btnDeleteScenario.setVerticalTextPosition(3);
    this.jToolBar3.add(this.btnDeleteScenario);
    this.jPanel1.add(this.jToolBar3, "First");
    this.lbScenarioList.setSelectionMode(0);
    this.lbScenarioList.addListSelectionListener(new ListSelectionListener() {
          public void valueChanged(ListSelectionEvent evt) {
            GalaxyEditorForm.this.lbScenarioListValueChanged(evt);
          }
        });
    this.jScrollPane1.setViewportView(this.lbScenarioList);
    this.jPanel1.add(this.jScrollPane1, "Center");
    this.pnlScenarioZonePanel.setTopComponent(this.jPanel1);
    this.jPanel2.setLayout(new BorderLayout());
    this.jToolBar4.setFloatable(false);
    this.jToolBar4.setRollover(true);
    this.jLabel4.setText("Zones:");
    this.jToolBar4.add(this.jLabel4);
    this.btnAddZone.setText("Add");
    this.btnAddZone.setFocusable(false);
    this.btnAddZone.setHorizontalTextPosition(0);
    this.btnAddZone.setVerticalTextPosition(3);
    this.jToolBar4.add(this.btnAddZone);
    this.btnDeleteZone.setText("Delete");
    this.btnDeleteZone.setFocusable(false);
    this.btnDeleteZone.setHorizontalTextPosition(0);
    this.btnDeleteZone.setVerticalTextPosition(3);
    this.jToolBar4.add(this.btnDeleteZone);
    this.btnEditZone.setText("Edit individually");
    this.btnEditZone.setFocusable(false);
    this.btnEditZone.setHorizontalTextPosition(0);
    this.btnEditZone.setVerticalTextPosition(3);
    this.btnEditZone.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            GalaxyEditorForm.this.btnEditZoneActionPerformed(evt);
          }
        });
    this.jToolBar4.add(this.btnEditZone);
    this.jPanel2.add(this.jToolBar4, "First");
    this.lbZoneList.setSelectionMode(0);
    this.lbZoneList.addListSelectionListener(new ListSelectionListener() {
          public void valueChanged(ListSelectionEvent evt) {
            GalaxyEditorForm.this.lbZoneListValueChanged(evt);
          }
        });
    this.jScrollPane2.setViewportView(this.lbZoneList);
    this.jPanel2.add(this.jScrollPane2, "Center");
    this.pnlScenarioZonePanel.setRightComponent(this.jPanel2);
    this.tpLeftPanel.addTab("Scenario/Zone", this.pnlScenarioZonePanel);
    this.pnlLayersPanel.setLayout(new BorderLayout());
    this.jToolBar6.setFloatable(false);
    this.jToolBar6.setRollover(true);
    this.jLabel1.setText("Layers:");
    this.jToolBar6.add(this.jLabel1);
    this.pnlLayersPanel.add(this.jToolBar6, "First");
    this.pnlLayersPanel.add(this.scpLayersList, "Center");
    this.tpLeftPanel.addTab("Layers", this.pnlLayersPanel);
    this.jSplitPane4.setDividerLocation(300);
    this.jSplitPane4.setOrientation(0);
    this.jSplitPane4.setResizeWeight(0.5D);
    this.jSplitPane4.setFocusCycleRoot(true);
    this.jSplitPane4.setLastDividerLocation(300);
    this.jPanel3.setPreferredSize(new Dimension(149, 300));
    this.jPanel3.setLayout(new BorderLayout());
    this.tbObjToolbar.setFloatable(false);
    this.tbObjToolbar.setRollover(true);
    this.tgbAddObject.setText("Add object...");
    this.tgbAddObject.setFocusable(false);
    this.tgbAddObject.setHorizontalTextPosition(0);
    this.tgbAddObject.setVerticalTextPosition(3);
    this.tgbAddObject.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            GalaxyEditorForm.this.tgbAddObjectActionPerformed(evt);
          }
        });
    this.tbObjToolbar.add(this.tgbAddObject);
    this.tgbDeleteObject.setText("Delete...");
    this.tgbDeleteObject.setFocusable(false);
    this.tgbDeleteObject.setHorizontalTextPosition(0);
    this.tgbDeleteObject.setVerticalTextPosition(3);
    this.tgbDeleteObject.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            GalaxyEditorForm.this.tgbDeleteObjectActionPerformed(evt);
          }
        });
    this.tbObjToolbar.add(this.tgbDeleteObject);
    this.jPanel3.add(this.tbObjToolbar, "First");
    DefaultMutableTreeNode treeNode1 = new DefaultMutableTreeNode("root");
    this.tvObjectList.setModel(new DefaultTreeModel(treeNode1));
    this.tvObjectList.setShowsRootHandles(true);
    this.tvObjectList.addTreeSelectionListener(new TreeSelectionListener() {
          public void valueChanged(TreeSelectionEvent evt) {
            GalaxyEditorForm.this.tvObjectListValueChanged(evt);
          }
        });
    this.jScrollPane3.setViewportView(this.tvObjectList);
    this.jPanel3.add(this.jScrollPane3, "Center");
    this.jSplitPane4.setTopComponent(this.jPanel3);
    this.jSplitPane4.setRightComponent(this.scpObjSettingsContainer);
    this.tpLeftPanel.addTab("Objects", this.jSplitPane4);
    this.jSplitPane1.setLeftComponent(this.tpLeftPanel);
    getContentPane().add(this.jSplitPane1, "Center");
    pack();
  }
  
  private void formWindowOpened(WindowEvent evt) {
    if (this.galaxyMode) {
      DefaultListModel<String> scenlist = new DefaultListModel();
      this.lbScenarioList.setModel(scenlist);
      for (Bcsv.Entry scen : this.galaxyArc.scenarioData) {
        scenlist.addElement(String.format("[%1$d] %2$s", new Object[] { Integer.valueOf(((Integer)scen.get("ScenarioNo")).intValue()), scen.get("ScenarioName") }));
      } 
      this.lbScenarioList.setSelectedIndex(0);
    } 
  }
  
  public void selectionChanged() {
    this.pnlObjectSettings.clear();
    if (this.selectedObj != null) {
      String layer = this.selectedObj.layer.equals("common") ? "Common" : ("Layer" + this.selectedObj.layer.substring(5).toUpperCase());
      this.lbSelected.setText(String.format("%1$s (%2$s, %3$s)", new Object[] { this.selectedObj.dbInfo.name, this.selectedObj.zone.zoneName, layer }));
      this.btnDeselect.setEnabled(true);
      this.tgbDeleteObject.setText("Delete");
      LinkedList<String> layerlist = new LinkedList();
      layerlist.add("Common");
      for (int l = 0; l < 26; l++) {
        String ls = String.format("Layer%1$c", new Object[] { Integer.valueOf(65 + l) });
        if (this.curZoneArc.objects.containsKey(ls.toLowerCase()))
          layerlist.add(ls); 
      } 
      this.pnlObjectSettings.addCategory("obj_general", "General settings");
      if (this.selectedObj.name != null && this.selectedObj.getClass() != StartObject.class)
        this.pnlObjectSettings.addField("name", "Object", "objname", (List)null, this.selectedObj.name); 
      if (this.galaxyMode)
        this.pnlObjectSettings.addField("zone", "Zone", "list", this.galaxyArc.zoneList, this.selectedObj.zone.zoneName); 
      this.pnlObjectSettings.addField("layer", "Layer", "list", layerlist, layer);
      this.selectedObj.getProperties(this.pnlObjectSettings);
      this.pnlObjectSettings.addTermination();
    } else if (this.selectedPathPoint != null) {
      PathObject path = this.selectedPathPoint.path;
      LinkedList<String> usagelist = new LinkedList<>();
      usagelist.add("General");
      usagelist.add("Camera");
      this.lbSelected.setText(String.format("[%3$d] %1$s (%2$s), point %4$d", new Object[] { path.data.get("name"), path.zone.zoneName, Integer.valueOf(path.pathID), Integer.valueOf(this.selectedPathPoint.index) }));
      this.btnDeselect.setEnabled(true);
      this.tgbDeleteObject.setText("Delete");
      this.pnlObjectSettings.addCategory("path_settings", "Path settings");
      if (this.galaxyMode)
        this.pnlObjectSettings.addField("[P]zone", "Zone", "list", this.galaxyArc.zoneList, this.selectedPathPoint.path.zone.zoneName); 
      this.pnlObjectSettings.addField("[P]l_id", "Path ID", "int", (List)null, Integer.valueOf(path.pathID));
      this.pnlObjectSettings.addField("[P]closed", "Closed", "bool", (List)null, Boolean.valueOf(((String)path.data.get("closed")).equals("CLOSE")));
      this.pnlObjectSettings.addField("[P]usage", "Usage", "list", usagelist, path.data.get("usage"));
      this.pnlObjectSettings.addField("[P]name", "Name", "text", (List)null, path.data.get("name"));
      this.pnlObjectSettings.addCategory("path_args", "Path arguments");
      this.pnlObjectSettings.addField("[P]path_arg0", "path_arg0", "int", (List)null, path.data.get("path_arg0"));
      this.pnlObjectSettings.addField("[P]path_arg1", "path_arg1", "int", (List)null, path.data.get("path_arg1"));
      this.pnlObjectSettings.addField("[P]path_arg2", "path_arg2", "int", (List)null, path.data.get("path_arg2"));
      this.pnlObjectSettings.addField("[P]path_arg3", "path_arg3", "int", (List)null, path.data.get("path_arg3"));
      this.pnlObjectSettings.addField("[P]path_arg4", "path_arg4", "int", (List)null, path.data.get("path_arg4"));
      this.pnlObjectSettings.addField("[P]path_arg5", "path_arg5", "int", (List)null, path.data.get("path_arg5"));
      this.pnlObjectSettings.addField("[P]path_arg6", "path_arg6", "int", (List)null, path.data.get("path_arg6"));
      this.pnlObjectSettings.addField("[P]path_arg7", "path_arg7", "int", (List)null, path.data.get("path_arg7"));
      this.pnlObjectSettings.addCategory("point_coords", "Point coordinates");
      this.pnlObjectSettings.addField("pnt0_x", "X", "float", (List)null, Float.valueOf(this.selectedPathPoint.point0.x));
      this.pnlObjectSettings.addField("pnt0_y", "Y", "float", (List)null, Float.valueOf(this.selectedPathPoint.point0.y));
      this.pnlObjectSettings.addField("pnt0_z", "Z", "float", (List)null, Float.valueOf(this.selectedPathPoint.point0.z));
      this.pnlObjectSettings.addField("pnt1_x", "Control 1 X", "float", (List)null, Float.valueOf(this.selectedPathPoint.point1.x));
      this.pnlObjectSettings.addField("pnt1_y", "Control 1 Y", "float", (List)null, Float.valueOf(this.selectedPathPoint.point1.y));
      this.pnlObjectSettings.addField("pnt1_z", "Control 1 Z", "float", (List)null, Float.valueOf(this.selectedPathPoint.point1.z));
      this.pnlObjectSettings.addField("pnt2_x", "Control 2 X", "float", (List)null, Float.valueOf(this.selectedPathPoint.point2.x));
      this.pnlObjectSettings.addField("pnt2_y", "Control 2 Y", "float", (List)null, Float.valueOf(this.selectedPathPoint.point2.y));
      this.pnlObjectSettings.addField("pnt2_z", "Control 2 Z", "float", (List)null, Float.valueOf(this.selectedPathPoint.point2.z));
      this.pnlObjectSettings.addCategory("point_args", "Point arguments");
      this.pnlObjectSettings.addField("point_arg0", "point_arg0", "int", (List)null, this.selectedPathPoint.data.get("point_arg0"));
      this.pnlObjectSettings.addField("point_arg1", "point_arg1", "int", (List)null, this.selectedPathPoint.data.get("point_arg1"));
      this.pnlObjectSettings.addField("point_arg2", "point_arg2", "int", (List)null, this.selectedPathPoint.data.get("point_arg2"));
      this.pnlObjectSettings.addField("point_arg3", "point_arg3", "int", (List)null, this.selectedPathPoint.data.get("point_arg3"));
      this.pnlObjectSettings.addField("point_arg4", "point_arg4", "int", (List)null, this.selectedPathPoint.data.get("point_arg4"));
      this.pnlObjectSettings.addField("point_arg5", "point_arg5", "int", (List)null, this.selectedPathPoint.data.get("point_arg5"));
      this.pnlObjectSettings.addField("point_arg6", "point_arg6", "int", (List)null, this.selectedPathPoint.data.get("point_arg6"));
      this.pnlObjectSettings.addField("point_arg7", "point_arg7", "int", (List)null, this.selectedPathPoint.data.get("point_arg7"));
      this.pnlObjectSettings.addTermination();
    } else {
      this.lbSelected.setText("none");
      this.btnDeselect.setEnabled(false);
      this.tgbDeleteObject.setText("Delete...");
    } 
    this.pnlObjectSettings.validate();
    this.pnlObjectSettings.repaint();
    this.glCanvas.requestFocusInWindow();
  }
  
  private void setStatusText() {
    if (this.galaxyMode) {
      this.lbStatusLabel.setText("Editing scenario " + this.lbScenarioList.getSelectedValue() + ", zone " + this.curZone);
    } else {
      this.lbStatusLabel.setText("Editing zone " + this.curZone);
    } 
  }
  
  private void populateObjectSublist(int layermask, ObjListTreeNode objnode, Class<?> type) {
    for (List<LevelObject> objs : (Iterable<List<LevelObject>>)this.curZoneArc.objects.values()) {
      for (LevelObject obj : objs) {
        if (obj.getClass() != type)
          continue; 
        if (!obj.layer.equals("common")) {
          int layernum = obj.layer.charAt(5) - 97;
          if ((layermask & 2 << layernum) == 0)
            continue; 
        } else if ((layermask & 0x1) == 0) {
          continue;
        } 
        TreeNode tn = objnode.addObject(obj);
        this.treeNodeList.put(Integer.valueOf(obj.uniqueID), tn);
      } 
    } 
  }
  
  private void populateObjectList(int layermask) {
    this.treeNodeList.clear();
    DefaultTreeModel objlist = (DefaultTreeModel)this.tvObjectList.getModel();
    DefaultMutableTreeNode root = new DefaultMutableTreeNode(this.curZone);
    objlist.setRoot(root);
    ObjListTreeNode objnode = new ObjListTreeNode();
    objnode.setUserObject("General objects");
    root.add(objnode);
    populateObjectSublist(layermask, objnode, GeneralObject.class);
    objnode = new ObjListTreeNode();
    objnode.setUserObject("Map parts");
    root.add(objnode);
    populateObjectSublist(layermask, objnode, MapPartObject.class);
    objnode = new ObjListTreeNode();
    objnode.setUserObject("Gravity");
    root.add(objnode);
    populateObjectSublist(layermask, objnode, GravityObject.class);
    objnode = new ObjListTreeNode();
    objnode.setUserObject("Starting points");
    root.add(objnode);
    populateObjectSublist(layermask, objnode, StartObject.class);
    objnode = new ObjListTreeNode();
    objnode.setUserObject("Paths");
    root.add(objnode);
    for (PathObject obj : this.curZoneArc.paths) {
      ObjListTreeNode tn = (ObjListTreeNode)objnode.addObject(obj);
      this.treeNodeList.put(Integer.valueOf(obj.uniqueID), tn);
      for (Map.Entry<Integer, TreeNode> ctn : tn.children.entrySet())
        this.treeNodeList.put(ctn.getKey(), ctn.getValue()); 
    } 
  }
  
  private void layerSelectChange(int index, boolean status) {
    JCheckBox cbx = (JCheckBox)this.lbLayersList.getModel().getElementAt(index);
    int layer = cbx.getText().equals("Common") ? 1 : (2 << cbx.getText().charAt(5) - 65);
    if (status) {
      this.zoneModeLayerBitmask |= layer;
    } else {
      this.zoneModeLayerBitmask &= layer ^ 0xFFFFFFFF;
    } 
    this.rerenderTasks.add("allobjects:");
    this.glCanvas.repaint();
  }
  
  private void btnDeselectActionPerformed(ActionEvent evt) {
    this.rerenderTasks.add("zone:" + this.selectedObj.zone.zoneName);
    this.selectedVal = 16777215;
    this.selectedObj = null;
    this.selectedPathPoint = null;
    selectionChanged();
    this.glCanvas.repaint();
  }
  
  private void saveChanges() {
    try {
      for (ZoneArchive zonearc : this.zoneArcs.values())
        zonearc.save(); 
      this.lbStatusLabel.setText("Changes saved.");
      if (!this.galaxyMode && this.parentForm != null) {
        this.parentForm.updateZone(this.galaxyName);
      } else {
        for (GalaxyEditorForm form : this.childZoneEditors.values())
          form.updateZone(form.galaxyName); 
      } 
      this.unsavedChanges = false;
    } catch (IOException ex) {
      this.lbStatusLabel.setText("Failed to save changes: " + ex.getMessage());
      ex.printStackTrace();
    } 
  }
  
  private void btnSaveActionPerformed(ActionEvent evt) {
    saveChanges();
  }
  
  private void formWindowClosing(WindowEvent evt) {
    if (this.galaxyMode)
      for (GalaxyEditorForm form : this.childZoneEditors.values())
        form.dispose();  
    if (this.unsavedChanges) {
      int res = JOptionPane.showConfirmDialog(this, "Save your changes?", "Whitehole", 1, 3);
      if (res == 2) {
        setDefaultCloseOperation(0);
      } else {
        setDefaultCloseOperation(2);
        if (res == 0)
          saveChanges(); 
      } 
    } 
  }
  
  private void tpLeftPanelStateChanged(ChangeEvent evt) {
    int tab = this.tpLeftPanel.getSelectedIndex();
  }
  
  private void tvObjectListValueChanged(TreeSelectionEvent evt) {
    String lastzone = "";
    if (this.selectedObj != null) {
      this.rerenderTasks.add("zone:" + this.selectedObj.zone.zoneName);
      lastzone = this.selectedObj.zone.zoneName;
    } else if (this.selectedPathPoint != null) {
      this.rerenderTasks.add("zone:" + this.selectedPathPoint.path.zone.zoneName);
      lastzone = this.selectedPathPoint.path.zone.zoneName;
    } 
    this.selectedSubVal = 0;
    if (evt.getNewLeadSelectionPath() == null) {
      this.selectedVal = 16777215;
      this.selectedObj = null;
      this.selectedPathPoint = null;
    } else {
      TreeNode selnode = (TreeNode)evt.getNewLeadSelectionPath().getLastPathComponent();
      Object selobj = null;
      if (selnode.getClass() == ObjTreeNode.class) {
        selobj = ((ObjTreeNode)selnode).object;
      } else if (selnode.getClass() == ObjListTreeNode.class) {
        selobj = ((ObjListTreeNode)selnode).object;
      } 
      if (selobj != null) {
        if (selobj.getClass().getSuperclass() == LevelObject.class) {
          this.selectedPathPoint = null;
          this.selectedObj = (LevelObject)((ObjTreeNode)selnode).object;
          this.selectedVal = this.selectedObj.uniqueID;
          if (!lastzone.equals(this.selectedObj.zone.zoneName))
            this.rerenderTasks.add("zone:" + this.selectedObj.zone.zoneName); 
        } else if (selobj.getClass() == PathPointObject.class || selobj.getClass() == PathObject.class) {
          if (selobj.getClass() == PathObject.class) {
            this.selectedPathPoint = (PathPointObject)((PathObject)selobj).points.get(Integer.valueOf(0));
          } else {
            this.selectedPathPoint = (PathPointObject)selobj;
          } 
          this.selectedObj = null;
          this.selectedVal = this.selectedPathPoint.uniqueID;
          if (!lastzone.equals(this.selectedPathPoint.path.zone.zoneName))
            this.rerenderTasks.add("zone:" + this.selectedPathPoint.path.zone.zoneName); 
        } 
      } else {
        this.selectedVal = 16777215;
        this.selectedObj = null;
        this.selectedPathPoint = null;
      } 
    } 
    selectionChanged();
    this.glCanvas.repaint();
  }
  
  private void tgbDeleteObjectActionPerformed(ActionEvent evt) {
    if (this.selectedObj != null) {
      if (this.tgbDeleteObject.isSelected()) {
        deleteObject(this.selectedObj.uniqueID);
        this.selectedVal = 16777215;
        this.selectedObj = null;
        this.selectedPathPoint = null;
        selectionChanged();
      } 
      this.tgbDeleteObject.setSelected(false);
    } else if (!this.tgbDeleteObject.isSelected()) {
      this.deletingObjects = false;
      setStatusText();
    } else {
      this.deletingObjects = true;
      this.lbStatusLabel.setText("Click the object you want to delete. Hold Shift to delete multiple objects. Right-click to abort.");
    } 
  }
  
  private void tgbAddObjectActionPerformed(ActionEvent evt) {
    if (this.tgbAddObject.isSelected()) {
      this.pmnAddObjects.show(this.tgbAddObject, 0, this.tgbAddObject.getHeight());
    } else {
      this.pmnAddObjects.setVisible(false);
      setStatusText();
    } 
  }
  
  private void lbZoneListValueChanged(ListSelectionEvent evt) {
    if (evt.getValueIsAdjusting())
      return; 
    if (this.lbZoneList.getSelectedValue() == null)
      return; 
    this.btnEditZone.setEnabled(true);
    int selid = this.lbZoneList.getSelectedIndex();
    this.curZone = this.galaxyArc.zoneList.get(selid);
    this.curZoneArc = this.zoneArcs.get(this.curZone);
    int layermask = ((Integer)this.curScenario.get(this.curZone)).intValue();
    populateObjectList(layermask << 1 | 0x1);
    setStatusText();
    this.glCanvas.repaint();
  }
  
  private void btnEditZoneActionPerformed(ActionEvent evt) {
    if (this.childZoneEditors.containsKey(this.curZone))
      if (!((GalaxyEditorForm)this.childZoneEditors.get(this.curZone)).isVisible()) {
        this.childZoneEditors.remove(this.curZone);
      } else {
        ((GalaxyEditorForm)this.childZoneEditors.get(this.curZone)).toFront();
        return;
      }  
    GalaxyEditorForm form = new GalaxyEditorForm(this, this.curZoneArc);
    form.setVisible(true);
    this.childZoneEditors.put(this.curZone, form);
  }
  
  private void lbScenarioListValueChanged(ListSelectionEvent evt) {
    if (evt.getValueIsAdjusting())
      return; 
    if (this.lbScenarioList.getSelectedValue() == null)
      return; 
    this.curScenarioID = this.lbScenarioList.getSelectedIndex();
    this.curScenario = this.galaxyArc.scenarioData.get(this.curScenarioID);
    DefaultListModel<String> zonelist = new DefaultListModel();
    this.lbZoneList.setModel(zonelist);
    for (String zone : this.galaxyArc.zoneList) {
      String layerstr = "ABCDEFGHIJKLMNOP";
      int layermask = ((Integer)this.curScenario.get(zone)).intValue();
      String layers = "Common+";
      for (int i = 0; i < 16; i++) {
        if ((layermask & 1 << i) != 0)
          layers = layers + layerstr.charAt(i); 
      } 
      if (layers.equals("Common+"))
        layers = "Common"; 
      zonelist.addElement(zone + " [" + layers + "]");
    } 
    this.lbZoneList.setSelectedIndex(0);
  }
  
  private void btnShowAllPathsActionPerformed(ActionEvent evt) {
    for (String zone : this.zoneArcs.keySet())
      this.rerenderTasks.add("zone:" + zone); 
    this.glCanvas.repaint();
  }
  
  private void btnShowFakecolorActionPerformed(ActionEvent evt) {
    this.glCanvas.repaint();
  }
  
  private void tgbReverseRotActionPerformed(ActionEvent evt) {
    Settings.reverseRot = this.tgbReverseRot.isSelected();
    Settings.save();
  }
  
  public void applySubzoneRotation(Vector3 delta) {
    if (!this.galaxyMode)
      return; 
    String szkey = String.format("%1$d/%2$s", new Object[] { Integer.valueOf(this.curScenarioID), this.curZone });
    if (this.subZoneData.containsKey(szkey)) {
      SubZoneData szdata = this.subZoneData.get(szkey);
      float xcos = (float)Math.cos(-(szdata.rotation.x * Math.PI) / 180.0D);
      float xsin = (float)Math.sin(-(szdata.rotation.x * Math.PI) / 180.0D);
      float ycos = (float)Math.cos(-(szdata.rotation.y * Math.PI) / 180.0D);
      float ysin = (float)Math.sin(-(szdata.rotation.y * Math.PI) / 180.0D);
      float zcos = (float)Math.cos(-(szdata.rotation.z * Math.PI) / 180.0D);
      float zsin = (float)Math.sin(-(szdata.rotation.z * Math.PI) / 180.0D);
      float x1 = delta.x * zcos - delta.y * zsin;
      float y1 = delta.x * zsin + delta.y * zcos;
      float x2 = x1 * ycos + delta.z * ysin;
      float z2 = -(x1 * ysin) + delta.z * ycos;
      float y3 = y1 * xcos - z2 * xsin;
      float z3 = y1 * xsin + z2 * xcos;
      delta.x = x2;
      delta.y = y3;
      delta.z = z3;
    } 
  }
  
  private Vector3 get3DCoords(Point pt, float depth) {
    Vector3 ret = new Vector3(this.camPosition.x * 10000.0F, this.camPosition.y * 10000.0F, this.camPosition.z * 10000.0F);
    depth *= 10000.0F;
    ret.x -= depth * (float)Math.cos(this.camRotation.x) * (float)Math.cos(this.camRotation.y);
    ret.y -= depth * (float)Math.sin(this.camRotation.y);
    ret.z -= depth * (float)Math.sin(this.camRotation.x) * (float)Math.cos(this.camRotation.y);
    float x = (pt.x - this.glCanvas.getWidth() / 2.0F) * this.pixelFactorX * depth;
    float y = -(pt.y - this.glCanvas.getHeight() / 2.0F) * this.pixelFactorY * depth;
    ret.x += x * (float)Math.sin(this.camRotation.x) - y * (float)Math.sin(this.camRotation.y) * (float)Math.cos(this.camRotation.x);
    ret.y += y * (float)Math.cos(this.camRotation.y);
    ret.z += -(x * (float)Math.cos(this.camRotation.x)) - y * (float)Math.sin(this.camRotation.y) * (float)Math.sin(this.camRotation.x);
    return ret;
  }
  
  private void offsetSelectionBy(Vector3 delta) {
    if (this.selectedObj != null) {
      this.selectedObj.position.x += delta.x;
      this.selectedObj.position.y += delta.y;
      this.selectedObj.position.z += delta.z;
      this.pnlObjectSettings.setFieldValue("pos_x", Float.valueOf(this.selectedObj.position.x));
      this.pnlObjectSettings.setFieldValue("pos_y", Float.valueOf(this.selectedObj.position.y));
      this.pnlObjectSettings.setFieldValue("pos_z", Float.valueOf(this.selectedObj.position.z));
      this.rerenderTasks.add("zone:" + this.selectedObj.zone.zoneName);
    } else if (this.selectedPathPoint != null) {
      switch (this.selectedSubVal) {
        case 0:
          this.selectedPathPoint.point0.x += delta.x;
          this.selectedPathPoint.point0.y += delta.y;
          this.selectedPathPoint.point0.z += delta.z;
          this.selectedPathPoint.point1.x += delta.x;
          this.selectedPathPoint.point1.y += delta.y;
          this.selectedPathPoint.point1.z += delta.z;
          this.selectedPathPoint.point2.x += delta.x;
          this.selectedPathPoint.point2.y += delta.y;
          this.selectedPathPoint.point2.z += delta.z;
          break;
        case 1:
          this.selectedPathPoint.point1.x += delta.x;
          this.selectedPathPoint.point1.y += delta.y;
          this.selectedPathPoint.point1.z += delta.z;
          break;
        case 2:
          this.selectedPathPoint.point2.x += delta.x;
          this.selectedPathPoint.point2.y += delta.y;
          this.selectedPathPoint.point2.z += delta.z;
          break;
      } 
      this.pnlObjectSettings.setFieldValue("pnt0_x", Float.valueOf(this.selectedPathPoint.point0.x));
      this.pnlObjectSettings.setFieldValue("pnt0_y", Float.valueOf(this.selectedPathPoint.point0.y));
      this.pnlObjectSettings.setFieldValue("pnt0_z", Float.valueOf(this.selectedPathPoint.point0.z));
      this.pnlObjectSettings.setFieldValue("pnt1_x", Float.valueOf(this.selectedPathPoint.point1.x));
      this.pnlObjectSettings.setFieldValue("pnt1_y", Float.valueOf(this.selectedPathPoint.point1.y));
      this.pnlObjectSettings.setFieldValue("pnt1_z", Float.valueOf(this.selectedPathPoint.point1.z));
      this.pnlObjectSettings.setFieldValue("pnt2_x", Float.valueOf(this.selectedPathPoint.point2.x));
      this.pnlObjectSettings.setFieldValue("pnt2_y", Float.valueOf(this.selectedPathPoint.point2.y));
      this.pnlObjectSettings.setFieldValue("pnt2_z", Float.valueOf(this.selectedPathPoint.point2.z));
      this.rerenderTasks.add(String.format("path:%1$d", new Object[] { Integer.valueOf(this.selectedPathPoint.path.uniqueID) }));
      this.rerenderTasks.add("zone:" + this.selectedPathPoint.path.zone.zoneName);
    } 
  }
  
  private void addObject(Point where) {
    GeneralObject generalObject;
    MapPartObject mapPartObject;
    GravityObject gravityObject;
    StartObject startObject = null;
    Vector3 pos = get3DCoords(where, Math.min(this.pickingDepth, 1.0F));
    if (this.galaxyMode) {
      String szkey = String.format("%1$d/%2$s", new Object[] { Integer.valueOf(this.curScenarioID), this.curZone });
      if (this.subZoneData.containsKey(szkey)) {
        SubZoneData szdata = this.subZoneData.get(szkey);
        Vector3.subtract(pos, szdata.position, pos);
        applySubzoneRotation(pos);
      } 
    } 
    String objtype = this.objectBeingAdded.substring(0, this.objectBeingAdded.indexOf('|'));
    String objname = this.objectBeingAdded.substring(this.objectBeingAdded.indexOf('|') + 1);
    LevelObject newobj = null;
    int pnodeid = -1;
    if (objtype.equals("path") || objtype.equals("pathpoint"))
      throw new UnsupportedOperationException("ADDING PATHS NOT SUPPORTED YET"); 
    switch (objtype) {
      case "general":
        generalObject = new GeneralObject(this.curZoneArc, "Placement/" + this.addingOnLayer + "/ObjInfo", this.curZoneArc.gameMask, objname, pos);
        pnodeid = 0;
        break;
      case "mappart":
        mapPartObject = new MapPartObject(this.curZoneArc, "MapParts/" + this.addingOnLayer + "/MapPartsInfo", this.curZoneArc.gameMask, objname, pos);
        pnodeid = 1;
        break;
      case "gravity":
        gravityObject = new GravityObject(this.curZoneArc, "Placement/" + this.addingOnLayer + "/PlanetObjInfo", this.curZoneArc.gameMask, objname, pos);
        pnodeid = 2;
        break;
      case "start":
        startObject = new StartObject(this.curZoneArc, "Start/" + this.addingOnLayer + "/StartInfo", this.curZoneArc.gameMask, pos);
        pnodeid = 3;
        break;
    } 
    int uid = 0;
    while (this.globalObjList.containsKey(Integer.valueOf(uid)) || this.globalPathList.containsKey(Integer.valueOf(uid)) || this.globalPathPointList.containsKey(Integer.valueOf(uid)))
      uid++; 
    if (uid > this.maxUniqueID)
      this.maxUniqueID = uid; 
    ((LevelObject)startObject).uniqueID = uid;
    this.globalObjList.put(Integer.valueOf(uid), startObject);
    ((List<LevelObject>)this.curZoneArc.objects.get(this.addingOnLayer.toLowerCase())).add(startObject);
    DefaultTreeModel objlist = (DefaultTreeModel)this.tvObjectList.getModel();
    ObjListTreeNode listnode = (ObjListTreeNode)((DefaultMutableTreeNode)objlist.getRoot()).getChildAt(pnodeid);
    TreeNode newnode = listnode.addObject((LevelObject)startObject);
    objlist.nodesWereInserted(listnode, new int[] { listnode.getIndex(newnode) });
    this.treeNodeList.put(Integer.valueOf(uid), newnode);
    this.rerenderTasks.add(String.format("addobj:%1$d", new Object[] { Integer.valueOf(uid) }));
    this.rerenderTasks.add("zone:" + this.curZone);
    this.glCanvas.repaint();
    this.unsavedChanges = true;
  }
  
  private void doAddObject(String type) {
    if (type.equals("start")) {
      this.objectBeingAdded = "start|Mario";
      this.addingOnLayer = "common";
    } else if (type.equals("path")) {
      this.objectBeingAdded = "path|lol";
    } else if (type.equals("pathpoint")) {
      this.objectBeingAdded = "pathpoint|lol";
    } else {
      ObjectSelectForm form = new ObjectSelectForm(this, this.curZoneArc.gameMask, null);
      form.setVisible(true);
      if (form.selectedObject.isEmpty()) {
        this.tgbAddObject.setSelected(false);
        return;
      } 
      this.objectBeingAdded = type + "|" + form.selectedObject;
      this.addingOnLayer = form.selectedLayer;
    } 
    this.lbStatusLabel.setText("Click the level view to place your object. Hold Shift to place multiple objects. Right-click to abort.");
  }
  
  private void deleteObject(int uid) {
    LevelObject obj = this.globalObjList.get(Integer.valueOf(uid));
    ((List)((ZoneArchive)this.zoneArcs.get(obj.zone.zoneName)).objects.get(obj.layer)).remove(obj);
    this.rerenderTasks.add(String.format("delobj:%1$d", new Object[] { Integer.valueOf(uid) }));
    this.rerenderTasks.add("zone:" + obj.zone.zoneName);
    if (this.treeNodeList.containsKey(Integer.valueOf(uid))) {
      DefaultTreeModel objlist = (DefaultTreeModel)this.tvObjectList.getModel();
      ObjTreeNode thenode = (ObjTreeNode)this.treeNodeList.get(Integer.valueOf(uid));
      objlist.removeNodeFromParent(thenode);
      this.treeNodeList.remove(Integer.valueOf(uid));
    } 
    this.glCanvas.repaint();
    this.unsavedChanges = true;
  }
  
  public void propPanelPropertyChanged(String propname, Object value) {
    if (this.selectedObj != null) {
      if (propname.equals("name")) {
        this.selectedObj.name = (String)value;
        this.selectedObj.loadDBInfo();
        DefaultTreeModel objlist = (DefaultTreeModel)this.tvObjectList.getModel();
        objlist.nodeChanged(this.treeNodeList.get(Integer.valueOf(this.selectedObj.uniqueID)));
        this.rerenderTasks.add("object:" + (new Integer(this.selectedObj.uniqueID)).toString());
        this.rerenderTasks.add("zone:" + this.selectedObj.zone.zoneName);
        this.glCanvas.repaint();
      } else if (propname.equals("zone")) {
        String oldzone = this.selectedObj.zone.zoneName;
        String newzone = (String)value;
        int uid = this.selectedObj.uniqueID;
        this.selectedObj.zone = this.zoneArcs.get(newzone);
        ((List)((ZoneArchive)this.zoneArcs.get(oldzone)).objects.get(this.selectedObj.layer)).remove(this.selectedObj);
        if (((ZoneArchive)this.zoneArcs.get(newzone)).objects.containsKey(this.selectedObj.layer)) {
          ((List<LevelObject>)((ZoneArchive)this.zoneArcs.get(newzone)).objects.get(this.selectedObj.layer)).add(this.selectedObj);
        } else {
          this.selectedObj.layer = "common";
          ((List<LevelObject>)((ZoneArchive)this.zoneArcs.get(newzone)).objects.get(this.selectedObj.layer)).add(this.selectedObj);
        } 
        for (int z = 0; z < this.galaxyArc.zoneList.size(); ) {
          if (!((String)this.galaxyArc.zoneList.get(z)).equals(newzone)) {
            z++;
            continue;
          } 
          this.lbZoneList.setSelectedIndex(z);
        } 
        if (this.treeNodeList.containsKey(Integer.valueOf(uid))) {
          TreeNode tn = this.treeNodeList.get(Integer.valueOf(uid));
          TreePath tp = new TreePath((Object[])((DefaultTreeModel)this.tvObjectList.getModel()).getPathToRoot(tn));
          this.tvObjectList.setSelectionPath(tp);
          this.tvObjectList.scrollPathToVisible(tp);
        } 
        selectionChanged();
        this.rerenderTasks.add("zone:" + oldzone);
        this.rerenderTasks.add("zone:" + newzone);
        this.glCanvas.repaint();
      } else if (propname.equals("layer")) {
        String oldlayer = this.selectedObj.layer;
        String newlayer = ((String)value).toLowerCase();
        this.selectedObj.layer = newlayer;
        ((List)this.curZoneArc.objects.get(oldlayer)).remove(this.selectedObj);
        ((List<LevelObject>)this.curZoneArc.objects.get(newlayer)).add(this.selectedObj);
        DefaultTreeModel objlist = (DefaultTreeModel)this.tvObjectList.getModel();
        objlist.nodeChanged(this.treeNodeList.get(Integer.valueOf(this.selectedObj.uniqueID)));
        this.rerenderTasks.add("zone:" + this.selectedObj.zone.zoneName);
        this.glCanvas.repaint();
      } else if (propname.startsWith("pos_") || propname.startsWith("dir_") || propname.startsWith("scale_")) {
        switch (propname) {
          case "pos_x":
            this.selectedObj.position.x = (float)((Double)value).doubleValue();
            break;
          case "pos_y":
            this.selectedObj.position.y = (float)((Double)value).doubleValue();
            break;
          case "pos_z":
            this.selectedObj.position.z = (float)((Double)value).doubleValue();
            break;
          case "dir_x":
            this.selectedObj.rotation.x = (float)((Double)value).doubleValue();
            break;
          case "dir_y":
            this.selectedObj.rotation.y = (float)((Double)value).doubleValue();
            break;
          case "dir_z":
            this.selectedObj.rotation.z = (float)((Double)value).doubleValue();
            break;
          case "scale_x":
            this.selectedObj.scale.x = (float)((Double)value).doubleValue();
            break;
          case "scale_y":
            this.selectedObj.scale.y = (float)((Double)value).doubleValue();
            break;
          case "scale_z":
            this.selectedObj.scale.z = (float)((Double)value).doubleValue();
            break;
        } 
        if (propname.startsWith("scale_") && this.selectedObj.renderer.hasSpecialScaling())
          this.rerenderTasks.add("object:" + (new Integer(this.selectedObj.uniqueID)).toString()); 
        this.rerenderTasks.add("zone:" + this.selectedObj.zone.zoneName);
        this.glCanvas.repaint();
      } else {
        Object val = Integer.valueOf(-1);
        if (value.getClass() == String.class) {
          try {
            val = Integer.valueOf(Integer.parseInt((String)value));
          } catch (NumberFormatException ex) {}
        } else if (value.getClass() == Double.class) {
          val = value;
        } 
        Object oldval = this.selectedObj.data.get(propname);
        if (oldval.getClass() == String.class) {
          this.selectedObj.data.put(propname, value);
        } else if (oldval.getClass() == Integer.class) {
          this.selectedObj.data.put(propname, Integer.valueOf(((Integer)val).intValue()));
        } else if (oldval.getClass() == Short.class) {
          this.selectedObj.data.put(propname, Short.valueOf((short)((Integer)val).intValue()));
        } else if (oldval.getClass() == Float.class) {
          this.selectedObj.data.put(propname, Float.valueOf((float)((Double)val).doubleValue()));
        } else {
          throw new UnsupportedOperationException("UNSUPPORTED PROP TYPE: " + oldval.getClass().getName());
        } 
        if (propname.startsWith("Obj_arg")) {
          int argnum = Integer.parseInt(propname.substring(7));
          if (this.selectedObj.renderer.boundToObjArg(argnum)) {
            this.rerenderTasks.add("object:" + (new Integer(this.selectedObj.uniqueID)).toString());
            this.rerenderTasks.add("zone:" + this.selectedObj.zone.zoneName);
            this.glCanvas.repaint();
          } 
        } else if (propname.equals("MarioNo")) {
          DefaultTreeModel objlist = (DefaultTreeModel)this.tvObjectList.getModel();
          objlist.nodeChanged(this.treeNodeList.get(Integer.valueOf(this.selectedObj.uniqueID)));
        } 
      } 
    } else if (this.selectedPathPoint != null) {
      PathObject path = this.selectedPathPoint.path;
      if (propname.equals("[P]zone")) {
        String oldzone = path.zone.zoneName;
        ZoneArchive oldzonearc = this.zoneArcs.get(oldzone);
        String newzone = (String)value;
        ZoneArchive newzonearc = this.zoneArcs.get(newzone);
        int uid = this.selectedPathPoint.uniqueID;
        oldzonearc.paths.remove(path);
        path.deleteStorage();
        int newid = 0;
        while (true) {
          boolean found = true;
          for (PathObject pobj : newzonearc.paths) {
            if (pobj.index == newid) {
              found = false;
              break;
            } 
          } 
          if (found)
            break; 
          newid++;
        } 
        path.zone = this.zoneArcs.get(newzone);
        newzonearc.paths.add(path);
        path.index = newid;
        path.createStorage();
        for (int z = 0; z < this.galaxyArc.zoneList.size(); ) {
          if (!((String)this.galaxyArc.zoneList.get(z)).equals(newzone)) {
            z++;
            continue;
          } 
          this.lbZoneList.setSelectedIndex(z);
        } 
        if (this.treeNodeList.containsKey(Integer.valueOf(uid))) {
          TreeNode tn = this.treeNodeList.get(Integer.valueOf(uid));
          TreePath tp = new TreePath((Object[])((DefaultTreeModel)this.tvObjectList.getModel()).getPathToRoot(tn));
          this.tvObjectList.setSelectionPath(tp);
          this.tvObjectList.scrollPathToVisible(tp);
        } 
        selectionChanged();
        this.rerenderTasks.add("zone:" + oldzone);
        this.rerenderTasks.add("zone:" + newzone);
        this.glCanvas.repaint();
      } else if (propname.equals("[P]l_id")) {
        int val = -1;
        try {
          val = Integer.parseInt((String)value);
        } catch (NumberFormatException ex) {}
        if (val != -1)
          path.pathID = val; 
      } else if (propname.equals("[P]closed")) {
        boolean closed = ((Boolean)value).booleanValue();
        if (closed) {
          path.data.put("closed", "CLOSE");
        } else {
          path.data.put("closed", "OPEN");
        } 
        this.rerenderTasks.add(String.format("path:%1$d", new Object[] { Integer.valueOf(path.uniqueID) }));
        this.glCanvas.repaint();
      } else if (propname.startsWith("pnt0_") || propname.startsWith("pnt1_") || propname.startsWith("pnt2_")) {
        switch (propname) {
          case "pnt0_x":
            this.selectedPathPoint.point0.x = (float)((Double)value).doubleValue();
            break;
          case "pnt0_y":
            this.selectedPathPoint.point0.y = (float)((Double)value).doubleValue();
            break;
          case "pnt0_z":
            this.selectedPathPoint.point0.z = (float)((Double)value).doubleValue();
            break;
          case "pnt1_x":
            this.selectedPathPoint.point1.x = (float)((Double)value).doubleValue();
            break;
          case "pnt1_y":
            this.selectedPathPoint.point1.y = (float)((Double)value).doubleValue();
            break;
          case "pnt1_z":
            this.selectedPathPoint.point1.z = (float)((Double)value).doubleValue();
            break;
          case "pnt2_x":
            this.selectedPathPoint.point2.x = (float)((Double)value).doubleValue();
            break;
          case "pnt2_y":
            this.selectedPathPoint.point2.y = (float)((Double)value).doubleValue();
            break;
          case "pnt2_z":
            this.selectedPathPoint.point2.z = (float)((Double)value).doubleValue();
            break;
        } 
        this.rerenderTasks.add(String.format("path:%1$d", new Object[] { Integer.valueOf(path.uniqueID) }));
        this.rerenderTasks.add("zone:" + path.zone.zoneName);
        this.glCanvas.repaint();
      } else {
        int intval = -1;
        try {
          intval = Integer.parseInt((String)value);
        } catch (NumberFormatException ex) {}
        if (propname.startsWith("[P]")) {
          propname = propname.substring(3);
          Object oldval = path.data.get(propname);
          if (oldval.getClass() == Integer.class) {
            path.data.put(propname, Integer.valueOf(intval));
          } else if (oldval.getClass() == Short.class) {
            path.data.put(propname, Short.valueOf((short)intval));
          } else if (oldval.getClass() == String.class) {
            path.data.put(propname, value);
          } else {
            throw new UnsupportedOperationException("UNSUPPORTED PROP TYPE: " + oldval.getClass().getName());
          } 
        } else {
          Object oldval = this.selectedPathPoint.data.get(propname);
          if (oldval.getClass() == Integer.class) {
            this.selectedPathPoint.data.put(propname, Integer.valueOf(intval));
          } else {
            throw new UnsupportedOperationException("UNSUPPORTED PROP TYPE: " + oldval.getClass().getName());
          } 
        } 
      } 
    } else {
      throw new UnsupportedOperationException("oops, bug. Tell Mega-Mario.");
    } 
    this.unsavedChanges = true;
  }
  
  public class GalaxyRenderer implements GLEventListener, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
    public final float fov;
    
    public final float zNear;
    
    public final float zFar;
    
    public class AsyncPrerenderer implements Runnable {
      private GL2 gl;
      
      public AsyncPrerenderer(GL2 gl) {
        this.gl = gl;
      }
      
      public void run() {
        this.gl.getContext().makeCurrent();
        if (GalaxyEditorForm.this.parentForm == null) {
          for (LevelObject obj : GalaxyEditorForm.this.globalObjList.values())
            obj.initRenderer(GalaxyEditorForm.this.renderinfo); 
          for (PathObject obj : GalaxyEditorForm.this.globalPathList.values())
            obj.prerender(GalaxyEditorForm.this.renderinfo); 
        } 
        GalaxyEditorForm.this.renderinfo.renderMode = GLRenderer.RenderMode.PICKING;
        GalaxyEditorForm.GalaxyRenderer.this.renderAllObjects(this.gl);
        GalaxyEditorForm.this.renderinfo.renderMode = GLRenderer.RenderMode.OPAQUE;
        GalaxyEditorForm.GalaxyRenderer.this.renderAllObjects(this.gl);
        GalaxyEditorForm.this.renderinfo.renderMode = GLRenderer.RenderMode.TRANSLUCENT;
        GalaxyEditorForm.GalaxyRenderer.this.renderAllObjects(this.gl);
        this.gl.getContext().release();
        GalaxyEditorForm.this.glCanvas.repaint();
        GalaxyEditorForm.this.setStatusText();
      }
    }
    
    public GalaxyRenderer() {
      this.fov = 1.2217305F;
      this.zNear = 0.01F;
      this.zFar = 1000.0F;
    }
    
    public void init(GLAutoDrawable glad) {
      GL2 gl = glad.getGL().getGL2();
      RendererCache.setRefContext(glad.getContext());
      GalaxyEditorForm.this.lastMouseMove = new Point(-1, -1);
      GalaxyEditorForm.this.pickingFrameBuffer = IntBuffer.allocate(9);
      GalaxyEditorForm.this.pickingDepthBuffer = FloatBuffer.allocate(1);
      GalaxyEditorForm.this.pickingDepth = 1.0F;
      GalaxyEditorForm.this.isDragging = false;
      GalaxyEditorForm.this.pickingCapture = false;
      GalaxyEditorForm.this.underCursor = 16777215;
      GalaxyEditorForm.this.selectedVal = 16777215;
      GalaxyEditorForm.this.selectedObj = null;
      GalaxyEditorForm.this.selectedPathPoint = null;
      GalaxyEditorForm.this.selectedSubVal = 0;
      GalaxyEditorForm.this.objectBeingAdded = "";
      GalaxyEditorForm.this.addingOnLayer = "";
      GalaxyEditorForm.this.deletingObjects = false;
      GalaxyEditorForm.this.renderinfo = new GLRenderer.RenderInfo();
      GalaxyEditorForm.this.renderinfo.drawable = glad;
      GalaxyEditorForm.this.renderinfo.renderMode = GLRenderer.RenderMode.OPAQUE;
      GalaxyEditorForm.this.camDistance = 1.0F;
      GalaxyEditorForm.this.camRotation = new Vector2(0.0F, 0.0F);
      GalaxyEditorForm.this.camTarget = new Vector3(0.0F, 0.0F, 0.0F);
      GalaxyEditorForm.this.camPosition = new Vector3(0.0F, 0.0F, 0.0F);
      updateCamera();
      GalaxyEditorForm.this.objDisplayLists = (HashMap)new HashMap<>();
      GalaxyEditorForm.this.zoneDisplayLists = (HashMap)new HashMap<>();
      GalaxyEditorForm.this.rerenderTasks = new PriorityQueue();
      for (int s = 0; s < GalaxyEditorForm.this.galaxyArc.scenarioData.size(); s++) {
        GalaxyEditorForm.this.zoneDisplayLists.put(Integer.valueOf(s), new int[] { 0, 0, 0 });
      } 
      gl.glFrontFace(2304);
      gl.glClearColor(0.0F, 0.0F, 0.125F, 1.0F);
      gl.glClear(16384);
      GalaxyEditorForm.this.lbStatusLabel.setText("Prerendering " + (GalaxyEditorForm.this.galaxyMode ? "galaxy" : "zone") + ", please wait...");
      SwingUtilities.invokeLater(new AsyncPrerenderer(gl));
      GalaxyEditorForm.this.inited = true;
    }
    
    private void renderSelectHighlight(GL2 gl) {
      try {
        gl.glUseProgram(0);
      } catch (GLException ex) {}
      for (int i = 0; i < 8; i++) {
        try {
          gl.glActiveTexture(33984 + i);
          gl.glDisable(3553);
        } catch (GLException ex) {}
      } 
      gl.glDisable(3553);
      gl.glEnable(3042);
      gl.glBlendEquation(32774);
      gl.glBlendFunc(770, 771);
      gl.glDisable(3058);
      gl.glDisable(3008);
      gl.glDepthMask(false);
      gl.glEnable(32823);
      gl.glPolygonOffset(-1.0F, -1.0F);
      GalaxyEditorForm.this.renderinfo.drawable = (GLAutoDrawable)GalaxyEditorForm.this.glCanvas;
      GLRenderer.RenderMode oldmode = GalaxyEditorForm.this.renderinfo.renderMode;
      GalaxyEditorForm.this.renderinfo.renderMode = GLRenderer.RenderMode.PICKING;
      gl.glColor4f(1.0F, 1.0F, 0.75F, 0.3F);
      GalaxyEditorForm.this.selectedObj.render(GalaxyEditorForm.this.renderinfo);
      gl.glDisable(32823);
      GalaxyEditorForm.this.renderinfo.renderMode = oldmode;
    }
    
    private void renderAllObjects(GL2 gl) {
      int mode = -1;
      switch (GalaxyEditorForm.this.renderinfo.renderMode) {
        case PICKING:
          mode = 0;
          break;
        case OPAQUE:
          mode = 1;
          break;
        case TRANSLUCENT:
          mode = 2;
          break;
      } 
      if (GalaxyEditorForm.this.galaxyMode) {
        for (String zone : GalaxyEditorForm.this.galaxyArc.zoneList)
          prerenderZone(gl, zone); 
        for (int s = 0; s < GalaxyEditorForm.this.galaxyArc.scenarioData.size(); s++) {
          int dl = ((int[])GalaxyEditorForm.this.zoneDisplayLists.get(Integer.valueOf(s)))[mode];
          if (dl == 0) {
            dl = gl.glGenLists(1);
            ((int[])GalaxyEditorForm.this.zoneDisplayLists.get(Integer.valueOf(s)))[mode] = dl;
          } 
          gl.glNewList(dl, 4864);
          Bcsv.Entry scenario = GalaxyEditorForm.this.galaxyArc.scenarioData.get(s);
          renderZone(gl, scenario, GalaxyEditorForm.this.galaxyName, ((Integer)scenario.get(GalaxyEditorForm.this.galaxyName)).intValue(), 0);
          gl.glEndList();
        } 
      } else {
        prerenderZone(gl, GalaxyEditorForm.this.galaxyName);
        int dl = ((int[])GalaxyEditorForm.this.zoneDisplayLists.get(Integer.valueOf(0)))[mode];
        if (dl == 0) {
          dl = gl.glGenLists(1);
          ((int[])GalaxyEditorForm.this.zoneDisplayLists.get(Integer.valueOf(0)))[mode] = dl;
        } 
        gl.glNewList(dl, 4864);
        renderZone(gl, null, GalaxyEditorForm.this.galaxyName, GalaxyEditorForm.this.zoneModeLayerBitmask, 99);
        gl.glEndList();
      } 
    }
    
    private void prerenderZone(GL2 gl, String zone) {
      int mode = -1;
      switch (GalaxyEditorForm.this.renderinfo.renderMode) {
        case PICKING:
          mode = 0;
          break;
        case OPAQUE:
          mode = 1;
          break;
        case TRANSLUCENT:
          mode = 2;
          break;
      } 
      ZoneArchive zonearc = GalaxyEditorForm.this.zoneArcs.get(zone);
      Set<String> layers = zonearc.objects.keySet();
      for (String layer : layers) {
        String key = zone + "/" + layer.toLowerCase();
        if (!GalaxyEditorForm.this.objDisplayLists.containsKey(key))
          GalaxyEditorForm.this.objDisplayLists.put(key, new int[] { 0, 0, 0 }); 
        int dl = ((int[])GalaxyEditorForm.this.objDisplayLists.get(key))[mode];
        if (dl == 0) {
          dl = gl.glGenLists(1);
          ((int[])GalaxyEditorForm.this.objDisplayLists.get(key))[mode] = dl;
        } 
        gl.glNewList(dl, 4864);
        for (LevelObject obj : zonearc.objects.get(layer)) {
          if (mode == 0)
            gl.glColor4ub((byte)(obj.uniqueID >>> 16), (byte)(obj.uniqueID >>> 8), (byte)obj.uniqueID, (byte)-1); 
          obj.render(GalaxyEditorForm.this.renderinfo);
        } 
        if (mode == 2 && GalaxyEditorForm.this.selectedObj != null && GalaxyEditorForm.this.selectedObj.zone.zoneName.equals(zone))
          renderSelectHighlight(gl); 
        if (layer.equalsIgnoreCase("common"))
          for (PathObject pobj : zonearc.paths) {
            if (!GalaxyEditorForm.this.btnShowAllPaths.isSelected() && (GalaxyEditorForm.this.selectedObj == null || !GalaxyEditorForm.this.selectedObj.data.containsKey("CommonPath_ID") || pobj.pathID != ((Short)GalaxyEditorForm.this.selectedObj.data.get("CommonPath_ID")).shortValue()) && (GalaxyEditorForm.this.selectedPathPoint == null || pobj.uniqueID != GalaxyEditorForm.this.selectedPathPoint.path.uniqueID))
              continue; 
            pobj.render(GalaxyEditorForm.this.renderinfo);
            if (mode == 1 && GalaxyEditorForm.this.selectedPathPoint != null && pobj.uniqueID == GalaxyEditorForm.this.selectedPathPoint.path.uniqueID) {
              Color4 selcolor = new Color4(1.0F, 1.0F, 0.5F, 1.0F);
              GalaxyEditorForm.this.selectedPathPoint.render(GalaxyEditorForm.this.renderinfo, selcolor, GalaxyEditorForm.this.selectedSubVal);
            } 
          }  
        gl.glEndList();
      } 
    }
    
    private void renderZone(GL2 gl, Bcsv.Entry scenario, String zone, int layermask, int level) {
      String alphabet = "abcdefghijklmnop";
      int mode = -1;
      switch (GalaxyEditorForm.this.renderinfo.renderMode) {
        case PICKING:
          mode = 0;
          break;
        case OPAQUE:
          mode = 1;
          break;
        case TRANSLUCENT:
          mode = 2;
          break;
      } 
      if (GalaxyEditorForm.this.galaxyMode) {
        gl.glCallList(((int[])GalaxyEditorForm.this.objDisplayLists.get(zone + "/common"))[mode]);
      } else {
        if ((layermask & 0x1) != 0)
          gl.glCallList(((int[])GalaxyEditorForm.this.objDisplayLists.get(zone + "/common"))[mode]); 
        layermask >>= 1;
      } 
      int l;
      for (l = 0; l < 16; l++) {
        if ((layermask & 1 << l) != 0)
          gl.glCallList(((int[])GalaxyEditorForm.this.objDisplayLists.get(zone + "/layer" + alphabet.charAt(l)))[mode]); 
      } 
      if (level < 5) {
        for (Bcsv.Entry subzone : ((ZoneArchive)GalaxyEditorForm.this.zoneArcs.get(zone)).subZones.get("common")) {
          gl.glPushMatrix();
          gl.glTranslatef(((Float)subzone.get("pos_x")).floatValue(), ((Float)subzone.get("pos_y")).floatValue(), ((Float)subzone.get("pos_z")).floatValue());
          gl.glRotatef(((Float)subzone.get("dir_z")).floatValue(), 0.0F, 0.0F, 1.0F);
          gl.glRotatef(((Float)subzone.get("dir_y")).floatValue(), 0.0F, 1.0F, 0.0F);
          gl.glRotatef(((Float)subzone.get("dir_x")).floatValue(), 1.0F, 0.0F, 0.0F);
          String zonename = (String)subzone.get("name");
          renderZone(gl, scenario, zonename, ((Integer)scenario.get(zonename)).intValue(), level + 1);
          gl.glPopMatrix();
        } 
        for (l = 0; l < 16; l++) {
          if ((layermask & 1 << l) != 0)
            for (Bcsv.Entry subzone : ((ZoneArchive)GalaxyEditorForm.this.zoneArcs.get(zone)).subZones.get("layer" + alphabet.charAt(l))) {
              gl.glPushMatrix();
              gl.glTranslatef(((Float)subzone.get("pos_x")).floatValue(), ((Float)subzone.get("pos_y")).floatValue(), ((Float)subzone.get("pos_z")).floatValue());
              gl.glRotatef(((Float)subzone.get("dir_z")).floatValue(), 0.0F, 0.0F, 1.0F);
              gl.glRotatef(((Float)subzone.get("dir_y")).floatValue(), 0.0F, 1.0F, 0.0F);
              gl.glRotatef(((Float)subzone.get("dir_x")).floatValue(), 1.0F, 0.0F, 0.0F);
              String zonename = (String)subzone.get("name");
              renderZone(gl, scenario, zonename, ((Integer)scenario.get(zonename)).intValue(), level + 1);
              gl.glPopMatrix();
            }  
        } 
      } 
    }
    
    public void dispose(GLAutoDrawable glad) {
      GL2 gl = glad.getGL().getGL2();
      GalaxyEditorForm.this.renderinfo.drawable = glad;
      for (int[] dls : GalaxyEditorForm.this.zoneDisplayLists.values()) {
        gl.glDeleteLists(dls[0], 1);
        gl.glDeleteLists(dls[1], 1);
        gl.glDeleteLists(dls[2], 1);
      } 
      for (int[] dls : GalaxyEditorForm.this.objDisplayLists.values()) {
        gl.glDeleteLists(dls[0], 1);
        gl.glDeleteLists(dls[1], 1);
        gl.glDeleteLists(dls[2], 1);
      } 
      if (GalaxyEditorForm.this.parentForm == null)
        for (LevelObject obj : GalaxyEditorForm.this.globalObjList.values())
          obj.closeRenderer(GalaxyEditorForm.this.renderinfo);  
      RendererCache.clearRefContext();
    }
    
    private void doRerenderTasks() {
      GL2 gl = GalaxyEditorForm.this.renderinfo.drawable.getGL().getGL2();
      while (!GalaxyEditorForm.this.rerenderTasks.isEmpty()) {
        int objid, pathid;
        LevelObject obj;
        PathObject pobj;
        String[] task = ((String)GalaxyEditorForm.this.rerenderTasks.poll()).split(":");
        switch (task[0]) {
          case "zone":
            GalaxyEditorForm.this.renderinfo.renderMode = GLRenderer.RenderMode.PICKING;
            prerenderZone(gl, task[1]);
            GalaxyEditorForm.this.renderinfo.renderMode = GLRenderer.RenderMode.OPAQUE;
            prerenderZone(gl, task[1]);
            GalaxyEditorForm.this.renderinfo.renderMode = GLRenderer.RenderMode.TRANSLUCENT;
            prerenderZone(gl, task[1]);
          case "object":
            objid = Integer.parseInt(task[1]);
            obj = GalaxyEditorForm.this.globalObjList.get(Integer.valueOf(objid));
            obj.closeRenderer(GalaxyEditorForm.this.renderinfo);
            obj.initRenderer(GalaxyEditorForm.this.renderinfo);
          case "addobj":
            objid = Integer.parseInt(task[1]);
            obj = GalaxyEditorForm.this.globalObjList.get(Integer.valueOf(objid));
            obj.initRenderer(GalaxyEditorForm.this.renderinfo);
          case "delobj":
            objid = Integer.parseInt(task[1]);
            obj = GalaxyEditorForm.this.globalObjList.get(Integer.valueOf(objid));
            obj.closeRenderer(GalaxyEditorForm.this.renderinfo);
            GalaxyEditorForm.this.globalObjList.remove(Integer.valueOf(obj.uniqueID));
          case "allobjects":
            GalaxyEditorForm.this.renderinfo.renderMode = GLRenderer.RenderMode.PICKING;
            renderAllObjects(gl);
            GalaxyEditorForm.this.renderinfo.renderMode = GLRenderer.RenderMode.OPAQUE;
            renderAllObjects(gl);
            GalaxyEditorForm.this.renderinfo.renderMode = GLRenderer.RenderMode.TRANSLUCENT;
            renderAllObjects(gl);
          case "path":
            pathid = Integer.parseInt(task[1]);
            pobj = GalaxyEditorForm.this.globalPathList.get(Integer.valueOf(pathid));
            pobj.prerender(GalaxyEditorForm.this.renderinfo);
        } 
      } 
    }
    
    public void display(GLAutoDrawable glad) {
      if (!GalaxyEditorForm.this.inited)
        return; 
      GL2 gl = glad.getGL().getGL2();
      GalaxyEditorForm.this.renderinfo.drawable = glad;
      doRerenderTasks();
      gl.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
      gl.glClearDepth(1.0D);
      gl.glClearStencil(0);
      gl.glClear(17664);
      gl.glMatrixMode(5888);
      gl.glLoadMatrixf(GalaxyEditorForm.this.modelViewMatrix.m, 0);
      try {
        gl.glUseProgram(0);
      } catch (GLException ex) {}
      gl.glDisable(3008);
      gl.glDisable(3042);
      gl.glDisable(3058);
      gl.glDisable(2896);
      gl.glDisable(3024);
      gl.glDisable(2832);
      gl.glDisable(2848);
      gl.glDisable(2881);
      int i;
      for (i = 0; i < 8; i++) {
        try {
          gl.glActiveTexture(33984 + i);
          gl.glDisable(3553);
        } catch (GLException ex) {}
      } 
      gl.glDisable(3553);
      gl.glCallList(((int[])GalaxyEditorForm.this.zoneDisplayLists.get(Integer.valueOf(GalaxyEditorForm.this.curScenarioID)))[0]);
      gl.glDepthMask(true);
      gl.glFlush();
      gl.glReadPixels(GalaxyEditorForm.this.lastMouseMove.x - 1, glad.getHeight() - GalaxyEditorForm.this.lastMouseMove.y + 1, 3, 3, 32993, 33639, GalaxyEditorForm.this.pickingFrameBuffer);
      gl.glReadPixels(GalaxyEditorForm.this.lastMouseMove.x, glad.getHeight() - GalaxyEditorForm.this.lastMouseMove.y, 1, 1, 6402, 5126, GalaxyEditorForm.this.pickingDepthBuffer);
      GalaxyEditorForm.this.pickingDepth = -(10.0F / (GalaxyEditorForm.this.pickingDepthBuffer.get(0) * 999.99F - 1000.0F));
      if (GalaxyEditorForm.this.btnShowFakecolor.isSelected()) {
        glad.swapBuffers();
        return;
      } 
      gl.glClearColor(0.0F, 0.0F, 0.125F, 1.0F);
      gl.glClearDepth(1.0D);
      gl.glClearStencil(0);
      gl.glClear(17664);
      gl.glMatrixMode(5888);
      gl.glLoadMatrixf(GalaxyEditorForm.this.modelViewMatrix.m, 0);
      gl.glEnable(3553);
      if (Settings.fastDrag)
        if (GalaxyEditorForm.this.isDragging) {
          gl.glPolygonMode(1028, 6913);
          gl.glPolygonMode(1029, 6912);
        } else {
          gl.glPolygonMode(1032, 6914);
        }  
      gl.glCallList(((int[])GalaxyEditorForm.this.zoneDisplayLists.get(Integer.valueOf(GalaxyEditorForm.this.curScenarioID)))[1]);
      gl.glCallList(((int[])GalaxyEditorForm.this.zoneDisplayLists.get(Integer.valueOf(GalaxyEditorForm.this.curScenarioID)))[2]);
      gl.glDepthMask(true);
      try {
        gl.glUseProgram(0);
      } catch (GLException ex) {}
      for (i = 0; i < 8; i++) {
        try {
          gl.glActiveTexture(33984 + i);
          gl.glDisable(3553);
        } catch (GLException ex) {}
      } 
      gl.glDisable(3553);
      gl.glDisable(3042);
      gl.glDisable(3008);
      gl.glBegin(1);
      gl.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
      gl.glVertex3f(0.0F, 0.0F, 0.0F);
      gl.glVertex3f(100000.0F, 0.0F, 0.0F);
      gl.glColor4f(0.0F, 1.0F, 0.0F, 1.0F);
      gl.glVertex3f(0.0F, 0.0F, 0.0F);
      gl.glVertex3f(0.0F, 100000.0F, 0.0F);
      gl.glColor4f(0.0F, 0.0F, 1.0F, 1.0F);
      gl.glVertex3f(0.0F, 0.0F, 0.0F);
      gl.glVertex3f(0.0F, 0.0F, 100000.0F);
      gl.glEnd();
      glad.swapBuffers();
    }
    
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
      if (!GalaxyEditorForm.this.inited)
        return; 
      GL2 gl = glad.getGL().getGL2();
      gl.glViewport(x, y, width, height);
      float aspectRatio = width / height;
      gl.glMatrixMode(5889);
      gl.glLoadIdentity();
      float ymax = 0.01F * (float)Math.tan(0.6108652353286743D);
      gl.glFrustum((-ymax * aspectRatio), (ymax * aspectRatio), -ymax, ymax, 0.009999999776482582D, 1000.0D);
      GalaxyEditorForm.this.pixelFactorX = 2.0F * (float)Math.tan(0.6108652353286743D) * aspectRatio / width;
      GalaxyEditorForm.this.pixelFactorY = 2.0F * (float)Math.tan(0.6108652353286743D) / height;
    }
    
    public void updateCamera() {
      Vector3 up;
      if (Math.cos(GalaxyEditorForm.this.camRotation.y) < 0.0D) {
        GalaxyEditorForm.this.upsideDown = true;
        up = new Vector3(0.0F, -1.0F, 0.0F);
      } else {
        GalaxyEditorForm.this.upsideDown = false;
        up = new Vector3(0.0F, 1.0F, 0.0F);
      } 
      GalaxyEditorForm.this.camPosition.x = GalaxyEditorForm.this.camDistance * (float)Math.cos(GalaxyEditorForm.this.camRotation.x) * (float)Math.cos(GalaxyEditorForm.this.camRotation.y);
      GalaxyEditorForm.this.camPosition.y = GalaxyEditorForm.this.camDistance * (float)Math.sin(GalaxyEditorForm.this.camRotation.y);
      GalaxyEditorForm.this.camPosition.z = GalaxyEditorForm.this.camDistance * (float)Math.sin(GalaxyEditorForm.this.camRotation.x) * (float)Math.cos(GalaxyEditorForm.this.camRotation.y);
      Vector3.add(GalaxyEditorForm.this.camPosition, GalaxyEditorForm.this.camTarget, GalaxyEditorForm.this.camPosition);
      GalaxyEditorForm.this.modelViewMatrix = Matrix4.lookAt(GalaxyEditorForm.this.camPosition, GalaxyEditorForm.this.camTarget, up);
      Matrix4.mult(Matrix4.scale(1.0E-4F), GalaxyEditorForm.this.modelViewMatrix, GalaxyEditorForm.this.modelViewMatrix);
    }
    
    public void mouseDragged(MouseEvent e) {
      if (!GalaxyEditorForm.this.inited)
        return; 
      float xdelta = (e.getX() - GalaxyEditorForm.this.lastMouseMove.x);
      float ydelta = (e.getY() - GalaxyEditorForm.this.lastMouseMove.y);
      if (!GalaxyEditorForm.this.isDragging && (Math.abs(xdelta) >= 3.0F || Math.abs(ydelta) >= 3.0F)) {
        GalaxyEditorForm.this.pickingCapture = true;
        GalaxyEditorForm.this.isDragging = true;
      } 
      if (!GalaxyEditorForm.this.isDragging)
        return; 
      if (GalaxyEditorForm.this.pickingCapture) {
        GalaxyEditorForm.this.underCursor = GalaxyEditorForm.this.pickingFrameBuffer.get(4) & 0xFFFFFF;
        GalaxyEditorForm.this.depthUnderCursor = GalaxyEditorForm.this.pickingDepth;
        GalaxyEditorForm.this.pickingCapture = false;
      } 
      GalaxyEditorForm.this.lastMouseMove = e.getPoint();
      if ((GalaxyEditorForm.this.selectedObj != null || GalaxyEditorForm.this.selectedPathPoint != null) && GalaxyEditorForm.this.selectedVal == GalaxyEditorForm.this.underCursor - GalaxyEditorForm.this.selectedSubVal) {
        if (GalaxyEditorForm.this.mouseButton == 1) {
          float objz = GalaxyEditorForm.this.depthUnderCursor;
          xdelta *= GalaxyEditorForm.this.pixelFactorX * objz * 10000.0F;
          ydelta *= -GalaxyEditorForm.this.pixelFactorY * objz * 10000.0F;
          Vector3 delta = new Vector3(xdelta * (float)Math.sin(GalaxyEditorForm.this.camRotation.x) - ydelta * (float)Math.sin(GalaxyEditorForm.this.camRotation.y) * (float)Math.cos(GalaxyEditorForm.this.camRotation.x), ydelta * (float)Math.cos(GalaxyEditorForm.this.camRotation.y), -(xdelta * (float)Math.cos(GalaxyEditorForm.this.camRotation.x)) - ydelta * (float)Math.sin(GalaxyEditorForm.this.camRotation.y) * (float)Math.sin(GalaxyEditorForm.this.camRotation.x));
          GalaxyEditorForm.this.applySubzoneRotation(delta);
          GalaxyEditorForm.this.offsetSelectionBy(delta);
          GalaxyEditorForm.this.unsavedChanges = true;
        } 
      } else {
        if (GalaxyEditorForm.this.mouseButton == 3) {
          if (GalaxyEditorForm.this.upsideDown)
            xdelta = -xdelta; 
          if (GalaxyEditorForm.this.tgbReverseRot.isSelected()) {
            xdelta = -xdelta;
            ydelta = -ydelta;
          } 
          if (GalaxyEditorForm.this.underCursor == 16777215 || GalaxyEditorForm.this.depthUnderCursor > GalaxyEditorForm.this.camDistance) {
            xdelta *= 0.002F;
            ydelta *= 0.002F;
            GalaxyEditorForm.this.camRotation.x -= xdelta;
            GalaxyEditorForm.this.camRotation.y -= ydelta;
          } else {
            xdelta *= 0.002F;
            ydelta *= 0.002F;
            float diff = GalaxyEditorForm.this.camDistance - GalaxyEditorForm.this.depthUnderCursor;
            GalaxyEditorForm.this.camTarget.x = (float)(GalaxyEditorForm.this.camTarget.x + diff * Math.cos(GalaxyEditorForm.this.camRotation.x) * Math.cos(GalaxyEditorForm.this.camRotation.y));
            GalaxyEditorForm.this.camTarget.y = (float)(GalaxyEditorForm.this.camTarget.y + diff * Math.sin(GalaxyEditorForm.this.camRotation.y));
            GalaxyEditorForm.this.camTarget.z = (float)(GalaxyEditorForm.this.camTarget.z + diff * Math.sin(GalaxyEditorForm.this.camRotation.x) * Math.cos(GalaxyEditorForm.this.camRotation.y));
            GalaxyEditorForm.this.camRotation.x -= xdelta;
            GalaxyEditorForm.this.camRotation.y -= ydelta;
            GalaxyEditorForm.this.camTarget.x = (float)(GalaxyEditorForm.this.camTarget.x - diff * Math.cos(GalaxyEditorForm.this.camRotation.x) * Math.cos(GalaxyEditorForm.this.camRotation.y));
            GalaxyEditorForm.this.camTarget.y = (float)(GalaxyEditorForm.this.camTarget.y - diff * Math.sin(GalaxyEditorForm.this.camRotation.y));
            GalaxyEditorForm.this.camTarget.z = (float)(GalaxyEditorForm.this.camTarget.z - diff * Math.sin(GalaxyEditorForm.this.camRotation.x) * Math.cos(GalaxyEditorForm.this.camRotation.y));
          } 
        } else if (GalaxyEditorForm.this.mouseButton == 1) {
          if (GalaxyEditorForm.this.underCursor == 16777215) {
            xdelta *= 0.005F;
            ydelta *= 0.005F;
          } else {
            xdelta *= Math.min(0.005F, GalaxyEditorForm.this.pixelFactorX * GalaxyEditorForm.this.depthUnderCursor);
            ydelta *= Math.min(0.005F, GalaxyEditorForm.this.pixelFactorY * GalaxyEditorForm.this.depthUnderCursor);
          } 
          GalaxyEditorForm.this.camTarget.x -= xdelta * (float)Math.sin(GalaxyEditorForm.this.camRotation.x);
          GalaxyEditorForm.this.camTarget.x -= ydelta * (float)Math.cos(GalaxyEditorForm.this.camRotation.x) * (float)Math.sin(GalaxyEditorForm.this.camRotation.y);
          GalaxyEditorForm.this.camTarget.y += ydelta * (float)Math.cos(GalaxyEditorForm.this.camRotation.y);
          GalaxyEditorForm.this.camTarget.z += xdelta * (float)Math.cos(GalaxyEditorForm.this.camRotation.x);
          GalaxyEditorForm.this.camTarget.z -= ydelta * (float)Math.sin(GalaxyEditorForm.this.camRotation.x) * (float)Math.sin(GalaxyEditorForm.this.camRotation.y);
        } 
        updateCamera();
      } 
      e.getComponent().repaint();
    }
    
    public void mouseMoved(MouseEvent e) {
      if (!GalaxyEditorForm.this.inited)
        return; 
      GalaxyEditorForm.this.lastMouseMove = e.getPoint();
    }
    
    public void mouseClicked(MouseEvent e) {}
    
    public void mousePressed(MouseEvent e) {
      if (!GalaxyEditorForm.this.inited)
        return; 
      if (GalaxyEditorForm.this.mouseButton != 0)
        return; 
      GalaxyEditorForm.this.mouseButton = e.getButton();
      GalaxyEditorForm.this.lastMouseMove = e.getPoint();
      GalaxyEditorForm.this.isDragging = false;
      e.getComponent().repaint();
    }
    
    public void mouseReleased(MouseEvent e) {
      if (!GalaxyEditorForm.this.inited)
        return; 
      if (e.getButton() != GalaxyEditorForm.this.mouseButton)
        return; 
      GalaxyEditorForm.this.mouseButton = 0;
      GalaxyEditorForm.this.lastMouseMove = e.getPoint();
      boolean shiftpressed = ((e.getModifiers() & 0x1) != 0);
      if (GalaxyEditorForm.this.isDragging) {
        GalaxyEditorForm.this.isDragging = false;
        if (Settings.fastDrag)
          e.getComponent().repaint(); 
        return;
      } 
      int objid = GalaxyEditorForm.this.pickingFrameBuffer.get(4);
      if (objid != GalaxyEditorForm.this.pickingFrameBuffer.get(1) || objid != GalaxyEditorForm.this.pickingFrameBuffer.get(3) || objid != GalaxyEditorForm.this.pickingFrameBuffer.get(5) || objid != GalaxyEditorForm.this.pickingFrameBuffer.get(7))
        return; 
      objid &= 0xFFFFFF;
      if (e.getButton() == 3) {
        if (!GalaxyEditorForm.this.objectBeingAdded.isEmpty()) {
          GalaxyEditorForm.this.objectBeingAdded = "";
          GalaxyEditorForm.this.tgbAddObject.setSelected(false);
          GalaxyEditorForm.this.setStatusText();
        } else if (GalaxyEditorForm.this.deletingObjects) {
          GalaxyEditorForm.this.deletingObjects = false;
          GalaxyEditorForm.this.tgbDeleteObject.setSelected(false);
          GalaxyEditorForm.this.setStatusText();
        } 
      } else if (objid == GalaxyEditorForm.this.selectedVal + GalaxyEditorForm.this.selectedSubVal || objid == 16777215) {
        GalaxyEditorForm.this.tvObjectList.setSelectionPath(null);
      } else if (!GalaxyEditorForm.this.objectBeingAdded.isEmpty()) {
        GalaxyEditorForm.this.addObject(GalaxyEditorForm.this.lastMouseMove);
        if (!shiftpressed) {
          GalaxyEditorForm.this.objectBeingAdded = "";
          GalaxyEditorForm.this.tgbAddObject.setSelected(false);
          GalaxyEditorForm.this.setStatusText();
        } 
      } else if (GalaxyEditorForm.this.deletingObjects) {
        GalaxyEditorForm.this.deleteObject(objid);
        if (!shiftpressed) {
          GalaxyEditorForm.this.deletingObjects = false;
          GalaxyEditorForm.this.tgbDeleteObject.setSelected(false);
          GalaxyEditorForm.this.setStatusText();
        } 
      } else {
        String oldzone = "";
        if (GalaxyEditorForm.this.selectedObj != null) {
          oldzone = GalaxyEditorForm.this.selectedObj.zone.zoneName;
        } else if (GalaxyEditorForm.this.selectedPathPoint != null) {
          oldzone = GalaxyEditorForm.this.selectedPathPoint.path.zone.zoneName;
        } 
        GalaxyEditorForm.this.selectedVal = objid;
        GalaxyEditorForm.this.selectedObj = null;
        GalaxyEditorForm.this.selectedPathPoint = null;
        String newzone = "";
        int uid = -1;
        if (GalaxyEditorForm.this.globalObjList.containsKey(Integer.valueOf(objid))) {
          GalaxyEditorForm.this.selectedObj = GalaxyEditorForm.this.globalObjList.get(Integer.valueOf(objid));
          newzone = GalaxyEditorForm.this.selectedObj.zone.zoneName;
          uid = GalaxyEditorForm.this.selectedObj.uniqueID;
          GalaxyEditorForm.this.selectedSubVal = 0;
        } else if (GalaxyEditorForm.this.globalPathPointList.containsKey(Integer.valueOf(objid))) {
          GalaxyEditorForm.this.selectedPathPoint = GalaxyEditorForm.this.globalPathPointList.get(Integer.valueOf(objid));
          newzone = GalaxyEditorForm.this.selectedPathPoint.path.zone.zoneName;
          uid = GalaxyEditorForm.this.selectedPathPoint.uniqueID;
        } 
        if (!oldzone.isEmpty() && !oldzone.equals(newzone))
          GalaxyEditorForm.this.rerenderTasks.add("zone:" + oldzone); 
        if (GalaxyEditorForm.this.galaxyMode)
          for (int z = 0; z < GalaxyEditorForm.this.galaxyArc.zoneList.size(); ) {
            if (!((String)GalaxyEditorForm.this.galaxyArc.zoneList.get(z)).equals(newzone)) {
              z++;
              continue;
            } 
            GalaxyEditorForm.this.lbZoneList.setSelectedIndex(z);
          }  
        GalaxyEditorForm.this.tpLeftPanel.setSelectedIndex(1);
        if (GalaxyEditorForm.this.treeNodeList.containsKey(Integer.valueOf(uid))) {
          TreeNode tn = (TreeNode)GalaxyEditorForm.this.treeNodeList.get(Integer.valueOf(uid));
          TreePath tp = new TreePath((Object[])((DefaultTreeModel)GalaxyEditorForm.this.tvObjectList.getModel()).getPathToRoot(tn));
          GalaxyEditorForm.this.tvObjectList.setSelectionPath(tp);
          GalaxyEditorForm.this.tvObjectList.scrollPathToVisible(tp);
        } 
        if (GalaxyEditorForm.this.selectedPathPoint != null)
          GalaxyEditorForm.this.selectedSubVal = objid - uid; 
      } 
      e.getComponent().repaint();
    }
    
    public void mouseEntered(MouseEvent e) {}
    
    public void mouseExited(MouseEvent e) {}
    
    public void mouseWheelMoved(MouseWheelEvent e) {
      if (!GalaxyEditorForm.this.inited)
        return; 
      if (GalaxyEditorForm.this.mouseButton == 1 && (GalaxyEditorForm.this.selectedObj != null || GalaxyEditorForm.this.selectedPathPoint != null) && GalaxyEditorForm.this.selectedVal == GalaxyEditorForm.this.underCursor - GalaxyEditorForm.this.selectedSubVal) {
        float delta = (float)e.getPreciseWheelRotation();
        delta = ((delta < 0.0F) ? -1.0F : 1.0F) * (float)Math.pow(delta, 2.0D) * 0.05F * 10000.0F;
        Vector3 vdelta = new Vector3(delta * (float)Math.cos(GalaxyEditorForm.this.camRotation.x) * (float)Math.cos(GalaxyEditorForm.this.camRotation.y), delta * (float)Math.sin(GalaxyEditorForm.this.camRotation.y), delta * (float)Math.sin(GalaxyEditorForm.this.camRotation.x) * (float)Math.cos(GalaxyEditorForm.this.camRotation.y));
        float xdist = delta * (GalaxyEditorForm.this.lastMouseMove.x - GalaxyEditorForm.this.glCanvas.getWidth() / 2.0F) * GalaxyEditorForm.this.pixelFactorX;
        float ydist = delta * (GalaxyEditorForm.this.lastMouseMove.y - GalaxyEditorForm.this.glCanvas.getHeight() / 2.0F) * GalaxyEditorForm.this.pixelFactorY;
        vdelta.x += -(xdist * (float)Math.sin(GalaxyEditorForm.this.camRotation.x)) - ydist * (float)Math.sin(GalaxyEditorForm.this.camRotation.y) * (float)Math.cos(GalaxyEditorForm.this.camRotation.x);
        vdelta.y += ydist * (float)Math.cos(GalaxyEditorForm.this.camRotation.y);
        vdelta.z += xdist * (float)Math.cos(GalaxyEditorForm.this.camRotation.x) - ydist * (float)Math.sin(GalaxyEditorForm.this.camRotation.y) * (float)Math.sin(GalaxyEditorForm.this.camRotation.x);
        GalaxyEditorForm.this.applySubzoneRotation(vdelta);
        GalaxyEditorForm.this.offsetSelectionBy(vdelta);
        GalaxyEditorForm.this.unsavedChanges = true;
      } else {
        float delta = (float)(e.getPreciseWheelRotation() * Math.min(0.1F, GalaxyEditorForm.this.pickingDepth / 10.0F));
        Vector3 vdelta = new Vector3(delta * (float)Math.cos(GalaxyEditorForm.this.camRotation.x) * (float)Math.cos(GalaxyEditorForm.this.camRotation.y), delta * (float)Math.sin(GalaxyEditorForm.this.camRotation.y), delta * (float)Math.sin(GalaxyEditorForm.this.camRotation.x) * (float)Math.cos(GalaxyEditorForm.this.camRotation.y));
        float xdist = delta * (GalaxyEditorForm.this.lastMouseMove.x - GalaxyEditorForm.this.glCanvas.getWidth() / 2.0F) * GalaxyEditorForm.this.pixelFactorX;
        float ydist = delta * (GalaxyEditorForm.this.lastMouseMove.y - GalaxyEditorForm.this.glCanvas.getHeight() / 2.0F) * GalaxyEditorForm.this.pixelFactorY;
        vdelta.x += -(xdist * (float)Math.sin(GalaxyEditorForm.this.camRotation.x)) - ydist * (float)Math.sin(GalaxyEditorForm.this.camRotation.y) * (float)Math.cos(GalaxyEditorForm.this.camRotation.x);
        vdelta.y += ydist * (float)Math.cos(GalaxyEditorForm.this.camRotation.y);
        vdelta.z += xdist * (float)Math.cos(GalaxyEditorForm.this.camRotation.x) - ydist * (float)Math.sin(GalaxyEditorForm.this.camRotation.y) * (float)Math.sin(GalaxyEditorForm.this.camRotation.x);
        GalaxyEditorForm.this.camTarget.x += vdelta.x;
        GalaxyEditorForm.this.camTarget.y += vdelta.y;
        GalaxyEditorForm.this.camTarget.z += vdelta.z;
        updateCamera();
      } 
      GalaxyEditorForm.this.pickingCapture = true;
      e.getComponent().repaint();
    }
    
    public void keyTyped(KeyEvent e) {}
    
    public void keyPressed(KeyEvent e) {
      int oldmask = GalaxyEditorForm.this.keyMask;
      switch (e.getKeyCode()) {
        case 37:
        case 100:
          GalaxyEditorForm.this.keyMask |= 0x1;
          break;
        case 39:
        case 102:
          GalaxyEditorForm.this.keyMask |= 0x2;
          break;
        case 38:
        case 104:
          GalaxyEditorForm.this.keyMask |= 0x4;
          break;
        case 40:
        case 98:
          GalaxyEditorForm.this.keyMask |= 0x8;
          break;
        case 33:
        case 105:
          GalaxyEditorForm.this.keyMask |= 0x10;
          break;
        case 34:
        case 35:
        case 97:
        case 99:
          GalaxyEditorForm.this.keyMask |= 0x20;
          break;
      } 
      if ((GalaxyEditorForm.this.keyMask & 0x3F) != 0) {
        Vector3 finaldelta;
        int disp;
        Vector3 delta = new Vector3();
        if (oldmask != GalaxyEditorForm.this.keyMask)
          GalaxyEditorForm.this.keyDelta = 0; 
        if (GalaxyEditorForm.this.keyDelta > 50) {
          disp = 10;
        } else {
          disp = 1;
        } 
        if ((GalaxyEditorForm.this.keyMask & 0x1) != 0) {
          delta.x = disp;
        } else if ((GalaxyEditorForm.this.keyMask & 0x2) != 0) {
          delta.x = -disp;
        } 
        if ((GalaxyEditorForm.this.keyMask & 0x4) != 0) {
          delta.y = disp;
        } else if ((GalaxyEditorForm.this.keyMask & 0x8) != 0) {
          delta.y = -disp;
        } 
        if ((GalaxyEditorForm.this.keyMask & 0x10) != 0) {
          delta.z = -disp;
        } else if ((GalaxyEditorForm.this.keyMask & 0x20) != 0) {
          delta.z = disp;
        } 
        if (e.isControlDown()) {
          finaldelta = delta;
        } else {
          finaldelta = new Vector3();
          finaldelta.x = (float)(-(delta.x * Math.sin(GalaxyEditorForm.this.camRotation.x)) - delta.y * Math.cos(GalaxyEditorForm.this.camRotation.x) * Math.sin(GalaxyEditorForm.this.camRotation.y) + delta.z * Math.cos(GalaxyEditorForm.this.camRotation.x) * Math.cos(GalaxyEditorForm.this.camRotation.y));
          finaldelta.y = (float)(delta.y * Math.cos(GalaxyEditorForm.this.camRotation.y) + delta.z * Math.sin(GalaxyEditorForm.this.camRotation.y));
          finaldelta.z = (float)(delta.x * Math.cos(GalaxyEditorForm.this.camRotation.x) - delta.y * Math.sin(GalaxyEditorForm.this.camRotation.x) * Math.sin(GalaxyEditorForm.this.camRotation.y) + delta.z * Math.sin(GalaxyEditorForm.this.camRotation.x) * Math.cos(GalaxyEditorForm.this.camRotation.y));
        } 
        if (GalaxyEditorForm.this.selectedObj != null || GalaxyEditorForm.this.selectedPathPoint != null) {
          GalaxyEditorForm.this.offsetSelectionBy(finaldelta);
        } else {
          GalaxyEditorForm.this.camTarget.x += finaldelta.x * 0.005F;
          GalaxyEditorForm.this.camTarget.y += finaldelta.y * 0.005F;
          GalaxyEditorForm.this.camTarget.z += finaldelta.z * 0.005F;
          updateCamera();
          e.getComponent().repaint();
        } 
        GalaxyEditorForm.this.keyDelta += disp;
      } 
    }
    
    public void keyReleased(KeyEvent e) {
      if (e.getKeyCode() == 127)
        if (GalaxyEditorForm.this.selectedObj != null) {
          if (GalaxyEditorForm.this.tgbDeleteObject.isSelected()) {
            GalaxyEditorForm.this.deleteObject(GalaxyEditorForm.this.selectedObj.uniqueID);
            GalaxyEditorForm.this.selectedVal = 16777215;
            GalaxyEditorForm.this.selectedObj = null;
            GalaxyEditorForm.this.selectedPathPoint = null;
            GalaxyEditorForm.this.selectionChanged();
          } 
          GalaxyEditorForm.this.tgbDeleteObject.setSelected(false);
        }  
      switch (e.getKeyCode()) {
        case 37:
        case 100:
          GalaxyEditorForm.this.keyMask &= 0xFFFFFFFE;
          break;
        case 39:
        case 102:
          GalaxyEditorForm.this.keyMask &= 0xFFFFFFFD;
          break;
        case 38:
        case 104:
          GalaxyEditorForm.this.keyMask &= 0xFFFFFFFB;
          break;
        case 40:
        case 98:
          GalaxyEditorForm.this.keyMask &= 0xFFFFFFF7;
          break;
        case 33:
        case 105:
          GalaxyEditorForm.this.keyMask &= 0xFFFFFFEF;
          break;
        case 34:
        case 35:
        case 97:
        case 99:
          GalaxyEditorForm.this.keyMask &= 0xFFFFFFDF;
          break;
      } 
      if ((GalaxyEditorForm.this.keyMask & 0x3F) == 0)
        GalaxyEditorForm.this.keyDelta = 0; 
    }
  }
  
  public class SubZoneData {
    String layer;
    
    Vector3 position;
    
    Vector3 rotation;
  }
}
