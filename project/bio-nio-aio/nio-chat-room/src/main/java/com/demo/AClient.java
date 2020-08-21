package com.demo;

import java.io.IOException;

/**
 * 客户端A
 */
public class AClient {

    public static void main(String[] args)
            throws IOException {
        new NioClient().start("AClient");
    }

}
