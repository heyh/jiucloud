<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%
	if(session.getAttribute("sessionInfo")!=null){
		response.sendRedirect(request.getContextPath()+"/index.jsp");
		return;
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>氿上云管理系统<%--= ResourceBundle.getBundle("config").getString("sys.title") --%></title>
<link rel="stylesheet" href="<%=request.getContextPath() %>/layout/login/base.css"> 
<link rel="stylesheet" href="<%=request.getContextPath() %>/layout/login/style.css"> 
<link rel="stylesheet" href="<%=request.getContextPath() %>/layout/login/login.css">

<jsp:include page="inc.jsp"></jsp:include>

<script>
var root="<%=request.getContextPath()%>";

$(function(){
	//进入页面，焦点在用户名文本框上
	$("#loginCode").focus();
});

/**
 * Ajax执行登录操作
 * @return
 */
function doLogin() {
	var validateResult = true;
	var name = $("#loginCode").val();
	var pwds = $("#password").val();
	if(name==""){
		$("#login_msg").html("用户名不能为空...").css("color","red");
		return;
	}
	if(pwds==""){
		$("#login_msg").html("密码不能为空...").css("color","red");
		return;
	}
	$("#login_msg").html("登录中，请稍后...").css("color","black");
	
	
	$.post('${pageContext.request.contextPath}/userController/login', {name:name,pwd:pwds}, function(result) {
		if (result.success) {
			window.location.href="${pageContext.request.contextPath}/index.jsp";
		} else {
			$.messager.alert('错误', result.msg, 'error');
			$("#login_msg").html("登陆错误.").css("color","red");
		}
		parent.$.messager.progress('close');
	}, "JSON");
	 
}

</script>
</head>
<body class="sign-in contrast-background">
<div id="wrapper">
    <div class="application">
        <div class="application-content">
        	<span><img src="<%=request.getContextPath() %>/layout/login_ico.png">氿上云管理系统</span>
        </div>
    </div>
    <div class="controls" onkeydown="if(event.keyCode==13){doLogin();}" id="loginTable">
        <div class="caret"></div>
        <div class="form-wrapper">
            <h1 class="text-center">登录</h1>
			<form accept-charset="UTF-8" action="index.html" method="get">
                <div class="row-fluid">
                    <div class="span12 icon-over-input">
                        <input class="span12" id="loginCode" name="loginCode" placeholder="用户名" value="" type="text">
                        <i class="icon-user muted"></i>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span12 icon-over-input">
                        <input class="span12" id="password" name="password" placeholder="密码" type="password">
                        <i class="icon-lock muted"></i>
                    </div>
                </div>
                <p id="login_msg" style="text-align: center;"></p>
                <button class="btn btn-block blue" name="button" type="button" onclick="doLogin();" style="margin-top:0px">登 录</button>
                <button class="btn btn-block" name="button" type="reset">重 置</button>
            </form>
        </div>
    </div>
  <div class="application" style="margin-top:20px;">
        	<!-- <span style="font-size:10px;text-align: center;">copyright @ 江苏施塔特信息科技有限公司&nbsp;&nbsp;版权所有</span> -->
        </div>
</div>
</body>
</html>