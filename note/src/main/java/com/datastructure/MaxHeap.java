package com.datastructure;

import java.util.Random;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-08-08
 */
public class MaxHeap<E extends Comparable<E>>{

    private Array<E> data;

    public MaxHeap(int capacity){
        data = new Array<>(capacity);
    }

    public MaxHeap(){
        data = new Array<>();
    }

    // 返回堆中的元素个数
    public int size(){
        return data.getSize();
    }

    // 返回一个布尔值, 表示堆中是否为空
    public boolean isEmpty(){
        return data.isEmpty();
    }

    // 父亲节点下标
    private int parent(int index){
        if(index == 0)
            throw new IllegalArgumentException("index-0 doesn't have parent.");
        return (index - 1) / 2;
    }

    // 左孩子下标
    private int leftChild(int index){
        return index * 2 + 1;
    }

    // 右孩子下标
    private int rightChild(int index){
        return index * 2 + 2;
    }


    // 添加元素
    public void add(E e){
        // 在数组末尾添加元素
        data.addLast(e);
        // 数组末尾元素和父节点元素比较，可能会互换位置
        siftUp(data.getSize() - 1);
    }

    // 数组末尾元素和父节点元素比较，可能会互换位置
    private void siftUp(int k){
        // 父亲元素比当前元素大
        while (k > 0 && data.get(parent(k)).compareTo(data.get(k)) < 0){
            // 当前元素和父亲元素互换位置
            data.swap(k, parent(k));
            // 设置下标k为parent(k)，继续循环直到退出循环
            k = parent(k);
        }
    }

    public E findMax(){
        if (data.getSize() == 0)
            throw new RuntimeException("没元素了");
        return data.get(0);
    }

    private void siftDown(int k){
        while (leftChild(k) < data.getSize()){
            // 获取左孩子下标
            int j = leftChild(k);
            // j+1即为右孩子下标，比较左右孩子的大小
            if (j+1 < data.getSize() && data.get(j+1).compareTo(data.get(j)) > 0)
                // 若右孩子大，则将j设置为右孩子下边
                j++;

            // 当前元素大于左右孩子的最大值，退出循环
            if (data.get(k).compareTo(data.get(j)) >= 0)
                break;

            // 当前元素和最大的左右孩子互换下标
            data.swap(k, j);
            // 元素换到了下标j的位置，所以k要设置为j
            k = j;
        }
    }

    public E extractMax(){
        E ret = findMax();   // 查找最大值
        data.swap(0, data.getSize() -1);   // 数组头尾元素互换
        data.removeLast();    // 删除数组最后一个元素
        siftDown(0);   // 从下标为0的元素与左右孩子比较，换位置
        return ret;
    }

    public static void main(String[] args) {

        int n = 1000000;

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

