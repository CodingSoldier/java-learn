package com.datastructure;

import java.util.ArrayList;

public class _11_RBTree {

    static class RBTree<K extends Comparable<K>, V> {

        // 定义红黑树颜色
        private static final boolean RED = true;
        private static final boolean BLACK = false;

        private class Node{
            public K key;
            public V value;
            public Node left, right;
            public boolean color;

            public Node(K key, V value){
                this.key = key;
                this.value = value;
                left = null;
                right = null;
                /**
                 * 初始化一个红黑树节点，首先将节点设置为红色，
                 * 在将节点添加到树中时，可能会将节点颜色改成黑色
                 */
                color = RED;
            }
        }

        private Node root;
        private int size;

        public RBTree(){
            root = null;
            size = 0;
        }

        public int getSize(){
            return size;
        }

        public boolean isEmpty(){
            return size == 0;
        }

        // 判断节点node的颜色
        private boolean isRed(Node node){
            if(node == null)
                return BLACK;
            return node.color;
        }

        // 返回以node为根节点的二分搜索树中，key所在的节点
        private Node getNode(Node node, K key){

            if(node == null)
                return null;

            if(key.equals(node.key))
                return node;
            else if(key.compareTo(node.key) < 0)
                return getNode(node.left, key);
            else // if(key.compareTo(node.key) > 0)
                return getNode(node.right, key);
        }

        public boolean contains(K key){
            return getNode(root, key) != null;
        }

        public V get(K key){

            Node node = getNode(root, key);
            return node == null ? null : node.value;
        }

        public void set(K key, V newValue){
            Node node = getNode(root, key);
            if(node == null)
                throw new IllegalArgumentException(key + " doesn't exist!");

            node.value = newValue;
        }

        //   node                     x
        //  /   \     左旋转         /  \
        // T1   x   --------->   node   T3
        //     / \              /   \
        //    T2 T3            T1   T2
        private Node leftRotate(Node node){
            Node x = node.right;
            // 左旋转
            node.right = x.left;
            x.left = node;
            x.color = node.color;
            node.color = RED;
            /**
             * 若出现x.color = node.color=RED; node.color = RED; 的情况
             * 由于我们已经将x节点返回给调用者，调用者就可以处理x的颜色了
             */
            return x;
        }

        //     node                   x
        //    /   \     右旋转       /  \
        //   x    T2   ------->   y   node
        //  / \                       /  \
        // y  T1                     T1  T2
        private Node rightRotate(Node node){
            Node x = node.left;
            // 右旋转
            node.left = x.right;
            x.right = node;
            x.color = node.color;
            node.color = RED;
            return x;
        }

        // 颜色翻转
        private void flipColors(Node node){
            node.color = RED;
            node.left.color = BLACK;
            node.right.color = BLACK;
        }

        // 以二分搜索树添加方法为基础改造为红黑树的添加方法
        // 向红黑树中添加新的元素(key, value)
        public void add(K key, V value){
            root = add(root, key, value);
            root.color = BLACK; // 保持红黑树根节点一直为黑色
        }

        private Node add(Node node, K key, V value){
            if(node == null){
                size ++;
                return new Node(key, value);
            }
            if(key.compareTo(node.key) < 0)
                node.left = add(node.left, key, value);
            else if(key.compareTo(node.key) > 0)
                node.right = add(node.right, key, value);
            else
                node.value = value;

            // 根据条件节点后的逻辑链条，编写处理情况
            // 是否左旋转
            if (isRed(node.right) && !isRed(node.left))
                node = leftRotate(node);
            // 是否右旋
            if (isRed(node.left) && isRed(node.left.left))
                node = rightRotate(node);
            // 是否颜色和翻转
            if (isRed(node.left) && isRed(node.right))
                flipColors(node);

            // 这3个判断条件不是if-else的关系，判断顺序也不能变

            // 由于是递归代码，假如返回的node是红色，则node返回给调用者后，还会执行上面的3个判断
            return node;
        }

        public static void main(String[] args){
            /**
             * 读取傲慢与偏见这本书，通过 “单词”:“单词在书中出现的次数” 这种key-value的形式把书中的单词-词频加到AVLTree中
             * FileUtil、傲慢与偏见.txt 可以到我的github下载
             * https://github.com/CodingSoldier/java-learn/tree/master/note/src/main/java/com/datastructure
             */
            ArrayList<String> words = new ArrayList<>();
            if(FileUtil.readFile("./note/src/main/java/com/datastructure/傲慢与偏见.txt", words)) {
                System.out.println("总单词数: " + words.size());
                RBTree<String, Integer> map = new RBTree<>();
                for (String word : words) {
                    if (map.contains(word))
                        map.set(word, map.get(word) + 1);
                    else
                        map.add(word, 1);
                }
                System.out.println("单词去重后的总数: " + map.getSize());
            }
        }
    }

}
