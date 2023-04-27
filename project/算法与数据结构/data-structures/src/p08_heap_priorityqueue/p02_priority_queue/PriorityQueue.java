package p08_heap_priorityqueue.p02_priority_queue;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * @author chenpq05
 * @since 2023/4/27 15:24
 */
public class PriorityQueue<E extends Comparable<E>> implements Queue<E> {

  private MaxHeap<E> data;

  public PriorityQueue() {
    this.data = new MaxHeap<>();
  }

  @Override
  public int getSize() {
    return data.size();
  }

  @Override
  public boolean isEmpty() {
    return data.isEmpty();
  }

  @Override
  public void enqueue(E e) {
    data.add(e);
  }

  @Override
  public E dequeue() {
    return data.extractMax();
  }

  @Override
  public E getFront() {
    return data.findMax();
  }
}

class Solution {

  public static int[] topKFrequent(int[] nums, int k) {
    // map存储 元素、元素出现次数 的映射关系
    HashMap<Integer, Integer> map = new HashMap<>();
    for (int i=0; i<nums.length; i++) {
      if (map.get(nums[i]) == null) {
        map.put(nums[i], 1);
      } else {
        map.put(nums[i], map.get(nums[i]) + 1);
      }
    }

    // 创建队列，出现频率小的元素排在前面
    java.util.PriorityQueue<Integer> queue = new java.util.PriorityQueue<>((o1, o2) -> map.get(o1) - map.get(o2));
    for (Integer key:map.keySet()) {
      if (queue.size() < k) { // 队列不满，将元素放到队列
        queue.add(key);
      } else if (map.get(queue.peek()) < map.get(key)) {
        // 队列满了，如果排在 最前面的元素出现次数 比 当前的元素出现次数 小，则出队，并将当前元素入队
        queue.poll();
        queue.add(key);
      }
    }

    // 转成数组返回
    int[] r = new int[queue.size()];
    for (int i=0; i<r.length; i++) {
      r[i] = queue.poll();
    }
    return r;
  }

  public static void main(String[] args) {
    int[] nums = {4, 1, -1, 2, -1, 2, 3};
    int[] ints = topKFrequent(nums, 2);

  }

}
