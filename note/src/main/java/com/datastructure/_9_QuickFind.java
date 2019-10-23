package com.datastructure;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-08-16
 */
public class _9_QuickFind {


    public static class QuickFind {

        private int[] id;

        /**
         * 初始化数据，数据是下标，id是元素值
         * 数据的id与元素值相等，表示没有相连接的数据
         */
        public QuickFind(int size){
            id = new int[size];
            for (int i=0; i<size; i++){
                id[i] = i;
            }
        }

        public int getSize(){
            return id.length;
        }

        private int find(int p){
            if (p<0 || p>=id.length){
                throw new RuntimeException("越界");
            }
            return id[p];
        }

        // 判断元素id[p]是否混合id[q]想连接
        public boolean isConnected(int p, int q){
            return find(p) == find(q);
        }

        // 连接接id[p]、id[q]
        public void unionElements(int p, int q){
            int pID = find(p);
            int qID = find(q);

            if (pID == qID){
                return;
            }

            // 将id[p]的值改成id[q]的值，则id[p]、id[q]就可以表示为相连接了
            for (int i=0; i<id.length; i++){
                if (id[i]==pID){
                    id[i] = qID;
                }
            }
        }

        public static void main(String[] args) {
            QuickFind uf1 = new QuickFind(10);
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

