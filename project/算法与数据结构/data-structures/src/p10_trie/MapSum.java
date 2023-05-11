package p10_trie;

import java.util.TreeMap;

/**
 * @author chenpq05
 * @since 2023/5/11 14:58
 */
public class MapSum {

  private class Node {

    private Integer value;
    private TreeMap<Character, Node> children;

    public Node() {
      this.value = 0;
      this.children = new TreeMap<>();
    }
  }

  private Node root;

  public MapSum() {
    this.root = new Node();
  }

  public void insert(String key, int val) {
    Node cur = root;
    for (int i = 0; i < key.length(); i++) {
      char c = key.charAt(i);
      if (cur.children.get(c) == null) {
        cur.children.put(c, new Node());
      }
      cur = cur.children.get(c);
    }
    cur.value = val;
  }

  public int sum(String prefix) {
    Node cur = root;
    for (int i = 0; i < prefix.length(); i++) {
      char c = prefix.charAt(i);
      if (cur.children.get(c) == null) {
        return 0;
      }
      cur = cur.children.get(c);
    }
    return sum(cur);
  }

  private int sum(Node node) {
    int result = node.value;
    //// 递归终止条件，不写也可以。node.children.size()不会进入递归循环
    //if (node.children.size() == 0) {
    //  return result;
    //}
    for (char c : node.children.keySet()) {
      // 递归加上所有子级的value
      result = result + sum(node.children.get(c));
    }
    return result;
  }

}