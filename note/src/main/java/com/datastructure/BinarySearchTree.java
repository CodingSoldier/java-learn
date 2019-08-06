package com.datastructure;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


//二分搜索树
public class BinarySearchTree<E extends Comparable<E>> {
    private class Node {
        // 节点值
        public E e;
        // 每个节点都可能有左节点、右节点。最后一层的节点没有左右节点可以视为left、right都为null
        public Node left, right;

        public Node(E e) {
            this.e = e;
            left = null;
            right = null;
        }
    }

    // 树使用root节点作为索引，增查删都从root开始操作
    private Node root;
    // 树中节点的数量，即树的大小
    private int size;

    public BinarySearchTree() {
        root = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(E e){
        // 使用root为索引
        root = add(root, e);
    }

    public Node add(Node node, E e){
        if (node == null){
            // 第一次添加元素，root就为new Node(e)
            size++;
            return new Node(e);
        }

        if (e.compareTo(node.e) < 0){
            // 在节点左边添加节点
            node.left = add(node.left, e);
        }else if (e.compareTo(node.e) > 0){
            // 在节点右边添加节点
            node.right = add(node.right, e);
        }
        return node;
    }

    // 前序遍历
    public void preOrder(){
        preOrder(root);
    }

    public void preOrder(Node node){
        if (node == null){
            return;
        }
        // 打印当前元素值
        System.out.println(node.e);
        preOrder(node.left);
        preOrder(node.right);
    }

    // 中序遍历，结果从小到大排序
    public void inOrder(){
        inOrder(root);
    }

    public void inOrder(Node node){
        if (node == null){
            return;
        }

        inOrder(node.left);
        System.out.println(node.e);
        inOrder(node.right);
    }

    // 后序遍历，先遍历父节点的子节点，在遍历父节点
    public void postOrder(){
        postOrder(root);
    }

    public void postOrder(Node node){
        if (node == null){
            return;
        }
        postOrder(node.left);
        postOrder(node.right);
        System.out.println(node.e);
    }

    // 广度优先遍历
    public void levelOrder(){
        if (root == null){
            return;
        }
        Queue<Node> q = new LinkedBlockingQueue<>();
        q.add(root);
        while (!q.isEmpty()){
            Node node = q.remove();
            System.out.println(node.e);
            if (node.left != null)
                q.add(node.left);
            if (node.right != null)
                q.add(node.right);
        }
    }

    // 最小值元素
    public E minimum(){
        if(size == 0)
            throw new IllegalArgumentException("空树");
        Node minNode = minimum(root);
        return minNode.e;
    }

    // 最小值节点
    private Node minimum(Node node){
        if( node.left == null )
            return node;
        return minimum(node.left);
    }

    // 最大值元素
    public E maximum(){
        if(size == 0)
            throw new IllegalArgumentException("空树");
        return maximum(root).e;
    }

    // 最大值节点
    private Node maximum(Node node){
        if( node.right == null )
            return node;
        return maximum(node.right);
    }

    public E removeMin(){
        // 查找最小值
        E ret = minimum();
        // 删除最小节点
        root = removeMin(root);
        // 返回最小值
        return ret;
    }

    public Node removeMin(Node node){
        // node.left == null，node必然是最小节点
        if (node.left == null){
            // 先保存最小节点的right
            Node nodeRight = node.right;
            // 再把最小节点的right指向null，以便java虚拟机回收最小节点内存
            node.right = null;
            size--;
            return nodeRight;
        }
        // node.left != null，递归node.left
        node.left = removeMin(node.left);
        return node;
    }

    public E removeMax(){
        E ret = maximum();
        root = removeMax(root);
        return ret;
    }

    private Node removeMax(Node node){
        // node.right == null，节点必然是最大节点
        if(node.right == null){
            // 保存最大节点的right
            Node leftNode = node.left;
            node.left = null;
            size --;
            return leftNode;
        }
        // node.right != null，递归node.right
        node.right = removeMax(node.right);
        return node;
    }

    public void remove(E e){
        remove(root, e);
    }

    private Node remove(Node node, E e){
        if (node == null)
            return null;
        if (e.compareTo(node.e) < 0){
            // 递归左侧节点
            node.left = remove(node.left, e);
            return node;
        }else if (e.compareTo(node.e) > 0){
            // 递归右侧节点
            node.right = remove(node.right, e);
            return node;
        }else {
            // 删除最小值
            if (node.left == null){
                Node rightNode = node.right;
                node.right = null;
                size--;
                return rightNode;
            }
            // 删除最大值
            if (node.right == null){
                Node leftNode = node.left;
                node.left=null;
                size--;
                return leftNode;
            }

            //当前node有左右孩子，找出右树中的最小节点successor
            Node successor = minimum(node.right);
            // successor.right = 删除当前node最小值后的树
            successor.right = removeMin(node.right);
            // successor.left = 当前node节点的left
            successor.left = node.left;
            node.left = node.right = null;
            // 返回successor
            return successor;
        }
    }

    public static void main(String[] args) {

        _5_BinarySearchTree.BinarySearchTree<Integer> bst = new _5_BinarySearchTree.BinarySearchTree<>();
        int[] nums = {28, 16, 30, 22, 13, 42, 29};
        for (int num:nums){
            bst.add(num);
        }

        System.out.println("-------前序遍历------");
        bst.preOrder();
        System.out.println("------中序遍历-------");
        bst.inOrder();
        System.out.println("------后序遍历-------");
        bst.postOrder();
        System.out.println("------广度优先遍历-------");
        bst.levelOrder();


        ArrayList<Integer> nums2 = new ArrayList<>();

        //while(!bst.isEmpty())
        //    nums2.add(bst.removeMin());
        //System.out.println(nums2);

        //while(!bst.isEmpty())
        //    nums2.add(bst.removeMax());
        //System.out.println(nums2);


        //bst.remove(16);
        //bst.preOrder();

    }

}
