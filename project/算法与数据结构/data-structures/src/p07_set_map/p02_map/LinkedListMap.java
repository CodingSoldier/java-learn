package p07_set_map.p02_map;

import java.util.ArrayList;
import java.util.HashSet;
import p07_set_map.p01_set.BSTSet;
import p07_set_map.p01_set.FileOperation;

/**
 * @author chenpq05
 * @since 2023/4/7 15:12
 */
public class LinkedListMap<K, V> implements Map<K, V> {

  private class Node<K, V> {
    private K key;
    private V value;
    private Node<K, V> next;

    public Node(K key, V value, Node<K, V> next) {
      this.key = key;
      this.value = value;
      this.next = next;
    }

    public Node() {
    }

  }

  private int size;
  private Node<K, V> dummyHead;

  public LinkedListMap() {
    this.dummyHead = new Node<>();
    this.size = 0;
  }

  /**
   * 获取节点
   * @param key
   * @return
   */
  public Node<K, V> getNode(K key) {
    Node<K, V> cur = dummyHead.next;
    while (cur != null) {
      if (cur.key.equals(key)) {
        return cur;
      }
      cur = cur.next;
    }
    return null;
  }

  @Override
  public void add(K key, V value) {
    Node<K, V> node = getNode(key);
    if (node != null) {
      node.value = value;
    } else {
      dummyHead.next = new Node<>(key, value, dummyHead.next);
      size++;
    }
  }

  @Override
  public V remove(K key) {
    //Node<K, V> cur = dummyHead.next;
    //while (cur != null) {
    //  if (cur.key.equals(key)) {
    //    return cur;
    //  }
    //  cur = cur.next;
    //}
    Node<K, V> pre = dummyHead;
    while (pre.next != null) {
      if (key.equals(pre.next.key)) {
        Node<K, V> delNode = pre.next;
        V value = delNode.value;
        delNode = null;
        pre.next = pre.next.next;
        size--;
        return value;
      }
      pre = pre.next;
    }
    return null;
  }

  @Override
  public boolean contains(K key) {
    V v = get(key);
    return v != null;
  }

  @Override
  public V get(K key) {
    Node<K, V> node = getNode(key);
    return node != null ? node.value : null;
  }

  @Override
  public void set(K key, V newValue) {
    V v = get(key);
  }

  @Override
  public int getSize() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }


  public static void main(String[] args){

    System.out.println("Pride and Prejudice");

    ArrayList<String> words = new ArrayList<>();
    if(FileOperation.readFile("D:\\mycode\\java-learn\\project\\算法与数据结构\\data-structures\\src\\pride-and-prejudice.txt", words)) {
      System.out.println("Total words: " + words.size());

      LinkedListMap<String, Integer> map = new LinkedListMap<>();
      HashSet<String> keySet = new HashSet<>();
      for (String word : words) {
        if (map.contains(word))
          map.set(word, map.get(word) + 1);
        else
          map.add(word, 1);

        keySet.add(word);
      }

      System.out.println("Total different words: " + map.getSize());
      System.out.println("Frequency of PRIDE: " + map.get("pride"));
      System.out.println("Frequency of PREJUDICE: " + map.get("prejudice"));

      for (String key : keySet) {
        map.remove(key);
      }
      System.out.println("===========================");
      System.out.println("Total different words: " + map.getSize());
      System.out.println("Frequency of PRIDE: " + map.get("pride"));
      System.out.println("Frequency of PREJUDICE: " + map.get("prejudice"));
    }

    System.out.println();
  }


}
