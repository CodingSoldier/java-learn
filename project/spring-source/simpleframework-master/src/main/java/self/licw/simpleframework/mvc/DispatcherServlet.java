package self.licw.simpleframework.mvc;

import self.licw.simpleframework.aop.AspectWeaver;
import self.licw.simpleframework.core.BeanContainer;
import self.licw.simpleframework.inject.DependencyInjector;
import self.licw.simpleframework.mvc.processor.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenpiqian
 * @date: 2020-08-21
 */
@WebServlet("/*")
public class DispatcherServlet extends HttpServlet {

    List<RequestProcessor> PROCESSOR = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        // 1、初始化容器
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("self.licw.o2o");
        new AspectWeaver().doAop();
        new DependencyInjector().doIoc();

        //2、初始化请求处理器责任链
        /**
         * PreRequestProcessor 责任链做预处理
         */
        PROCESSOR.add(new PreRequestProcessor());
        PROCESSOR.add(new StaticRequestProcessor());
        PROCESSOR.add(new JspRequestProcessor());
        PROCESSOR.add(new ControllerRequestProcessor());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }
}














