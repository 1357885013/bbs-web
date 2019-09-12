<%@ page import="com.ljj.entity.User" %>
<%@ page import="com.ljj.service.impl.UserService" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>管理用户</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
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

        .table-responsive {
            max-width: 1000px;
            margin: 20px auto;
        }

        table {
            background-color: rgba(255, 255, 255, 0.90);
        }

        table td {
            vertical-align: middle !important;
            width: min-content;
        }

        .icon-button-del {
            border: 1px black solid;
            border-radius: 2px;
            padding: 1px;
        }

        .icon-button-add {
            border: 1px black solid;
            border-radius: 2px;
        }
    </style>
</head>
<body>
</h1>
<div class="center-block">
    <%
        UserService userService = new UserService();
        ArrayList<User> users = userService.list();
        pageContext.setAttribute("users", users);
    %>
    <div class="table-responsive">
        <button style="float: right" type="button" class="btn btn-primary" data-toggle="modal" data-target="#modal-add">
            添加用户
        </button>
        <table class="table" classId="{$classId}">
            <thead>
            <tr>
                <th>#</th>
                <th>用户名</th>
                <th>权限</th>
                <th>禁用</th>
                <th>修改</th>
                <th>注销</th>
            </tr>
            </thead>
            <tbody id="tbody">
            <c:forEach var="user" items="${users}">
                <tr id=${user.id}>
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td><c:choose><c:when
                            test="${user.isAdmin()}">管理员</c:when><c:otherwise>普通用户</c:otherwise></c:choose></td>
                    <td><c:choose><c:when
                            test="${user.isBaned()}">被禁用</c:when><c:otherwise>正常</c:otherwise></c:choose></td>
                    <td>
                        <button type="button" style="display: inline" class="btn btn-warning  btn-sm btn-change"
                                name="text" data-toggle="modal" data-target="#modal-change">修改
                        </button>
                    </td>
                    <td>
                        <button type="button" style="display: inline" class="btn btn-danger  btn-sm"
                                onclick="if(confirm('将注销用户！'))delStu(this)">注销
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<%--行副本，添加新行是复制用--%>
<div hidden="hidden">
    <table>
        <tbody>
        <tr class="copyOfRow">
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td>
                <button type="button" style="display: inline" class="btn btn-warning  btn-sm btn-change"
                        name="text" data-toggle="modal" data-target="#modal-change">修改
                </button>
            </td>
            <td>
                <button type="button" style="display: inline" class="btn btn-danger  btn-sm"
                        onclick="if(confirm('将注销用户！'))delStu(this)">注销
                </button>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<!--添加学生信息 弹出框-->
<div class="modal fade" id="modal-add" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">添加用户</h4>
            </div>
            <form id="form-add" class="container-fluid">
                <div class="form-group">
                    <label for="add-name">用户名</label>
                    <input type="text" class="form-control" id="add-name" name="username" placeholder="用户名">
                </div>
                <div class="form-group">
                    <label for="add-password">密码</label>
                    <input type="password" class="form-control" id="add-password" name="password" placeholder="密码">
                </div>
                <div class="form-group">
                    <label for="add-repassword">确认密码</label>
                    <input type="password" class="form-control" id="add-repassword" placeholder="确认密码">
                </div>
                <div class="form-group">
                    <input type="checkbox" class="" id="add-admin" name="admin">
                    <label for="add-admin">设为管理员</label>
                </div>
                <div class="form-group">
                    <input type="checkbox" class="" id="add-ban" name="ban">
                    <label for="add-ban">禁用账户</label>
                </div>
                <input type="hidden" name="type" value="add">
                <div class="modal-footer">
                    <button type="button" class="modal-btn btn btn-primary">添加</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<!--修改学生信息 弹出框-->
