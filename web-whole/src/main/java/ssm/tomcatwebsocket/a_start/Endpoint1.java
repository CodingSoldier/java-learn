package ssm.tomcatwebsocket.a_start;

/* 使用@ServerEndpoint来注册一个Websocket的ServerEndpoint类。本类会被tomcat注入到一个Set集合中，
 * 同时将Set集合传入WebSocketConfig的getAnnotatedEndpointClasses(Set<Class<?>> scans)方法中*/

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/endpoint1")
public class Endpoint1 {

	public Endpoint1(){
		System.out.println("前端浏览器每个标签也发送请求时：本构造函数也执行了，@ServerEndpoint不是单例");
	}

	/* 前端通过  new WebSocket/MozWebSocket("ws://localhost:8080/endpoint1") 来触发open方法*/
	@OnOpen
	public void open(Session session){
		System.out.println("前端发送请求，打开通道。  sessionId是："+session.getId());
	}

	/* 前台的ws.send()方法触发本方法,本方法可以给前台发送消息 */
	@OnMessage
	public void message(Session session, String msg){
		System.out.println("前台ws.send()发送的文本："+msg);
		try {
			//向前台发送消息
			session.getBasicRemote().sendText("后台向前台发送的信息"+msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@OnClose
	public void close(Session session){
		System.out.println("页面关闭，通道关闭，sessionId是："+session.getId());
	}

}











