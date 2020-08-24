package self.licw.simpleframework.mvc.processor.impl;

import lombok.extern.slf4j.Slf4j;
import self.licw.simpleframework.mvc.RequestProcessorChain;
import self.licw.simpleframework.mvc.processor.RequestProcessor;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

/**
 * jsp资源请求处理
 * http://localhost:8080/simpleframework-master/templates/form-page.jsp
 */
@Slf4j
public class JspRequestProcessor implements RequestProcessor {
    // jsp请求的RequestDispatcher名称
    private static final String JSP_DISPATCHER = "jsp";
    // jsp请求前缀
    private static final String JSP_RESOURCE_PREFIX = "/templates/";
    // 处理jsp的RequestDispatcher
    private RequestDispatcher jspDispatcher;

    public JspRequestProcessor(ServletContext servletContext) {
        this.jspDispatcher = servletContext.getNamedDispatcher(JSP_DISPATCHER);
        if (jspDispatcher == null){
            throw new RuntimeException("没有jsp调度器");
        }
        log.info("jsp调度器是{}", JSP_DISPATCHER);
    }

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        if (isJspResource(requestProcessorChain.getRequestPath())){
            jspDispatcher.forward(requestProcessorChain.getRequest(), requestProcessorChain.getResponse());
            return false;
        }
        return true;
    }

    /**
     * 是否是jsp请求
     */
    private boolean isJspResource(String url){
        return url.startsWith(JSP_RESOURCE_PREFIX);
    }
}
