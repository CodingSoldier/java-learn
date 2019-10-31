package com.datastructure_arithmetic.b_graph_base;

import java.util.Vector;

// 稀疏图 -- 邻接表
public class SparseGraph implements Graph{

    private int nodeTotal;  // 节点数
    private int nodeEdgeNum;  // 节点的边数
    private boolean directed;   // 是否为有向图
    private Vector<Integer>[] dataArrVector; // 图的具体数据

    // 构造函数
    public SparseGraph(int nodeTotal , boolean directed ){
        assert nodeTotal >= 0;
        this.nodeTotal = nodeTotal;
        this.nodeEdgeNum = 0;    // 初始化没有任何边
        this.directed = directed;
        // dataArrVector初始化为nodeTotal个空的vector, 表示每一个dataArrVector[i]都为空, 即没有任何边
        dataArrVector = (Vector<Integer>[])new Vector[nodeTotal];
        for(int i = 0 ; i < nodeTotal ; i ++)
            dataArrVector[i] = new Vector<Integer>();
    }

    public int getNodeTotal(){ return nodeTotal;} // 返回节点个数
    public int getNodeEdgeNum(){ return nodeEdgeNum;} // 返回节点边的个数

    // 向图中添加一个边
    public void addEdge(int node1, int node2){

        assert node1 >= 0 && node1 < nodeTotal;
        assert node2 >= 0 && node2 < nodeTotal;

        dataArrVector[node1].add(node2);
        if( node1 != node2 && !directed )
            dataArrVector[node2].add(node1);

        nodeEdgeNum++;
    }

    // 验证图中是否有从node1到node2的边
    public boolean hasEdge(int node1, int node2){

        assert node1 >= 0 && node1 < nodeTotal;
        assert node2 >= 0 && node2 < nodeTotal;

        for(int i = 0; i < dataArrVector[node1].size() ; i ++ )
            if( dataArrVector[node1].elementAt(i) == node2)
                return true;
        return false;
    }

    // 显示图的信息
    public void show(){

        for(int i = 0; i < nodeTotal; i ++ ){
            System.out.print("vertex " + i + ":\t");
            for(int j = 0; j < dataArrVector[i].size() ; j ++ )
                System.out.print(dataArrVector[i].elementAt(j) + "\t");
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
        // 邻接表，dataArrVector[node]中的节点都和node相连接
        return dataArrVector[node];
    }

    public static void main(String[] args) {
        SparseGraph dg = new SparseGraph(4, false);
        dg.addEdge(1,2);

        dg.show();
    }

}
