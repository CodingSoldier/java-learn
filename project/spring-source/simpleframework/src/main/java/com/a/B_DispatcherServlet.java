package com.a;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**

 Spring MVC仅通过DispatcherServlet这一个Servlet进行请求派发
 DispatcherServlet主要功能：
 	拦截所有请求 @WebServlet("/")
 	解析请求
 	派发给对应的Controller方法

 */
@Slf4j
@WebServlet("/")
public class B_DispatcherServlet extends HttpServlet {

	@Override
	public void init() throws ServletException {
		System.out.println("init方法执行");
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("service方法执行");
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = "我的简易框架";
		req.setAttribute("name", name);
		log.debug("name is " + name);
		// 返回jsp页面
		req.getRequestDispatcher("/WEB-INF/jsp/hello.jsp").forward(req, resp);
	}

	@Override
	public void destroy() {
		System.out.println("destroy方法执行");
	}
}
