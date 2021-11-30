package whitehole;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class CheckBoxList extends JList {
  protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
  
  private EventListener eventListener;
  
  public CheckBoxList() {
    setCellRenderer(new CellRenderer());
    addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
            int index = CheckBoxList.this.locationToIndex(e.getPoint());
            if (index != -1) {
              JCheckBox checkbox = (JCheckBox)CheckBoxList.this.getModel().getElementAt(index);
              checkbox.setSelected(!checkbox.isSelected());
              if (CheckBoxList.this.eventListener != null)
                CheckBoxList.this.eventListener.checkBoxStatusChanged(index, checkbox.isSelected()); 
              CheckBoxList.this.repaint();
            } 
          }
        });
    setSelectionMode(0);
    this.eventListener = null;
  }
  
  public void setEventListener(EventListener listener) {
    this.eventListener = listener;
  }
  
  protected class CellRenderer implements ListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      JCheckBox checkbox = (JCheckBox)value;
      checkbox.setBackground(isSelected ? CheckBoxList.this.getSelectionBackground() : CheckBoxList.this.getBackground());
      checkbox.setForeground(isSelected ? CheckBoxList.this.getSelectionForeground() : CheckBoxList.this.getForeground());
      checkbox.setEnabled(CheckBoxList.this.isEnabled());
      checkbox.setFont(CheckBoxList.this.getFont());
      checkbox.setFocusPainted(false);
      checkbox.setBorderPainted(true);
      checkbox.setBorder(isSelected ? UIManager.getBorder("List.focusCellHighlightBorder") : CheckBoxList.noFocusBorder);
      return checkbox;
    }
  }
  
  public static interface EventListener {
    void checkBoxStatusChanged(int param1Int, boolean param1Boolean);
  }
}
