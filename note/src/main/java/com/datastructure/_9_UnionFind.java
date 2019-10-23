package com.datastructure;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-08-16
 */
public class _9_UnionFind {

    public static class QuickUnion {

        //父亲节点数组
        private int[] parent;

        public QuickUnion(int size){
            parent = new int[size];
            for (int i=0; i<size; i++){
                parent[i] = i;
            }
        }

        public int getSize(){
            return parent.length;
        }

        // 返回的是根节点
        private int find(int p){
            if (p<0 || p>=parent.length){
                throw new RuntimeException("越界");
            }

            /**
             * 如果p的父亲节点不是自己，则继续寻找上一级节点，直至节点的父亲节点也是自己
             * 即返回根节点
             */
            while (p != parent[p]){
                p=parent[p];
            }
            return p;
        }

        public boolean isConnected(int p, int q){
            return find(p) == find(q);
        }

        public void unionElements(int p, int q){
            int pRoot = find(p);
            int qRoot = find(q);

            if (pRoot == qRoot){
                return;
            }

            // 将p根节点指向q的根节点
            parent[pRoot] = qRoot;
        }


        public static void main(String[] args) {

            QuickUnion uf1 = new QuickUnion(10);
            System.out.println(uf1.find(8));
            System.out.println(uf1.find(1));
            System.out.println(uf1.isConnected(1, 8));

            uf1.unionElements(1, 8);

            System.out.println(uf1.find(8));
            System.out.println(uf1.find(1));
            uf1.isConnected(1, 8);
            System.out.println(uf1.isConnected(1, 8));

        }

    }



    public class UnionFind {

        private int[] root;
        private int[] rank;

        public UnionFind(int size){
            root = new int[size];
            for (int i=0; i<size; i++){
                root[i] = i;
                rank[i] = 1;
            }
        }

        public int getSize(){
            return root.length;
        }

        private int findRoot(int p){
            if (p < 0 || p >= root.length)
                throw new RuntimeException("非法参数异常");

            while (p != root[p]){
                root[p] = root[root[p]];
                p = root[p];
            }
            return p;
        }

        public boolean isConnected(int p, int q){
            return findRoot(p) == findRoot(q);
        }

        public void unionElements(int p, int q){
            int pRoot = findRoot(p);
            int qRoot = findRoot(q);
            if (pRoot == qRoot)
                return;

            if (rank[pRoot] < rank[qRoot])
                root[pRoot] = qRoot;
            else if (rank[pRoot] > rank[qRoot])
                root[qRoot] = pRoot;
            else {
                root[pRoot] = qRoot;
                rank[qRoot] += 1;
            }

        }


    }

}
