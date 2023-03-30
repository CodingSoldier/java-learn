package p03_linked.p01_linked;

/**
 * 链表
 */
public class LinkedList<E> {

  private class Node<E> {
    private E e;
    private Node<E> next;

    public Node() {
      this(null, null);
    }

    public Node(E e, Node<E> next) {
      this.e = e;
      this.next = next;
    }

    public E getE() {
      return e;
    }

    public void setE(E e) {
      this.e = e;
    }

    public Node<E> getNext() {
      return next;
    }

    public void setNext(Node<E> next) {
      this.next = next;
    }
  }

  private Node<E> dummyHead;
  private int size;

  public LinkedList() {
    this.dummyHead = new Node();
    this.size = 0;
  }

  public int getSize() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public void add(int index, E e) {
    if (index < 0 || index > size) {
      throw new IllegalArgumentException("非法参数异常");
    }
    Node prev = dummyHead;
    for (int i=0; i<index; i++) {
      prev = prev.next;
    }
    prev.next = new Node(e, prev.next);
    size++;
  }

  public void addFirst(E e) {
    add(0, e);
  }

  public void addLast(E e) {
    add(size, e);
  }

  public E get(int index) {
    if (index < 0 || index >= size) {
      throw new IllegalArgumentException("非法参数异常");
    }
    Node<E> prev = dummyHead;
    for (int i = 0; i < index; i++) {
      prev = prev.next;
    }
    return prev.getNext().getE();
  }

  public E getFirst() {
    return get(0);
  }

  public E getLast() {
    return get(size - 1);
  }

  public void set(int index, E e) {
    if (index < 0 || index >= size) {
      throw new IllegalArgumentException("非法参数异常");
    }
    Node prev = dummyHead;
    for (int i=0; i<size; i++) {
      prev = prev.next;
    }
    prev.next.setE(e);
  }

  public boolean contains(E e) {
    Node prev = dummyHead;
    while (prev.next != null) {
      if (e.equals(prev.next.getE())) {
        return true;
      }
    }
    return false;
  }

  public E remove(int index) {
    if (index < 0 || index >= size) {
      throw new IllegalArgumentException("非法参数异常");
    }
    Node<E> prev = dummyHead;
    for (int i=0; i<index; i++) {
      prev = prev.next;
    }
    Node<E> cur = prev.next;
    E e = cur.getE();
    prev.next = cur.next;
    cur.next = null;
    size--;
    return e;
  }

  public E removeFirst() {
    return remove(0);
  }

  public E removeLast() {
    return remove(size-1);
  }

  public boolean removeElement(E e) {
    if (size == 0) {
      return false;
    }
    Node<E> prev = dummyHead;
    while (prev.next != null) {
      if (prev.next.getE().equals(e)) {
        prev.next = prev.next.next;
        size--;
        return true;
      } else {
        prev = prev.next;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("LinkedList: ");
    Node<E> prev = dummyHead;
    while (prev.next != null) {
      prev = prev.next;
      sb.append(prev.getE());
      sb.append(" -> ");
    }
    sb.append("NULL");
    return sb.toString();
  }

  public static void main(String[] args) {

    LinkedList<Integer> linkedList = new LinkedList<>();
    for(int i = 0 ; i < 5 ; i ++){
      linkedList.addFirst(i);
      System.out.println(linkedList);
    }

    linkedList.add(2, 666);
    System.out.println(linkedList);

    linkedList.remove(2);
    System.out.println(linkedList);

    linkedList.removeFirst();
    System.out.println(linkedList);

    linkedList.removeLast();
    System.out.println(linkedList);

    linkedList.removeElement(3);
    System.out.println(linkedList);
  }

}
