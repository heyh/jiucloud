<%@ page language="java" import="sy.util.PropertyUtil" pageEncoding="UTF-8"%>
<%
	String path = PropertyUtil.getFilePathHome() + "/upload/swfsource/" + request.getAttribute("v");
//    String path = PropertyUtil.getFilePathHome() + "/upload/swfsource/" + "9105/ar09_eng.swf";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <style type="text/css" media="screen">
        html, body	{
            height:100%;
        }

        body
        {
            margin:0;
            padding:0;
            overflow:auto;
        }

        #flashContent
        {
            display:none;
        }

    </style>

    <script type="text/javascript" src="<%=request.getContextPath()%>/flexpaper/js/swfobject/swfobject.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/flexpaper/js/flexpaper_flash.js"></script>

    <script type="text/javascript">
        if(window.addEventListener)
            window.addEventListener('DOMMouseScroll', handleWheel, false);
        window.onmousewheel = document.onmousewheel = handleWheel;

        if (window.attachEvent)
            window.attachEvent("onmousewheel", handleWheel);

        function handleWheel(event){
            try{
                if(!window.document.FlexPaperViewer.hasFocus()){return true;}
                window.document.FlexPaperViewer.setViewerFocus(true);
                window.document.FlexPaperViewer.focus();

                if(navigator.appName == "Netscape"){
                    if (event.detail)
                        delta = 0;
                    if (event.preventDefault){
                        event.preventDefault();
                        event.returnValue = false;
                    }
                }
                return false;
            }catch(err){return true;}
        }
    </script>

    <script type="text/javascript">
        var swfVersionStr = "9.0.124";
        var xiSwfUrlStr = "${expressInstallSwf}";
        var flashvars = {
            <%--SwfFile : escape('<%=request.getContextPath()%>/flexpaper/ar09_eng.swf?v1.2.4'),--%>
            SwfFile : escape('<%=path%>?v1.2.4'),
            Scale : 1,
            ZoomTransition : "easeOut",
            ZoomTime : 0.5,
            ZoomInterval : 0.1,
            FitPageOnLoad : false,
            FitWidthOnLoad : false,
            PrintEnabled : true,
            FullScreenAsMaxWindow : false,
            ProgressiveLoading : true,

            PrintToolsVisible : true,
            ViewModeToolsVisible : true,
            ZoomToolsVisible : true,
            FullScreenVisible : true,
            NavToolsVisible : true,
            CursorToolsVisible : true,
            SearchToolsVisible : true,

            localeChain: "en_US"
        };
        var params = {

        }
        params.quality = "high";
        params.bgcolor = "#ffffff";
        params.allowscriptaccess = "sameDomain";
        params.allowfullscreen = "true";
        var attributes = {};
        attributes.id = "FlexPaperViewer";
        attributes.name = "FlexPaperViewer";
        swfobject.embedSWF(
                "<%=request.getContextPath()%>/flexpaper/FlexPaperViewer.swf", "flashContent",
                "800", "550",
                swfVersionStr, xiSwfUrlStr,
                flashvars, params, attributes);
        swfobject.createCSS("#flashContent", "display:block;text-align:left;");
    </script>

</head>
<body>
<div style="text-align:center;">
    <div id="flashContent">
        <p>
            To view this page ensure that Adobe Flash Player version
            9.0.124 or greater is installed.
        </p>
        <script type="text/javascript">
            var pageHost = ((document.location.protocol == "https:") ? "https://" :	"http://");
            document.write("<a href='http://www.adobe.com/go/getflashplayer'><img src='"
            + pageHost + "www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /></a>" );
        </script>
    </div>
</div>
</body>
</html>
