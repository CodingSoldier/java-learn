<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript"  src="/static/libs/jquery-2.0.3.js"></script>
<script type="text/javascript">
	var  ws;
	var url="ws://localhost:8080/talkSocket?username=${sessionScope.username}&param2=参数2测试";
	//进入本页面就打开websocket通道
   	ws = new WebSocket(url);
	
	ws.onopen = function(event){
		console.log(event);
	};
	
   	ws.onmessage = function(event){
   		var data = JSON.parse(event.data);
   		//欢迎谁，data中有alert才执行
   		if(data.alert){
    		$("#content").append(data.alert+"<br/>");
   		}
   		//在线用户列表刷新
   		if(data.names){
   			$("#userList").html("");
   			$(data.names).each(function(){
   				$("#userList").append("<input type='checkbox' value='"+this+"'/>"+this+" <br/>");
   			});     			
   		}
   		//聊天内容
   		if(data.sendMsg){
			$("#content").append(data.date+"<br/>"+data.sendMsg+"<br/>");    			
   		}

		if(data.socketMsg){
			console.log(data.socketMsg)
		}
   	};
	
	//发送聊天信息
	function send(){
		var sendMessage = $("#msg").val();
		var checkedUserName = $("#userList :checked");  
		var jsonObj = {
			sendMessage:sendMessage
		};
		
		//单聊
		if(checkedUserName.size() > 0){
			jsonObj.checkedUserName = checkedUserName.val();
			jsonObj.type = "singleTalk";
		}
		ws.send( JSON.stringify(jsonObj) );
		$("#msg").val("");
	}

	//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
	window.onbeforeunload = function(){
		ws.close();
	}

	function serverToClient(){
		$.ajax({
			url: "/useSocket"
		})
	}

</script>
</head>
<body>

	<button onclick="serverToClient()">后台向a1用户的前台推送消息</button>

	<h3>欢迎 ${sessionScope.username } 使用本系统！！</h3>

	<div  id="content"  style="
		border: 1px solid black; width: 400px; height: 300px;
		float: left;
	"  ></div>
	<div  id="userList"  style="
		border: 1px solid black; width: 100px; height: 300px;
		float:left;
	"  ></div>

	<div  style="clear: both;" >
		<input id="msg"  /><button  onclick="send();"  >send</button>
	</div>

</body>
</html>