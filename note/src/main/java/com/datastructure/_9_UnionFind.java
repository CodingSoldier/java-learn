package com.datastructure;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-08-16
 */
public class _9_UnionFind {
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
