<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
    String underlingUsers = null;
    List<Map<String, Object>> docCostInfos = new ArrayList<Map<String, Object>>();
    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        underlingUsers = sessionInfo.getUnderlingUsers();
        docCostInfos = sessionInfo.getCostTypeInfos().get("docCostInfos");
    }
%>

<!DOCTYPE html>
<html>
<head>
<title>资料审批</title>
<jsp:include page="../../inc.jsp"></jsp:include>
    <link href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/css/select2.min.css" rel="stylesheet" />
    <script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/js/select2.min.js"></script>
<script type="text/javascript">

    formatterFirstDate = function(date) {
        var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
        var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
        + (date.getMonth() + 1);
        return date.getFullYear() + '-' + month + '-' + '01';
    };
    formatterCurrentDate = function(date) {
        var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
        var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
        + (date.getMonth() + 1);
        return date.getFullYear() + '-' + month + '-' + day;
    };

    window.onload = function () {
        $('#startTimeDoc').datebox('setValue', formatterFirstDate(new Date()));
        $('#endTimeDoc').datebox('setValue', formatterCurrentDate(new Date()));
    }

	var dataGrid;
    $(function() {
        dataGrid = $('#dataGridDoc')
                .datagrid(
                {
                    url : '${pageContext.request.contextPath}/fieldDataController/dataGrid?source=doc&needApproved=1',
                    fit : true,
                    fitColumns : true,
                    border : true,
                    pagination : true,
                    idField : 'id',
                    pageSize : 10,
                    pageList : [ 10, 20, 30, 40, 50 ],
                    sortName : 'name',
                    sortOrder : 'asc',
                    checkOnSelect : false,
                    selectOnCheck : false,
                    nowrap : false,
                    rownumbers: true,
                    columns : [ [
                        {
                            field : 'projectName',
                            title : '工程名称',
                            width : 250

                        },
                        {
                            field : 'costType',
                            title : '类型',
                            width : 100
                        },
                        {
                            field : 'dataName',
                            title : '名称',
                            width : 100
                        },
                        {
                            field : 'uname',
                            title : '操作人',
                            width : 100
                        },
                        {
                            field : 'creatTime',
                            title : '入库时间',
                            width : 100
                        },
                        {
                            field : 'needApproved',
                            title : '审批状态',
                            width : 100,
                            formatter : function(value, row, index) {
                                var str = '';
                                if ('0' == value) {
                                    str = '不需审批'
                                } else if ('1' == value) {
                                    str = '<span style="color: #ff0000">' + '未审批' + '</span>';
                                } else if ('2' == value) {
                                    str = '审批通过';
                                }  else if ('8' == value) {
                                    str = '<span style="color: #ff0000">' + '审批中' + '</span>';
                                } else if ('9' == value) {
                                    str = '审批未通过'
                                }
                                return str;
                            }
                        },
                        {
                            field : 'action',
                            title : '操作',
                            width : 100,
                            formatter : function(value, row, index) {
                                var str = '';
                                str += $
                                        .formatString(
                                        ' <img style="cursor:pointer" onclick="FileFun(\'{0}\');" src="{1}" title="附件管理"/>',
                                        row.id,
                                        '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/fujianguanli-blue.png');
                                if ('1' == row.needApproved || '8' == row.needApproved) {
                                    str += '&nbsp;';
                                    str += $
                                            .formatString(
                                            '<img style="cursor:pointer" onclick="approvedFun(\'{0}\', 2);" src="{1}" title="结束审批"/>',
                                            row.id,
                                            '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/approved-true.png');

                                    str += '&nbsp;';
                                    str += $
                                            .formatString(
                                            '<img style="cursor:pointer" onclick="approvedFun(\'{0}\', 8);" src="{1}" title="继续审批"/>',
                                            row.id,
                                            '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/approved-jixu.png');

                                    str += '&nbsp;';
                                    str += $
                                            .formatString(
                                            '<img style="cursor:pointer" onclick="approvedFun(\'{0}\', 9);" src="{1}" title="审批不通过"/>',
                                            row.id,
                                            '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/approved-false.png');
                                }
                                return str;
                            }
                        }
                    ] ],
                    toolbar : '#toolbarDoc',
                    onLoadSuccess : function() {
                        parent.$.messager.progress('close');
                        $(this).datagrid('tooltip');
                    }
                });
    });

    // 审批资料
    function approvedFun(id, approvedState) {

        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = dataGrid.datagrid('getSelections');
            id = rows[0].id;
        }

        var approvedTip = '';
        var approvedOption = '';
        if(approvedState == '2') {
            approvedTip = '确认审批通过,并结束后续审批?';
        } else if(approvedState == '8') {
            approvedTip = '确认审批通过,并继续后续审批?';
        } else if(approvedState == '9') {
            approvedTip = '确认打回当前记录?';
        }

        parent.$.messager
                .confirm(
                '询问',
                approvedTip,
                function(b) {
                    if (b) {
                        if(approvedState == '9') {
                            approvedOption = prompt("审批意见","")
                        }
                        parent.$.messager.progress({
                            title : '提示',
                            text : '数据处理中，请稍后....'
                        });
                        $.ajax({
                            type : "post",
                            url : '${pageContext.request.contextPath}/fieldDataController/securi_approvedField',
                            data : {
                                id : id,
                                approvedState: approvedState,
                                approvedOption: approvedOption
                            },
                            dataType : "json",
                            success : function(data) {
                                if (data.success == true) {
                                    searchFunDoc();
                                }
                            }
                        });
                    }
                });
    };

    $(document).ready(function() {

        $("#unameDoc").select2({
            placeholder: "可以模糊查询",
            allowClear: true,
            data:<%=underlingUsers%>
        });
        $("#docCostType").select2({
            tags: "true",
            placeholder: "可以模糊查询",
            allowClear: true
        });

    });

    //过滤条件查询
    function searchFunDoc() {
        var startTimeDoc = $('#startTimeDoc').datebox('getValue').substring(0, 10) + ' 00:00:00';
        var endTimeDoc = $('#endTimeDoc').datebox('getValue').substring(0, 10) + ' 23:59:59';
        $('#dataGridDoc').datagrid('reload',{uname:$('#unameDoc').val(),costType:$('#docCostType').val(),
            startTime: startTimeDoc, endTime:endTimeDoc});
    }
    //清除条件
    function cleanFunDoc() {
        $('#toolbarDoc input').val('');
        $('#dataGridDoc').datagrid('reload', {});
    }

    //附件管理
    function FileFun(id) {
        parent.$
                .modalDialog({
                    title : '附件管理',
                    width : 800,
                    height : 600,
                    href : '${pageContext.request.contextPath}/fieldDataController/securi_fieldDataFile?id='
                    + id,
                    buttons : [ {
                        text : '关闭',
                        handler : function() {
                            parent.$.modalDialog.handler.dialog('close');
                        }
                    } ]
                });
    }

