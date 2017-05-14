<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%


%>
<style>
    .table_style {
        /*width: 90%;*/
        /*margin-bottom: 20px;*/
        /*border:1px solid #EDEDED*/

    }
    .table_style td{height:32px; color:#0e0e0e;padding-top:10px;padding-left:15px;color:#666}
</style>
<script type="text/javascript">
    var _url = '${pageContext.request.contextPath}/fieldDataController/securi_zjsprojectTreeGrid';
    var _clickUrl = '${pageContext.request.contextPath}/fieldDataController/securi_getZjsprojectTreeGridChild';
    var dataGrid;
    $(function () {

        dataGrid = $('#dataGrid')
            .treegrid(
                {
                    url: _url,
                    method: 'get',
                    idField: 'id',
                    treeField: 'name',
                    iconCls: 'icon-ok',
                    pageSize: 10,
                    pageList: [10, 20, 50],
                    rownumbers: true,
                    animate: true,
                    striped:true,//隔行变色,
                    collapsible: true,
                    fitColumns: true,
                    pagination: true,
                    columns: [[
                        {
                            title: '项目名称',
                            field: 'name',
                            width: 200
                        },
                        {
                            title: '项目编码',
                            field: 'zjsdwgcBh',
                            width: 200
                        },
                        {
                            title: '操作人',
                            field: 'userName',
                            width: 200
                        },
                        {
                            title: '路径',
                            field: 'filePath',
                            width: 100,
                            hidden: true
                        },
                        {
                            field: 'action',
                            title: '操作（选择）',
                            width: 100,
                            formatter: function (value, row,
                                                 index) {
                                var str = '';
                                if (row.level == 3) {
                                    str += $
                                        .formatString(
                                            '<img onclick="chooseProjectFun(\'{0}\',\'{1}\', \'{2}\', \'{3}\');" src="{4}" title="选择"/>',
                                            row.id,
                                            row.name,
                                            row.filePath,
                                            row.userId,
                                            '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/choose-blue.png');
                                }
                                return str;
                            }
                        }]],

                    onBeforeLoad: function (row, param) {
                        if(row)$(this).treegrid('options').url = _clickUrl;
                    },
                    toolbar: '#toolbar',
                    onLoadSuccess:function(data){
                        delete $(this).treegrid('options').queryParams['id'];
                        $('#searchForm table').show();
                        $(this).treegrid('collapseAll');
                        parent.$.messager.progress('close');
                        $(this).treegrid('tooltip');
                    },
                });
    });


    function chooseProjectFun(id, name, filePath, userId) {
        document.getElementById("projectId").value = id;
        document.getElementById("projectName").value = name;
        document.getElementById("fileName").value = filePath;
        document.getElementById("userId").value = userId;
    }

    $(function () {
        $('#form')
            .form(
                {
                    url: '${pageContext.request.contextPath}/fieldDataController/securi_backFill',

                    onSubmit: function () {
                        parent.$.messager.progress({
                            title: '提示',
                            text: '回填中，请稍后....'
                        });
                        return true;
                    },
                    success: function (result) {
                        parent.$.messager.progress('close');
                        result = $.parseJSON(result);
                        if (result.success) {
                            parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
                            parent.$.modalDialog.handler.dialog('close');
                            parent.$.messager.alert('提示', '回填成功', 'success');
                        } else {
                            parent.$.messager.alert('错误', result.msg, 'error');
                        }
                    }
                });

    });

    //过滤条件查询
    function searchFun() {
        if($('#startTime').val() != '' && $('#endTime').val() != '') {
            $('#startTime').val($('#startTime').val().substring(0, 10));
            $('#endTime').val($('#endTime').val().substring(0, 10));
        }

        $.post('${pageContext.request.contextPath}/fieldDataController/securi_zjsprojectTreeGrid',
            {keyword:$('#keyword').val(), startTime: $('#startTime').val(), endTime: $('#endTime').val()},
            function(data) {
                dataGrid.treegrid('loadData', data);
            }, 'json');
    }

</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <form class="form-horizontal" name="form" id="form" method="post" enctype="multipart/form-data" role="form">
        <div class="row-fluid" style="margin-top: 5px">
            <div class="span6">
                <div class="control-group">
                    <label class="control-label" for="feeType">费用类型:</label>
                    <div class="controls">
                        <select id="feeType" name="feeType">
                            <option value="0">审计</option>
                            <option value="1">预算</option>
                        </select>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="projectName">单位工程:</label>

                    <div class="controls">
                        <input name="projectId" id="projectId" value='' type="hidden"/>
                        <input name="fileName" id="fileName" value='' type="hidden"/>
                        <input name="userId" id="userId" value="" type="hidden"/>
                        <div class="form-group">
                            <input type="text" class="form-control" id="projectName" name="projectName" readonly="readonly" placeholder="选择导入项目">
                        </div>
                    </div>
                </div>
            </div>
            <div class="span6">

            </div>

        </div>
    </form>

    <%--<hr />--%>


    <div class="easyui-layout" data-options="fit : true,border : false">
        <!-- 条件查询 -->
        <div data-options="region:'north',title:'查询条件',border:false" style="height: 75px; overflow: auto;">
            <form class="form-inline" id="searchForm">
                <table class="table_style">
                    <tr>
                        <td>
                            <div class="form-group">
                                <input type="text" class="form-control" name="keyword" id="keyword" placeholder="输入项目部分信息"/>
                            </div>
                        </td>

                        <td>
                            <div class="form-group">
                                <input type="text" name="startTime" id='startTime' placeholder="点击选择时间"
                                       onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly"
                                       value='${first}'/> -
                                <input type="text" name="endTime" id='endTime' placeholder="点击选择时间"
                                       onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly"
                                       value='${last}'/>
                            </div>
                        </td>
                        <td>
                            <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'search_new',plain:true" onclick="searchFun();">查询</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <!-- 显示表格内容，list -->
        <div data-options="region:'center',border:false">
            <table id="dataGrid" style="height: 300px;"></table>
        </div>
    </div>

</div>