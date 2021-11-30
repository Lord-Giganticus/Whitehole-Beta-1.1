package whitehole;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import whitehole.smg.ZoneArchive;

public class PropertyPanel extends JPanel {
  public LinkedHashMap<String, Category> categories;
  
  public LinkedHashMap<String, Field> fields;
  
  private int curRow;
  
  private int curIndex;
  
  private Category curCategory;
  
  private EventListener eventListener;
  
  public PropertyPanel() {
    setLayout(new GridBagLayout());
    this.categories = new LinkedHashMap<>();
    this.fields = new LinkedHashMap<>();
    this.curRow = 0;
    this.curIndex = 0;
    this.curCategory = null;
    this.eventListener = null;
  }
  
  public void setEventListener(EventListener listener) {
    this.eventListener = listener;
  }
  
  public void clear() {
    removeAll();
    this.categories.clear();
    this.fields.clear();
    this.curRow = 0;
    this.curIndex = 0;
    this.curCategory = null;
  }
  
  public void addCategory(String name, String caption) {
    Category cat = new Category();
    this.categories.put(name, cat);
    cat.name = name;
    cat.caption = caption;
    cat.startRow = this.curRow;
    cat.endRow = this.curRow;
    cat.startIndex = this.curIndex;
    cat.endIndex = this.curIndex;
    this.curCategory = cat;
    JToggleButton btn = new JToggleButton("[-] " + cat.caption);
    cat.header = btn;
    btn.setSelected(true);
    btn.setFont(btn.getFont().deriveFont(1));
    add(btn, new GridBagConstraints(0, this.curRow, 3, 1, 1.0D, 0.0D, 10, 2, new Insets(1, 1, 0, 1), 0, 0));
    btn.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            for (PropertyPanel.Category c : PropertyPanel.this.categories.values()) {
              if (!c.header.equals(evt.getSource()))
                continue; 
              if (!c.header.isSelected()) {
                c.header.setText("[+] " + c.caption);
                for (int j = c.startIndex + 1; j <= c.endIndex; j++)
                  PropertyPanel.this.getComponent(j).setVisible(false); 
                break;
              } 
              c.header.setText("[-] " + c.caption);
              for (int i = c.startIndex + 1; i <= c.endIndex; i++)
                PropertyPanel.this.getComponent(i).setVisible(true); 
            } 
          }
        });
    this.curRow++;
    this.curIndex++;
  }
  
  public void addField(String name, String caption, String type, List choices, Object val) {
    JButton extrabtn;
    if (this.curCategory == null)
      throw new NullPointerException("You must add a category before adding fields."); 
    Field field = new Field();
    this.fields.put(name, field);
    field.name = name;
    field.caption = caption;
    field.type = type;
    field.row = this.curRow;
    field.index = this.curIndex;
    add(new JLabel(field.caption + ":"), new GridBagConstraints(0, this.curRow, 1, 1, 0.4000000059604645D, 0.0D, 22, 0, new Insets(1, 1, 0, 1), 0, 0));
    this.curIndex++;
    switch (field.type) {
      case "text":
      case "int":
        field.field = new JTextField(val.toString());
        ((JTextField)field.field).addKeyListener(new KeyListener() {
              public void keyPressed(KeyEvent evt) {}
              
              public void keyTyped(KeyEvent evt) {}
              
              public void keyReleased(KeyEvent evt) {
                for (PropertyPanel.Field field : PropertyPanel.this.fields.values()) {
                  if (!field.field.equals(evt.getSource()))
                    continue; 
                  String val = ((JTextField)evt.getSource()).getText();
                  try {
                    if (!field.type.equals("text"))
                      val = String.format("%1$d", new Object[] { Long.valueOf(Long.parseLong(val)) }); 
                    ((JTextField)evt.getSource()).setForeground(Color.getColor("text"));
                  } catch (NumberFormatException ex) {
                    val = "0";
                    ((JTextField)evt.getSource()).setForeground(new Color(16728128));
                  } 
                  PropertyPanel.this.eventListener.propertyChanged(field.name, val);
                } 
              }
            });
        add(field.field, new GridBagConstraints(1, this.curRow, 2, 1, 0.6000000238418579D, 0.0D, 10, 2, new Insets(1, 1, 0, 1), 0, 0));
        this.curIndex++;
        break;
      case "float":
        field.field = new JSpinner();
        ((JSpinner)field.field).setModel(new SpinnerNumberModel(((Float)val).floatValue(), -3.4028234663852886E38D, 3.4028234663852886E38D, 1.0D));
        field.field.setPreferredSize(new Dimension(10, (field.field.getMinimumSize()).height));
        ((JSpinner)field.field).addChangeListener(new ChangeListener() {
              public void stateChanged(ChangeEvent evt) {
                for (PropertyPanel.Field field : PropertyPanel.this.fields.values()) {
                  if (!field.field.equals(evt.getSource()))
                    continue; 
                  PropertyPanel.this.eventListener.propertyChanged(field.name, ((JSpinner)evt.getSource()).getValue());
                } 
              }
            });
        add(field.field, new GridBagConstraints(1, this.curRow, 2, 1, 0.6000000238418579D, 0.0D, 10, 2, new Insets(1, 1, 0, 1), 0, 0));
        this.curIndex++;
        break;
      case "list":
        field.field = new JComboBox();
        for (Object item : choices)
          ((JComboBox<Object>)field.field).addItem(item); 
        ((JComboBox)field.field).setSelectedItem(val);
        ((JComboBox)field.field).addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                for (PropertyPanel.Field field : PropertyPanel.this.fields.values()) {
                  if (!field.field.equals(evt.getSource()))
                    continue; 
                  PropertyPanel.this.eventListener.propertyChanged(field.name, ((JComboBox)evt.getSource()).getSelectedItem());
                } 
              }
            });
        add(field.field, new GridBagConstraints(1, this.curRow, 2, 1, 0.6000000238418579D, 0.0D, 10, 2, new Insets(1, 1, 0, 1), 0, 0));
        this.curIndex++;
        break;
      case "bool":
        field.field = new JCheckBox();
        ((JCheckBox)field.field).setText(" ");
        ((JCheckBox)field.field).setSelected(((Boolean)val).booleanValue());
        ((JCheckBox)field.field).addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                for (PropertyPanel.Field field : PropertyPanel.this.fields.values()) {
                  if (!field.field.equals(evt.getSource()))
                    continue; 
                  PropertyPanel.this.eventListener.propertyChanged(field.name, Boolean.valueOf(((JCheckBox)evt.getSource()).isSelected()));
                } 
              }
            });
        add(field.field, new GridBagConstraints(1, this.curRow, 2, 1, 0.6000000238418579D, 0.0D, 10, 2, new Insets(1, 1, 0, 1), 0, 0));
        this.curIndex++;
        break;
      case "objname":
        field.field = new JTextField(val.toString());
        ((JTextField)field.field).addKeyListener(new KeyListener() {
              public void keyPressed(KeyEvent evt) {}
              
              public void keyTyped(KeyEvent evt) {}
              
              public void keyReleased(KeyEvent evt) {
                for (PropertyPanel.Field field : PropertyPanel.this.fields.values()) {
                  if (!field.field.equals(evt.getSource()))
                    continue; 
                  PropertyPanel.this.eventListener.propertyChanged(field.name, ((JTextField)evt.getSource()).getText());
                } 
              }
            });
        add(field.field, new GridBagConstraints(1, this.curRow, 1, 1, 0.6000000238418579D, 0.0D, 10, 2, new Insets(1, 1, 0, 1), 0, 0));
        this.curIndex++;
        extrabtn = new JButton("...");
        extrabtn.setPreferredSize(new Dimension((extrabtn.getMinimumSize()).height, (extrabtn.getMinimumSize()).height));
        extrabtn.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
                Component form = (Component)evt.getSource();
                while (true) {
                  form = form.getParent();
                  if (form.getClass() == GalaxyEditorForm.class) {
                    GalaxyEditorForm gform = (GalaxyEditorForm)form;
                    PropertyPanel panel = (PropertyPanel)((Component)evt.getSource()).getParent();
                    int index = 0;
                    for (Component c : panel.getComponents()) {
                      if (c.equals(evt.getSource()))
                        break; 
                      index++;
                    } 
                    JTextField field = (JTextField)panel.getComponents()[index - 1];
                    ObjectSelectForm objsel = new ObjectSelectForm(gform, ((ZoneArchive)gform.zoneArcs.get(gform.galaxyName)).gameMask, field.getText());
                    objsel.setVisible(true);
                    field.setText(objsel.selectedObject);
                    for (PropertyPanel.Field f : PropertyPanel.this.fields.values()) {
                      if (!f.field.equals(field))
                        continue; 
                      PropertyPanel.this.eventListener.propertyChanged(f.name, objsel.selectedObject);
                    } 
                    return;
                  } 
                } 
              }
            });
        add(extrabtn, new GridBagConstraints(2, this.curRow, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(1, 1, 0, 1), 0, 0));
        this.curIndex++;
        break;
    } 
    this.curCategory.endRow = this.curRow;
    this.curCategory.endIndex = this.curIndex - 1;
    this.curRow++;
  }
  
  public void addTermination() {
    add(Box.createVerticalGlue(), new GridBagConstraints(0, this.curRow, 3, 1, 1.0D, 1.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
    this.curRow++;
    this.curIndex++;
  }
  
  public void setFieldValue(String field, Object value) {
    Field f = this.fields.get(field);
    switch (f.type) {
      case "text":
      case "objname":
        ((JTextField)f.field).setText((String)value);
        break;
      case "int":
        ((JTextField)f.field).setText(String.format("%1$d", new Object[] { Long.valueOf(Long.parseLong((String)value)) }));
        break;
      case "float":
        ((JSpinner)f.field).setValue(Double.valueOf(((Float)value).floatValue()));
        break;
      case "bool":
        ((JCheckBox)f.field).setSelected(((Boolean)value).booleanValue());
        break;
    } 
  }
  
  public class Category {
    String name;
    
    String caption;
    
    int startRow;
    
    int endRow;
    
    int startIndex;
    
    int endIndex;
    
    JToggleButton header;
  }
  
  public class Field {
    String name;
    
    String caption;
    
    String type;
    
    int row;
    
    int index;
    
    Component field;
  }
  
  public static interface EventListener {
    void propertyChanged(String param1String, Object param1Object);
  }
}
