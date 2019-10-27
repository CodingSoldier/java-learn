package com.datastructure_arithmetic.b_graph_base;

// 图的接口
public interface Graph {

    public int V();
    public int E();
    public void addEdge(int v, int w);
    boolean hasEdge(int v, int w);
    void show();
    public Iterable<Integer> adj(int v);
}