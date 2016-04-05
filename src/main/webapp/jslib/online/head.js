var locat = (window.location + '').split('/');
$(function () {
    locat = locat[0] + '//' + locat[2] + '/' + locat[3];
});

var user = ""; //当前登录用户

$(function () {
    $.ajax({
        type: "POST",
        url: locat + '/userController/securi_getSessionUser',
        data: encodeURI(""),
        dataType: 'json',
        cache: false,
        success: function (data) {
            user = data.uid;//用户ID
            online();//连接在线管理
        }
    });
});

//在线管理
var websocket;
function online() {
    if (window.WebSocket) {
        websocket = new WebSocket(encodeURI('ws://127.0.0.1:8889'));

        websocket.onopen = function () {
            //连接成功
            websocket.send('[join]' + user);
        };
        websocket.onerror = function () {
            //连接失败
        };
        websocket.onclose = function () {
            //连接断开
        };
        //消息接收
        websocket.onmessage = function (message) {
            var message = JSON.parse(message.data);
            if (message.type == 'count') {
                userCount = message.msg;
            } else if (message.type == 'goOut') {
                $("body").html("");
                goOut("此用户在其它终端已经登录,您暂时无法登录");
            } else if (message.type == 'thegoout') {
                $("body").html("");
                goOut("您被系统管理员强制下线");
            } else if (message.type == 'userlist') {
                userlist = message.list;
            }
        };
    }
}

//在线总数
var userCount = 0;
function getUserCount() {
    websocket.send('[count]' + user);
    return userCount;
}
//用户列表
var userlist = "";
function getUserlist() {
    websocket.send('[getUserlist]' + user);
    return userlist;
}
//强制下线
function goOut(msg) {
    alert(msg);
    //window.location.href = locat + "/logout";
    window.location.href = 'http://www.9393915.com/department';
}
//强制某用户下线
function goOutUser(theuser) {
    websocket.send('[goOut]' + theuser);
}