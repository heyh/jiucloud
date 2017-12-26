<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="net.sf.json.JSONArray" %>
<%@ page import="net.sf.json.JSONObject" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    List<Map<String, Object>> billCostInfos = new ArrayList<Map<String, Object>>();
    JSONArray jsonArray = new JSONArray();
    JSONArray billCostTree = new JSONArray();

    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        billCostInfos = sessionInfo.getCostTypeInfos().get("billCostInfos");
        for (Map<String, Object> nodeMap : billCostInfos) {
            JSONObject nodeJson = JSONObject.fromObject(nodeMap);
            jsonArray.add(nodeJson);
        }
        billCostTree = sessionInfo.getBillCostTree();
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>氿上网数据统计</title>
    <jsp:include page="../../inc.jsp"></jsp:include>
    <link href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/css/select2.min.css" rel="stylesheet"/>
    <script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/js/select2.min.js"></script>
    <link rel="stylesheet" type="text/css" media="screen"
          href="${pageContext.request.contextPath}/app/analysis/css-table.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/app/analysis/js/style-table.js"></script>
    <link rel="stylesheet" type="text/css" media="screen"
          href="${pageContext.request.contextPath}/jslib/MonthPicker/monthPicker.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jslib/MonthPicker/monthPicker.js"></script>

    <script type="text/javascript">

        $(document).ready(function () {
            parent.$.messager.progress('close');

            $('#searchForm table').show();

            $("#projectName").select2({
                allowClear: true,
            });

            var optionstring = '';
            debugger
            var arr =  ${departments};
            if (arr.length < 2) {
                $("#selDepartmentId").hide();
            } else {
                $("#selDepartmentId").show();
            }

            for (var i in arr) {
                if ("${selDepartmentId}" == arr[i].id) {
                    optionstring += "<option value=" + arr[i].id + " selected = 'selected' >" + arr[i].name + "</option>";
                } else {
                    optionstring += "<option value=" + arr[i].id + ">" + arr[i].name + "</option>";
                }

            }
            $("#selDepartmentId").html(optionstring);

        });

        function searchFun() {
            $('#searchForm').submit();
        }

        function exportFun(objTab) {
            var str = '';
            str += '&startDate=' + $('#startDate').val();
            str += '&endDate=' + $('#endDate').val();
            str += '&selDepartmentId=' + $('#selDepartmentId').val();
            var url = "${pageContext.request.contextPath}/analysisController/securi_boqExecl?" + str;
            window.open(url);
        }

        $(document).ready(function () {
            $('#costTypeRef').combotree({
                data: <%= billCostTree %>,
                lines: true,
                editable: true,
                onLoadSuccess: function () {
                    $('#costTypeRef').combotree('tree').tree("collapseAll");
                },
                //选择树节点触发事件
                onSelect: function (node) {
                    var _jsonArray = <%= jsonArray %>;
                    for (var i = 0; i < _jsonArray.length; i++) {
                        if (_jsonArray[i].nid == node.id) {
                            $('#itemCode').val(_jsonArray[i].itemCode);
                            $('#costType').val(_jsonArray[i].costType);
                            break;
                        }
                    }
                }
            });
        });

        (function(){
            $.fn.combotree.defaults.editable = true;
            $.extend($.fn.combotree.defaults.keyHandler,{
                up:function(){
                    console.log('up');
                },
                down:function(){
                    console.log('down');
                },
                enter:function(){
                    console.log('enter');
                },
                query:function(q){
                    var t = $(this).combotree('tree');
                    var nodes = t.tree('getChildren');
                    for(var i=0; i<nodes.length; i++){
                        var node = nodes[i];
                        if (node.text.indexOf(q) >= 0){
                            $(node.target).show();
                        } else {
                            $(node.target).hide();
                        }
                    }
                    var opts = $(this).combotree('options');
                    if (!opts.hasSetEvents){
                        opts.hasSetEvents = true;
                        var onShowPanel = opts.onShowPanel;
                        opts.onShowPanel = function(){
                            var nodes = t.tree('getChildren');
                            for(var i=0; i<nodes.length; i++){
                                $(nodes[i].target).show();
                            }
                            onShowPanel.call(this);
                        };
                        $(this).combo('options').onShowPanel = opts.onShowPanel;
                    }
                }
            });
        })(jQuery);
    </script>


