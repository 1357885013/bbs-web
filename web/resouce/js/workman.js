//websocket 连接以及消息处理
if (typeof console == "undefined") {    this.console = { log: function (msg) {  } };}
// 如果浏览器不支持websocket，会使用这个flash自动模拟websocket协议
WEB_SOCKET_SWF_LOCATION = "/swf/WebSocketMain.swf";
// 开启flash的websocket debug
WEB_SOCKET_DEBUG = true;

var ws;
$(function connect()
{
    //在登录成功后建立websocket连接.
    var uid=getCookie('uid');
    if(""!=uid){
        ws=new WebSocket("ws://"+document.domain+":7272");
        ws.onopen=onopen;
        ws.onmessage=onmessage;
    }
});
function onopen()
{
    //uid和clientId绑定
    var uid=getCookie('uid');
    if(""!=uid){
        //var login_data='{"type":"login","uid":'+uid+'}';
        var login_data="{'type':'login','uid':'"+uid+"'}";
        var data={'type':'login','uid':uid};
        console.log("绑定，发送登录数据:"+data);
        ws.send(JSON.stringify(data));
    }
}

function onmessage(e)
{
    var data = JSON.parse(e.data);
    console.log(data);
    switch(data['type']) {
        case 'ping':
            ws.send('{"type":"pong"}');
            break;
        case 'connect':
            console.log("连接成功");
            getRecord();
            break;
        case 'login':
            //提示登录返回信息
            showMessage(data['message']);
            //是否需要验证码,login.php还有一处.
            if (data['attempts'] > 5) {
                $(".captcha").hide().show();
                refreshImg();
            } else
                $(".captcha").hide().hide();
            break;
        case 'message':
            showMessage(data['message']);
            break;
        case 'say':
            addRecord((data['content']));
            console.log((data['content']));
            sessionStorage.setItem('chatRec' + data['content']['id'], JSON.stringify(data['content']));
            break;
        //提问功能相关代码
        case 'record':
            creatClass(data);
            break;
        case 'teaQue1':
            $(".waitForId.btn"+data['stuId']).addClass("ques"+data['quesId']).data('quesId',data['quesId']).removeClass("waitForId");
            break;
        case 'teaQue2':
            $(".ques"+data['quesId']).data('answer',data['answer']);
            $("#wait2").append($(".ques"+data['quesId']));
            break;
        case 'stuQue':
            $("#giveAns").modal('show');
            $("#gaQues").val(data['ques']);
            $("#gaButton").data("quesId",data['quesId']);
            break;
    }
}
function getRecord()
{
    //获取聊天记录
    if(1!=sessionStorage.getItem("chat")){
        $.ajax({
            url: publicDir+'index.php/common/chat/getRecord',
            type: 'post',
            data: "uid="+getCookie('uid'),
            success: function (data)
            {
                data=JSON.parse(data);
                if('message'==data['type']){
                    showMessage(data['message']);
                }else if ('record' == data['type']) {
                    creatClass(data);
                }
            }
        });
    }else{
        reShowClass();
        recoveryShow();
    }
}
function creatClass(data) {
    //array('type'=>'record','roomId'=>'','roomName'=>'','content'=>'');
    $(".mainbox").append("<div id=\"chatBox"+data['roomId']+"\" class=\"chatBox container\" data-id=\""+data['roomId']+"\">\n" +
        "    <div class=\" title-div\" >\n" +
        "      <p>"+data['roomName']+"</p>\n" +
        "    </div>\n" +
        "    <div class=\"scrollbar scrollbar-chatbox\">\n" +
        "      <table class=\"\" style=\"margin-top: 6px\">\n" +
        "      </table>\n" +
        "    </div>\n" +
        "    <div class=\"container footer-chatbox\">\n" +
        "      <textarea class=\"form-control\" rows=\"3\"></textarea>\n" +
        "      <button class=\"btn-dark\" onclick=\"sayToClass(this)\">发送</button>\n" +
        "    </div>\n" +
        "  </div>");
    $("#chat-dl").append("<dd id='chatBtn"+data['roomId']+"'><button onclick=\"chatToggle(this)\""+
        "  data-target=\"chatBox"+data['roomId']+"\"  style=\"display: block; \">"+data['roomName']+"</button></dd>");
    for (var each in data['content']){
        //{id: 54, roomId: 23, userId: "11", time: "2019-04-12 13:40:58", content: "123"}
        addRecord(data['content'][each]);
    }
    //sessionStorage.setItem("chatBox"+data['roomId'],$("#chatBox"+data['roomId']).html().toString());
    //sessionStorage.setItem("chatBtn"+data['roomId'],$("#chatBtn"+data['roomId']).html().toString());
    sessionStorage.setItem("chatBox"+data['roomId'],document.getElementById("chatBox"+data['roomId']).outerHTML);
    sessionStorage.setItem("chatBtn"+data['roomId'],document.getElementById("chatBtn"+data['roomId']).outerHTML);
    sessionStorage.setItem('chat',1);
}
//显示每条聊天数据
function addRecord(data) {
    console.log("addRecord:"+data);
    $("#chatBox"+data['roomId']+" table").append("<tr>\n" +
        "<th class='head-th'><div class='headImg id"+data['userId']+"' /></th>\n" +
        "<th ><p class='chat-name'>"+data['userName']+"</p>\n" +
        "<p class='chat-text' >"+data['content']+"</p></th>\n" +
        "</tr>");
    $(".headImg.id"+data['userId']).css('background-color',getColorById(data['userId']));
}
//页面跳转,刷新后重新显示 聊天框 and 聊天框 按钮
function reShowClass() {
    var chatBoxs=new Array();
    for (var i=0; i<sessionStorage.length; i++) {
        var key = sessionStorage.key(i);
        if('chatBox'==key.substr(0,7)){
                $(".mainbox").append(sessionStorage.getItem(key));
                chatBoxs.push(key);
        }else if('chatBtn'==key.substr(0,7)){
                $("#chat-dl").append(sessionStorage.getItem(key));
        }
    }
    var temp=new Array();
    //插入期间聊天数据
    for (var i=0; i<sessionStorage.length; i++) {
        var key = sessionStorage.key(i);
        if('chatRec'==key.substr(0,7)){
            addRecord(JSON.parse(sessionStorage.getItem(key)));
            temp.push(key);
        }
    }
    for (var i=0; i<temp.length; i++) {
        sessionStorage.removeItem(temp[i]);
    }
    //重新存储聊天框
    for (var i=0; i<chatBoxs.length; i++) {
        var temp1=document.getElementById(chatBoxs[i]).outerHTML;
        sessionStorage.setItem(chatBoxs[i],temp1);
    }
    sessionStorage.setItem('chat',1);
}
function sayToClass(ele) {
    var uid=getCookie('uid');
    var name=getCookie("name");
    var content=$(ele).prev().val();
    var classId=$(ele).parent().parent().data('id');
    var data="classId="+classId+"&content="+content+"&uid="+uid;
    console.log("send say:"+data);
    $.ajax({
        url: publicDir+'index.php/common/chat/sayToClass',
        type: 'post',
        data: data,
        success: function (data)
        {
            var uid=getCookie('uid');
            data=JSON.parse(data);
            showMessage(data['message']);
            if(data['address']){
            }
        }
    });
}
function recoveryShow() {
    //chatBox 状态重现
    var _fixed = getCookie("fixed");
    var _tar = getCookie("tar");
    if(_fixed==1){
        $(".mainbox").animate({marginRight:435},300,'swing',function(){
        });
        $("#chatBox").animate({right:0},300,'swing',function(){
        });
        $("#"+_tar).animate({right:0},300,'swing',function(){
        });
    }else if (_fixed == 0) {
        $(".mainbox").animate({marginRight:35},300,'swing',function(){
        });
        $("#chatBox").animate({right:-400},300,'swing',function(){
        });
        $(".chatBox").animate({right:-400},300,'swing',function(){
        });
    }
}
//chatBox 显示`隐藏
function chatToggle(ele) {
    ele=$(ele);
    var _fixed = getCookie("fixed");
    if(!_fixed)_fixed=0;
    var _tar = getCookie("tar");
    if(_fixed==1){
        if(_tar!=ele.data('target')){
            $("#"+ele.data('target')).animate({right:0},300,'swing',function(){
                $("#"+ele.data('target')+" dt a.close").hide().width('68px').fadeIn(500);
            });
            $("#"+_tar).animate({right:-400},300,'swing',function(){
                $("#"+ele.data('target')+" dt a.close").hide().width('68px').fadeIn(500);
            });
            setCookie('tar',ele.data('target'));
            setCookie('fixed',1);
        }

    }else{
        $(".mainbox").animate({marginRight:435},300,'swing',function(){
        });
        //$("#mainbox").css(margin)
        $("#chatBox").animate({right:0},300,'swing',function(){
            $("#"+ele.data('target')+" dt a.close").hide().width('68px').fadeIn(500);
        });
        $("#"+ele.data('target')).animate({right:0},300,'swing',function(){
            $("#"+ele.data('target')+" dt a.close").hide().width('68px').fadeIn(500);
        });
        setCookie('tar',ele.data('target'));
        setCookie('fixed',1);
    }
}
function chatClose(ele){
    var _tar = getCookie("tar");
    $("#"+_tar).animate({right:-400},300,'swing',function(){
    });
    $("#chatBox").animate({right:-400},300,'swing',function(){
    });
    $(".mainbox").animate({marginRight:35},300,'swing',function(){
    });
    setCookie('fixed',0);
}

function getColorById(i){
    if(i<10)i=i*302.3;
    if(i<100)i=i*31.2;
    for(;i>255;i*=0.98);
    var temp=i.toString().substring(i.toString().length-3);
    i+=parseInt(temp);
    for(;i>255;i-=255);
    i=parseInt(i);
    if(i<10)i+=10;

    var R=i*(i/100);
    for(;R>255;R-=255);
    if(R<50)R+=60;
    R=parseInt(R).toString(16);

    var G=i*(i%100);
    for(;G>255;G-=255);
    if(G<50)G+=60;
    G=parseInt(G).toString(16);

    var B=i*(i%10);
    for(;B>255;B-=255);
    if(B<50)B+=60;
    B=parseInt(B).toString(16);

    //console.log(i+":"+R+":"+G+":"+B);
    return "#"+R+G+B;
}