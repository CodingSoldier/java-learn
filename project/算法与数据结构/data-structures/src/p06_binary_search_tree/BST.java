package p06_binary_search_tree;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author chenpq05
 * @since 2023/4/6 15:05
 */
public class BST<E extends Comparable<E>> {

  private class Node<E> {
    private E e;
    private Node<E> left, right;

    public Node(E e) {
      this.e = e;
    }

  }

  private Node<E> root;
  private int size;

  public int size(){
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public void add(E e) {
    root = add(root, e);
  }

  private Node<E> add(Node<E> node, E e) {
    if (node == null) {
      size++;
      return new Node<>(e);
    }

    if (e.compareTo(node.e) < 0) {
      node.left = add(node.left, e);
    } else if (e.compareTo(node.e) > 0){
      node.right = add(node.right, e);
    }
    return node;
  }

  public boolean contains(E e) {
    return contains(root, e);
  }

  private boolean contains(Node<E> node, E e) {
    if (node == null) {
      return false;
    }
    if (e.equals(node.e)) {
      return true;
    } else if (e.compareTo(node.e) < 0) {
      return contains(node.left, e);
    } else {
      return contains(node.right, e);
    }
  }

  public void preOrder() {
    preOrder(root);
  }

  private void preOrder(Node<E> node) {
    if (node == null) {
      return;
    }
    System.out.println(node.e);
    preOrder(node.left);
    preOrder(node.right);
  }

  public void preOrderNR() {
    if (root == null) {
      return;
    }
    Stack<Node<E>> stack = new Stack();
    stack.push(root);
    while (!stack.isEmpty()) {
      Node<E> node = stack.pop();
      System.out.println(node.e);
      if (node.right != null) {
        stack.push(node.right);
      }
      if (node.left != null) {
        stack.push(node.left);
      }
    }
  }

  public void inOrder() {
    inOrder(root);
  }

  private void inOrder(Node<E> node) {
    if (node == null) {
      return;
    }
    inOrder(node.left);
    System.out.println(node.e);
    inOrder(node.right);
  }

  public void postOrder() {
    postOrder(root);
  }

  private void postOrder(Node<E> node) {
    if (node == null) {
      return;
    }
    postOrder(node.left);
    postOrder(node.right);
    System.out.println(node.e);
  }

  public void levelOrder() {
    if (root == null) {
      return;
    }
    Queue<Node<E>> queue = new LinkedBlockingQueue<>();
    queue.add(root);
    while (!queue.isEmpty()) {
      Node<E> node = queue.poll();
      System.out.println(node.e);

      if (node.left != null) {
        queue.add(node.left);
      }
      if (node.right != null) {
        queue.add(node.right);
      }
    }
  }

  public E minimum() {
    if (root == null) {
      return null;
    }
    Node<E> min = minimum(root);
    return min.e;
  }

  private Node<E> minimum(Node<E> node) {
    if (node.left == null) {
      return node;
    }
    return minimum(node.left);
  }

  public E maximum() {
    if (root == null) {
      return null;
    }
    Node<E> max = maximum(root);
    return max.e;
  }

  private Node<E> maximum(Node<E> node) {
    if (node.right == null) {
      return node;
    }
    return maximum(node.right);
  }

  public E removeMin() {
    if (root == null) {
      return null;
    }
    E min = minimum();
    root = removeMin(root);
    return min;
  }

  private Node<E> removeMin(Node<E> node) {
    if (node.left == null) {
      Node<E> rightNode = node.right;
      node.right = null;
      size--;
      return rightNode;
    }
    node.left = removeMin(node.left);
    return node;
  }

  public E removeMax() {
    if (root == null) {
      return null;
    }
    E max = maximum();
    root = removeMax(root);
    return max;
  }

  private Node<E> removeMax(Node<E> node) {
    if (node.right == null) {
      Node<E> leftNode = node.left;
      node.left = null;
      size--;
      return leftNode;
    }
    node.right = removeMax(node.right);
    return node;
  }

  public void remove(E e) {
    root = remove(root, e);
  }

  private Node<E> remove(Node<E> node, E e) {
    if (node == null) {
      return null;
    }
    if (e.compareTo(node.e) < 0) {
      node.left = remove(node.left, e);
      return node;
    } else if (e.compareTo(node.e) > 0) {
      node.right = remove(node.right, e);
      return node;
    } else {
      if (node.left == null) {
        Node<E> rightNode = node.right;
        node.right = null;
        size--;
        return rightNode;
      } else if (node.right == null) {
        Node<E> leftNode = node.left;
        node.left = null;
        size--;
        return leftNode;
      } else {
        Node<E> rightMin = minimum(node.right);
        rightMin.right = removeMin(node.right);
        rightMin.left = node.left;
        node.left = node.right = null;
        return rightMin;
      }
    }
  }

  public static void main(String[] args) {

    //BST<Integer> bst = new BST<>();
    //int[] nums = {5, 3, 6, 8, 4, 2};
    //for(int num: nums) {
    //  bst.add(num);
    //}
    //
    ///////////////////
    ////      5      //
    ////    /   \    //
    ////   3    6    //
    ////  / \    \   //
    //// 2  4     8  //
    ///////////////////
    //System.out.println("前序遍历");
    //bst.preOrder();
    //System.out.println("前序遍历--非递归");
    //bst.preOrderNR();
    //
    //System.out.println("中序遍历");
    //bst.inOrder();
    //
    //System.out.println("后序遍历");
    //bst.postOrder();
    //
    //System.out.println("层序遍历");
    //bst.levelOrder();


    BST<Integer> bstRemove = new BST<>();
    Random random = new Random();

    int n = 1000;

    // test removeMin
    for(int i = 0 ; i < n ; i ++) {
      bstRemove.add(random.nextInt(10000));
    }

    ArrayList<Integer> nums = new ArrayList<>();
    while(!bstRemove.isEmpty()) {
      nums.add(bstRemove.removeMin());
    }

    System.out.println(nums);
    for(int i = 1 ; i < nums.size() ; i ++) {
      if(nums.get(i - 1) > nums.get(i)) {
        throw new IllegalArgumentException("Error!");
      }
    }

    System.out.println("removeMin test completed.");

    // test removeMax
    for(int i = 0 ; i < n ; i ++) {
      bstRemove.add(random.nextInt(10000));
    }

    nums = new ArrayList<>();
    while(!bstRemove.isEmpty()) {
      nums.add(bstRemove.removeMax());
    }

    System.out.println(nums);

    for(int i = 1 ; i < nums.size() ; i ++) {
      if(nums.get(i - 1) < nums.get(i)) {
        throw new IllegalArgumentException("Error!");
      }
    }

    System.out.println("removeMax test completed.");
  }

}
