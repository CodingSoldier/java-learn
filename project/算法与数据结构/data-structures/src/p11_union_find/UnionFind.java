package p11_union_find;

/**
 * @author chenpq05
 * @since 2023/5/12 15:23
 */
public class UnionFind implements UF {

  private int[] parent;

  public UnionFind(int size) {
    this.parent = new int[size];
    for (int i=0; i<size; i++) {
      this.parent[i] = i;
    }
  }

  private int find(int index) {
    if (index <0 || index >= this.parent.length) {
      throw new IllegalArgumentException("数组下标越界");
    }
    // 根节点的下标 == 根节点的值
    while (index != this.parent[index]) {
      index = this.parent[index];
    }
    return index;
  }

  @Override
  public int getSize() {
    return parent.length;
  }

  @Override
  public boolean isConnected(int indexP, int indexQ) {
    return find(indexP) == find(indexQ);
  }

  @Override
  public void unionElements(int indexP, int indexQ) {
    int pRoot = find(indexP);
    int qRoot = find(indexQ);
    if (pRoot == qRoot) {
      return;
    }

    this.parent[pRoot] = qRoot;
  }
}
