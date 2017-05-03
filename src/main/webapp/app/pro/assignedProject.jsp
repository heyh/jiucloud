<%--
  Created by IntelliJ IDEA.
  User: heyh
  Date: 16/2/1
  Time: 下午9:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    Integer parentId = 1;
    List<String> rightList = null;
    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        parentId = sessionInfo.getParentId();
        rightList = sessionInfo.getRightList();
    }

%>

<script type="text/javascript"
        src="${pageContext.request.contextPath}/jslib/bootstrap-datepicker/js/bootstrap-datepicker.js"
        charset="UTF-8"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/jslib/bootstrap-datepicker/js/locales/bootstrap-datepicker.zh-CN.js"
        charset="UTF-8"></script>
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/jslib/bootstrap-datepicker/dist/css/bootstrap-datepicker.css">
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/jslib/bootstrap-datepicker/dist/css/bootstrap-datepicker.standalone.css">


<!-- 引入EasyUI -->
<script type="text/javascript"
        src="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.3/jquery.easyui.min.js"
        charset="UTF-8"></script>

<link rel="stylesheet" href="${pageContext.request.contextPath}/jslib/zTree_v3/css/zTreeStyle/zTreeStyle.css"
      type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/jslib/zTree_v3/js/jquery.ztree.core.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/jslib/zTree_v3/js/jquery.ztree.excheck.js"></script>

<style>
    .container-fluid {
        background: #f7f7f7;
    }

</style>

<script type="text/javascript">

    var zTree;
    var setting = {
        check: {
            enable: true,
            showLine: true
        },
        view: {
            dblClickExpand: false
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            onCheck: onCheck
        }
    };


    function onCheck(e, treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj("departmentTree");
        var nodes = zTree.getCheckedNodes(true);
        var v = "";
        var belongDeparts = "";
        for (var i = 0, l = nodes.length; i < l; i++) {
            v += nodes[i].name + ",";
            belongDeparts += nodes[i].id + ",";
        }
        if (v.length > 0) {
            v = v.substring(0, v.length - 1);
            belongDeparts = belongDeparts.substring(0, belongDeparts.length - 1);
        }
        var departmentObj = $("#departmentSel");
        departmentObj.attr("value", v);
        $('#belongDeparts').val(belongDeparts);
    }

    function showDepartment() {
        var departmentObj = $("#departmentSel");
        var departmentOffset = $("#departmentSel").offset();
        $("#departmentContent").css({
            left: departmentOffset.left + "px",
            top: departmentOffset.top + departmentObj.outerHeight() + "px"
        }).slideDown("fast");

        $("body").bind("mousedown", onBodyDown);
    }
    function hideDepartment() {
        $("#departmentContent").fadeOut("fast");
        $("body").unbind("mousedown", onBodyDown);
    }
    function onBodyDown(event) {
        if (!(event.target.id == "departmentBtn" || event.target.id == "departmentSel" || event.target.id == "departmentContent" || $(event.target).parents("#departmentContent").length > 0)) {
            hideDepartment();
        }
    }

    $(document).ready(function () {
        $.fn.zTree.init($("#departmentTree"), setting, ${departmentList});
        showDepartment();
    });

    $(function () {
        parent.$.messager.progress('close');
        $('#form').form({
            url: '${pageContext.request.contextPath}/projectController/securi_assignedProject',
            onSubmit: function () {
                parent.$.messager.progress({
                    title: '提示',
                    text: '数据处理中，请稍后....'
                });
                var isValid = $(this).form('validate');
                if (!isValid) {
                    parent.$.messager.progress('close');
                }

                return isValid;
            },
            success: function (result) {
                parent.$.messager.progress('close');
                result = $.parseJSON(result);
                if (result.success) {
                    parent.$.messager.progress('close');
                    jQuery.messager.show({
                        title: '温馨提示:',
                        msg: '分配成功!',
                        timeout: 3000,
                        showType: 'show'
                    });
                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    parent.$.messager.alert('错误', result.msg, 'error');
                }
            }
        });
    });


</script>

<div class="container-fluid">

    <form class="form-horizontal" name="form" id="form" method="post" role="form">
        <input name="id" id="id" value="${pro.id}" type="hidden"/>
        <input name="belongDeparts" id="belongDeparts" value="${pro.belongDeparts}" type="hidden">
        <div>
            <ul style="border: 1px solid #5aabe1;overflow-y: auto;margin:0 auto;width: 200px;">
                <li>
                    <input style="margin-top:10px; margin-bottom:10px; width:150px" id="departmentSel"
                           name="departmentSel"
                           type="text" readonly placeholder="选择部门" onclick="showDepartment();"/>
                    &nbsp;
                    <a id="departmentBtn" href="#" onclick="showDepartment(); return false;" style="width: auto">选择</a>
                    <div id="departmentContent" style="display:none; ">
                        <ul id="departmentTree" class="ztree"></ul>
                    </div>
                </li>
            </ul>
        </div>
    </form>

</div>

