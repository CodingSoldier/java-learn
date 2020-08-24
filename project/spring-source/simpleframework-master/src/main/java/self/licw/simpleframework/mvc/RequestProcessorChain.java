package self.licw.simpleframework.mvc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import self.licw.simpleframework.mvc.processor.RequestProcessor;
import self.licw.simpleframework.mvc.render.ResultRender;
import self.licw.simpleframework.mvc.render.impl.DefaultRequestRender;
import self.licw.simpleframework.mvc.render.impl.InternalErrorResultRender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

/**
 * 1、以责任链的模式执行注册的请求处理器
 * 2、委派给特定的Render实例对处理后的结果进行渲染
 */
@Data
@Slf4j
public class RequestProcessorChain {
    // 请求处理器迭代器
    private Iterator<RequestProcessor> requestProcessorIterator;
    // 请求request
    private HttpServletRequest request;
    // 响应response
    private HttpServletResponse response;

    // 请求方法
    private String requestMethod;
    // 请求路径
    private String requestPath;
    // 响应状态码
    private int responseCode;
    // 对应本次请求的结果渲染器实例
    private ResultRender resultRender;

    public RequestProcessorChain(Iterator<RequestProcessor> requestProcessorIterator, HttpServletRequest request, HttpServletResponse response) {
        this.requestProcessorIterator = requestProcessorIterator;
        this.request = request;
        this.response = response;

        this.requestMethod = request.getMethod();
        this.requestPath = request.getPathInfo();
        this.responseCode = HttpServletResponse.SC_OK;
    }

    /**
     * 以责任链的模式执行请求链
     */
    public void doRequestProcessorChain(){
        try {
            // 1、遍历请求处理器类列表
            while (requestProcessorIterator.hasNext()){
                // 2、请求处理器返回false，退出循环
                if (!requestProcessorIterator.next().process(this)){
                    break;
                }
            }
        }catch (Exception e){
            // 3、如果请求处理器出现异常，则交由内部异常渲染器处理
            this.resultRender = new InternalErrorResultRender(e.getMessage());
            log.error("doRequestProcessorChain error: ", e);
        }
    }

    /**
     * 渲染器执行
     */
    public void doRender(){
        // 1、如果请求处理器实现类为选择合适的渲染器，则使用默认的
        if (this.resultRender == null){
            this.resultRender = new DefaultRequestRender();
        }
        try {
            this.resultRender.render(this);
        } catch (Exception e) {
            log.error("doRender error: ", e);
            throw new RuntimeException(e);
        }
    }

}
