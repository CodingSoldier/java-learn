package com.datastructure_arithmetic_2;

public class E_Linked {

    static public class ListNode {
        public int val;
        public ListNode next;
        public ListNode(int x) { val = x; }

        // 根据n个元素的数组arr创建一个链表
        // 使用arr为参数，创建另外一个ListNode的构造函数
        public ListNode (int[] arr){

            if(arr == null || arr.length == 0)
                throw new IllegalArgumentException("arr can not be empty");

            this.val = arr[0];
            ListNode curNode = this;
            for(int i = 1 ; i < arr.length ; i ++){
                curNode.next = new ListNode(arr[i]);
                curNode = curNode.next;
            }
        }

        // 返回以当前ListNode为头结点的链表信息字符串
        @Override
        public String toString(){

            StringBuilder s = new StringBuilder("");
            ListNode curNode = this;
            while(curNode != null){
                s.append(Integer.toString(curNode.val));
                s.append(" -> ");
                curNode = curNode.next;
            }
            s.append("NULL");
            return s.toString();
        }
    }
    /**
     * TODO
     * 5-1 链表，在节点间穿针引线 Reverse Linked List
     * https://leetcode-cn.com/problems/reverse-linked-list/
     * 反转一个单链表。
     */
    static class Solution206 {
        public ListNode reverseList(ListNode head) {
            ListNode pre = null;
            ListNode current = head;
            while (current != null){
                ListNode next = current.next;
                current.next = pre;
                pre = current;
                current = next;
            }
            return pre;
        }
    }


    /**
     * 5-3 设立链表的虚拟头结点 Remove Linked List Elements
     * https://leetcode-cn.com/problems/remove-linked-list-elements/
     * 删除链表中等于给定值 val 的所有节点。
     *
     */
    static class Solution203 {
        public ListNode removeElements(ListNode head, int val) {
            ListNode dummmyHead = new ListNode(0);
            dummmyHead.next = head;
            ListNode current = dummmyHead;
            while (current.next != null){
                if (current.next.val == val){
                    current.next = current.next.next;
                }else {
                    current = current.next;
                }
            }
            return dummmyHead.next;
        }
    }


    /**
     * 5-4 复杂的穿针引线 Swap Nodes in Pairs
     * https://leetcode-cn.com/problems/swap-nodes-in-pairs/
     */
    static class Solution24 {
        static public ListNode swapPairs(ListNode head) {
            ListNode dummyHead = new ListNode(0);
            dummyHead.next = head;
            ListNode d = dummyHead;
            while (d.next != null && d.next.next != null){
                ListNode node1 = d.next;
                ListNode node2 = d.next.next;
                ListNode next = d.next.next.next;

                node2.next = node1;
                node1.next = next;
                d.next = node2;
                d = node1;

            }
            return dummyHead.next;
        }

        public static void main(String[] args) {
            int[] arr = {1, 2, 3, 4};
            ListNode listNode = new ListNode(arr);
            swapPairs(listNode);
        }
    }


}