</head>
<body>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 条件查询 -->
    <div data-options="region:'north',title:'查询条件',border:false" style="height: 120px; overflow: hidden;">
        <form id="searchForm" autocomplete="off">
            <table class="table table-hover table-condensed" style="display: none;">
                <tr>
                    <td>
                        <div class="form-group">
                            <input type="text" name="startDate" id='startDate' placeholder="开始时间"
                                   onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly"
                                   value='${first}' style="margin-top: 10px"/> -
                            <input type="text" name="endDate" id='endDate' placeholder="结束时间"
                                   onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly"
                                   value='${last}' style="margin-top: 10px"/>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <select id="selDepartmentId" name="selDepartmentId" style="margin-top: 10px">
                            </select>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="text" style="height: 30px" class="easyui-combotree" name="costTypeRef" id="costTypeRef" placeholder="请选择" value="${costType}">
                            <input type="hidden" name="costType" id="costType" value="${costType}">
                            <input type="hidden" name="itemCode" id="itemCode" value="${itemCode}">

                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: center;" colspan=3>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'search_new',plain:true" onclick="searchFun();">查询</a>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'out_new',plain:true" onclick="exportFun();">execl导出</a>
                    </td>
                </tr>

            </table>
        </form>
    </div>

    <div id="toolbar" style="display: none;">
        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'brick_add',plain:true" onclick="searchFun();">列表展示</a>
    </div>

    <!-- 显示表格内容，list -->
    <div data-options="region:'center',border:false">
        <table style="width: 100%">
            <%--<thead>--%>
            <%--<tr>--%>
                <%--<th colspan="6" style="font-size: medium">市政养护工程清单一览表</th>--%>
            <%--</tr>--%>
            <%--</thead>--%>
            <%--<tbody></tbody>--%>
            <c:forEach items="${projects}" var="project" varStatus="status">
                <thead>
                <tr>
                    <th colspan="8" style="text-align: left;">工程名称：${project.projectName}</th>
                </tr>
                <tr>
                    <th scope="col">序号</th>
                    <th scope="col">项目编码</th>
                    <th scope="col">项目名称</th>
                    <th scope="col">项目特征描述</th>
                    <th scope="col">计量单位</th>
                    <th scope="col">工程量</th>
                    <th scope="col">审批状态</th>
                    <th scope="col">备注</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${boq}" var="item" varStatus="status">
                    <c:choose>
                        <c:when test="${item.project_id == project.projectId}">
                            <tr>
                                <th style="text-align: center; width: 50px">${status.index+1}</th>
                                <td style="text-align: center">${item.itemCode}</td>
                                <td style="text-align: left">${item.dataName}</td>
                                <td style="text-align: left">${item.remark}</td>
                                <td style="text-align: center; width: 50px" >${item.unit}</td>
                                <td style="text-align: center">${item.count}</td>
                                <c:choose>
                                    <c:when test="${item.needApproved == '0'}">
                                        <td style="text-align: center; width: 50px">不需审批</td>
                                    </c:when>
                                    <c:when test="${item.needApproved == '1'}">
                                        <td style="text-align: center; width: 50px">未审批</td>
                                    </c:when>
                                    <c:when test="${item.needApproved == '2'}">
                                        <td style="text-align: center; width: 50px">审批通过</td>
                                    </c:when>
                                    <c:when test="${item.needApproved == '8'}">
                                        <td style="text-align: center; width: 50px">审批中</td>
                                    </c:when>
                                    <c:when test="${item.needApproved == '9'}">
                                        <td style="text-align: center; width: 50px;color: #ff0000">审批未通过</td>
                                    </c:when>
                                </c:choose>
                                <td style="text-align: center">${item.id}</td>
                            </tr>
                        </c:when>
                    </c:choose>

                </c:forEach>
                </tbody>
            </c:forEach>
        </table>
    </div>
</div>

</body>
</html>