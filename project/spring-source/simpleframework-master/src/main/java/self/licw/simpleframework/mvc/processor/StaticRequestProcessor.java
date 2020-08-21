package self.licw.simpleframework.mvc.processor;

import self.licw.simpleframework.mvc.RequestProcessorChain;

/**
 * 静态资源请求处理，包括图片、css、js等
 */
public class StaticRequestProcessor implements RequestProcessor {
    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        return false;
    }
}
