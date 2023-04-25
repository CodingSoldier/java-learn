package p08_heap_priorityqueue.p01_heap;

import java.util.Random;

/**
 * @author chenpq05
 * @since 2023/4/25 16:26
 */

public class MaxHeap<E extends Comparable<E>> {

  private Array<E> data;
  private int size;

  public MaxHeap() {
    this.data = new Array<>();
    this.size = 0;
  }

  public int getSize() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  private int parentIndex(int i) {
    return (i - 1)/2;
  }

  private int leftChildIndex(int i) {
    return 2*i + 1;
  }

  private int rightChildIndex(int i) {
    return 2*i + 2;
  }

  private void siftUp(int i) {
    if (i < 0) {
      throw new RuntimeException("下标不能小于0");
    }
    while (i > 0 && data.get(parentIndex(i)).compareTo(data.get(i)) < 0) {
      data.swap(parentIndex(i), i);
      i = parentIndex(i);
    }
  }

  private void siftDown(int i) {
    if (i < 0) {
      throw new RuntimeException("下标不能小于0");
    }
    // 左孩子下标
    int childIndex = leftChildIndex(i);
    // 最大孩子的下标
    int bigChildIndex = data.get(childIndex).compareTo(data.get(childIndex + 1)) >= 0
        ? childIndex : childIndex + 1;
    // 当前节点小于最大的孩子
    while (data.get(i).compareTo(data.get(bigChildIndex)) < 0) {
      // 当前节点、最大孩子 交换
      data.swap(i, bigChildIndex);
      i = bigChildIndex;

      // 再次找最大的左右孩子
      childIndex = leftChildIndex(i);
      if (childIndex + 1 >= data.getSize()) {
        break;
      }
      bigChildIndex = data.get(childIndex).compareTo(data.get(childIndex + 1)) >= 0
          ? childIndex : childIndex + 1;
    }
  }

  public void add(E elem) {
    data.addLast(elem);
    siftUp(data.getSize() - 1);
  }

  public E findMax() {
    return data.get(0);
  }

  public E extractMax() {
    E max = findMax();
    data.addFirst(data.removeLast());
    siftDown(0);
    return max;
  }

  public static void main(String[] args) {
    int n = 100000;

    MaxHeap<Integer> maxHeap = new MaxHeap<>();
    Random random = new Random();
    for(int i = 0 ; i < n ; i ++)
      maxHeap.add(random.nextInt(Integer.MAX_VALUE));

    int[] arr = new int[n];
    for(int i = 0 ; i < n ; i ++)
      arr[i] = maxHeap.extractMax();

    for(int i = 1 ; i < n ; i ++)
      if(arr[i-1] < arr[i])
        throw new IllegalArgumentException("Error");

    System.out.println("Test MaxHeap completed.");
  }

}
