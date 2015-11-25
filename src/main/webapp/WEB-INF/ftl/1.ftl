<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<link rel="shortcut icon" type="image/x-icon" href="http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/images/icon/common/favicon22c41b.ico">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<title>${info.title}</title>
<link rel="stylesheet" type="text/css" href="${contextPath}/style/css/info.css">
<link rel="stylesheet" type="text/css" href="${contextPath}/style/css/article.css">
<!--[if lt IE 9]>
<link rel="stylesheet" type="text/css" href="${contextPath}/style/css/page_mp_article_improve_pc22ebff.css">
<![endif]-->
<script type="text/javascript">
function closePage() {
        if (navigator.userAgent.indexOf("MSIE") > 0) {
            if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
                window.opener = null; window.close();
            }
            else {
                window.open('', '_top'); window.top.close();
            }
        }
        else if (navigator.userAgent.indexOf("Firefox") > 0) {
            window.location.href = 'about:blank '; //火狐默认状态非window.open的页面window.close是无效的
            //window.history.go(-2);
        }
        else {
            window.opener = null;
            window.open('', '_self', '');
            window.close();
        }
    }
</script>
</head>
<body id="activity-detail" class="zh_CN">
<div id="header" class="header">
  <header id="defaultHeader">
  	<a class="button" id="backButton" onClick="closePage();">${back}</a>
    <h1 id="pageTitle">${title}</h1>
    <a class="menuButton" id="menubadge"></a>
  </header>
</div>
<div class="rich_media" id="js_article">
  <div class="top_banner" id="js_top_ad_area"></div>
  <div class="rich_media_inner">
    <h2 id="activity-name" class="rich_media_title"> ${info.title}</h2>
    <div class="rich_media_meta_list"> <em class="rich_media_meta rich_media_meta_text" id="post-date">${info.createTime}</em> <a id="post-user" href="javascript:void(0);" class="rich_media_meta rich_media_meta_link rich_media_meta_nickname">${info.fwRealname}</a> </div>
    <div id="page-content">
      <div id="img-content">
        <div id="js_content" class="rich_media_content">
  			 ${info.content}
        </div>
      <!--  <div id="js_toobar" class="rich_media_tool"> <a href="javascript:void(0);" id="js_view_source" class="media_tool_meta meta_primary">阅读原文</a>
          <div style="display:none;" class="media_tool_meta link_primary meta_primary" id="js_read_area">阅读 <span id="readNum"></span></div>
          <a id="like" href="javascript:void(0);" class="media_tool_meta meta_primary link_primary meta_praise" style="display:none;"> <i class="icon_praise_gray"></i><span id="likeNum" class="praise_num"></span> </a> <a href="javascript:void(0);" class="media_tool_meta link_primary meta_extra" id="js_report_article">举报</a> </div>
      </div> -->
      <div id="js_bottom_ad_area"></div>
      <div style="display:none;" id="js_iframetest"></div>
    </div>
  </div>
</div>
</body>
</html>
