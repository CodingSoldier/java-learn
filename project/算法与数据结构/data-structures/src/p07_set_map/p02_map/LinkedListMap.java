package p07_set_map.p02_map;

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

  @Override
  public void add(K key, V value) {
    Node<K, V> cur = dummyHead.next;
    if (contains(key)) {
      while (cur != null) {
        if (cur.key.equals(key)) {
          cur.value = value;
          break;
        }
        cur = cur.next;
      }
    } else {
      dummyHead.next = new Node<>(key, value, dummyHead.next);
    }
  }

  @Override
  public V remove(K key) {
    return null;
  }

  @Override
  public boolean contains(K key) {
    V v = get(key);
    return v != null;
  }

  @Override
  public V get(K key) {
    Node<K, V> cur = dummyHead.next;
    while (cur != null) {
      if (cur.key.equals(key)) {
        return cur.value;
      }
      cur = cur.next;
    }
    return null;
  }

  @Override
  public void set(K key, V newValue) {

  }

  @Override
  public int getSize() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }
}
