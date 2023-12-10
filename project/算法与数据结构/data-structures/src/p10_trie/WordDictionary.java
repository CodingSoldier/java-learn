package p10_trie;

import java.util.Set;
import java.util.TreeMap;

/**
 * @author chenpq05
 * @since 2023/5/11 10:08
 */
public class WordDictionary {

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

  public WordDictionary() {
    this.root = new Node();
  }

  public void addWord(String word) {
    Node cur = root;
    for (int i=0; i<word.length(); i++) {
      char c = word.charAt(i);
      if (cur.children.get(c) == null) {
        cur.children.put(c, new Node());
      }
      cur = cur.children.get(c);
    }
    cur.isWord = true;
  }

  public boolean search(String word) {
    return match(root, word, 0);
  }

  private boolean match(Node node, String word, int index) {
    // index从0开始，递归到底的情况
    if (word.length() == index) {
      return node.isWord;
    }
    char c = word.charAt(index);
    if (c != '.') {
      if (node.children.get(c) == null) {
        return false;
      }
      return match(node.children.get(c), word, index + 1);
    } else {
      Set<Character> keySet = node.children.keySet();
      for (Character key:keySet) {
        // 字符为. 能匹配所有children，有一个children满足后面的字符，就返回true
        if (match(node.children.get(key), word, index + 1)) {
          return true;
        }
      }
      // 所有children后面的字符都不满足 word 字符串 . 后面的字符，返回false
      return false;
    }
  }

}
