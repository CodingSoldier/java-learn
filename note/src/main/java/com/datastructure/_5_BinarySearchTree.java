package com.datastructure;

import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

public class _5_BinarySearchTree {

    //二分搜索树
    public static class BinarySearchTree<E extends Comparable<E>> {
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

        public boolean contains(E e){
            return contains(root, e);
        }

        public boolean contains(Node node, E e){
            if (node == null){
                return false;
            }

            if (e.compareTo(node.e) == 0){
                return true;
            }else if (e.compareTo(node.e) < 0){
                return contains(node.left, e);
            }else {
                return contains(node.right, e);
            }
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

        // 非递归方式实现前序遍历
        public void preOrderNR(){
            if (root == null){
                return;
            }
            Stack<Node> stack = new Stack<>();
            stack.push(root);
            while (!stack.isEmpty()){
                Node node = stack.pop();
                System.out.println(node.e);

                if (node.right != null)
                    stack.push(node.right);
                if (node.left != null)
                    stack.push(node.left);
            }
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


        public E removeMax(){
            E ret = maximum();
            root = removeMax(root);
            return ret;
        }

        private Node removeMax(Node node){

            if(node.right == null){
                Node leftNode = node.left;
                node.left = null;
                size --;
                return leftNode;
            }

            node.right = removeMax(node.right);
            return node;
        }


        public static void main(String[] args) {
            BinarySearchTree<Integer> bts = new BinarySearchTree<>();
            int[] nums = {28, 16, 30, 22, 13, 42, 29};
            for (int num:nums){
                bts.add(num);
            }
            System.out.println("-------前序遍历------");
            bts.preOrder();
            System.out.println("------中序遍历-------");
            bts.inOrder();
            System.out.println("------后序遍历-------");
            bts.postOrder();
            //
            ////bts.preOrderNR();
            //
            //bts.levelOrder();

            //BinarySearchTree<Integer> bst = new BinarySearchTree<>();
            //Random random = new Random();
            //
            //int n = 1000;
            //
            //// test removeMin
            //for(int i = 0 ; i < n ; i ++)
            //    bst.add(random.nextInt(10000));
            //
            //ArrayList<Integer> nums = new ArrayList<>();
            //while(!bst.isEmpty())
            //    nums.add(bst.removeMin());
            //System.out.println(nums);
            //
            //// test removeMax
            //bst = new BinarySearchTree<>();
            //for(int i = 0 ; i < n ; i ++)
            //    bst.add(random.nextInt(10000));
            //
            //nums = new ArrayList<>();
            //while(!bst.isEmpty())
            //    nums.add(bst.removeMax());
            //System.out.println(nums);
        }

    }

}
