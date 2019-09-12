<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录</title>

    <script src="resouce/js/cookie.js"></script>
    <script src="resouce/js/jquery-3.1.1.js" type="application/javascript"></script>
    <script src="resouce/js/bootstrap.min.js"></script>

    <link href="resouce/css/bootstrap.min.css" rel="stylesheet">

    <script src="resouce/js/validate-then-ajax-send.js"></script>
    <style>
        .body {
            width: 400px;
            margin: 200px auto;
            border: 1px solid gray;
            padding: 20px;
            border-radius: 5px;
        }
    </style>
</head>
<body>

<div class="body">

    <div id="login"> <!--登录框-->
        <div style="border-bottom: solid #ddd 1px;margin: 0 -12px;margin-bottom: 22px;"><h2
                style="text-align: center;margin-top: 0px;">登录</h2></div>
        <form id="form" method="post" action="login">
            <div class="form-group">
                <label for="username">邮箱</label>
                <input type="text" class="form-control" id="username" name="username" placeholder="邮箱">
                <!--type写不对不会实时验证字段正确性-->
            </div>
            <div class="form-group">
                <label for="password">密码</label>
                <input type="password" class="form-control" id="password" name="password" placeholder="密码">
            </div>

            <input id="clientId" type="hidden" name="clientId">
            <p style="color: darkred;"><%=request.getAttribute("message")%>
            </p>
            <div class="btn-div">
                <button onclick="submit_login(this)" class="btn btn-default">登录</button>
            </div>
        </form>
    </div>
    <script>
        form = $('#form');

        //form.validate(myValid);

        function submit_login(ele) {
            var err = form.valid();
            if (!err) {
                return 0;
            }
            $.ajax({
                url: "{:url('student/login/login')}",
                type: "post",
                data: form.serialize(),
                success: function (data) {
                    data = JSON.parse(data);
                    if ('login' == data['type'] && 0 == data['code']) {
                        refreshImg();
                        $("#captcha").val("");
                    }
                    sessionStorage.clear();

                    progressMessage(data);
                }
            });
        }

        function refreshImg() {
            $("#captchaImg").attr("src", "{:url('common/captcha/getImg')}?rnd=" + Math.random());
        }
    </script>
</div>
</body>
</html>