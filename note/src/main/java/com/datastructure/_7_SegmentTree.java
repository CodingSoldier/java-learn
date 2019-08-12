package com.datastructure;

public class _7_SegmentTree {

    public interface Merger<E>{
        E merge(E a, E b);
    }



    // 线段树类
    public static class SegmentTree<E>{
        private E[] data;   // 原始数组
        private E[] tree;   // 线段树类
        private Merger<E> merger;   //线段树区间操作接口

        public SegmentTree(E[] arr, Merger<E> merger){
            this.merger = merger;

            // data存储原始数组
            data = (E[])new Object[arr.length];
            for(int i = 0 ; i < arr.length ; i ++)
                data[i] = arr[i];

            // 组成线段树的数组长度是原始数组的4倍
            tree = (E[])new Object[4 * arr.length];
            // 生成线段树数组
            buildSegmentTree(0, 0, arr.length - 1);
        }

        // 在treeIndex的位置创建表示区间[l...r]的线段树
        private void buildSegmentTree(int treeIndex, int l, int r){

            if(l == r){
                tree[treeIndex] = data[l];
                return;
            }

            int leftTreeIndex = leftChild(treeIndex);
            int rightTreeIndex = rightChild(treeIndex);

            // int mid = (l + r) / 2;  l + r可能会大于整型最大值改成l + (r - l) / 2
            int mid = l + (r - l) / 2;
            //从下标为0的节点递归给线段树节点赋值
            buildSegmentTree(leftTreeIndex, l, mid);
            buildSegmentTree(rightTreeIndex, mid + 1, r);

            //线段树节点值
            tree[treeIndex] = merger.merge(tree[leftTreeIndex], tree[rightTreeIndex]);
        }

        public int getSize(){
            return data.length;
        }

        public E get(int index){
            if(index < 0 || index >= data.length)
                throw new IllegalArgumentException("Index is illegal.");
            return data[index];
        }

        // 线段树的左右孩子下标计算方式，请查看https://blog.csdn.net/u010606397/article/details/98854128
        private int leftChild(int index){
            return 2*index + 1;
        }

        private int rightChild(int index){
            return 2*index + 2;
        }

        public E query(int queryL, int queryR){
            if (queryL < 0 || queryL >= data.length ||
                    queryR < 0 || queryR >= data.length || queryL > queryR)
                throw new RuntimeException("下标越界");

            return query(0, 0, data.length -1, queryL, queryR);
        }

        private E query(int treeIndex, int l, int r, int queryL, int queryR){
            if (l == queryL && r == queryR)
                return tree[treeIndex];

            int mid = l + (r - l)/2;

            int leftTreeIndex = leftChild(treeIndex);
            int rightTreeIndex = rightChild(treeIndex);

            if (queryL >= mid + 1)
                return query(rightTreeIndex, mid+1, r, queryL, queryR);
            else if (queryR <= mid)
                return query(leftTreeIndex, l, mid, queryL, queryR);

            E leftResult = query(leftTreeIndex, l, mid, queryL, mid);
            E rightResult = query(rightTreeIndex, mid + 1, r, mid + 1, queryR);

            return merger.merge(leftResult, rightResult);
        }

        public void set(int index, E e){
            data[index] = e;
            set(0, 0, data.length - 1, index, e);
        }

        private void set(int treeIndex, int l, int r, int index, E e){
            if (l == r){
                tree[treeIndex] = e;
                return;
            }

            int mid = l + (r - l)/2;
            int leftTreeIndex = leftChild(treeIndex);
            int rightTreeIndex = rightChild(treeIndex);
            if (index >= mid + 1)
                set(rightTreeIndex, mid + 1, r, index, e);
            else
                set(leftTreeIndex, l, mid, index, e);

            tree[treeIndex] = merger.merge(tree[leftTreeIndex], tree[rightTreeIndex]);

        }


        @Override
        public String toString(){
            StringBuilder res = new StringBuilder();
            res.append('[');
            for(int i = 0 ; i < tree.length ; i ++){
                if(tree[i] != null)
                    res.append(tree[i]);
                else
                    res.append("null");

                if(i != tree.length - 1)
                    res.append(", ");
            }
            res.append(']');
            return res.toString();
        }

        public static void main(String[] args) {
            Integer[] nums = {-2, 0, 3, -5, 2, -1};
            SegmentTree<Integer> segTree = new SegmentTree<>(nums, (a, b) -> a+b);
            System.out.println(segTree);
        }


    }




}
