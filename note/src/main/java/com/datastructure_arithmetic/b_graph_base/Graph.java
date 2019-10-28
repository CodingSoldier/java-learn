package com.datastructure_arithmetic.b_graph_base;

// 图的接口
public interface Graph {

    int getNodeTotal();
    int getNodeEdgeNum();
    void addEdge(int node1, int node2);
    boolean hasEdge(int node1, int node2);
    void show();
    Iterable<Integer> adjacentNode(int node);
}