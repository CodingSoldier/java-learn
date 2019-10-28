package com.datastructure_arithmetic;

import com.datastructure_arithmetic.b_graph_base.Graph;

/**
 * @author chenpiqian
 * @date: 2019-10-28
 */
public class Components {

    Graph G;
    private boolean[] visited;
    private int count;
    private int[] id;

    public Components(Graph graph){
        G = graph;
        visited = new boolean[G.getNodeTotal()];
        id = new int[G.getNodeTotal()];
        count = 0;
        for (int i=0; i<G.getNodeTotal(); i++){
            visited[i] = false;
            id[i] = -1;
        }

        for (int i=0; i<G.getNodeTotal(); i++){
            if (!visited[i]){
                deepFirstSearch(i);
                count++;
            }
        }
    }

    private void deepFirstSearch(int node){
        visited[node] = true;
        id[node] = count;
        for (int i:G.adjacentNode(node)){
            if (!visited[i]){
                deepFirstSearch(node);
            }
        }
    }

    int count(){
        return count;
    }

    boolean isConnected(int node1, int node2){
        return id[node1] == id[node2];
    }
}
