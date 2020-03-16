package com.example.ee4springservlet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
异步servlet原生写法
 */
@WebServlet(
        asyncSupported = true,  //开启异步特性
        name = "asyncServlet",
        urlPatterns = "/async-servlet"
)
public class AsyncServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 判断是否支持异步
        if (req.isAsyncSupported()){
            AsyncContext asyncContext = req.startAsync();
            asyncContext.setTimeout(50L);

            // 添加异步监听器
            asyncContext.addListener(new AsyncListener() {
                @Override
                public void onComplete(AsyncEvent event) throws IOException {
                    System.out.println("监听器，完成方法");
                }

                @Override
                public void onTimeout(AsyncEvent event) throws IOException {
                    HttpServletResponse servletResponse = (HttpServletResponse)event.getSuppliedResponse();
                    servletResponse.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
                    System.out.println("监听器，timeout方法");
                }

                @Override
                public void onError(AsyncEvent event) throws IOException {
                    System.out.println("监听器，error方法");
                }

                @Override
                public void onStartAsync(AsyncEvent event) throws IOException {
                    System.out.println("监听器，开始方法");
                }
            });

             //注释这些代码，不返回responseBody，模拟timeout
            System.out.println("servlet方法");
            ServletResponse servletResponse = asyncContext.getResponse();
            servletResponse.setContentType("text/plain;charset=UTF-8");
            PrintWriter writer = servletResponse.getWriter();
            writer.println("输出responseBody");
            writer.flush();
        }
    }
}
