package com.datastructure;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-08-01
 */
public class _3_LinkListQueue {

    //使用链表实现一个队列
    public static class LinkListQueue<E>{
        // 链表节点
        private class Node{
            // 当前节点值
            public E e;
            // 当前节点的next属性指向下一个节点
            public Node next;

            public Node(E e, Node next){
               this.e = e;
               this.next = next;
            }
            public Node(E e){
               this.e = e;
               this.next = null;
            }
            public Node(){
               this.e = null;
               this.next = null;
            }

            @Override
            public String toString(){
                return e.toString();
            }
        }

        private Node head, tail;  //队首、队尾
        private int size;  //队列大小

        // 初始化队列
        public LinkListQueue(){
            head = null;
            tail = null;
            size = 0;
        }

        public int getSize(){
            return size;
        }

        public boolean isEmpty(){
            return size == 0;
        }

        // 入队
        public void enqueue(E e){
            // tail == null  队列中没有元素
            if (tail == null){
                tail = new Node(e);
                head = tail;
            }else {
                // 队列中已经有元素了，末尾元素的next指向一个新建的Node节点
                tail.next = new Node(e);
                // 队列类中维护的tail属性（队尾）也要指向新建的节点
                tail = tail.next;
            }
            size++;  // 队列大小加一
        }

        //出队
        public E dequeue(){
            if (isEmpty())
                throw new RuntimeException("队列空了亲");

            //获取队首节点
            Node cur = head;
            // 队列类中维护的head属性（队首）指向下一节点
            head = head.next;
            // 删除出队节点的引用，以便java虚拟机回收内存空间
            cur.next = null;

            // 队首指向null，表明队列中没有元素了
            if (head == null)
                tail = null;

            size--;   // 队列大小减一
            // 返回队首节点值
            return cur.e;
        }

        @Override
        public String toString(){
            StringBuilder res = new StringBuilder();
            res.append("Queue: front ");

            Node cur = head;
            while(cur != null) {
                res.append(cur + "->");
                cur = cur.next;
            }
            res.append("NULL tail");
            return res.toString();
        }

        public static void main(String[] args){

            LinkListQueue<Integer> queue = new LinkListQueue<>();
            for(int i = 0 ; i < 10 ; i ++){
                queue.enqueue(i);
                System.out.println(queue);

                if(i % 3 == 0){
                    queue.dequeue();
                    System.out.println(queue);
                }
            }
            for(int i = 0 ; i < 7 ; i ++){
                queue.dequeue();
                System.out.println(queue);
            }
        }

    }

}

























