package self.licw.simpleframework.mvc.processor;

import self.licw.simpleframework.mvc.RequestProcessorChain;

/**
 * 请求预处理，包括编码以及路径处理
 */
public class PreRequestProcessor implements RequestProcessor {
    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        return false;
    }
}
