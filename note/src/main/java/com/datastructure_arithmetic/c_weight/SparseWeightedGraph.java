package com.datastructure_arithmetic.c_weight;

import java.util.Vector;

// 稀疏图 - 邻接表
public class SparseWeightedGraph<Weight extends Number & Comparable> implements WeightedGraph{

    private int n;
    private int m;
    private boolean directed;
    private Vector<Edge<Weight>>[] g;

    public SparseWeightedGraph(int n, boolean directed){
        this.n = n;
        this.m = 0;
        this.directed = directed;

        g = (Vector<Edge<Weight>>[])new Vector[n];
        for (int i=0; i<n; i++){
            g[i] = new Vector<Edge<Weight>>();
        }
    }

    public int V(){return n;}
    public int E(){return m;}

    public void addEdge(Edge e){
        g[e.v()].add(new Edge<>(e));
        if (e.v() != e.w() && !directed){
            g[e.w()].add(new Edge(e.w(), e.v(), e.wt()));
        }
        m++;
    }

    public boolean hasEdge(int v, int w){
        for (int i=0; i<g[v].size(); i++){
            if (g[v].elementAt(i).other(v) == w){
                return true;
            }
        }
        return false;
    }

    public void show(){
        for (int i=0; i<n; i++){
            System.out.println("Vertex "+i+":\t");
            for (int j=0; j<g[i].size(); j++){
                Edge e = g[i].elementAt(j);
            }
        }
    }

    public Iterable<Edge<Weight>> adj(int v){
        return g[v];
    }


}























