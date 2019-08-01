package com.datastructure;

import java.util.Arrays;
import java.util.Stack;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-07-31
 */
public class _1_Array {

    static class Array<E>{
        private E[] data;
        private int size;

        public Array(int capacity){
            data = (E[])new Object[capacity];
            size = 0;
        }

        public Array(){
            this(10);
        }

        public int getCapacity(){
            return data.length;
        }


        public int getSize(){
            return this.size;
        }

        public void add(int index, E e){
            if (index < 0 || index >= data.length)
                throw new RuntimeException("下标错误");

            if (this.size == data.length)
                resize(size*2);

            for (int i = size -1 ; i >= index; i--){
                data[i + 1] = data[i];
            }
            data[index] = e;
            size++;
        }

        public E remove(int index){
            E ret = data[index];
            for (int i = index; i<size-1; i++){
                data[i] = data[i+1];
            }
            size--;
            if (data.length !=0 && size < data.length/4 )
                resize(data.length/2);
            return ret;
        }

        public void resize(int capacity){
            E[] newData = (E[])new Object[capacity];
            for (int i=0; i<size; i++){
                newData[i] = data[i];
            }
            data = newData;
        }

        @Override
        public String toString() {
            return "Array{" +
                    "data=" + Arrays.toString(data) +
                    ", size=" + size +
                    '}';
        }

        public static void main(String[] args) {
            Array a1 = new Array();
            a1.add(1, 1);
            a1.add(2, 2);
            a1.add(3, 3);
            System.out.println(a1.toString());
            a1.remove(1);
            System.out.println(a1.toString());
        }

    }
/**
 时间复杂度简单分析
 O(1)、O(n)、O(lgn)、O(nlogn)、O(n^2)
 大O描述的是算法运行时间和输入数据之间的关系

 */
    /**
     时间复杂度是O(n),n是nums中元素的个数，算法和n呈线性关系
     实际时间是 T = c1*n + c2，为了便于分析忽略常数c1、c2
     T = 2*n +2、T = 2000*n + 1000  时间复杂度都是O(n)
     T = 1*n*n + 0   时间复杂度是O(n^2)
     n很小的时候，O(n^2)有可能比O(n)还快
     O精确定义是渐进时间复杂度，描述n趋近于无穷的情况
     T = 1*n*n + 300n + 0   时间复杂度也是O(n^2)，低阶项会被忽略，因为n趋向无穷时低阶项影响很小
     */
    public static int sum(int[] nums){
        int sum = 0;
        for (int num:nums)
            sum += num;
        return sum;
    }



    // 括号匹配
    static class Solution{
        public static boolean vaild(String s){
            Stack<Character> stack = new Stack<>();
            for (int i=0; i<s.length(); i++){
                if ('(' == s.charAt(i)|| '['==s.charAt(i)
                        ||'{'==s.charAt(i) ){
                    stack.push(s.charAt(i));
                }else {
                    if (stack.isEmpty()){
                        return false;
                    }
                    if (')'==s.charAt(i) && '('!= stack.pop()){
                        return false;
                    }
                    if (']'==s.charAt(i) && '[' != stack.pop()){
                        return false;
                    }
                    if ('}'==s.charAt(i) && '{' != stack.pop()){
                        return false;
                    }
                }
            }
            return stack.isEmpty();
        }

        public static void main(String[] args) {
            System.out.println(vaild("()"));
        }
    }

}
