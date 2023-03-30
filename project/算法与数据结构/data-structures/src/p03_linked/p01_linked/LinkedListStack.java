package p03_linked.p01_linked;

import p02_stack_and_queues.p02_array_stack.Stack;

/**
 * 使用链表实现栈
 */
public class LinkedListStack<E> implements Stack<E> {

  private LinkedList<E> linkedList;

  public LinkedListStack() {
    this.linkedList = new LinkedList<>();
  }

  @Override
  public int getSize() {
    return this.linkedList.getSize();
  }

  @Override
  public boolean isEmpty() {
    return this.linkedList.isEmpty();
  }

  @Override
  public void push(E e) {
    this.linkedList.addFirst(e);
  }

  @Override
  public E pop() {
    return this.linkedList.removeFirst();
  }

  @Override
  public E peek() {
    return this.linkedList.getFirst();
  }

  @Override
  public String toString(){
    StringBuilder res = new StringBuilder();
    res.append("Stack: top ");
    res.append(this.linkedList);
    return res.toString();
  }

  public static void main(String[] args) {
    LinkedListStack<Integer> stack = new LinkedListStack<>();
    for(int i = 0 ; i < 5 ; i ++){
      stack.push(i);
      System.out.println(stack);
    }
    stack.pop();
    System.out.println(stack);
  }

}
