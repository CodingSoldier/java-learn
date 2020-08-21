package com.demo;

import java.io.IOException;

/**
 * 客户端B
 */
public class BClient {

    public static void main(String[] args)
            throws IOException {
        new NioClient().start("BClient");
    }

}
