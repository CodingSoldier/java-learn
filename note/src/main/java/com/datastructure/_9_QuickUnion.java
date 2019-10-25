package com.datastructure;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-08-16
 */
public class _9_QuickUnion {

    public static class QuickUnion {

        // 指向的父亲节点数组
        private int[] parent;
        // rank[i]表示以i为根的集合元素个数
        private int[] sz;

        public QuickUnion(int size){
            parent = new int[size];
            sz = new int[size];

            for (int i=0; i<size; i++){
                parent[i] = i;
                sz[i] = 1;
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


            if (sz[pRoot] < sz[qRoot]){
                // rank[pRoot]个数少
                // pRoot作为子节点指向qRoot
                parent[pRoot] = qRoot;
                sz[qRoot] += sz[pRoot];
            } else {
                // rank[qRoot] <= rank[pRoot]
                // qRoot作为子节点指向pRoot
                parent[qRoot] = pRoot;
                sz[pRoot] += sz[pRoot];
            }

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


public static class QuickUnionRank {

    // 指向的父亲节点数组
    private int[] parent;
    // rank[i]表示以i为根的树高度
    private int[] rank;

    public QuickUnionRank(int size){
        parent = new int[size];
        rank = new int[size];

        for (int i=0; i<size; i++){
            parent[i] = i;
            rank[i] = 1;
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


        if (rank[pRoot] < rank[qRoot]){
            // pRoot高度 小于 qRoot高度
            // pRoot指向qRoot
            parent[pRoot] = qRoot;
        } else if (rank[qRoot] < rank[pRoot]){
            // qRoot高度 小于 pRoot高度
            // qRoot指向pRoot
            parent[qRoot] = pRoot;
        } else {
            parent[pRoot] = qRoot;
        }

    }


    public static void main(String[] args) {

        QuickUnionRank uf1 = new QuickUnionRank(10);
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


}
