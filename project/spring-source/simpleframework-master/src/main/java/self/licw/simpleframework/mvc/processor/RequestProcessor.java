package self.licw.simpleframework.mvc.processor;

import self.licw.simpleframework.mvc.RequestProcessorChain;

/**
 * 处理processor责任链逻辑（process方法）
 */
public interface RequestProcessor {

    boolean process(RequestProcessorChain requestProcessorChain) throws Exception;

}
