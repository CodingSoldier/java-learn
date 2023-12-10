package p11_union_find;

/**
 * @author chenpq05
 * @since 2023/5/12 14:13
 */
public interface UF {

  int getSize();

  boolean isConnected(int p, int q);

  void unionElements(int p, int q);

}
