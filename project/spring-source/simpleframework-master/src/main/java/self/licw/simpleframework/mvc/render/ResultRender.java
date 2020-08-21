package self.licw.simpleframework.mvc.render;

import self.licw.simpleframework.mvc.RequestProcessorChain;

/**
 * 渲染请求结果
 */
public interface ResultRender {
    //执行渲染
    void render(RequestProcessorChain requestProcessorChain) throws Exception;
}
