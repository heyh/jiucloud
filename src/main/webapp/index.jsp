<%@page import="sy.util.ConfigUtil"%>
<%@page import="sy.pageModel.SessionInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<%
    String uid = request.getParameter("uid");
    String cid = request.getParameter("cid");
    if (uid == null || "".equals(uid)) {
        SessionInfo sessionInfo = (SessionInfo) session
                .getAttribute(ConfigUtil.getSessionInfoName());
        if (sessionInfo == null) {
            response.sendRedirect(request.getContextPath());
        } else {
            uid=sessionInfo.getId();
            cid=sessionInfo.getCompid();
        }
    }
    String loginUrl="user/login.jsp?uid="+uid+"&cid="+cid;
%>
<html>
<head>
    <title>氿上云管理系统</title>
    <jsp:include page="inc.jsp"></jsp:include>
    <script type="text/javascript">
        var index_tabs;
        var index_tabsMenu;
        var index_layout;
        $(function() {
            index_layout = $('#index_layout').layout({
                fit : true
            });
            /*index_layout.layout('collapse', 'east');*/

            index_tabs = $('#index_tabs')
                    .tabs(
                    {
                        fit : true,
                        border : false,
                        onContextMenu : function(e, title) {
                            e.preventDefault();
                            index_tabsMenu.menu('show', {
                                left : e.pageX,
                                top : e.pageY
                            }).data('tabTitle', title);
                        },
                        tools : [
                            {
                                iconCls : 'database_refresh',
                                handler : function() {
                                    var href = index_tabs.tabs(
                                            'getSelected').panel(
                                            'options').href;
                                    if (href) {/*说明tab是以href方式引入的目标页面*/
                                        var index = index_tabs
                                                .tabs(
                                                'getTabIndex',
                                                index_tabs
                                                        .tabs('getSelected'));
                                        index_tabs
                                                .tabs('getTab', index)
                                                .panel('refresh');
                                    } else {/*说明tab是以content方式引入的目标页面*/
                                        var panel = index_tabs.tabs(
                                                'getSelected').panel(
                                                'panel');
                                        var frame = panel
                                                .find('iframe');
                                        try {
                                            if (frame.length > 0) {
                                                for (var i = 0; i < frame.length; i++) {
                                                    frame[i].contentWindow.document
                                                            .write('');
                                                    frame[i].contentWindow
                                                            .close();
                                                    frame[i].src = frame[i].src;
                                                }
                                                if (navigator.userAgent
                                                                .indexOf("MSIE") > 0) {// IE特有回收内存方法
                                                    try {
                                                        CollectGarbage();
                                                    } catch (e) {
                                                    }
                                                }
                                            }
                                        } catch (e) {
                                        }
                                    }
                                }
                            },
                            {
                                iconCls : 'delete',
                                handler : function() {
                                    var index = index_tabs
                                            .tabs(
                                            'getTabIndex',
                                            index_tabs
                                                    .tabs('getSelected'));
                                    var tab = index_tabs.tabs('getTab',
                                            index);
                                    if (tab.panel('options').closable) {
                                        index_tabs.tabs('close', index);
                                    } else {
                                        $.messager
                                                .alert(
                                                '提示',
                                                '['
                                                + tab
                                                        .panel('options').title
                                                + ']不可以被关闭！',
                                                'error');
                                    }
                                }
                            } ]
                    });

            index_tabsMenu = $('#index_tabsMenu').menu(
                    {
                        onClick : function(item) {
                            var curTabTitle = $(this).data('tabTitle');
                            var type = $(item.target).attr('title');

                            if (type === 'refresh') {
                                index_tabs.tabs('getTab', curTabTitle).panel(
                                        'refresh');
                                return;
                            }

                            if (type === 'close') {
                                var t = index_tabs.tabs('getTab', curTabTitle);
                                if (t.panel('options').closable) {
                                    index_tabs.tabs('close', curTabTitle);
                                }
                                return;
                            }

                            var allTabs = index_tabs.tabs('tabs');
                            var closeTabsTitle = [];

                            $.each(allTabs, function() {
                                var opt = $(this).panel('options');
                                if (opt.closable && opt.title != curTabTitle
                                        && type === 'closeOther') {
                                    closeTabsTitle.push(opt.title);
                                } else if (opt.closable && type === 'closeAll') {
                                    closeTabsTitle.push(opt.title);
                                }
                            });

                            for (var i = 0; i < closeTabsTitle.length; i++) {
                                index_tabs.tabs('close', closeTabsTitle[i]);
                            }
                        }
                    });
        });

        function ac(params) {
            var iframe = '<iframe src="'
                    + params.url
                    + '" frameborder="0" style="border:0;width:100%;height:98%;"></iframe>';
            var t = $('#index_tabs');
            var opts = {
                title : params.title,
                closable : true,
                iconCls : params.iconCls,
                content : iframe,
                border : false,
                fit : true
            };
            if (t.tabs('exists', opts.title)) {
                t.tabs('select', opts.title);
                parent.$.messager.progress('close');
            } else {
                t.tabs('add', opts);
            }

        }

        function logoutFun(b) {
            $.getJSON('${pageContext.request.contextPath}/userController/logout', {
                t : new Date()
            }, function(result) {
                if (b) {
                    location.replace('${pageContext.request.contextPath}/');
                } else {
                    window.location.href = '';
                }
            });
        }

        <!-- 单点登录 -->
        $(function () {
            var websocket;
            if (window.WebSocket) {
                websocket = new WebSocket(encodeURI('ws://127.0.0.1:8889'));

                websocket.onopen = function () {
                    //连接成功
                    websocket.send('[join]' + <%= uid %>);
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
                    if (message.type == 'goOut') {
                        $("body").html("");
                        alert("此用户在其它终端已经登录,您暂时无法登录");
                        window.location.href = 'http://www.9393915.com/department';
                    }
                };
            }
        });

        $(function () {
            getNeedApproveList();
        });

        setInterval(getNeedApproveList, 30000);

        function getNeedApproveList() {
            $.ajax({
                url: '${pageContext.request.contextPath}/fieldDataController/securi_getNeedApproveList',
                data: {currentApprovedUser: <%= uid %>, cid: <%= cid %>},
                type: 'post',
                dataType: 'json',
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                success: function (data) {
                    if (data.success) {
                        console.log(data.obj)
                        $('.notice_num').text(data.obj.needApproveList.length);

                        var htmlStr = '';
                        $.each(data.obj.needApproveList, function(index, item) {
                            htmlStr +=
                            '<div class="con_list_item clearfix">' +
                            '   <div class="span4">' +
                            '       <div><a class="info_desc">' + item.uname + '提交一份申请【' + item.dataName + '】,需要您的审批' + '</a></div>' +
                            '       <div><a class="date">' + item.creatTime + '</a></div>' +
                            '   </div>' +
                            '   <div class="span2 text-right">' +
                            '       <button type="button" class="btn" onclick="goApprove(' + item.itemCode + ')">去审批</button>' +
                            '   </div>' +
                            '</div>'
                        })
                        $('#noticeDiv').html(htmlStr);

                        $('#comp').text('企业名称: ' + data.obj.compName);
                        $('#userName').text('欢迎您, ' + data.obj.userName);
                    }
                }
            });
        };

        function goApprove(todo) {
            var url = '${pageContext.request.contextPath}/projectStatController/approve';
            var text = '项目数据审批';
            var iconCls = 'approve';

            <%--if(todo == 0) {--%>
                <%--url = '${pageContext.request.contextPath}/projectStatController/dataApprove';--%>
                <%--text = '项目数据审批';--%>
                <%--iconCls = 'approve';--%>
            <%--} else if (todo == 1) {--%>
                <%--url = '${pageContext.request.contextPath}/projectStatController/docApprove';--%>
                <%--text = '项目数据审批';--%>
                <%--iconCls = 'approve';--%>
            <%--}--%>

            var params = {
                url : url,
                title : text,
                iconCls : 'approve'
            }
            window.parent.ac(params);
        }
    </script>

    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/layout/css/common.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/layout/css/company.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/layout/css/global.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/css/yjd_web.css">


