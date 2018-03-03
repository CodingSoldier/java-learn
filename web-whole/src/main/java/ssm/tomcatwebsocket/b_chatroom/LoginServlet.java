package ssm.tomcatwebsocket.b_chatroom;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    public LoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//将username设置到session中，talk.jsp将username通过websocket传给TalkSocket.java
		request.setCharacterEncoding("utf-8");
		String username = request.getParameter("username");
		request.getSession().setAttribute("username", username);
		response.sendRedirect("/websocket/talk.jsp");
	}

}
