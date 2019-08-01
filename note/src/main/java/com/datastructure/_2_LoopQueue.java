package com.datastructure;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-08-01
 */
public class _2_LoopQueue {


    /**
     * 循环队列
     * 循环队列的出队（弹出第一个元素）,不需要移动元素下标，均摊复杂度是 O(1)
     * 数组队列的出队（弹出第一个元素），需要移动所有元素下标，均摊复杂度是 O(n)
     */
    static class LoopQueue<E>{
        private E[] data;
        private int front, tail;
        private int size;

        public LoopQueue(){
            data = (E[]) new Object[11];
            front = 0;
            tail = 0;
            size = 0;
        }

        public int getCapacity(){
            return data.length -1;
        }

        public boolean isEmpty(){
            return front == tail;
        }

        public int getSize(){
            return size;
        }

        public void enqueue(E e){
            if ((tail + 1) % data.length == front){
                resize(getCapacity() * 2);
            }
            data[tail] = e;
            tail = (tail + 1) % data.length;
            size++;
        }

        public E dequeue(){
            if (isEmpty())
                throw new RuntimeException("没数据了");
            E ret = data[front];
            data[front] = null;
            front = (front + 1) % data.length;
            size--;
            if (size < getCapacity()/4 && getCapacity()/2 != 0){
                resize(getCapacity()/2);
            }
            return ret;
        }

        private void resize(int newCapacity){
            E[] newData = (E[])new Object[newCapacity+1];
            for (int i=0; i<size; i++){
                newData[i] = data[(i+front) % data.length];
            }
            front = 0;
            tail = size;
            data = newData;
        }

        @Override
        public String toString(){

            StringBuilder res = new StringBuilder();
            res.append(String.format("Queue: size = %d , capacity = %d\n", size, getCapacity()));
            res.append("front [");
            for(int i = front ; i != tail ; i = (i + 1) % data.length){
                res.append(data[i]);
                if((i + 1) % data.length != tail)
                    res.append(", ");
            }
            res.append("] tail");
            return res.toString();
        }

        public static void main(String[] args){

            LoopQueue<Integer> queue = new LoopQueue<>();
            for(int i = 0 ; i < 20 ; i ++){
                queue.enqueue(i);
                System.out.println(queue);

                if(i % 3 == 2){
                    queue.dequeue();
                    System.out.println(queue);
                }
            }
        }

    }





}

























