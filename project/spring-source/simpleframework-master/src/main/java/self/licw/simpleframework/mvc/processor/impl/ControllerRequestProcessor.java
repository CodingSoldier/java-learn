package self.licw.simpleframework.mvc.processor.impl;

import self.licw.simpleframework.mvc.RequestProcessorChain;
import self.licw.simpleframework.mvc.processor.RequestProcessor;

/**
 * controller请求处理器
 * 功能：
 * 1、针对特定请求，选择匹配的Controller方法进行处理
 * 2、解析请求里的参数及其对应的值，并赋值给Controller方法的参数
 * 3、选择合适的Render，为后续请求处理结果的渲染做准备
 */
public class ControllerRequestProcessor implements RequestProcessor {
    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        return false;
    }
}
