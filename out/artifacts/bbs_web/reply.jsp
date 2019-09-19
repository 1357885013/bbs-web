<%@ page import="com.ljj.dao.IReplyDao" %>
<%@ page import="com.ljj.entity.Post" %>
<%@ page import="com.ljj.entity.Reply" %>
<%@ page import="com.ljj.factory.MySqlSessionFactory" %>
<%@ page import="org.apache.ibatis.session.SqlSession" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>${postId}</title>
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
        int postId = Integer.parseInt(request.getParameter("postId"));
        SqlSession sqlSession = MySqlSessionFactory.getSqlSession();

//        Post post = sqlSession.selectOne("getAll", postId);

        IReplyDao dao = sqlSession.getMapper(IReplyDao.class);
        Post post = dao.getAll(postId);

        List<Reply> replys = post.getReplies();
        pageContext.setAttribute("post", post);
        pageContext.setAttribute("replys", replys);
    %>


    <div class="table-responsive">
        <button style="float: right" type="button" class="btn btn-primary" data-toggle="modal" data-target="#modal-add">
            回复
        </button>
        <h1>${post.title}</h1>
        <table class="table" classId="{$classId}">
            <thead>
            <tr>
                <th>#</th>
                <th>内容</th>
                <th>时间</th>
                <th>发帖人</th>
                <c:if test="${user.isAdmin()}">

                    <th>修改</th>
                    <th>删除</th>
                </c:if>
            </tr>
            </thead>
            <tbody id="tbody">
            <tr>
                <td>1</td>
                <td>${post.context}</td>
                <td>${post.getFormatTime()}</td>
                <td>${post.getUserName()}</td>
            </tr>
            <c:forEach var="reply" items="${replys}">
                <tr id=${reply.id}>
                    <td>${reply.id}</td>
                    <td>${reply.context}</td>
                    <td>${reply.getFormatTime()}</td>
                    <td>${reply.getUserName()}</td>
                    <c:if test="${user.isAdmin() ||  user.getId() == reply.getUserId()}">

                        <td>
                            <button type="button" style="display: inline" class="btn btn-warning  btn-sm btn-change"
                                    name="text" data-toggle="modal" data-target="#modal-change">修改
                            </button>
                        </td>
                    </c:if>
                    <c:if test="${user.isAdmin() || user.getId() == post.getUserId()|| user.getId() == reply.getUserId()}">
                        <td>
                            <button type="button" style="display: inline" class="btn btn-danger  btn-sm"
                                    onclick="if(confirm('将完全删除版块！'))delStu(this)">删除
                            </button>
                        </td>
                    </c:if>
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
            <td>
                <button type="button" class="btn btn-info btn-sm" style="display: inline"
                        onclick="window.location.replace('post?blockId='+$(this).parent().parent().attr('id'))">
                    进入
                </button>
            </td>
            <td>
                <button type="button" style="display: inline" class="btn btn-warning  btn-sm btn-change"
                        name="text" data-toggle="modal" data-target="#modal-change">修改
                </button>
            </td>
            <td>
                <button type="button" style="display: inline" class="btn btn-danger  btn-sm"
                        onclick="if(confirm('将完全删除版块！'))delStu(this)">删除
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
                <h4 class="modal-title">回复</h4>
            </div>
            <form id="form-add" class="container-fluid">
                <div class="form-group">
                    <label for="add-content">内容</label>
                    <input type="text" class="form-control name" id="add-content" name="content" placeholder="内容">
                </div>
                <input type="hidden" name="type" value="add">
                <div class="modal-footer">
                    <button type="button" class="modal-btn btn btn-primary">回复</button>
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
                    <label>内容</label>
                    <input type="text" class="form-control" name="content" placeholder="内容">
                </div>
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
    var blockId = ${blockId};
    var postId = ${postId};
    $('#modal-change').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget); // Button that triggered the modal
        var modal = $(this);
        var ele = button.parent().parent();
        id = ele.attr("id");


        ele = ele.children();
        copy = $("#form-change").children();
        $(copy[0]).find("input").val($(ele[1]).text());
        $(copy[1]).find("input").val($(ele[2]).text());

        modal.find('.modal-btn').off();
        modal.find('.modal-btn').on("click", function () {
            $.ajax({ //TODO:点击修改后禁用输入框
                url: "reply",
                type: "post",
                data: $("#form-change").serialize() + "&id=" + id + "&blockId=" + blockId + "&postId=" + postId,
                success: function (data) {
                    data = JSON.parse(data);
                    showMessage(data["message"]);
                    if (data["code"] === 1) {
                        $(ele[1]).text($(copy[0]).find("input").val());
                        $(ele[2]).text($(copy[1]).find("input").val());
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
                url: "reply",
                type: "post",
                data: $("#form-add").serialize() + "&blockId=" + blockId + "&postId=" + postId + "&time=" + Date.parse(new Date()),
                success: function (data) {
                    data = JSON.parse(data);
                    console.log(data);
                    showMessage(data['message']);
                    if (data['code'] === 1) {
                        window.location.reload();
                    } else if (data['code'] === 0) {
                        showMessage(data['message']);
                    }
                }
            });
        });
    });

    function delStu(ele) {
        $.ajax({
            url: "reply",
            type: "post",
            data: "id=" + $(ele).parent().parent().attr("id") + "&postId=" + postId + "&type=delete",
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