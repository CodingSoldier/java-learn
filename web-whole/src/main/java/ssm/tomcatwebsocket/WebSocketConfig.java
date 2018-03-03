package ssm.tomcatwebsocket;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Set;

/* 新建WebSocketConfig实现ServerApplicationConfig接口，
 * tomcat启动时就会执行此实现类中的getAnnotatedEndpointClasses、getEndpointConfigs方法*/
public class WebSocketConfig implements ServerApplicationConfig {
	/* getAnnotatedEndpointClasses：使用注解方式来注册websocket
	 * getEndpointConfigs：使用接口方式注册websocket
	 * 本教程使用注解方式*/

	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scans) {
		//System.out.println("tomcat一启动就执行本方法getAnnotatedEndpointClasses");

		//使用@ServerEndpoint注解的类会被设置到Set集合中，scans有了大小
		System.out.println(scans.size());

		//传入set集合，可以对set集合进行过滤，再返回set集合
		return scans;
	}

	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> arg0) {
		//System.out.println("tomcat一启动就执行本方法getEndpointConfigs，此方法先于getAnnotatedEndpointClasses执行");
		return null;
	}

}












