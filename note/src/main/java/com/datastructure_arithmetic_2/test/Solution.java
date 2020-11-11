package com.datastructure_arithmetic_2.test;


public class Solution{
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null){
            return head;
        }
        ListNode pre = null;
        ListNode current = head;
        while (current != null){
            ListNode next = head.next;
            current.next = pre;
            pre = current;
            current = next;
        }
        return pre;
    }
}