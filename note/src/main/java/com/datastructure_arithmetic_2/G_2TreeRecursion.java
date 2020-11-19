package com.datastructure_arithmetic_2;

import java.util.ArrayList;
import java.util.List;

public class G_2TreeRecursion {

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * 7-1 二叉树天然的递归结构
     * https://leetcode-cn.com/problems/maximum-depth-of-binary-tree/
     */
    static class Solution104 {
        public int maxDepth(TreeNode root) {
            // 递归中止条件
            if (root == null){
                return 0;
            }

            // 左右子树的深度
            int leftDepth = maxDepth(root.left);
            int rightDepth = maxDepth(root.right);

            // 取左右子树最大深度 + 1
            return Math.max(leftDepth, rightDepth) + 1;
        }
    }


    /**
     * 7-2 一个简单的二叉树问题引发的血案 Invert Binary Tree
     * https://leetcode-cn.com/problems/invert-binary-tree/
     */
    static class Solution226 {
        public TreeNode invertTree(TreeNode root) {
            // 递归中止条件
            if (root == null){
                return null;
            }

            // 左子树、右子树对调
            TreeNode temp = root.left;
            root.left = root.right;
            root.right = temp;

            // 对左子树、右子树同样执行同样的翻转过程
            invertTree(root.left);
            invertTree(root.right);

            return root;
        }
    }



    /**
     * 完全二叉树：除了最后一层，所有层的节点数达到最大，如此同时，最后一层的所有节点都在最左侧。
     * 满二叉树：所有层的节点数达到最大。
     * 平衡二叉树：每一个节点的左右子树的高度差不超过1
     */

    /**
     * 7-3 注意递归的终止条件 Path Sum
     * https://leetcode-cn.com/problems/path-sum/
     */
    static class Solution112 {
        public boolean hasPathSum(TreeNode root, int sum) {
            if (root == null){
                return false;
            }

            if (root.left == null && root.right == null){
                return sum - root.val == 0;
            }

            return hasPathSum(root.left, sum - root.val) || hasPathSum(root.right, sum - root.val);
        }
    }


    /**
     * 7-4 定义递归问题 Binary Tree Path
     * https://leetcode-cn.com/problems/binary-tree-paths/
     */
    static class Solution257 {
        public List<String> binaryTreePaths(TreeNode root) {
            if (root == null){
                return new ArrayList<>();
            }

            if (root.left == null && root.right == null){
                ArrayList<String> list = new ArrayList<>();
                list.add(String.valueOf(root.val));
                return list;
            }

            ArrayList<String> res = new ArrayList<>();

            List<String> resLeft = binaryTreePaths(root.left);
            List<String> resRight = binaryTreePaths(root.right);

            /**
             * StringBuilder比字符串拼接快
             */
            for (String e : resLeft) {
                String path = root.val + "->" + e;
                res.add(path);
            }
            for (String e : resRight) {
                String path = root.val + "->" + e;
                res.add(path);
            }

            return res;
        }
    }


    /**
     * 7-5 稍复杂的递归逻辑 Path Sum III
     * https://leetcode-cn.com/problems/path-sum-iii/
     */
    static class Solution437 {

        public int pathSum(TreeNode root, int sum) {
            if (root == null){
                return 0;
            }
            return findPath(root, sum) + pathSum(root.left, sum) + pathSum(root.right, sum);
        }

        public int findPath(TreeNode node, int num){
            if (node == null){
                return 0;
            }

            int res = 0;
            if (node.val == num){
                res += 1;
            }

            res += findPath(node.left, num - node.val);
            res += findPath(node.right, num - node.val);

            return res;
        }
    }


    /**
     * 7-6 二分搜索树中的问题 Lowest Common Ancestor of a Binary Search Tree
     * https://leetcode-cn.com/problems/lowest-common-ancestor-of-a-binary-search-tree/
     */
    static class Solution235 {
        public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
            if (root == null){
                return null;
            }

            if (p.val < root.val && q.val < root.val){
                return lowestCommonAncestor(root.left, p, q);
            }

            if (p.val > root.val && q.val > root.val){
                return lowestCommonAncestor(root.right, p, q);
            }

            return root;
        }

        public static void main(String[] args) {
            TreeNode n6 = new TreeNode(6);
            TreeNode n2 = new TreeNode(2);
            TreeNode n8 = new TreeNode(8);
            n6.left = n2;
            n6.right = n8;

            TreeNode n0 = new TreeNode(0);
            TreeNode n4 = new TreeNode(4);
            n2.left = n0;
            n2.right = n4;

            TreeNode n3 = new TreeNode(3);
            TreeNode n5 = new TreeNode(5);
            n4.left = n3;
            n4.right = n5;

            TreeNode n7 = new TreeNode(7);
            TreeNode n9 = new TreeNode(9);
            n8.left = n7;
            n8.right = n9;

            TreeNode res = lowestCommonAncestor(n6, n2, n8);
            System.out.println(res);

        }
    }

}
