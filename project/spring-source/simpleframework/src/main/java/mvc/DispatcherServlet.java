package mvc;

import lombok.extern.slf4j.Slf4j;
import org.core.BeanContainer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自研MVC框架
 */
@Slf4j
@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

	@Override
	public void init() {
		// 1、初始化容器
		BeanContainer beanContainer = BeanContainer.getInstance();
		beanContainer.loadBeans("com");
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("service方法执行");
		doGet(req, resp);
	}

}
