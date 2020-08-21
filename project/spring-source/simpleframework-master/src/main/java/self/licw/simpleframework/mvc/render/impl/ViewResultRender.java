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
            //1.如果入参类型是ModelAndView 直接赋值给成员变量
            this.modelAndView = (ModelAndView) result;
        }else if (result instanceof String){
            //2.如果传入参数是String 则为视图 需要包装后才赋值给成员变量
            this.modelAndView = new ModelAndView().setView((String)result);
        }else{
            //3。针对其他情况，直接抛出异常
            throw new RuntimeException("illegal request result type");
        }
    }

    /**
     * 将请求处理结果按照视图路径转发至对应视图进行展示
     * @param requestProcessorChain
     * @throws Exception
     */
    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        HttpServletRequest request = requestProcessorChain.getRequest();
        HttpServletResponse response = requestProcessorChain.getResponse();
        String path = modelAndView.getView();
        Map<String,Object> model = modelAndView.getModel();
        //从model中遍历所有属性，设置request中的参数
        for (Map.Entry<String,Object> entry:model.entrySet()){
            System.out.println(entry.getValue());
            request.setAttribute(entry.getKey(),entry.getValue());
        }
        //JSP  带上属性跳转到对应的jsp路径，用的还是该请求的request
        request.getRequestDispatcher(VIEW_PATH + path).forward(request,response);
    }
}
