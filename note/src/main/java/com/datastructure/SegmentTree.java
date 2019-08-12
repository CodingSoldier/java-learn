package com.datastructure;

// 线段树类
public class SegmentTree<E>{
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

