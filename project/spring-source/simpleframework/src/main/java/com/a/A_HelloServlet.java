package com.a;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 pom.xml配置

 <packaging>war</packaging>

 <properties>
 <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
 </properties>

 <dependency>
	 <groupId>javax.servlet</groupId>
	 <artifactId>javax.servlet-api</artifactId>
	 <version>4.0.1</version>
	 <scope>provided</scope>
 </dependency>

 <dependency>
	 <groupId>javax.servlet.jsp</groupId>
	 <artifactId>javax.servlet.jsp-api</artifactId>
	 <version>2.3.3</version>
	 <scope>provided</scope>
 </dependency>


 <build>
 <finalName>simpleframework</finalName>
 <pluginManagement>
 <plugins>
 <plugin>
 <!-- https://mvnrepository.com/artifact/org.apache.maven.plugins/maven-compiler-plugin -->
 <groupId>org.apache.maven.plugins</groupId>
 <artifactId>maven-compiler-plugin</artifactId>
 <version>3.8.1</version>
 <configuration>
 <source>11</source>
 <target>11</target>
 </configuration>
 </plugin>
 <plugin>
 <groupId>org.apache.tomcat.maven</groupId>
 <artifactId>tomcat7-maven-plugin</artifactId>
 <version>2.2</version>
 <configuration>
 <path>/${project.artifactId}</path>
 <systemProperties>
 <java.util.logging.SimpleFormatter.format>%1$tT %3$s %5$s %n</java.util.logging.SimpleFormatter.format>
 </systemProperties>
 </configuration>
 </plugin>
 </plugins>
 </pluginManagement>
 </build>

 Idea Configuration
 	1、新建Maven配置，配置 tomcat7:run

 访问地址
 	http://localhost:8080/simpleframework/hello

 1、程序启动时，Servlet没初始化，在首次调用时，servlet才初始化，先执行init方法。init方法只执行一次
 2、接着到service方法，service方法调用doXX方法
 3、destroy方法在程序销毁时被执行
 */
@Slf4j
@WebServlet("/hello")
public class A_HelloServlet extends HttpServlet {

	@Override
	public void init() throws ServletException {
		System.out.println("init方法执行");
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("service方法执行");
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = "我的简易框架";
		req.setAttribute("name", name);
		log.debug("name is " + name);
		// 返回jsp页面
		req.getRequestDispatcher("/WEB-INF/jsp/hello.jsp").forward(req, resp);
	}

	@Override
	public void destroy() {
		System.out.println("destroy方法执行");
	}
}
