package p03_linked.p01_linked;

/**
 * 链表实现队列 与 数组实现队列 的区别：
 *    数组实现队列需要“浪费一个元素”，array.length - 1 = 队列的size()
 *    队列不需要“浪费一个元素”
 */
public class LinkedListQueue<E> implements Queue<E>{

  private class Node<E> {
    private E e;
    private Node<E> next;

    public Node(E e) {
      this(e, null);
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

  private int size;
  private Node<E> head;
  private Node<E> tail;

  public LinkedListQueue() {
    this.size = 0;
    head=null;
    tail=null;
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
  public void enqueue(E e) {
    if(tail == null){
      tail = new Node(e);
      head = tail;
    }
    else{
      tail.next = new Node(e);
      tail = tail.next;
    }
    size ++;
  }

  @Override
  public E dequeue() {
    if (isEmpty()) {
      throw new IllegalArgumentException("已无数据");
    }
    Node<E> cur = head;
    head = head.next;
    cur.next= null;
    if (head==null) {
      tail=null;
    }
    size--;
    return cur.e;
  }

  @Override
  public E getFront() {
    if (isEmpty()) {
      throw new IllegalArgumentException("已无数据");
    }
    return head.getE();
  }

  @Override
  public String toString(){
    StringBuilder res = new StringBuilder();
    res.append("Queue: front ");

    Node<E> cur = head;
    while (cur.next != null) {
      res.append(cur.e);
      res.append(" -> ");
      cur = cur.next;
    }
    res.append("NULL tail");
    return res.toString();
  }

  public static void main(String[] args){

    LinkedListQueue<Integer> queue = new LinkedListQueue<>();
    for(int i = 0 ; i < 10 ; i ++){
      queue.enqueue(i);
      System.out.println("入队 " + queue);

      if(i % 3 == 2){
        Integer dequeue = queue.dequeue();
        System.out.println("出队 " + dequeue);
        System.out.println("出队 " + queue);
      }
    }
  }

}
