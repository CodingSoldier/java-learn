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

    // 基于循环数组实现队列
    static class LoopQueue<E>{
        private E[] data;  // 数组
        private int front, tail;  //队头、队尾
        private int size;  //队列元素大小

        public LoopQueue(){
            data = (E[]) new Object[11];  //初始化队列的数组
            // 初始化时队头、队尾下标都为0，队列大小也是0
            front = 0;
            tail = 0;
            size = 0;
        }

        // 队列容量是数组长度减1，因为要空一个元素，以便区别队列是否为空、队列是否满了需要扩容
        public int getCapacity(){
            return data.length -1;
        }

        //队列为空
        public boolean isEmpty(){
            return front == tail;
        }

        //队列大小
        public int getSize(){
            return size;
        }

        //扩容、缩容
        private void resize(int newCapacity){
            // 数组长度要比容量多一个
            E[] newData = (E[])new Object[newCapacity+1];
            //将旧数据从队头开始，添加到新数组中
            for (int i=0; i<size; i++){
                newData[i] = data[(i+front) % data.length];
            }
            front = 0;  //添加完成，队头在新数组下标为0的位置
            tail = size;  //添加完成，队列大小就是队尾的下标
            data = newData;  //队列的数组data指向扩容后的数组
        }

        //入队
        public void enqueue(E e){
            //如果队列满了，则扩容
            if ((tail + 1) % data.length == front){
                resize(getCapacity() * 2);
            }
            data[tail] = e;
            //队尾下标移动
            tail = (tail + 1) % data.length;
            //队列大小加一
            size++;
        }

        //出队
        public E dequeue(){
            if (isEmpty())
                throw new RuntimeException("没数据了");
            //队头数据存储到变量中
            E ret = data[front];
            data[front] = null;
            //队头下标移动
            front = (front + 1) % data.length;
            //队列大小减一
            size--;

            //这是缩容，如果队列大小小于容量的1/4且容量的1/2不为0，队列缩容为原来的1/2
            if (size < getCapacity()/4 && getCapacity()/2 != 0){
                resize(getCapacity()/2);
            }
            //返回队头数据
            return ret;
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

                if(i % 8 == 2){
                    queue.dequeue();
                    System.out.println(queue);
                }
            }
        }

    }





}

























