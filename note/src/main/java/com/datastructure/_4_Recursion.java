package com.datastructure;

import org.junit.Test;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-08-02
 */
public class _4_Recursion {

    // 递归求解sum
    public static int sum(int[] arr){
        return sum(arr, arr.length-1);
    }

    public static int sum(int[] arr, int index){
        if (index == 0){
            return arr[0];
        }
        return arr[index] + sum(arr, index -1);
    }

    @Test
    public static void test001() {
        System.out.println(sum(new int[]{1, 2, 4, 5}));
    }




    public static class ListNode {

        public int val;
        public ListNode next;

        public ListNode(int x) {
            val = x;
        }

        public ListNode(int[] arr){
            if(arr == null || arr.length == 0)
                throw new IllegalArgumentException("arr can not be empty");
            this.val = arr[0];
            ListNode cur = this;
            for(int i = 1 ; i < arr.length ; i ++){
                cur.next = new ListNode(arr[i]);
                cur = cur.next;
            }
        }

        @Override
        public String toString(){
            StringBuilder s = new StringBuilder();
            ListNode cur = this;
            while(cur != null){
                s.append(cur.val + "->");
                cur = cur.next;
            }
            s.append("NULL");
            return s.toString();
        }



        // 递归删除列表中指定元素
        public static ListNode removeElements(ListNode head, int val) {

           if (head == null){
               return null;
           }
           head.next =removeElements(head.next, val);
           return head.val == val ? head.next : head;
        }


        public static void main(String[] args) {
            int[] nums = {1, 2, 6, 3, 4, 5, 6};
            ListNode head = new ListNode(nums);
            System.out.println(head);

            ListNode res = removeElements(head, 6);
            System.out.println(res);
        }
    }




}
