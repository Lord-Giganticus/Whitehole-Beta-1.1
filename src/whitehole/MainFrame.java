package whitehole;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import whitehole.fileio.ExternalFilesystem;
import whitehole.fileio.FilesystemBase;
import whitehole.smg.GameArchive;

public class MainFrame extends JFrame {
  private HashMap<String, GalaxyEditorForm> galaxyEditors;
  
  private JList GalaxyList;
  
  private JButton btnAbout;
  
  private JButton btnBcsvEditor;
  
  private JButton btnOpenGalaxy;
  
  private JButton btnOpenGame;
  
  private JButton btnSettings;
  
  private JMenu jMenu1;
  
  private JScrollPane jScrollPane1;
  
  private JToolBar.Separator jSeparator1;
  
  private JToolBar.Separator jSeparator2;
  
  private JToolBar jToolBar1;
  
  private JLabel lbStatusBar;
  
  public MainFrame() {
    initComponents();
    this.galaxyEditors = new HashMap<>();
  }
  
  private void initComponents() {
    this.jMenu1 = new JMenu();
    this.jToolBar1 = new JToolBar();
    this.btnOpenGame = new JButton();
    this.jSeparator1 = new JToolBar.Separator();
    this.btnOpenGalaxy = new JButton();
    this.jSeparator2 = new JToolBar.Separator();
    this.btnSettings = new JButton();
    this.btnAbout = new JButton();
    this.btnBcsvEditor = new JButton();
    this.jScrollPane1 = new JScrollPane();
    this.GalaxyList = new JList();
    this.lbStatusBar = new JLabel();
    this.jMenu1.setText("jMenu1");
    setDefaultCloseOperation(3);
    addWindowListener(new WindowAdapter() {
          public void windowOpened(WindowEvent evt) {
            MainFrame.this.formWindowOpened(evt);
          }
        });
    this.jToolBar1.setFloatable(false);
    this.jToolBar1.setRollover(true);
    this.btnOpenGame.setText("Select game folder");
    this.btnOpenGame.setFocusable(false);
    this.btnOpenGame.setHorizontalTextPosition(0);
    this.btnOpenGame.setVerticalTextPosition(3);
    this.btnOpenGame.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            MainFrame.this.btnOpenGameActionPerformed(evt);
          }
        });
    this.jToolBar1.add(this.btnOpenGame);
    this.jToolBar1.add(this.jSeparator1);
    this.btnOpenGalaxy.setText("Open galaxy");
    this.btnOpenGalaxy.setEnabled(false);
    this.btnOpenGalaxy.setFocusable(false);
    this.btnOpenGalaxy.setHorizontalTextPosition(0);
    this.btnOpenGalaxy.setVerticalTextPosition(3);
    this.btnOpenGalaxy.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            MainFrame.this.btnOpenGalaxyActionPerformed(evt);
          }
        });
    this.jToolBar1.add(this.btnOpenGalaxy);
    this.jToolBar1.add(this.jSeparator2);
    this.btnSettings.setText("Settings");
    this.btnSettings.setFocusable(false);
    this.btnSettings.setHorizontalTextPosition(0);
    this.btnSettings.setVerticalTextPosition(3);
    this.btnSettings.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            MainFrame.this.btnSettingsActionPerformed(evt);
          }
        });
    this.jToolBar1.add(this.btnSettings);
    this.btnAbout.setText("About...");
    this.btnAbout.setFocusable(false);
    this.btnAbout.setHorizontalTextPosition(0);
    this.btnAbout.setVerticalTextPosition(3);
    this.btnAbout.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            MainFrame.this.btnAboutActionPerformed(evt);
          }
        });
    this.jToolBar1.add(this.btnAbout);
    this.btnBcsvEditor.setText("BCSV editor");
    this.btnBcsvEditor.setEnabled(false);
    this.btnBcsvEditor.setFocusable(false);
    this.btnBcsvEditor.setHorizontalTextPosition(0);
    this.btnBcsvEditor.setVerticalTextPosition(3);
    this.btnBcsvEditor.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            MainFrame.this.btnBcsvEditorActionPerformed(evt);
          }
        });
    this.jToolBar1.add(this.btnBcsvEditor);
    this.GalaxyList.addMouseListener(new MouseAdapter() {
          public void mouseClicked(MouseEvent evt) {
            MainFrame.this.GalaxyListMouseClicked(evt);
          }
        });
    this.GalaxyList.addListSelectionListener(new ListSelectionListener() {
          public void valueChanged(ListSelectionEvent evt) {
            MainFrame.this.GalaxyListValueChanged(evt);
          }
        });
    this.jScrollPane1.setViewportView(this.GalaxyList);
    this.lbStatusBar.setText("jLabel1");
    this.lbStatusBar.setToolTipText("");
    this.lbStatusBar.setVerticalTextPosition(3);
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jToolBar1, -1, 414, 32767).addComponent(this.lbStatusBar, -1, -1, 32767).addComponent(this.jScrollPane1));
    layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jToolBar1, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jScrollPane1, -1, 257, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.lbStatusBar, -2, 21, -2)));
    pack();
  }
  
  private void btnOpenGameActionPerformed(ActionEvent evt) {
    JFileChooser fc = new JFileChooser();
    fc.setFileSelectionMode(1);
    fc.setDialogTitle("Open a game archive");
    String lastdir = Preferences.userRoot().get("lastGameDir", null);
    if (lastdir != null)
      fc.setSelectedFile(new File(lastdir)); 
    if (fc.showOpenDialog(this) != 0)
      return; 
    for (GalaxyEditorForm form : this.galaxyEditors.values())
      form.dispose(); 
    this.galaxyEditors.clear();
    String seldir = fc.getSelectedFile().getPath();
    Preferences.userRoot().put("lastGameDir", seldir);
    try {
      Whitehole.game = new GameArchive((FilesystemBase)new ExternalFilesystem(seldir));
      this.lbStatusBar.setText("Game directory successfully opened");
    } catch (IOException ex) {
      this.lbStatusBar.setText("Failed to open the directory");
      return;
    } 
    DefaultListModel<String> galaxylist = new DefaultListModel();
    this.GalaxyList.setModel(galaxylist);
    List<String> galaxies = Whitehole.game.getGalaxies();
    for (String galaxy : galaxies)
      galaxylist.addElement(galaxy); 
    this.btnBcsvEditor.setEnabled(true);
  }
  
  private void formWindowOpened(WindowEvent evt) {
    setTitle(Whitehole.fullName);
    setIconImage(Toolkit.getDefaultToolkit().createImage(Whitehole.class.getResource("/Resources/icon.png")));
    this.lbStatusBar.setText("Ready");
    if (Settings.objectDBUpdate) {
      this.lbStatusBar.setText("Checking for object database updates...");
      ObjectDBUpdater updater = new ObjectDBUpdater(this.lbStatusBar);
      updater.start();
    } 
  }
  
  private void btnAboutActionPerformed(ActionEvent evt) {
    String msg = "Whitehole v1.1 PRIVATE BETA\n\nA level editor for Super Mario Galaxy 1 and 2\n\nWhitehole is free software, and shouldn't be provided as\na part of a paid software package.\n\nIf you downloaded Whitehole from a site other than Kuribo64,\nwe can't guarantee anything as to the package's cleanness.\n\nMain coding: Mega-Mario\nCredits: \n * Phantom Wings, Treeki, yaz0r, thakis, groepaz/hitmen\n * Dirbaio for programming help\n * NWPlayer123 for design suggestions\n\nSee Kuribo64 (http://kuribo64.net/) for more details.\n";
    if ("v1.1 PRIVATE BETA".toLowerCase().contains("private")) {
      msg = msg + "\nThis is a private beta version. Leak it out and this'll be the last one you get.\n";
    } else if ("v1.1 PRIVATE BETA".toLowerCase().contains("beta")) {
      msg = msg + "\nThis is a beta version so don't expect full stability.\n";
    } 
    JOptionPane.showMessageDialog(this, msg, "About Whitehole...", 1);
  }
  
  private void btnBcsvEditorActionPerformed(ActionEvent evt) {
    (new BcsvEditorForm()).setVisible(true);
  }
  
  private void openGalaxy() {
    String gal = this.GalaxyList.getSelectedValue().toString();
    if (this.galaxyEditors.containsKey(gal))
      if (!((GalaxyEditorForm)this.galaxyEditors.get(gal)).isVisible()) {
        this.galaxyEditors.remove(gal);
      } else {
        ((GalaxyEditorForm)this.galaxyEditors.get(gal)).toFront();
        return;
      }  
    GalaxyEditorForm form = new GalaxyEditorForm(gal);
    form.setVisible(true);
    this.galaxyEditors.put(gal, form);
  }
  
  private void GalaxyListMouseClicked(MouseEvent evt) {
    if (evt.getClickCount() < 2)
      return; 
    openGalaxy();
  }
  
  private void btnOpenGalaxyActionPerformed(ActionEvent evt) {
    openGalaxy();
  }
  
  private void GalaxyListValueChanged(ListSelectionEvent evt) {
    boolean hasSelection = (this.GalaxyList.getSelectedIndex() >= 0);
    this.btnOpenGalaxy.setEnabled(hasSelection);
  }
  
  private void btnSettingsActionPerformed(ActionEvent evt) {
    (new SettingsForm(this, true)).setVisible(true);
  }
}
