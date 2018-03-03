package ssm.tomcatwebsocket.b_chatroom;

import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/useSocket")
public class UseSocket extends HttpServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("socketMsg", "后台主动向前台推送消息");
        TalkSocket talkSocket = new TalkSocket();
        Session socketSession = talkSocket.userAndSession.get("a1"); //这个session是websocket的session，不是HTTP的session
        if(socketSession != null){
            socketSession.getAsyncRemote().sendText(JSON.toJSONString(map));
        }
    }
}
