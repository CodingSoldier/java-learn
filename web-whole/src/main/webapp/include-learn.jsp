<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="/static/libs/jquery-2.0.3.js"></script>
</head>
<body>
<%--
处理引入的html中文乱码问题。web.xml中添加:
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">

<jsp-config>
    <jsp-property-group>
      <url-pattern>*.html</url-pattern>
      <page-encoding>UTF-8</page-encoding>
    </jsp-property-group>
</jsp-config>
--%>

    <%--编译阶段执行，导入一个组件--%>
    <%@ include file="/static/component/c1/c1.html" %>

    <%--请求处理阶段执行，引入html报错--%>
    <%--<jsp:include page="/static/page/templete/t2.html" flush="true"/>--%>

</body>
</html>
