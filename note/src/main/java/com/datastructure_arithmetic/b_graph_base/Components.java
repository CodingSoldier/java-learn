package com.datastructure_arithmetic.b_graph_base;

// 连通分量
public class Components {

    Graph G;                    // 图的引用
    private boolean[] visited; // 记录深度遍历的过程中节点是否被访问
    private int count;         // 记录连通分量个数
    private int[] id;           // 每个节点所对应的连通分量标记

    // 构造函数，求出无权图的连通分量
    public Components(Graph graph){

        // 算法初始化
        G = graph;
        // visited数组长度是图节点的个数
        visited = new boolean[G.getNodeTotal()];
        id = new int[G.getNodeTotal()];
        count = 0;
        for( int i = 0 ; i < G.getNodeTotal() ; i ++ ){
            // 刚才开始，所有节点都没遍历
            visited[i] = false;
            // 刚才开始，每个节点不属于任何连通分量
            id[i] = -1;
        }

        // 求图的连通分量
        for( int i = 0 ; i < G.getNodeTotal() ; i ++ )
            if( !visited[i] ){
                // i未遍历，深度遍历i
                deepFirstSearch(i);
                // i深度遍历完了，连通分量++
                count++;
            }
    }

    // 图的深度优先遍历
    void deepFirstSearch(int node){

        // 当前节点设置为已经遍历
        visited[node] = true;
        // 当前节点属于连通分量count
        id[node] = count;

        // 深度遍历当前节点的相邻节点
        for( int i: G.adjacentNode(node) ){
            if( !visited[i] )
                deepFirstSearch(i);
        }
    }

    // 返回图的连通分量个数
    int count(){
        return count;
    }

    // 查询点node1和点node2是否连通
    boolean isConnected(int node1, int node2){
        assert node1 >= 0 && node1 < G.getNodeTotal();
        assert node2 >= 0 && node2 < G.getNodeTotal();
        return id[node1] == id[node2];
    }

}