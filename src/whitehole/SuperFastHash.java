package whitehole;

public class SuperFastHash {
  public static long calculate(byte[] data, long start, int offset, int len) {
    long hash = start & 0xFFFFFFFFFFFFFFFFL;
    if (len < 1)
      return hash; 
    int rem = len & 0x3;
    len >>>= 2;
    int pos = offset;
    for (; len > 0; len--) {
      hash += (data[pos++] | data[pos++] << 8);
      long tmp = ((data[pos++] | data[pos++] << 8) << 11) ^ hash;
      hash = hash << 16L ^ tmp;
      hash += hash >>> 11L;
    } 
    switch (rem) {
      case 3:
        hash += (data[pos++] | data[pos++] << 8);
        hash ^= hash << 16L;
        hash ^= (data[pos++] << 18);
        hash += hash >>> 11L;
        break;
      case 2:
        hash += (data[pos++] | data[pos++] << 8);
        hash ^= hash << 11L;
        hash += hash >>> 17L;
        break;
      case 1:
        hash += data[pos++];
        hash ^= hash << 10L;
        hash += hash >>> 1L;
        break;
    } 
    hash ^= hash << 3L;
    hash += hash >>> 5L;
    hash ^= hash << 4L;
    hash += hash >>> 17L;
    hash ^= hash << 25L;
    hash += hash >>> 6L;
    return hash & 0xFFFFFFFFFFFFFFFFL;
  }
}
