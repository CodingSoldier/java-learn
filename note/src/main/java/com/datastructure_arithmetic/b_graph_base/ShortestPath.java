package com.datastructure_arithmetic.b_graph_base;

import java.util.Vector;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;

// 广度优先遍历
public class ShortestPath {

    private Graph G;   // 图的引用
    private int startNode;     // 起始点
    private boolean[] visited;  // 记录dfs的过程中节点是否被访问
    private int[] from;         // 记录路径, from[i]表示查找的路径上i的上一个节点
    private int[] ord;          // 记录路径中节点的次序。ord[i]表示i节点在路径中的次序。


    // 构造函数, 寻路算法, 寻找图graph从startNode点到其他点的路径
    public ShortestPath(Graph graph, int startNode){

        // 算法初始化
        G = graph;
        assert startNode >= 0 && startNode < G.getNodeTotal();

        visited = new boolean[G.getNodeTotal()];
        from = new int[G.getNodeTotal()];
        ord = new int[G.getNodeTotal()];
        for( int i = 0 ; i < G.getNodeTotal() ; i ++ ){
            visited[i] = false;
            from[i] = -1;
            ord[i] = -1;
        }
        this.startNode = startNode;

        // 无向图最短路径算法, 从startNode开始广度优先遍历整张图
        Queue<Integer> q = new LinkedList<Integer>();

        q.add(startNode);
        visited[startNode] = true;
        ord[startNode] = 0;
        while( !q.isEmpty() ){
            int v = q.remove();
            for( int i : G.adjacentNode(v) )
                if( !visited[i] ){
                    q.add(i);
                    visited[i] = true;
                    from[i] = v;
                    ord[i] = ord[v] + 1;
                }
        }
    }

    // 查询从startNode点到target点是否有路径
    public boolean hasPath(int target){
        assert target >= 0 && target < G.getNodeTotal();
        return visited[target];
    }

    // 查询从startNode点到target点的路径, 存放在vec中
    public Vector<Integer> path(int target){

        assert hasPath(target) ;

        Stack<Integer> s = new Stack<Integer>();
        // 通过from数组逆向查找到从startNode到target的路径, 存放到栈中
        int p = target;
        while( p != -1 ){
            s.push(p);
            p = from[p];
        }

        // 从栈中依次取出元素, 获得顺序的从startNode到target的路径
        Vector<Integer> res = new Vector<Integer>();
        while( !s.empty() )
            res.add( s.pop() );

        return res;
    }

    // 打印出从startNode点到target点的路径
    public void showPath(int target){

        assert hasPath(target) ;

        Vector<Integer> vec = path(target);
        for( int i = 0 ; i < vec.size() ; i ++ ){
            System.out.print(vec.elementAt(i));
            if( i == vec.size() - 1 )
                System.out.println();
            else
                System.out.print(" -> ");
        }
    }

    // 查看从startNode点到target点的最短路径长度
    // 若从startNode到target不可达，返回-1
    public int length(int w){
        assert w >= 0 && w < G.getNodeTotal();
        return ord[w];
    }
}