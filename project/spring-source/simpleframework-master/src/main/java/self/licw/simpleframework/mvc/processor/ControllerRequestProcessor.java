package self.licw.simpleframework.mvc.processor;

import self.licw.simpleframework.mvc.RequestProcessorChain;

/**
 * controller请求处理器
 */
public class ControllerRequestProcessor implements RequestProcessor {
    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        return false;
    }
}
