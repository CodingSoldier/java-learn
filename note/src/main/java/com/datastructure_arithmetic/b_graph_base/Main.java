package com.datastructure_arithmetic.b_graph_base;

// 测试图的联通分量、深度遍历寻址
public class Main {

    public static void main(String[] args) {

        //// TestG1.txt
        //String filename1 = "note\\src\\main\\java\\com\\datastructure_arithmetic\\testG1.txt";
        //SparseGraph g1 = new SparseGraph(13, false);
        //ReadGraph readGraph1 = new ReadGraph(g1, filename1);
        //Components component1 = new Components(g1);
        //System.out.println("TestG1.txt, 连通分量: " + component1.count());
        //System.out.println();
        //
        //// TestG2.txt
        //String filename2 = "note\\src\\main\\java\\com\\datastructure_arithmetic\\testG2.txt";
        //SparseGraph g2 = new SparseGraph(6, false);
        //ReadGraph readGraph2 = new ReadGraph(g2, filename2);
        //Components component2 = new Components(g2);
        //System.out.println("TestG2.txt, 连通分量: " + component2.count());



        //String filename1 = "note\\src\\main\\java\\com\\datastructure_arithmetic\\testG1.txt";
        //SparseGraph g = new SparseGraph(13, false);
        //ReadGraph readGraph = new ReadGraph(g, filename1);
        //g.show();
        //System.out.println();
        //
        //Path path = new Path(g,0);
        //System.out.println("Path from 0 to 6 : ");
        //path.showPath(6);



        String filename = "note\\src\\main\\java\\com\\datastructure_arithmetic\\testG.txt";
        SparseGraph g = new SparseGraph(7, false);
        ReadGraph readGraph = new ReadGraph(g, filename);
        g.show();

        // 比较使用深度优先遍历和广度优先遍历获得路径的不同
        // 广度优先遍历获得的是无权图的最短路径
        Path dfs = new Path(g,0);
        System.out.print("DFS : ");
        dfs.showPath(6);

        ShortestPath bfs = new ShortestPath(g,0);
        System.out.print("BFS : ");
        bfs.showPath(6);


    }
}
