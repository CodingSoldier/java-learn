package com.datastructure;

import java.util.Random;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-08-08
 */
public class _6_MaxHeap1 {

    static public class Array<E> {

        private E[] data;
        private int size;

        // 构造函数，传入数组的容量capacity构造Array
        public Array(int capacity){
            data = (E[])new Object[capacity];
            size = 0;
        }

        // 无参数的构造函数，默认数组的容量capacity=10
        public Array(){
            this(10);
        }

        // 获取数组中的元素个数
        public int getSize(){
            return size;
        }

        // 返回数组是否为空
        public boolean isEmpty(){
            return size == 0;
        }

        // 在index索引的位置插入一个新元素e
        public void add(int index, E e){

            if(index < 0 || index > size)
                throw new IllegalArgumentException("Add failed. Require index >= 0 and index <= size.");

            if(size == data.length)
                resize(2 * data.length);

            for(int i = size - 1; i >= index ; i --)
                data[i + 1] = data[i];

            data[index] = e;

            size ++;
        }

        // 向所有元素后添加一个新元素
        public void addLast(E e){
            add(size, e);
        }

        // 获取index索引位置的元素
        public E get(int index){
            if(index < 0 || index >= size)
                throw new IllegalArgumentException("Get failed. Index is illegal.");
            return data[index];
        }

        // 修改index索引位置的元素为e
        public void set(int index, E e){
            if(index < 0 || index >= size)
                throw new IllegalArgumentException("Set failed. Index is illegal.");
            data[index] = e;
        }

        // 从数组中删除index位置的元素, 返回删除的元素
        public E remove(int index){
            if(index < 0 || index >= size)
                throw new IllegalArgumentException("Remove failed. Index is illegal.");

            E ret = data[index];
            for(int i = index + 1 ; i < size ; i ++)
                data[i - 1] = data[i];
            size --;
            data[size] = null; // loitering objects != memory leak

            if(size == data.length / 4 && data.length / 2 != 0)
                resize(data.length / 2);
            return ret;
        }


        // 从数组中删除最后一个元素, 返回删除的元素
        public E removeLast(){
            return remove(size - 1);
        }


        public void swap(int i, int j){

            if(i < 0 || i >= size || j < 0 || j >= size)
                throw new IllegalArgumentException("Index is illegal.");

            E t = data[i];
            data[i] = data[j];
            data[j] = t;
        }

        @Override
        public String toString(){

            StringBuilder res = new StringBuilder();
            res.append(String.format("Array: size = %d , capacity = %d\n", size, data.length));
            res.append('[');
            for(int i = 0 ; i < size ; i ++){
                res.append(data[i]);
                if(i != size - 1)
                    res.append(", ");
            }
            res.append(']');
            return res.toString();
        }

        // 将数组空间的容量变成newCapacity大小
        private void resize(int newCapacity){

            E[] newData = (E[])new Object[newCapacity];
            for(int i = 0 ; i < size ; i ++)
                newData[i] = data[i];
            data = newData;
        }
    }




    static public class MaxHeap<E extends Comparable<E>>{

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
                    // 若右孩子大，则将j设置为右孩子下标
                    j++;

                // 当前元素大于最大的左右孩子，退出循环
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

    }






    public static void main(String[] args) {

        int n = 10000;

        MaxHeap<Integer> maxHeap = new MaxHeap<>();
        Random random = new Random();
        for(int i = 0 ; i < n ; i ++)
            maxHeap.add(random.nextInt(10000));

        int[] arr = new int[n];
        for(int i = 0 ; i < n ; i ++){
            arr[i] = maxHeap.extractMax();
            System.out.println(arr[i]);
        }

        for(int i = 1 ; i < n ; i ++)
            if(arr[i-1] < arr[i])
                throw new IllegalArgumentException("Error");

        System.out.println("Test MaxHeap completed.");
    }


}