</head>
<body>

<jsp:include page="<%=loginUrl%>"></jsp:include>

<div id="index_layout">
    <!-- class="logo" -->
    <div data-options="region:'north'" style="height: 40px; overflow: hidden;">
        <div class="fixed-top" id="header" style="height: 40px;">
            <nav role="navigation" id="nav">
                <a class="logo" href="http://www.9393915.com">氿上网</a>
                <ul class="top-nav fr">

                    <li class="menu-item"><a id="comp"></a></li>
                    <li class="menu-item"><a id="userName"></a></li>
                    <li class="menu-item"><div style="padding-top:3px; height:15px; width:1px; border-left:1px #fff solid"></div></li>
                    <li class="menu-item"><a target="_blank" href="http://www.9393915.com/user/base/profile">账号</a></li>
                    <!-- <li class="menu-item"><a href="javascript:logoutFun()">退出</a></li> -->
                    <li class="menu-item"><a href="http://www.9393915.com/department">退出</a></li>
                </ul>
                <ul class="sub-nav left" style="padding-left: 10%">
                    <li><a href="http://www.9393915.com">首页</a></li>
                    <li><a href="http://www.9393915.com/user/base/index">个人主页</a></li>
                    <li><a href="http://www.9393915.com/service">业务管理</a></li>
                    <li><a href="http://www.9393915.com/department">企业用户</a></li>

                    <div class="notice_area pull-right dropdown" >
                        <div id="notice_nav" class="notice_nav " data-toggle="dropdown">
                            <span class="glyphicon icon-bell icon-white">
                            </span><span class="notice_num">0</span>
                        </div>
                        <div class="dropdown-menu notice_con" aria-labelledby="notice_area">
                            <div class="notice_con_til"><a style="font-size: 14px;">站内消息通知</a></div>
                            <div class="notice_con_list">
                                <div class="row" id="noticeDiv">
                                </div>
                            </div>
                        </div>
                    </div>

                </ul>

            </nav>
        </div>

    </div>
    <div
            data-options="region:'west',href:'${pageContext.request.contextPath}/layout/west.jsp',split:true"
            style="width: 200px; overflow: hidden;"></div>
    <div data-options="region:'center'" style="overflow: hidden;">
        <div id="index_tabs" style="overflow: hidden;">
            <div title="首页" data-options="border:false"
                 style="overflow: hidden;">
                <iframe src="${pageContext.request.contextPath}/layout/index.jsp"
                        frameborder="0" style="border: 0; width: 100%; height: 98%;"></iframe>
            </div>
        </div>
    </div>
    <div
            data-options="region:'east',href:'${pageContext.request.contextPath}/layout/east.jsp'"
            title="" style="width: 2px; overflow: hidden;"></div>
    <div
            data-options="region:'south',href:'${pageContext.request.contextPath}/layout/south.jsp',border:false"
            style="height: 30px; overflow: hidden;"></div>
</div>

<div id="index_tabsMenu" style="width: 120px; display: none;">
    <div title="refresh" data-options="iconCls:'transmit'">刷新</div>
    <div class="menu-sep"></div>
    <div title="close" data-options="iconCls:'delete'">关闭</div>
    <div title="closeOther" data-options="iconCls:'delete'">关闭其他</div>
    <div title="closeAll" data-options="iconCls:'delete'">关闭所有</div>
</div>

</body>
</html>