package com.datastructure_arithmetic.b_graph_base;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

/**
 * 从文件中读取一个图
 */
public class ReadGraph {

    private Scanner scanner;

    /**
     * 读取文件，创建一个图
     * @param graph
     * @param filename
     */
    public ReadGraph(Graph graph, String filename) {
        // 读取文件
        readFile(filename);

        try {
            // 文件第一行的两个数字是点数、边数
            int V = scanner.nextInt();
            if (V < 0)
                throw new IllegalArgumentException("定点数非负");
            assert V == graph.getNodeTotal();
            int E = scanner.nextInt();
            if (E < 0)
                throw new IllegalArgumentException("边数非负");

            // 从第二行开始，将数据添加到图对象中
            for (int i = 0; i < E; i++) {
                int v = scanner.nextInt();
                int w = scanner.nextInt();
                assert v >= 0 && v < V;
                assert w >= 0 && w < V;
                graph.addEdge(v, w);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readFile(String filename) {
        assert filename != null;
        try {
            File file = new File(filename);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                scanner = new Scanner(new BufferedInputStream(fis), "UTF-8");
                scanner.useLocale(Locale.ENGLISH);
            } else {
                throw new IllegalArgumentException(filename + "doesn't exist.");
            }
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Could not open " + filename, ioe);
        }
    }
}