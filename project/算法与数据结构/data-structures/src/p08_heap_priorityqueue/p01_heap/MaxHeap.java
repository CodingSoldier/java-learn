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

  public MaxHeap(E[] arr) {
    this.data = new Array<>(arr);
    for (int i = parentIndex(data.getSize() - 1); i >= 0; i--) {
      siftDown(i);
    }
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
    if (childIndex + 1 > data.getSize() -1) {
      return;
    }
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

  public E replace(E elem) {
    E max = findMax();
    data.addFirst(elem);
    siftDown(0);
    return max;
  }

  public static void main(String[] args) {


    //int n = 100000;
    //
    //MaxHeap<Integer> maxHeap = new MaxHeap<>();
    //Random random = new Random();
    //for(int i = 0 ; i < n ; i ++)
    //  maxHeap.add(random.nextInt(Integer.MAX_VALUE));
    //
    //int[] arr = new int[n];
    //for(int i = 0 ; i < n ; i ++)
    //  arr[i] = maxHeap.extractMax();
    //
    //for(int i = 1 ; i < n ; i ++)
    //  if(arr[i-1] < arr[i])
    //    throw new IllegalArgumentException("Error");
    //
    //System.out.println("Test MaxHeap completed.");

    int n = 10;

    Random random = new Random();
    Integer[] testData = new Integer[n];
    for(int i = 0 ; i < n ; i ++)
      testData[i] = random.nextInt(Integer.MAX_VALUE);

    double time1 = testHeap(testData, false);
    System.out.println("Without heapify: " + time1 + " s");

    double time2 = testHeap(testData, true);
    System.out.println("With heapify: " + time2 + " s");
  }

  private static double testHeap(Integer[] testData, boolean isHeapify){

    long startTime = System.nanoTime();

    MaxHeap<Integer> maxHeap;
    if(isHeapify)
      maxHeap = new MaxHeap<>(testData);
    else{
      maxHeap = new MaxHeap<>();
      for(int num: testData)
        maxHeap.add(num);
    }

    int[] arr = new int[testData.length];
    for(int i = 0 ; i < testData.length ; i ++)
      arr[i] = maxHeap.extractMax();

    for(int i = 1 ; i < testData.length ; i ++)
      if(arr[i-1] < arr[i])
        throw new IllegalArgumentException("Error");
    System.out.println("Test MaxHeap completed.");

    long endTime = System.nanoTime();

    return (endTime - startTime) / 1000000000.0;
  }

}
