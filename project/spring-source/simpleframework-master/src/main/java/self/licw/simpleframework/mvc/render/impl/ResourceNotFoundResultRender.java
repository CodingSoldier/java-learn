package self.licw.simpleframework.mvc.render.impl;

import self.licw.simpleframework.mvc.RequestProcessorChain;
import self.licw.simpleframework.mvc.render.ResultRender;

import javax.servlet.http.HttpServletResponse;

/**
 * 资源找不到时使用的渲染器
 */
public class ResourceNotFoundResultRender implements ResultRender {
    private String path;
    private String method;

    public ResourceNotFoundResultRender(String path, String method) {
        this.path=path;
        this.method=method;
    }

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        requestProcessorChain.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND, "获取不到"+path+"的请求方法"+method);
    }
}
