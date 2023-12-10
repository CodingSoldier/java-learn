package p11_union_find;

/**
 * @author chenpq05
 * @since 2023/5/12 15:23
 */
public class SizeUnionFind implements UF {

  private int[] parent;
  private int[] sz;

  public SizeUnionFind(int size) {
    this.parent = new int[size];
    for (int i=0; i<size; i++) {
      this.parent[i] = i;
      this.sz[i] = 1;
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

    if (this.sz[pRoot] < this.sz[qRoot]) {
      this.parent[pRoot] = qRoot;
      this.sz[pRoot] += this.sz[qRoot];
    } else {
      this.parent[qRoot] = pRoot;
      this.sz[qRoot] += pRoot;
    }

  }
}
