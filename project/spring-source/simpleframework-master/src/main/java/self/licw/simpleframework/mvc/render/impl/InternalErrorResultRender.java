package self.licw.simpleframework.mvc.render.impl;

import self.licw.simpleframework.mvc.RequestProcessorChain;
import self.licw.simpleframework.mvc.render.ResultRender;

import javax.servlet.http.HttpServletResponse;

/**
 * 内部异常渲染器
 */
public class InternalErrorResultRender implements ResultRender {
    private String msg;

    public InternalErrorResultRender(String msg) {
        this.msg = msg;
    }

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        requestProcessorChain.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,msg);
    }
}
