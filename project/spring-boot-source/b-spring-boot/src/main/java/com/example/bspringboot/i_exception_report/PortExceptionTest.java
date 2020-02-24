package com.example.bspringboot.i_exception_report;

import java.net.ServerSocket;

/**
 * @author chenpiqian
 * @date: 2020-02-24
 */
public class PortExceptionTest {

    /**
      测试端口占用异常，本类先占用7777端口
      org.springframework.boot.SpringApplication#handleRunFailure(org.springframework.context.ConfigurableApplicationContext, java.lang.Throwable, java.util.Collection, org.springframework.boot.SpringApplicationRunListeners)

     */
    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(7777);
        serverSocket.accept();
    }

}
