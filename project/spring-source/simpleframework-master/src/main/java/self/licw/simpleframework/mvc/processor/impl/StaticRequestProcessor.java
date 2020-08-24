package self.licw.simpleframework.mvc.processor.impl;

import lombok.extern.slf4j.Slf4j;
import self.licw.simpleframework.mvc.RequestProcessorChain;
import self.licw.simpleframework.mvc.processor.RequestProcessor;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

/**
 * 静态资源请求处理，包括图片、css、js等
 * http://localhost:8080/simpleframework-master/static/icon.png
 */
@Slf4j
public class StaticRequestProcessor implements RequestProcessor {
    // 静态资源请求的RequestDispatcher名称，在web.xml中配置
    public static final String DEFAULT_TOMCAT_SERVLET = "default";
    // 静态目录
    public static final String STATIC_RESOURCE_PREFIX = "/static/";
    // tomcat默认请求派发器
    RequestDispatcher defaultDispatcher;

    public StaticRequestProcessor(ServletContext servletContext) {
        this.defaultDispatcher = servletContext.getNamedDispatcher(DEFAULT_TOMCAT_SERVLET);
        if (this.defaultDispatcher == null){
            throw new RuntimeException("There is no default tomcat servlet");
        }
        log.info("没有静态资源处理器", DEFAULT_TOMCAT_SERVLET);
    }

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        // 1、请求url是否以 /static/ 开头
        if (isStaticResource(requestProcessorChain.getRequestPath())){
            // 2、静态资源，使用defaultDispatcher转发请求
            defaultDispatcher.forward(requestProcessorChain.getRequest(), requestProcessorChain.getResponse());
            return false;
        }
        // 进入下一处理器
        return true;
    }

    // 通过请求路径前缀判断是否是静态资源请求 /static/
    private boolean isStaticResource(String requestPath){
        return requestPath.startsWith(STATIC_RESOURCE_PREFIX);
    }
}