</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
        <div id="toolbarDoc" class="fee_detail" style="display: none; height: 40px">
            <div style="margin-top: 6px">
                <span>操作人:</span>
                <select  style="width: 150px" name="unameDoc" id="unameDoc">
                    <option ></option>
                </select>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <span>资料类型:</span>
                <select style="width: 150px"  name="docCostType" id="docCostType">
                    <option></option>
                    <c:forEach var="costTypeInfo" items="<%= docCostInfos %>" varStatus="index">
                        <c:if test="${costTypeInfo.isSend == '0'}">
                            <optgroup label="${costTypeInfo.costType}"> " " </optgroup>
                        </c:if>
                        <c:if test="${costTypeInfo.isSend == '1'}">
                            <option value="${costTypeInfo.costType}">&nbsp;&nbsp;&nbsp;&nbsp;${costTypeInfo.costType}</option>
                        </c:if>
                    </c:forEach>
                </select>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <span>起止时间:</span>
                <input style="width: 150px" class="easyui-datebox" name="startTimeDoc" id='startTimeDoc' editable="false" placeholder="点击选择时间"  value='${first }' />
                -
                <input style="width: 150px" class="easyui-datebox"  name="endTimeDoc" id='endTimeDoc' editable="false" placeholder="点击选择时间"  value='${last }' />
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a href="javascript:void(0);" class="easyui-button" data-options="plain:true" onclick="searchFunDoc();">过滤条件</a>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a href="javascript:void(0);" class="easyui-button" data-options="plain:true" onclick="cleanFunDoc();">清空条件</a>
            </div>
        </div>

        <div data-options="region:'center',border:false">
            <table id="dataGridDoc" class="easyui-datagrid" />
        </div>
    </div>

</body>
</html>