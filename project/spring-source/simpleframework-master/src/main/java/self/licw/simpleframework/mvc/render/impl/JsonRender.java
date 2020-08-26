package self.licw.simpleframework.mvc.render.impl;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import self.licw.simpleframework.mvc.RequestProcessorChain;
import self.licw.simpleframework.mvc.render.ResultRender;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * json渲染器
 */
@Slf4j
public class JsonRender implements ResultRender {
    private Object jsonData;

    public JsonRender(Object result) {
        this.jsonData = result;
    }

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception{
        // 设置响应头
        HttpServletResponse response = requestProcessorChain.getResponse();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        // 输出数据
        try (PrintWriter printWriter = response.getWriter()){
            printWriter.write(gson.toJson(this.jsonData));
            printWriter.flush();
        }
    }
}
