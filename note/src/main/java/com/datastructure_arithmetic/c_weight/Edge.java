package com.datastructure_arithmetic.c_weight;

// 边
public class Edge<Weight extends Number & Comparable> implements Comparable<Edge>{

    private int a, b;
    private Weight weight;

    public Edge(int a, int b, Weight weight){
        this.a = a;
        this.b = b;
        this.weight = weight;
    }

    public Edge(Edge<Weight> e){
        this.a = e.a;
        this.b = e.b;
        this.weight = e.weight;
    }

    public int v(){return a;}
    public int w(){return b;}
    public Weight wt(){return weight;}

    public int other(int x){
        return x == a ? b : a;
    }

    @Override
    public int compareTo(Edge that) {
        if ( weight.compareTo(that.wt()) < 0 ){
            return -1;
        }else if (weight.compareTo(that.wt()) > 0){
            return +1;
        }else {
            return 0;
        }
    }

}
