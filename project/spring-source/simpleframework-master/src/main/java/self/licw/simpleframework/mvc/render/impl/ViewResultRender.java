package self.licw.simpleframework.mvc.render.impl;

import self.licw.simpleframework.mvc.RequestProcessorChain;
import self.licw.simpleframework.mvc.render.ResultRender;
import self.licw.simpleframework.mvc.type.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 页面渲染器
 */
public class ViewResultRender implements ResultRender {

    private static final String VIEW_PATH = "/templates/";
    private ModelAndView modelAndView;

    public ViewResultRender(Object result) {
        if (result instanceof ModelAndView){
            // 1、result是ModelAndView，直接赋值给成员变量
            this.modelAndView = (ModelAndView)result;
        }else if (result instanceof String){
            // 2、result是String，创建ModelAndView并设置view路径
            this.modelAndView = new ModelAndView().setView((String) result);
        }else{
            // 3、其他情况抛出异常
            throw new RuntimeException("返回结果不正确");
        }
    }

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        HttpServletRequest request = requestProcessorChain.getRequest();
        HttpServletResponse response = requestProcessorChain.getResponse();
        String path = modelAndView.getView();
        Map<String, Object> model = modelAndView.getModel();
        // 从model中遍历所有属性，并设置request中的参数
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }
        // 使用JSPDispatcher转发请求
        request.getRequestDispatcher(VIEW_PATH + path).forward(request, response);
    }
}
