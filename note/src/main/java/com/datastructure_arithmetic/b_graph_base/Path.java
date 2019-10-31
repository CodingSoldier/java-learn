package com.datastructure_arithmetic.b_graph_base;

import java.util.Vector;
import java.util.Stack;

// 深度遍历寻址
public class Path {

    private Graph G;   // 图的引用
    private int startNode;     // 起始点
    private boolean[] visited;  // 记录深度遍历的过程中节点是否被访问
    private int[] from;         // 记录路径, from[i]表示查找的路径上i的上一个节点

    // 构造函数, 寻路算法, 寻找图graph从startNode点到其他点的路径
    public Path(Graph graph, int startNode){

        // 算法初始化
        G = graph;
        assert startNode >= 0 && startNode < G.getNodeTotal();

        visited = new boolean[G.getNodeTotal()];
        from = new int[G.getNodeTotal()];
        for( int i = 0 ; i < G.getNodeTotal() ; i ++ ){
            visited[i] = false;
            from[i] = -1;
        }
        this.startNode = startNode;

        // 寻路算法
        deepFirstSearch(startNode);
    }


    // 图的深度优先遍历
    private void deepFirstSearch(int node ){
        visited[node] = true;
        // node的相邻节点
        for( int i : G.adjacentNode(node) )
            // 相邻节点未被遍历过
            if( !visited[i] ){
                // from[i]位置存储上一个节点node
                from[i] = node;
                deepFirstSearch(i);
            }
    }

    // 查询从startNode点到targetNode点是否有路径
    boolean hasPath(int targetNode){
        assert targetNode >= 0 && targetNode < G.getNodeTotal();
        return visited[targetNode];
    }

    // 查询从s点到w点的路径, 存放在vec中
    Vector<Integer> path(int targetNode){

        assert hasPath(targetNode) ;

        // 栈，先进后出
        Stack<Integer> s = new Stack<Integer>();
        // 通过from数组逆向查找到从startNode到targetNode的路径, 存放到栈中
        int p = targetNode;
        // p不为-1，即节点p有上一个节点，上一个节点存放在from[p]中
        while( p != -1 ){
            s.push(p);
            p = from[p];
        }

        // 从栈中依次取出元素，获得顺序的从startNode到targetNode的路径
        Vector<Integer> res = new Vector<Integer>();
        while( !s.empty() )
            res.add( s.pop() );

        return res;
    }

    // 打印出从startNode到targetNode的路径
    void showPath(int targetNode){

        assert hasPath(targetNode) ;

        Vector<Integer> vec = path(targetNode);
        for( int i = 0 ; i < vec.size() ; i ++ ){
            System.out.print(vec.elementAt(i));
            if( i == vec.size() - 1 )
                System.out.println();
            else
                System.out.print(" -> ");
        }
    }
}
