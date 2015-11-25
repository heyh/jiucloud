<%@ page import="sy.util.PropertyUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width">
</head>
<body>
<%
	sy.util.GetRealPath grp = new sy.util.GetRealPath(request
			.getSession().getServletContext());
%>
	<%-- <video controls="controls" autoplay>
		<source
			src="<%=grp.getRealPath()%>upload/source/${v}"
			type="video/mp4">
	</video> --%>
	
	<div align="center"><object classid="clsid:22D6F312-B0F6-11D0-94AB-0080C74C7E95" id="MediaPlayer1" width="900" height="550">
        <param name="AudioStream" value="-1">
        <param name="AutoSize" value="0">
        <param name="AutoStart" value="0">      <!--  设置是否为自动播放，-1为自动播放，0为不自动播放  -->     
        <param name="AnimationAtStart" value="-1">
        <param name="AllowScan" value="-1">
        <param name="AllowChangeDisplaySize" value="-1">
        <param name="AutoRewind" value="0">
        <param name="Balance" value="0">
        <param name="BaseURL" value>
        <param name="BufferingTime" value="5">
        <param name="CaptioningID" value>
        <param name="ClickToPlay" value="-1">
        <param name="CursorType" value="0">
        <param name="CurrentPosition" value="-1">
        <param name="CurrentMarker" value="0">
        <param name="DefaultFrame" value>
        <param name="DisplayBackColor" value="0">
        <param name="DisplayForeColor" value="16777215">
        <param name="DisplayMode" value="0">
        <param name="DisplaySize" value="2">
        <param name="Enabled" value="-1">
        <param name="EnableContextMenu" value="-1">
        <param name="EnablePositionControls" value="-1">
        <param name="EnableFullScreenControls" value="0">
        <param name="EnableTracker" value="-1">   
        <param name="Filename" value="<%= PropertyUtil.getFilePathHome()%>upload/source/${v}">   <!--  设置播放的路径 ，一般通过参数传入 -->
        <param name="InvokeURLs" value="-1">
        <param name="Language" value="-1">
        <param name="Mute" value="0">
        <param name="PlayCount" value="1">
        <param name="PreviewMode" value="0">
        <param name="Rate" value="1">
        <param name="SAMILang" value>
        <param name="SAMIStyle" value>
        <param name="SAMIFileName" value>
        <param name="SelectionStart" value="-1">
        <param name="SelectionEnd" value="-1">
        <param name="SendOpenStateChangeEvents" value="-1">
        <param name="SendWarningEvents" value="-1">
        <param name="SendErrorEvents" value="-1">
        <param name="SendKeyboardEvents" value="0">
        <param name="SendMouseClickEvents" value="0">
        <param name="SendMouseMoveEvents" value="0">
        <param name="SendPlayStateChangeEvents" value="-1">
        <param name="ShowCaptioning" value="0">
        <param name="ShowControls" value="-1">
        <param name="ShowAudioControls" value="-1">
        <param name="ShowDisplay" value="0">
        <param name="ShowGotoBar" value="0">
        <param name="ShowPositionControls" value="-1">
        <param name="ShowStatusBar" value="-1">
        <param name="ShowTracker" value="-1">
        <param name="TransparentAtStart" value="0">
        <param name="VideoBorderWidth" value="0">
        <param name="VideoBorderColor" value="0">
        <param name="VideoBorder3D" value="0">
        <param name="Volume" value="-40">
        <param name="WindowlessVideo" value="0">
        </object></div>
</body>
</html>
