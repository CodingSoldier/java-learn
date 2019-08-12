package com.datastructure;

import java.util.ArrayList;
import java.util.TreeMap;

public class _8_Trie {


    // 字典树
    public static class Trie {

        // 字典树中的节点
        private class Node{
            // 判断当前节点是否为字符串最后一个字符
            public boolean isWord;
            // 使用TreeMap存储当前节点与子节点的关系
            public TreeMap<Character, Node> next;

            public Node(boolean isWord){
                this.isWord = isWord;
                next = new TreeMap<>();
            }

            public Node(){
                this(false);
            }
        }

        //根节点
        private Node root;
        private int size;

        public Trie(){
            root = new Node();
            size = 0;
        }

        public int getSize(){
            return size;
        }

        // 查找操作
        public boolean contains(String word){
            Node cur = root;
            // 循环字符串，将字符添加到字典树中
            for (int i=0; i<word.length(); i++){
                char c = word.charAt(i);
                // 未找到字符串中的某字符，返回false
                if (cur.next.get(c) == null)
                    return false;
                cur = cur.next.get(c);
            }

            /**
             * 在字典树中存在查找的字符串word，查找到最后一个字符
             * 若此节点字符结束标识是true，则字典树包含此字符串，返回true,否则返回false
             */
            return cur.isWord;
        }

        // 添加操作
        public void add(String word){
            Node cur = root;
            for (int i=0; i<word.length(); i++){
                char c = word.charAt(i);
                // 没储存此字符，追加字符
                if (cur.next.get(c) == null){
                    cur.next.put(c, new Node());
                }
                // 继续遍历下一个节点
                cur = cur.next.get(c);
            }

            // 新增的字符串在树中不存在，设置当前node为字符串word的最后一个字符节点
            // 树的size加1
            if (!cur.isWord){
                cur.isWord = true;
                size++;
            }
        }

        // 前缀搜索
        public boolean isPrefix(String prefix){
            Node cur = root;
            for (int i=0; i<prefix.length(); i++){
                char c = prefix.charAt(i);
                if (cur.next.get(c) == null){
                    return false;
                }
                cur = cur.next.get(c);
            }
            return true;
        }

        public static void main(String[] args) {
            ArrayList<String> wordList = new ArrayList<>();
            wordList.add("first");
            wordList.add("second");
            wordList.add("third");
            wordList.add("different");
            wordList.add("words");

            Trie trie = new Trie();
            for (String word: wordList){
                trie.add(word);
            }
            for (String word: wordList){
                System.out.println(trie.contains(word));
            }
            System.out.println(trie.contains("no-in"));
        }

    }


}
