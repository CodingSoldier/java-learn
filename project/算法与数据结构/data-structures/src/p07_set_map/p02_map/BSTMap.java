package p07_set_map.p02_map;

/**
 * @author chenpq05
 * @since 2023/4/21 15:53
 */
public class BSTMap<K extends Comparable<K>, V> implements Map<K, V>{

  private class Node<K, V> {
    private K key;
    private V value;
    private Node<K, V> left,right;

    public Node(K key, V value) {
      this.key = key;
      this.value = value;
      this.left = null;
      this.right = null;
    }
  }

  private Node<K, V> root;
  private int size;

  public BSTMap() {
    root = null;
    size = 0;
  }

  private Node<K,V> getNode(Node<K,V> node, K key) {
    if (node == null) {
      return null;
    }
    if (key.compareTo(node.key) < 0) {
      return getNode(node.left, key);
    } else if (key.compareTo(node.key) > 0) {
      return getNode(node.right, key);
    } else {
      return node;
    }
  }

  @Override
  public void add(K key, V value) {
    root = addNode(root, key, value);
  }

  public Node<K, V> addNode(Node<K,V> node, K key, V value) {
    if (node == null) {
      size++;
      return new Node<>(key, value);
    }
    if (key.compareTo(node.key) < 0) {
      node.left = addNode(node.left, key, value);
    } else if (key.compareTo(node.key) > 0) {
      node.right = addNode(node.right, key, value);
    } else {
      node.value = value;
    }
    return node;
  }

  @Override
  public boolean contains(K key) {
    Node<K, V> node = getNode(root, key);
    return node != null;
  }

  @Override
  public V get(K key) {
    Node<K, V> node = getNode(root, key);
    return node != null ? node.value : null;
  }

  @Override
  public void set(K key, V newValue) {
    Node<K, V> node = getNode(root, key);
    if (node != null) {
      node.value = newValue;
    }
  }

  @Override
  public int getSize() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public V remove(K key) {
    return null;
  }

  private Node<K, V> minimum(Node<K, V> node) {
    if (node.left == null) {
      return node;
    }
    return minimum(node.left);
  }

  private Node<K, V> removeMin(Node<K, V> node) {
    if (node.left == null) {
      Node<K, V> rightNode = node.right;
      node.right = null;
      size--;
      return rightNode;
    }
    node.left = removeMin(node.left);
    return node;
  }

  private Node<K, V> removeNode(Node<K, V> node, K key) {
    if (node == null) {
      return null;
    }
    if (key.compareTo(node.key) < 0) {
      node.left = removeNode(node.left, key);
    } else if (key.compareTo(node.key) > 0) {
      node.right = removeNode(node.right, key);
    } else { // key == node.key
      if (node.left == null) {
        Node<K, V> rightNode = node.right;
        node.right = null;
        size--;
        return rightNode;
      } else if (node.right == null) {
        Node<K, V> leftNode = node.left;
        node.left = null;
        size--;
        return leftNode;
      }
      Node<K, V> successor = minimum(node);
      successor.right = removeMin(node);
      successor.left = node.left;
      node.left = node.right = null;
      return successor;
    }
    return node;
  }

}
