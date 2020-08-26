<%@ page language="java" contentType="text/html; ISO-8859-1" pageEncoding="UTF-8"%>
<!--
加上这句 可以忽略EL表达式错误，不然会无法显示变量的值
-->
<%@page isELIgnored="false" %>
<html>
    <head>
        <title>form-page</title>
    </head>
    <body>
        <table>
            <h2 style="color: red"> 表单提交：</h2><br>
            <form id="headlineInfo" method="post" action="/simpleframework-master/test/model/view">
                头条说明:<input type="text" name="lineName"><br>
                结果：<h3>状态码：${result.code} 信息：${result.msg}</h3><br>
                <input type="submit" value="提交">
            </form>
        </table>
    </body>
</html>