<div class="modal fade" id="modal-change" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">修改</h4>
            </div>
            <form id="form-change" class="container-fluid">
                <div class="form-group">
                    <label for="add-name">用户名</label>
                    <input type="text" class="form-control" id="change-name" name="username" placeholder="用户名">
                </div>
                <div class="form-group">
                    <label for="add-password">密码</label>
                    <input type="password" class="form-control" id="change-password" name="password" placeholder="不修改则留空">
                </div>
                <div class="form-group">
                    <label for="add-repassword">确认密码</label>
                    <input type="password" class="form-control" id="change-repassword" placeholder="不修改则留空">
                </div>
                <div class="form-group">
                    <input type="checkbox" class="" id="change-admin" name="admin">
                    <label for="change-admin">设为管理员</label>
                </div>
                <div class="form-group">
                    <input type="checkbox" class="" id="change-ban" name="ban">
                    <label for="change-ban">禁用账户</label>
                </div>
                <input type="hidden" id="change-userId" name="userId" value="-1">
                <input type="hidden" name="type" value="change">
                <div class="modal-footer">
                    <button type="button" class="modal-btn btn btn-primary">修改</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<script>
    $('#modal-change').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget); // Button that triggered the modal
        var modal = $(this);
        var ele = button.parent().parent();
        id = ele.attr("id");
        ele = ele.children();

        copy = $("#form-change").children();
        $("#change-userId").val(id);
        $(copy[0]).find("input").val($(ele[1]).text());
        $("#change-admin").prop("checked",$(ele[2]).text()==="管理员");
        $(copy[4]).find("input").prop("checked",$(ele[3]).text()==="被禁用");

        modal.find('.modal-btn').off();
        modal.find('.modal-btn').on("click", function () {
            $.ajax({ //TODO:点击修改后禁用输入框
                url: "user",
                type: "post",
                data: $("#form-change").serialize(),
                success: function (data) {
                    data = JSON.parse(data);
                    showMessage(data["message"]);
                    if (data["code"] === 1) {
                        $(ele[1]).text($(copy[0]).find("input").val());
                        $(ele[2]).text($(copy[3]).find("input").is(":checked")?"管理员":"普通用户");
                        $(ele[3]).text($(copy[4]).find("input").is(":checked")?"被禁用":"正常");
                        modal.modal('hide');
                    }
                }
            });
        });
    });

    $('#modal-add').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        var modal = $(this);
        modal.find('.modal-btn').off();
        modal.find('.modal-btn').on("click", function () {
            $.ajax({
                url: "user",
                type: "post",
                data: $("#form-add").serialize() + "&time=" + Date.parse(new Date()),
                success: function (data) {
                    data = JSON.parse(data);
                    console.log(data);
                    showMessage(data['message']);
                    if (data['code'] === 1) {
                        modal.modal("hide");
                        var row = $(".copyOfRow").clone();
                        row.removeClass("copyOfRow").attr("id", data["id"])
                        var trs = row.children();
                        $(trs[0]).html(data['id']);
                        $(trs[1]).html($('#add-name').val());
                        $(trs[2]).html($('#add-admin').is(":checked") ? "管理员" : "普通用户");
                        $(trs[3]).html($('#add-ban').is(":checked") ? "被禁用" : "正常");
                        row.appendTo("#tbody");
                    } else if (data['code'] === -1) {
                        $("#add-name").val("")
                    }
                }
            });
        });
    });

    function delStu(ele) {
        $.ajax({
            url: "user",
            type: "post",
            data: "id=" + $(ele).parent().parent().attr("id") + "&type=delete",
            success: function (data) {
                data = JSON.parse(data);
                if (1 == data['code']) {
                    $(ele).parent().parent().remove();
                }
                showMessage(data['message']);
            }
        });
    }
</script>


<!--一下为公用-->
<!--信息提示模态框-->
<div class="modal fade" id="mes" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-body">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<script>
    function showMessage(message) {
        var mesText = $("#mes .modal-body");
        if (1 == $("#mes.in").length)
            mesText.append("<h1>" + message + "</h1>");
        else
            mesText.empty().append("<h1>" + message + "</h1>");

        //$("#mes").modal('keyboard');
        $("#mes").modal('show');
    }

    function progressMessage(data) {
        if (data['message'])
            showMessage(data['message']);
        if (data['address']) {
            setTimeout("showMessage('跳转中...')", 2000);
            //前面要加上'http://'才是绝对地址
            temp = document.domain + data['address'];
            setTimeout("window.location.href='http://" + temp + "'", 3000);
        }
    }

    function reDir(pathName) {
        if (document.location.pathname != pathName) {
            showMessage('跳转中...');
            //前面要加上'http://'才是绝对地址
            temp = document.domain + pathName;
            setTimeout("window.location.href='http://" + temp + "'", 1000);
        }
    }
</script>
</body>
</html>