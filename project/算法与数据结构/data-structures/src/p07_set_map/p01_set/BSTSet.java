package p07_set_map.p01_set;

import java.util.TreeSet;
import p06_binary_search_tree.BST;

/**
 * @author chenpq05
 * @since 2023/4/7 11:06
 */
public class BSTSet<E extends Comparable<E>> implements Set<E> {

  private BST bst;

  public BSTSet() {
    this.bst = new BST();
  }

  @Override
  public void add(E e) {
    bst.add(e);
  }

  @Override
  public boolean contains(E e) {
    return bst.contains(e);
  }

  @Override
  public void remove(E e) {
    bst.remove(e);
  }

  @Override
  public int getSize() {
    return bst.size();
  }

  @Override
  public boolean isEmpty() {
    return bst.isEmpty();
  }


  public static int uniqueMorseRepresentations(String[] words) {
    String[] codes = {".-","-...","-.-.","-..",".","..-.","--.","....","..",".---","-.-",".-..","--","-.","---",".--.","--.-",".-.","...","-","..-","...-",".--","-..-","-.--","--.."};
    TreeSet set = new TreeSet();
    for (int i = 0; i < words.length; i++) {
      String w = words[i];
      StringBuilder sb = new StringBuilder();
      for (int j = 0; j < w.length(); j ++) {
        int index = w.charAt(j) - 'a';
        String code = codes[index];
        sb.append(code);
      }
      set.add(sb.toString());
    }
    return set.size();
  }

  public static void main(String[] args) {
    String[] strings = {"gin", "zen", "gig", "msg"};
    int i = uniqueMorseRepresentations(strings);
    System.out.println(i);
  }

}
