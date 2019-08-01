package com.datastructure;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-08-01
 */
public class _3_LinkList {

    //链表
    public static class LinkList<E>{
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

        // 初始化一个空节点作为头结点
        private Node dummyHead;
        private int size;

        public LinkList(){
            dummyHead = new Node();
            size = 0;
        }

        public boolean isEmpty(){
            return size == 0;
        }

        public int getSize(){
            return size;
        }

        public void add(int index, E e){
            if (index < 0 || index > size)
                throw new RuntimeException("下标问题");

            Node prev = dummyHead;
            for (int i=0; i<index; i++){
                prev = prev.next;
            }
            prev.next = new Node(e, prev.next);
            size++;
        }

        public void addFirst(E e){
            add(0, e);
        }

        public void addLast(E e){
            add(size, e);
        }

        public E get(int index){
            if (index < 0 || index > size)
                throw new RuntimeException("下标问题");

            Node cur = dummyHead.next;
            for (int i=0; i<size; i++){
                cur = cur.next;
            }
            return cur.e;
        }

        public E getFirst(){
            return get(0);
        }

        public E getLast(){
            return get(size - 1);
        }

        public void set(int index, E e){
            if (index < 0 || index >= size)
                throw new RuntimeException("下标问题");
            Node cur = dummyHead.next;
            for (int i=0; i<size; i++){
                cur = cur.next;
            }
            cur.e = e;
        }

        public E remove(int index){
            if (index < 0 || index >= size)
                throw new RuntimeException("下标问题");
            Node prev = dummyHead;
            for (int i=0; i<index; i++){
                prev = prev.next;
            }
            Node cur = prev.next;
            prev.next = cur.next;
            cur.next = null;
            size--;
            return cur.e;
        }

        public E removeFirst(){
            return remove(0);
        }

        public E removeLast(){
            return remove(size - 1);
        }

        @Override
        public String toString(){
            StringBuilder res = new StringBuilder();

            Node cur = dummyHead.next;
            while(cur != null){
                res.append(cur + "->");
                cur = cur.next;
            }
            res.append("NULL");

            return res.toString();
        }

        public static void main(String[] args) {

            LinkList<Integer> linkedList = new LinkList<>();
            for(int i = 0 ; i < 5 ; i ++){
                linkedList.addFirst(i);
                System.out.println(linkedList);
            }

            linkedList.add(2, 666);
            System.out.println(linkedList);

            linkedList.remove(2);
            System.out.println(linkedList);

            linkedList.removeFirst();
            System.out.println(linkedList);

            linkedList.removeLast();
            System.out.println(linkedList);
        }

    }

}

























