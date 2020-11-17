package com.datastructure_arithmetic_2;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class F_Stack {

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    /**
     *  6-1 栈的基础应用 Valid Parentheses
     * https://leetcode-cn.com/problems/valid-parentheses/
     */
    static class Solution20 {
        public boolean isValid(String s) {
            Stack<Character> stack = new Stack<>();
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '(' || s.charAt(i) == '[' || s.charAt(i) == '{'){
                    stack.push(s.charAt(i));
                }else {
                    if (stack.size() == 0){
                        return false;
                    }

                    Character match = null;
                    if (s.charAt(i) == ')'){
                        match = '(';
                    }else if (s.charAt(i) == ']'){
                        match = '[';
                    }else if (s.charAt(i) == '}'){
                        match = '{';
                    }else {
                        return false;
                    }

                    Character top = stack.pop();
                    if (top != match){
                        return false;
                    }
                }
            }

            if (stack.size() != 0){
                return false;
            }

            return true;
        }
    }


    /**
     * 6-4 队列的典型应用 Binary Tree Level Order Traversal
     * https://leetcode-cn.com/problems/binary-tree-level-order-traversal/
     */
    static class Solution102 {
        public List<List<Integer>> levelOrder(TreeNode root) {
            ArrayList<List<Integer>> res = new ArrayList<>();
            if (root == null){
                return res;
            }

            LinkedList<Pair<TreeNode, Integer>> queue = new LinkedList<>();
            queue.addLast(new Pair(root, 0));

            while (!queue.isEmpty()){
                Pair<TreeNode, Integer> front = queue.removeFirst();
                TreeNode node = front.getKey();
                Integer level = front.getValue();

                if (level == res.size()){
                    res.add(new ArrayList());
                }

                res.get(level).add(node.val);
                if (node.left != null){
                    queue.addLast(new Pair<>(node.left, level + 1));
                }
                if (node.right != null){
                    queue.addLast(new Pair<>(node.right, level + 1));
                }
            }
            return res;
        }
    }


    /**
     * 6-7 优先队列相关的算法问题 Top K Frequent Elements
     * https://leetcode-cn.com/problems/top-k-frequent-elements/
     */
    static class Solution347 {
        public int numSquares(int n) {

        }
    }






}
