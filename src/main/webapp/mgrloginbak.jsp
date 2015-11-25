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
<title>南京理工大学机动车管理系统</title>
<link rel="stylesheet" href="<%=request.getContextPath() %>/layout/login/base.css"> 
<link rel="stylesheet" href="<%=request.getContextPath() %>/layout/login/style.css"> 
<link rel="stylesheet" href="<%=request.getContextPath() %>/layout/login/login.css">

<!-- 引入bootstrap样式 -->
<link href="<%=request.getContextPath() %>/layout/login/css/bootstrap.min.css" rel="stylesheet" media="screen">

<jsp:include page="inc.jsp"></jsp:include>

<script>
var root="<%=request.getContextPath()%>";

$(function(){
	setDialog();
	
	//进入页面，焦点在用户名文本框上
	$("#loginCode").focus();
});

/**
 * Ajax执行登录操作
 * @return
 */
function doLogin() {
	var validateResult = true;
	//easyui 表单验证
	$('#loginTable input').each(function () {
		if ($(this).attr('required') || $(this).attr('validType')) {
			if (!$(this).validatebox('isValid')) {
				//如果验证不通过，则返回false
				validateResult = false;
				return;
		    }
		}
    });
	if(validateResult==false){
		//如果验证不通过，则不执行登录操作
		return;
	}
	
	$("#login_msg").html("登录中，请稍后...");
	
	var name = $("#loginCode").val();
	var pwds = $("#password").val();
	$.post('${pageContext.request.contextPath}/userController/login', {name:name,pwd:pwds}, function(result) {
		if (result.success) {
			 
			window.location.href="${pageContext.request.contextPath}/index.jsp";
			//$('#loginDialog').dialog('close');
			//$('#sessionInfoDiv').html($.formatString('[<strong>{0}</strong>]，欢迎你！您使用[<strong>{1}</strong>]IP登录！', result.obj.name, result.obj.ip));
		} else {
			$.messager.alert('错误', result.msg, 'error');
			$("#login_msg").html("登录错误.");
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
        	<span><img src="<%=request.getContextPath() %>/layout/login_ico.png">南京理工大学机动车管理系统</span>
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
                <button class="btn btn-block blue" name="button" type="button" onclick="doLogin();" style="margin-top:0px">登 录</button>
                <button class="btn btn-block" name="button" type="reset" onclick="doReset();">重 置</button>
            </form>
        </div>
    </div>
  <div class="application" style="margin-top:20px;">
			<!-- <span style="font-size:10px;text-align: center;">技术支持&nbsp;&nbsp江苏施塔特信息科技有限公司</span> -->
		</div>
</div>



</body></html>