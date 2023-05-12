package p11_union_find;

/**
 * @author chenpq05
 * @since 2023/5/12 14:15
 */
public class QuitFind implements UF {

  private int[] ids;

  public QuitFind(int size) {
    this.ids = new int[size];
    for (int i=0; i<size; i++) {
      this.ids[i] = i;
    }
  }

  private int find(int index) {
    if (index<0 || index>=ids.length) {
      throw new IllegalArgumentException("下标越界");
    }

    return ids[index];
  }

  @Override
  public int getSize() {
    return ids.length;
  }

  @Override
  public boolean isConnected(int indexP, int indexQ) {
    return find(indexP) == find(indexQ);
  }

  @Override
  public void unionElements(int indexP, int indexQ) {
    int idP = find(indexP);
    int idQ = find(indexQ);
    if (idP == idQ) {
      return;
    }
    for (int i=0; i<ids.length; i++) {
      if (ids[i] == idP) {
        ids[i] = idQ;
      }
    }
  }

}
