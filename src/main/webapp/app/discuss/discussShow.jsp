<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="sy.util.StringUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!doctype html>
<html>
<head>
    <jsp:include page="../../inc.jsp"></jsp:include>
    <meta name="viewport" content="width=device-width,initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <title>comment</title>
    <style>
        /* common */
        *{ margin:0; padding:0;}
        body{
            font-family:Microsoft YaHei;
            font-size:14px;
            line-height:1.5;
            color:#24292e;
            background-color:#fff;
            padding-bottom:50px;
        }
        .clearfix:after {visibility:hidden;display:block;font-size:0;content:" ";clear:both;height:0;}
        .clearfix {zoom:1;}
        .l-space{ margin-left:10px;}
        .t-space-10{ margin-top:10px;}
        h1{ font-size:16px; }
        .font-small{ font-size:12px;}
        .font-gray{ color:#999;}

        /*discussion-header*/
        .discussion-header-box{ background-color:#f2f2f2; text-align:center; padding:20px 0;}
        .discussion-header,.discussion-bucket{ width:80%; margin:0 auto; text-align:left;}
        .meta-strong{color:#24292e;}

        /*discussion-bucket*/
        .discussion-bucket-box{ text-align:center;}
        .discussion-sidebar{ width:220px; float:right;}
        .discussion-timeline{ width: calc(100% - 240px); float:left; }
        .user-head{ width:50px; height:50px; border-radius:50px; overflow:hidden; float:left; background-color:#f2f2f2;}
        .user-comment{width:calc(100% - 60px); float:right;}
        .comment-content-wrapper{ position:relative; }
        .arrow-left { width:10px; height:20px; background:url(${pageContext.request.contextPath}/app/discuss/images/arrow.png) no-repeat left center; position:absolute; left:0; top:10px; z-index:10; }
        .comment-content{ border-radius:6px; border:1px solid #ebebeb; padding:10px; margin-left:9px; margin-top:3px; color:#666;}
        .comment-meta{ margin-left:9px;}
        .comment-user-name{ float:left;}
        .comment-time{ float:right;}
        .comment-list{ margin-top:20px;}
        .comment-list:first-child { margin-top:0;}
        .my-comment .user-head{ float:right;}
        .my-comment .user-comment{ float:left;}
        .my-comment .comment-user-name{ float:right;}
        .my-comment .comment-time{ float:left;}
        .my-comment .arrow-left { right:0; left:auto;transform:rotate(180deg); -ms-transform:rotate(180deg); -moz-transform:rotate(180deg); 	/* Firefox */-webkit-transform:rotate(180deg); /* Safari 和 Chrome */-o-transform:rotate(180deg); 	/* Opera */}
        .my-comment .comment-content{ margin-left:0; margin-right:9px;}
        .my-comment .comment-meta{ margin-left:0; margin-right:9px;}

        /*add comment*/
        .add-comment textarea{ width:calc(100% - 22px);border:1px solid #ebebeb; border-radius:6px; height:80px; line-height:1.5;padding:10px; color:#666;}
        .add-comment button{ width:80px; height:35px; margin-top:5px; line-height:35px; background-color:#24292e; color:#fff; border:0; float:right; cursor:pointer;}
        .add-comment button:hover{ background-color:#000;}

        /*add comment*/
        .discussion-sidebar{}
        .user-info{ border:1px solid #ebebeb; padding:10px;}
        .banner{ background-color:#f9f9f9; height:60px; padding:10px;}
    </style>

    <script type="text/javascript">
        function addDiscuss() {
            $.ajax({
                url: '${pageContext.request.contextPath}/discussController/securi_addDiscuss',
                type: 'post',
                dataType: 'json',
                data: {'discussType': $('#discussType').val(), 'discussId': $('#discussId').val(), 'content': $('#content').val()},
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                success: function (data) {
                    if (data.rspCode == '0') {
                        alert('发表成功');
                        window.location.reload();
                    }
                }
            });
        }
    </script>
</head>

<body>
<form  name="form" id="form" method="post" enctype="multipart/form-data" role="form">
<div class="discussion-pannel">

    <div class="discussion-header-box">
        <div class="discussion-header">
            <h1 class="issue-title">
                <span>${varTitle}</span>
            </h1>
            <div class="font-small font-gray issue-meta">
                <span class="meta-strong author">${varName}</span>
                <c:choose>
                    <c:when test="${discussType == '0'}">
                        <span>发布于</span>
                        <span><fmt:formatDate value="${varDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                    </c:when>
                </c:choose>
                <span class="l-space">评论</span>
                <span class="meta-strong comment-num">${discussCount}</span>
            </div>
        </div>
    </div>

    <div class="discussion-bucket-box t-space-10">
        <div class="discussion-bucket clearfix">
            <%--<div class="discussion-sidebar">--%>
                <%--<div class="user-info">--%>
                    <%--<span>ME</span>--%>
                    <%--<p class="font-small">相关信息内容</p>--%>
                <%--</div>--%>
                <%--<div class="banner t-space-10">--%>
                    <%--<span class="font-small">其他模块信息</span>--%>
                <%--</div>--%>
            <%--</div>--%>
            <div class="discussion-timeline">
                <!--begin comment-lists-->
                <div class="comment-lists">
                <c:forEach items="${discussList}" var="discuss" varStatus="discussStatus">
                    <c:choose>
                        <c:when test="${discuss.isMyself == '0'}">
                            <div class="comment-list clearfix">
                                <div class="user-head"><img src="${pageContext.request.contextPath}/app/discuss/images/user_head/other.png" alt="woman"/></div>
                                <div class="user-comment">
                                    <div class="comment-meta clearfix">
                                        <span class="font-small font-gray comment-user-name">${discuss.createUser}</span>
                                        <span class="font-small font-gray comment-time"><fmt:formatDate value="${discuss.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                                    </div>
                                    <div class="comment-content-wrapper">
                                        <div class="arrow-left"></div>
                                        <div class="comment-content">
                                            <p>${discuss.content}</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:when test="${discuss.isMyself == '1'}">
                            <div class="comment-list clearfix my-comment">
                                <div class="user-head"><img src="${pageContext.request.contextPath}/app/discuss/images/user_head/me.png" alt="me"/></div>
                                <div class="user-comment">
                                    <div class="comment-meta clearfix">
                                        <span class="font-small font-gray comment-user-name">自己</span>
                                        <span class="font-small font-gray comment-time"><fmt:formatDate value="${discuss.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                                    </div>
                                    <div class="comment-content-wrapper">
                                        <div class="arrow-left"></div>
                                        <div class="comment-content">
                                            <p>${discuss.content}</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                    </c:choose>

                </c:forEach>

                </div>

                <!--end comment-lists-->
                <div class="add-comment t-space-10 clearfix">
                    <input type="hidden" id="discussType" name="discussType" value="${discussType}">
                    <input type="hidden" id="discussId" name="discussId" value="${discussId}">
                    <textarea class="font-small" id="content" name="content" placeholder="回复："></textarea>
                    <button type="button" onclick="addDiscuss()">添加评论</button>
                </div>
            </div>
        </div>
    </div>
</div>
</form>
</body>
</html>
