package whitehole;

import java.util.Enumeration;
import java.util.Iterator;

public class Iterator2Enumeration implements Enumeration {
  private Iterator iterator;
  
  public Iterator2Enumeration(Iterator it) {
    this.iterator = it;
  }
  
  public boolean hasMoreElements() {
    return this.iterator.hasNext();
  }
  
  public Object nextElement() {
    return this.iterator.next();
  }
}
