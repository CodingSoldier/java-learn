package com.datastructure_arithmetic.b_graph_base;

import java.util.Vector;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-10-28
 */

// 稠密图 -- 邻接矩阵
public class DenseGraph implements Graph{

    private int nodeTotal;  // 节点数
    private int nodeEdgeNum;  // 节点的边数
    private boolean directed;   // 是否为有向图
    private boolean[][] data2Arr;      // 图的具体数据

    // 构造函数
    public DenseGraph(int nodeTotal , boolean directed ){
        assert nodeTotal >= 0;
        this.nodeTotal = nodeTotal;
        this.nodeEdgeNum = 0;    // 初始化没有任何边
        this.directed = directed;
        // boolean型变量的默认值为false
        // 图的数据data2Arr初始化为nodeTotal*nodeTotal的布尔矩阵, 每一个data2Arr[i][j]均为false, 表示没有任何边
        data2Arr = new boolean[nodeTotal][nodeTotal];
    }

    public int getNodeTotal(){ return nodeTotal;} // 返回节点个数
    public int getNodeEdgeNum(){ return nodeEdgeNum;} // 返回节点边的个数

    // 向图中添加一个边
    public void addEdge(int node1, int node2){

        assert node1 >= 0 && node1 < nodeTotal;
        assert node2 >= 0 && node2 < nodeTotal;

        if( hasEdge(node1, node2) )
            return;

        data2Arr[node1][node2] = true;

        if( !directed ){
            //无向图，node2、node1也相连
            data2Arr[node2][node1] = true;
        }

        nodeEdgeNum++;
    }

    // 验证图中是否有从node1到node2的边
    public boolean hasEdge(int node1, int node2){
        assert node1 >= 0 && node1 < nodeTotal;
        assert node2 >= 0 && node2 < nodeTotal;
        return data2Arr[node1][node2];
    }

    // 显示图的信息
    public void show(){
        for(int i = 0; i < nodeTotal; i ++ ){
            for(int j = 0; j < nodeTotal; j ++ )
                System.out.print(data2Arr[i][j]+"\t");
            System.out.println();
        }
    }

    /**
     * 获取相邻节点
     * @param node
     * @return
     */
    public Iterable<Integer> adjacentNode(int node){
        assert node >= 0 && node < nodeTotal;
        Vector<Integer> nodeVector = new Vector<>();
        // data2Arr[node]是一个数组，遍历此数组
        for (int i=0; i<nodeTotal; i++){
            // data2Arr[node][i]为true，则node和数值为i的节点相连
            if (data2Arr[node][i]){
                // 将节点i添加到数组中
                nodeVector.add(i);
            }
        }
        return nodeVector;
    }

    public static void main(String[] args) {
        DenseGraph dg = new DenseGraph(4, false);
        dg.addEdge(1,2);

        dg.show();
    }

}




















