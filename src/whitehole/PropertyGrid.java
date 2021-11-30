package whitehole;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class PropertyGrid extends JTable {
  private JFrame parent;
  
  public PropertyGrid(JFrame parent) {
    this.parent = parent;
    setModel(new PGModel());
  }
  
  public void clear() {}
  
  public void addCategory(String name, String caption) {}
  
  public void addField(String name, String caption, String type, List choices, Object val) {}
  
  public class PGModel extends AbstractTableModel {
    public int getRowCount() {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public int getColumnCount() {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }
}
