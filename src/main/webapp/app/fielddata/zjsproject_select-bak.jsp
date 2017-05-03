<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String userId = null;
    List<Map<String, Object>> largeCostTypeInfos = new ArrayList<Map<String, Object>>();
    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        userId = sessionInfo.getId();
        List<Map<String, Object>> costInfos = sessionInfo.getCostTypeInfos().get("dataCostInfos");
        for (Map<String, Object> costInfo : costInfos) {
            if (costInfo.get("pid").equals("-1")) {
                largeCostTypeInfos.add(costInfo);
            }
        }
    }

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
    var dataGrid;
    $(function () {
        $
                .ajax({
                    type: 'POST',
                    url: '${pageContext.request.contextPath}/fieldDataController/securi_zjsprojectTreeGrid',
                    cache: false,
                    dataType: 'json',
                    success: function (data) {
                        dataGrid = $('#dataGrid')
                                .treegrid(
                                        {
                                            idField: 'id',
                                            treeField: 'id',
                                            width: '100%',
                                            height: '450',
                                            parentField: 'pid',
                                            striped: true,
                                            data: $.parseJSON(data),
                                            animate: true,
                                            rownumbers: true,
                                            columns: [[

                                                {
                                                    title: 'ID',
                                                    field: 'id',
                                                    width: 100
                                                },
                                                {
                                                    title: '项目编码',
                                                    field: 'zjsdwgcBh',
                                                    width: 200
                                                },
                                                {
                                                    title: '项目名称',
                                                    field: 'name',
                                                    width: 400
                                                },
                                                {
                                                    title: '路径',
                                                    field: 'filePath',
                                                    width: 400,
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
                                            toolbar: '#toolbar',
                                            onLoadSuccess: function () {
                                                $('#searchForm table').show();
                                                $(this).treegrid('collapseAll');
                                                parent.$.messager.progress('close');
                                                $(this).treegrid('tooltip');
                                            }
                                        });
                    }
                });

    });

    function chooseProjectFun(id, name, filePath, userId) {
        document.getElementById("projectId").value = id;
        document.getElementById("projectName").value = name;
        document.getElementById("fileName").value = filePath;
        document.getElementById("userId").value = userId;

        var largeCostTypeName = $('#largeCostType  option:selected').text();
        var projectName = $('#projectName').val();
        $('#dataName').val(projectName + '-' + largeCostTypeName);
    }

    $(function () {
        $('#form')
                .form(
                        {
                            url: '${pageContext.request.contextPath}/fieldDataController/securi_cloudImport',

                            onSubmit: function () {
                                if (document.getElementById("proId").value == '') {
                                    alert('请选择项目');
                                    return false;
                                }
                                if (document.getElementById("largeCostType").value == '') {
                                    alert('请选择费用类别');
                                    return false;
                                }
                                if (document.getElementById("section").value == '') {
                                    alert('请选择标段');
                                    return false;
                                }
                                if (document.getElementById("projectId").value == '') {
                                    alert('请选择氿上项目');
                                    return false;
                                }
                                parent.$.messager.progress({
                                    title : '提示',
                                    text : '导入中，请稍后....'
                                });
                                return true;
                            },
                            success: function (result) {
                                parent.$.messager.progress('close');
                                result = $.parseJSON(result);
                                if (result.success) {
                                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
                                    parent.$.modalDialog.handler.dialog('close');
                                    parent.$.messager.alert('提示', '导入成功', 'success');
                                } else {
                                    parent.$.messager.alert('错误', result.msg, 'error');
                                }
                            }
                        });

    });

    $.ajax({
        url: '${pageContext.request.contextPath}/projectController/securi_getProjects',    //后台webservice里的方法名称
        type: "post",
        dataType: "json",
        contentType: "application/json",
        traditional: true,
        success: function (data) {
            debugger
            var projectInfos = data.obj;
            var optionstring = "";
            for (var i in projectInfos) {
                if ($('#maxProjectId').val() == projectInfos[i].id) {
                    optionstring += "<option value=\"" + projectInfos[i].id + "\" selected = 'selected'>" + projectInfos[i].text + "</option>";
                } else {
                    optionstring += "<option value=\"" + projectInfos[i].id + "\" >" + projectInfos[i].text + "</option>";
                }
            }
            $("#proId").html("<option value=''>请选择项目</option> "+optionstring);
            changeProject();
        },
        error: function (msg) {
            alert("系统异常，请联系管理员！");
        }
    });

    function changeProject() {
        $.ajax({
            url: '${pageContext.request.contextPath }/itemController/securi_getSelectItems',
            type: 'post',
            data: {'projectId': $('#proId').val()},
            dataType: 'json',
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            success: function (data) {
                if (data.success) {
                    var _section = data.obj.section;
                    var sectionInfos = data.obj.itemList;
                    var optionstring = "";
                    for (var i in sectionInfos) {
                        if (_section == sectionInfos[i].id) {
                            optionstring += "<option value=\"" + sectionInfos[i].id + "\" selected = 'selected'>" + sectionInfos[i].text + "</option>";
                        }
                        optionstring += "<option value=\"" + sectionInfos[i].id + "\" >" + sectionInfos[i].text + "</option>";
                    }
                    $("#section").html("<option value=''>请选择标段</option> "+optionstring);

                }

            }
        });
    };

    function changeLargeCostType() {
        var largeCostTypeName = $('#largeCostType  option:selected').text();
        var projectName = $('#projectName').val();
        $('#dataName').val(projectName + '-' + largeCostTypeName);
    }

    //过滤条件查询
    function searchFun() {
        if($('#startTime').val() != '' && $('#endTime').val() != '') {
            $('#startTime').val($('#startTime').val().substring(0, 10));
            $('#endTime').val($('#endTime').val().substring(0, 10));
        }
        dataGrid.treegrid('load', $.serializeObject($('#searchForm')));

        $.post('${pageContext.request.contextPath}/fieldDataController/securi_zjsprojectTreeGrid',
            {keyword:$('#keyword').val(), startTime: $('#startTime').val(), endTime: $('#endTime').val()},
            function(data) {
                debugger;
                dataGrid.treegrid('loadData', $.parseJSON(data));
            }, 'json');
    }

</script>

<div class="easyui-layout" data-options="fit:true,border:false" style="overflow:auto ">
    <form class="form-inline" role="form" id="form" method="post" enctype="multipart/form-data">
        <input type="hidden" id="maxProjectId" name="maxProjectId" value="${maxProjectId}"/>
        <table class="table_style">
            <tr>
                <td>
                    <span>项目</span>
                </td>
                <td>
                    <select style="width:220px;" id="proId" name="proId" onchange="changeProject()">
                    </select>
                </td>

            </tr>
            <tr>
                <td>
                    <span>费用类别</span>
                </td>
                <td>
                    <select style="width:220px;" id="largeCostType" name="largeCostType" onchange="changeLargeCostType()">
                        <option value="">请选择费用类别</option>
                        <c:forEach var="largeCostTypeInfo" items="<%= largeCostTypeInfos %>" varStatus="index">
                            <option value="${largeCostTypeInfo.itemCode}">${largeCostTypeInfo.costType}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td>
                    <span>标段</span>
                </td>
                <td>
                    <select style="width:220px;" id="section" name="section" onchange="changeSection()">
                        <option>请选择标段</option>
                    </select>
                </td>

            </tr>
            <tr>
                <td>
                    <span>单位工程</span>
                </td>
                <td>
                    <input name="projectId" id="projectId" value='' type="hidden"/>
                    <input name="fileName" id="fileName" value='' type="hidden"/>
                    <input name="userId" id="userId" value="" type="hidden"/>
                    <div class="form-group">
                        <input type="text" class="form-control" id="projectName" name="projectName" readonly="readonly"
                               placeholder="选择导入项目">
                    </div>
                </td>

            </tr>
            <tr>
                <td>
                    <span>名称</span>
                </td>
                <td>
                    <input type="text" id="dataName" name="dataName" value=""/>
                </td>
            </tr>
        </table>
    </form>
    <hr />


    <div class="easyui-layout" data-options="fit : true,border : false">
        <!-- 条件查询 -->
        <div data-options="region:'north',title:'查询条件',border:false"
             style="height: 75px; overflow: hidden;">
            <form class="form-inline" id="searchForm">
                <table class="table_style">
                    <tr>
                        <%--<td>--%>
                            <%--<div class="form-group">--%>
                                <%--<input type="text" class="form-control" name="keyword" id="keyword" placeholder="输入项目部分信息"/>--%>
                            <%--</div>--%>
                        <%--</td>--%>

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
            <table id="dataGrid"></table>
        </div>
    </div>

    <!-- toolBar -->
    <%--<div id="toolbar" style="display: none;">--%>
        <%--<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'search_new',plain:true" onclick="searchFun();">查询</a>--%>
    <%--</div>--%>

</div>