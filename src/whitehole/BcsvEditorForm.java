package whitehole;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle;
import javax.swing.table.DefaultTableModel;
import whitehole.fileio.FilesystemBase;
import whitehole.fileio.RarcFilesystem;
import whitehole.smg.Bcsv;

public class BcsvEditorForm extends JFrame {
  private FilesystemBase archive;
  
  private Bcsv bcsv;
  
  private JButton btnAddRow;
  
  private JButton btnDeleteRow;
  
  private JButton btnOpen;
  
  private JButton btnSave;
  
  private JLabel jLabel1;
  
  private JLabel jLabel2;
  
  private JScrollPane jScrollPane1;
  
  private JToolBar.Separator jSeparator1;
  
  private JToolBar.Separator jSeparator2;
  
  private JToolBar.Separator jSeparator3;
  
  private JToolBar.Separator jSeparator4;
  
  private JToolBar jToolBar1;
  
  private JTextField tbArchiveName;
  
  private JTextField tbFileName;
  
  private JTable tblBcsv;
  
  public BcsvEditorForm() {
    initComponents();
    this.archive = null;
    this.bcsv = null;
  }
  
  private void initComponents() {
    this.jToolBar1 = new JToolBar();
    this.jLabel1 = new JLabel();
    this.tbArchiveName = new JTextField();
    this.jSeparator2 = new JToolBar.Separator();
    this.jLabel2 = new JLabel();
    this.tbFileName = new JTextField();
    this.jSeparator1 = new JToolBar.Separator();
    this.btnOpen = new JButton();
    this.jSeparator3 = new JToolBar.Separator();
    this.btnSave = new JButton();
    this.jSeparator4 = new JToolBar.Separator();
    this.btnAddRow = new JButton();
    this.btnDeleteRow = new JButton();
    this.jScrollPane1 = new JScrollPane();
    this.tblBcsv = new JTable();
    setDefaultCloseOperation(2);
    setTitle("BCSV editor");
    addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent evt) {
            BcsvEditorForm.this.formWindowClosing(evt);
          }
        });
    this.jToolBar1.setFloatable(false);
    this.jToolBar1.setRollover(true);
    this.jLabel1.setText("Archive: ");
    this.jToolBar1.add(this.jLabel1);
    this.tbArchiveName.setText("/StageData/HeavenlyBeachGalaxy/HeavenlyBeachGalaxyScenario.arc");
    this.jToolBar1.add(this.tbArchiveName);
    this.jToolBar1.add(this.jSeparator2);
    this.jLabel2.setText("File: ");
    this.jToolBar1.add(this.jLabel2);
    this.tbFileName.setText("/heavenlybeachgalaxyscenario/scenariodata.bcsv");
    this.tbFileName.setToolTipText("");
    this.jToolBar1.add(this.tbFileName);
    this.jToolBar1.add(this.jSeparator1);
    this.btnOpen.setText("Open");
    this.btnOpen.setFocusable(false);
    this.btnOpen.setHorizontalTextPosition(0);
    this.btnOpen.setVerticalTextPosition(3);
    this.btnOpen.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            BcsvEditorForm.this.btnOpenActionPerformed(evt);
          }
        });
    this.jToolBar1.add(this.btnOpen);
    this.jToolBar1.add(this.jSeparator3);
    this.btnSave.setText("Save");
    this.btnSave.setFocusable(false);
    this.btnSave.setHorizontalTextPosition(0);
    this.btnSave.setVerticalTextPosition(3);
    this.btnSave.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            BcsvEditorForm.this.btnSaveActionPerformed(evt);
          }
        });
    this.jToolBar1.add(this.btnSave);
    this.jToolBar1.add(this.jSeparator4);
    this.btnAddRow.setText("Add row");
    this.btnAddRow.setFocusable(false);
    this.btnAddRow.setHorizontalTextPosition(0);
    this.btnAddRow.setVerticalTextPosition(3);
    this.btnAddRow.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            BcsvEditorForm.this.btnAddRowActionPerformed(evt);
          }
        });
    this.jToolBar1.add(this.btnAddRow);
    this.btnDeleteRow.setText("Delete row");
    this.btnDeleteRow.setFocusable(false);
    this.btnDeleteRow.setHorizontalTextPosition(0);
    this.btnDeleteRow.setVerticalTextPosition(3);
    this.btnDeleteRow.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            BcsvEditorForm.this.btnDeleteRowActionPerformed(evt);
          }
        });
    this.jToolBar1.add(this.btnDeleteRow);
    this.tblBcsv.setModel(new DefaultTableModel(new Object[0][], (Object[])new String[0]));
    this.tblBcsv.setAutoResizeMode(0);
    this.jScrollPane1.setViewportView(this.tblBcsv);
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jToolBar1, -1, 702, 32767).addComponent(this.jScrollPane1));
    layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jToolBar1, -2, 23, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jScrollPane1, -1, 469, 32767)));
    pack();
  }
  
  private void btnOpenActionPerformed(ActionEvent evt) {
    try {
      if (this.archive != null)
        this.archive.close(); 
      if (this.bcsv != null)
        this.bcsv.close(); 
      this.archive = null;
      this.bcsv = null;
      this.archive = (FilesystemBase)new RarcFilesystem(Whitehole.game.filesystem.openFile(this.tbArchiveName.getText()));
      this.bcsv = new Bcsv(this.archive.openFile(this.tbFileName.getText()));
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(this, "Failed to open BCSV file: " + ex.getMessage(), "Whitehole", 0);
      try {
        if (this.bcsv != null)
          this.bcsv.close(); 
        if (this.archive != null)
          this.archive.close(); 
      } catch (IOException ex2) {}
      this.bcsv = null;
      this.archive = null;
      return;
    } 
    DefaultTableModel table = (DefaultTableModel)this.tblBcsv.getModel();
    table.setRowCount(0);
    table.setColumnCount(0);
    for (Bcsv.Field field : this.bcsv.fields.values())
      table.addColumn(field.name); 
    for (Bcsv.Entry entry : this.bcsv.entries) {
      Vector<Object> row = new Vector(this.bcsv.fields.size());
      for (Bcsv.Field field : this.bcsv.fields.values()) {
        Object val = entry.get(Integer.valueOf(field.nameHash));
        row.add(val);
      } 
      table.addRow(row);
    } 
  }
  
  private void btnAddRowActionPerformed(ActionEvent evt) {
    DefaultTableModel table = (DefaultTableModel)this.tblBcsv.getModel();
    table.addRow((Object[])null);
  }
  
  private void btnDeleteRowActionPerformed(ActionEvent evt) {
    int sel = this.tblBcsv.getSelectedRow();
    if (sel < 0)
      return; 
    DefaultTableModel table = (DefaultTableModel)this.tblBcsv.getModel();
    table.removeRow(sel);
  }
  
  private void formWindowClosing(WindowEvent evt) {
    try {
      if (this.bcsv != null)
        this.bcsv.close(); 
      if (this.archive != null)
        this.archive.close(); 
    } catch (IOException ex) {}
  }
  
  private void btnSaveActionPerformed(ActionEvent evt) {
    this.bcsv.entries.clear();
    for (int r = 0; r < this.tblBcsv.getRowCount(); r++) {
      Bcsv.Entry entry = new Bcsv.Entry();
      int c = 0;
      for (Bcsv.Field field : this.bcsv.fields.values()) {
        Object valobj = this.tblBcsv.getValueAt(r, c);
        String val = (valobj == null) ? "" : valobj.toString();
        try {
          switch (field.type) {
            case 0:
            case 3:
              entry.put(Integer.valueOf(field.nameHash), Integer.valueOf(Integer.parseInt(val)));
              break;
            case 4:
              entry.put(Integer.valueOf(field.nameHash), Short.valueOf(Short.parseShort(val)));
              break;
            case 5:
              entry.put(Integer.valueOf(field.nameHash), Byte.valueOf(Byte.parseByte(val)));
              break;
            case 2:
              entry.put(Integer.valueOf(field.nameHash), Float.valueOf(Float.parseFloat(val)));
              break;
            case 6:
              entry.put(Integer.valueOf(field.nameHash), val);
              break;
          } 
        } catch (NumberFormatException ex) {
          switch (field.type) {
            case 0:
            case 3:
              entry.put(Integer.valueOf(field.nameHash), Integer.valueOf(0));
              break;
            case 4:
              entry.put(Integer.valueOf(field.nameHash), Short.valueOf((short)0));
              break;
            case 5:
              entry.put(Integer.valueOf(field.nameHash), Byte.valueOf((byte)0));
              break;
            case 2:
              entry.put(Integer.valueOf(field.nameHash), Float.valueOf(0.0F));
              break;
            case 6:
              entry.put(Integer.valueOf(field.nameHash), "");
              break;
          } 
        } 
        c++;
      } 
      this.bcsv.entries.add(entry);
    } 
    try {
      this.bcsv.save();
      this.archive.save();
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    } 
  }
}
