<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/9/18
  Time: 12:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="resouce/js/jquery.min.js"></script>
    <script src="resouce/js/vue.js"></script>
    <title>Title</title>
</head>
<body>

<div id="app">
    {{ message }}
</div>
<div id="app-2">
  <span v-bind:title="message">
    鼠标悬停几秒钟查看此处动态绑定的提示信息！
  </span>
</div>

<script>
    var app = new Vue({
        el: '#app',
        data: {
            message: 'Hello Vue!'
        }
    });
    var app2 = new Vue({
        el: '#app-2',
        data: {
            message: '页面加载于 ' + new Date().toLocaleString()
        }
    });
</script>

</body>
</html>