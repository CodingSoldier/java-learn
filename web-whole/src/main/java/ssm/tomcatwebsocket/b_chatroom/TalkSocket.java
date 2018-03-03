package ssm.tomcatwebsocket.b_chatroom;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/talkSocket")
public class TalkSocket {

	private String username;
	//存储Session、username。并将username和session存储为map结构，建立username和session的一一对应关系
	private static Set<Session> sessions = new HashSet<>();
	private static Set<String> usernames = new HashSet<>();
	public static HashMap<String, Session> userAndSession = new HashMap<String, Session>();

/* 执行new WebSocket("ws://localhost:8080/talkSocket")，就会执行本中的@OnOpen方法
* url中可以带参数，比如username*/
	@OnOpen
	public void open(Session session){
		String queryString="";
		HashMap<String, String> queryMap = new HashMap<String, String>();
		try {
			queryString = URLDecoder.decode(session.getQueryString(), "UTF-8");
		} catch (UnsupportedEncodingException e){
			System.out.println("接收参数编码出错");
		}
		String[] queryList = StringUtils.split(queryString, "&");
		for (String str : queryList){
			String queryKey = StringUtils.substringBefore(str, "=");
			String queryVal = StringUtils.substringAfter(str, "=");
			if (StringUtils.isNotEmpty(queryKey)){
				queryMap.put(queryKey, queryVal);
			}
		}

		username = queryMap.get("username");
		String welcomeMsg = "欢迎   "+username+"   进入聊天室";
		System.out.println("queryString:  "+queryString);
		System.out.println("username:   "+username);

		//给每个session广播信息
		sessions.add(session);
		usernames.add(username);
		userAndSession.put(username, session);
		Message message = new Message();
		message.setAlert(welcomeMsg);
		message.setNames(usernames);
		//传给前台的信息是一个json数据
		brodercast(sessions, JSON.toJSONString(message));
	}

	@OnMessage
	public void onMessage(Session session, String msg){
		ContentVo contentVo = JSON.parseObject(msg, ContentVo.class);
		Message message = new Message();
		message.setDate(new Date().toLocaleString());
		message.setSendMsg(contentVo.getSendMessage());
		//单聊
		if("singleTalk".equals(contentVo.getType())){
			userAndSession.get(contentVo.getCheckedUserName())
				.getAsyncRemote().sendText(JSON.toJSONString(message));
		}else {  //群聊
			brodercast(sessions, JSON.toJSONString(message));
		}
	}

	@OnClose
	public void close(Session session){
		sessions.remove(session);
		usernames.remove(username);
		userAndSession.remove(username);
		Message message = new Message();
		String msg = username+"离开聊天室";
		message.setAlert(msg);
		message.setNames(usernames);
		brodercast(sessions, JSON.toJSONString(message));

	}

    //捕获客户端强行关闭websocket链接发生的异常
    @OnError
    public void onError(Throwable e, Session session){
		close(session);
        if(e instanceof IOException){
            System.out.println(username+"   客户端强行关闭websocket链接,");
			e.printStackTrace();
        }
    }

	//广播方法，对每个session广播msg
	public void brodercast(Set<Session> sessions, String msg){
		for(Session session:sessions){
			try {
				session.getBasicRemote().sendText(msg);
				//前台通过  ws.onmessage = function(event){}接收信息
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}








