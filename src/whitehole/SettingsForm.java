package whitehole;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.LayoutStyle;

public class SettingsForm extends JDialog {
  private JButton btnCancel;
  
  private JButton btnOk;
  
  private JCheckBox chkFastDrag;
  
  private JCheckBox chkObjectDBUpdate;
  
  private JCheckBox chkUseShaders;
  
  public SettingsForm(Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
  }
  
  private void initComponents() {
    this.chkObjectDBUpdate = new JCheckBox();
    this.chkUseShaders = new JCheckBox();
    this.chkFastDrag = new JCheckBox();
    this.btnCancel = new JButton();
    this.btnOk = new JButton();
    setDefaultCloseOperation(2);
    setTitle("Settings");
    addWindowListener(new WindowAdapter() {
          public void windowOpened(WindowEvent evt) {
            SettingsForm.this.formWindowOpened(evt);
          }
        });
    this.chkObjectDBUpdate.setText("Check for object database updates on startup");
    this.chkObjectDBUpdate.setBorder(BorderFactory.createTitledBorder(""));
    this.chkUseShaders.setText("Use shaders for 3D rendering");
    this.chkFastDrag.setText("Render objects in low-res when dragging");
    this.btnCancel.setText("Cancel");
    this.btnCancel.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            SettingsForm.this.btnCancelActionPerformed(evt);
          }
        });
    this.btnOk.setText("OK");
    this.btnOk.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            SettingsForm.this.btnOkActionPerformed(evt);
          }
        });
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.chkFastDrag).addComponent(this.chkUseShaders).addComponent(this.chkObjectDBUpdate)).addGap(0, 0, 32767)).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.btnOk).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.btnCancel))).addContainerGap()));
    layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.chkObjectDBUpdate).addGap(18, 18, 18).addComponent(this.chkUseShaders).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.chkFastDrag).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 26, 32767).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.btnCancel).addComponent(this.btnOk)).addContainerGap()));
    pack();
  }
  
  private void formWindowOpened(WindowEvent evt) {
    this.chkObjectDBUpdate.setSelected(Settings.objectDBUpdate);
    this.chkUseShaders.setSelected(Settings.useShaders);
    this.chkFastDrag.setSelected(Settings.fastDrag);
  }
  
  private void btnCancelActionPerformed(ActionEvent evt) {
    dispose();
  }
  
  private void btnOkActionPerformed(ActionEvent evt) {
    Settings.objectDBUpdate = this.chkObjectDBUpdate.isSelected();
    Settings.useShaders = this.chkUseShaders.isSelected();
    Settings.fastDrag = this.chkFastDrag.isSelected();
    Settings.save();
    dispose();
  }
}
