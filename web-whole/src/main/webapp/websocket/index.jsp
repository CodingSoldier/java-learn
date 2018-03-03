<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript"  src="/static/libs/jquery-2.0.3.js"></script>
<script type="text/javascript">
	var ws;
	var url = "ws://localhost:8080/endpoint1";
	function connect(){
		//打开websocket通道
		if("WebSocket" in window){
			ws = new WebSocket(url);
		} else if("MozWebSocket" in window){
			ws = new MozWebSocket(url);
		} else{
			alert("此浏览器不支持websocket");
			return;
		}

		//接收后台@OnMessage方法发送给前台的信息,前台接收到的是一个event对象
		ws.onmessage = function(event){
			console.log(event);
			$("#show-msg").html(event.data);
		}
	}

	function send(){
		var value = $("#msg").val();
		//connect()方法先new出ws对象，再发送文本
		ws.send(value);
	}

</script>
</head>
<body>

	<button  onclick="connect();">connect</button>
	<hr><hr>
	<input id="msg">
	<button onclick="send()">先打开websocket通道，再发送input中的文本信息</button>
	<button onclick="close()">关闭websocket</button>
	<hr><hr>
	<div>后台发过来的文本：</div>
	<div id="show-msg"></div>

</body>
</html>