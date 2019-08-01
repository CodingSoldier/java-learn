package com.datastructure;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-08-01
 */
public class _3_LinkListQueue {

    //使用链表实现一个单项队列
    public static class LinkListQueue<E>{
        // 链表节点
        private class Node{
            public E e;
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

        private Node head, tail;
        private int size;

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

        public void enqueue(E e){
            if (tail == null){
                tail = new Node(e);
                head = tail;
            }else {
                tail.next = new Node(e);
                tail = tail.next;
            }
            size++;
        }

        public E dequeue(){
            if (isEmpty())
                throw new RuntimeException("下标问题");

            Node cur = head;
            head = head.next;
            cur.next = null;

            if (head.next == null)
                tail = null;

            size--;
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

                if(i % 3 == 2){
                    queue.dequeue();
                    System.out.println(queue);
                }
            }
        }

    }

}

























