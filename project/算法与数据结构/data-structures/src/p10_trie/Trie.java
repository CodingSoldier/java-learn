package p10_trie;

import java.util.TreeMap;

/**
 * @author chenpq05
 * @since 2023/5/10 16:49
 */
public class Trie {

  private class Node {
    private boolean isWord;
    private TreeMap<Character, Node> children;

    public Node(boolean isWord) {
      this.isWord = isWord;
      this.children = new TreeMap<>();
    }

    public Node() {
      this(false);
    }
  }

  private Node root;
  private Integer size;

  public Trie() {
    this.root = new Node();
    this.size = 0;
  }

  public void insert(String word) {
    Node cur = root;
    for (int i=0; i<word.length(); i++) {
      char c = word.charAt(i);
      if (cur.children.get(c) == null) {
        cur.children.put(c, new Node());
      }
      cur = cur.children.get(c);
    }
    if (!cur.isWord) {
      cur.isWord=true;
      size++;
    }
  }

  public boolean search(String word) {
    Node cur = root;
    for (int i=0; i<word.length(); i++) {
      char c = word.charAt(i);
      if (cur.children.get(c) == null) {
        return false;
      }
      cur = cur.children.get(c);
    }
    return cur.isWord;
  }

  public boolean startsWith(String word) {
    Node cur = root;
    for (int i=0; i<word.length(); i++) {
      char c = word.charAt(i);
      if (cur.children.get(c) == null) {
        return false;
      }
      cur = cur.children.get(c);
    }
    return true;
  }


}
