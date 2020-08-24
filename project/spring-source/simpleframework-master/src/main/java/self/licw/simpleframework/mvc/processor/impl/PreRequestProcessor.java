package self.licw.simpleframework.mvc.processor.impl;

import lombok.extern.slf4j.Slf4j;
import self.licw.simpleframework.mvc.RequestProcessorChain;
import self.licw.simpleframework.mvc.processor.RequestProcessor;

/**
 * 请求预处理，包括编码以及路径处理
 */
@Slf4j
public class PreRequestProcessor implements RequestProcessor {
    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        // 1、设置请求编码，将其统一设置为UTF-8
        requestProcessorChain.getRequest().setCharacterEncoding("UTF-8");
        // 2、将请求路径末尾的/剔除，为后续匹配Controller请求路径做准备
        String requestPath = requestProcessorChain.getRequestPath();
        if (requestPath.length() > 1 && requestPath.endsWith("/")){
            String newRequestPath = requestPath.substring(0, requestPath.length() -1);
            requestProcessorChain.setRequestPath(newRequestPath);
        }
        log.info("proprocess request {} {}", requestProcessorChain.getRequestMethod(), requestProcessorChain.getRequestPath());
        return true;
    }
}
