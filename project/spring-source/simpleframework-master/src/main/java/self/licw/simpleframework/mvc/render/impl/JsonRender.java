package self.licw.simpleframework.mvc.render.impl;

import com.google.gson.Gson;
import self.licw.simpleframework.mvc.RequestProcessorChain;
import self.licw.simpleframework.mvc.render.ResultRender;

import java.io.PrintWriter;

/**
 * json渲染器
 */
public class JsonRender implements ResultRender {
    private Object jsonData;
    public JsonRender(Object result) {
        this.jsonData = result;
    }

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        //设置响应头
        requestProcessorChain.getResponse().setContentType("application/json");
        requestProcessorChain.getResponse().setCharacterEncoding("UTF-8");
        //响应流写入经过gson格式化之后的处理结果
        PrintWriter printWriter = requestProcessorChain.getResponse().getWriter();
        Gson gson = new Gson();
        printWriter.write(gson.toJson(jsonData));
        printWriter.flush();
    }
}
