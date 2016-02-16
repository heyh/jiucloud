<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
    Integer parentId = 1;
    String projectInfos = null;
    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        parentId = sessionInfo.getParentId();
        projectInfos = sessionInfo.getProjectInfos();
    }

%>
<!DOCTYPE html>
<html>
<head>
<title>工程名称管理</title>
<jsp:include page="../../inc.jsp"></jsp:include>
    <link href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/css/select2.min.css" rel="stylesheet" />
    <script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/js/select2.min.js"></script>
<script type="text/javascript">

	var dataGrid;
	//Ajax加载数据库中数据，传入rows与total
	$(function() {
		dataGrid = $('#dataGrid')
				.datagrid(
						{
							url : '${pageContext.request.contextPath}/projectController/dataGrid',
							fit : true,
							fitColumns : false,
							border : false,
							pagination : true,
							idField : 'id',
							pageSize : 10,
							pageList : [ 10, 20, 30, 40, 50 ],
							sortName : 'name',
							sortOrder : 'asc',
//							checkOnSelect : false,
//							selectOnCheck : true,
							nowrap : false,
							frozenColumns : [ [{
								field : 'projectId',
								title : '项目编号',
								width : 100,
								sortable : true
							} ] ],
							columns : [ [
									{
										field : 'proName',
										title : '项目名称',
										width : 250
									},
									{
										field : 'shortname',
										title : '工程简称',
										width : 100
									},
									{
										field : 'provice',
										title : '所在区域',
										width : 100,
										formatter : function(value, row, index) {
											var str = value + '-' + row.city;
											return str;
										}
									},
									{
										field : 'gchtj',
										title : '工程合同价',
										width : 100
									},
									{
										field : 'manager',
										title : '施工项目经理',
										width : 100
									},
									{
										field : 'gczt',
										title : '工程状态',
										width : 100
									},
                                    {
                                        field : 'isLock',
                                        title : '锁定状态',
                                        width : 100,
                                        formatter: function(value, row, index) {
                                            var str = '1' == value ? '锁定' : '正常';
                                            return str;
                                        }

                                    },
									{
										field : 'coll',
										title : '协作单位',
										width : 100,
										formatter : function(value, row, index) {
											var str = '';
											str += $
													.formatString(
															'<img onclick="collFun(\'{0}\');" src="{1}" title="查看协作单位"/>',
															row.id,
															'${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/wanglaidanwei-blue.png');
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
															'<img onclick="viewFun(\'{0}\');" src="{1}" title="详情"/>',
															row.id,
															'${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/preview-blue.png');
											return str;
										}
									} ] ],
							toolbar : '#toolbar',
							onLoadSuccess : function() {
								$('#searchForm table').show();
								parent.$.messager.progress('close');
								$(this).datagrid('tooltip');
							},
                            onClickRow: function (rowIndex, rowData) {
                                $(this).datagrid('unselectRow', rowIndex);
                            }

                        });
	});

    //查看协作单位
    function collFun(id) {
        var url = '${pageContext.request.contextPath}/collcontroller/securi_toShowPage?pid=' + id;
        var text = "协作单位管理";
        var params = {
            url : url,
            title : text,
            iconCls : 'wrench'
        }
        window.parent.ac(params);
        parent.$.modalDialog.handler.dialog('close');
    }

    //查看
    function viewFun(id) {

        if (id == undefined) {
            var rows = dataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }

        var url = '${pageContext.request.contextPath}/projectStatController/showProjectDetail4Leader?id=' + id;
        var text = "项目详情";
        var params = {
            url : url,
            title : text,
            iconCls : 'wrench'
        };
        window.parent.ac(params);
    }

	//过滤条件查询
	function searchFun() {
		if ($('#kgrq').val() != '') {
			$('#kgrq').val($('#kgrq').val().substring(0, 10) + ' 00:00:00');
		}
		if ($('#jgrq').val() != '') {
			$('#jgrq').val($('#jgrq').val().substring(0, 10) + ' 23:59:59');
		}
		dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
	};

	//清除条件
	function cleanFun() {
		$('#searchForm input').val('');
		dataGrid.datagrid('load', {});
	};

    // select2搜索下拉框
    $(document).ready(function() {
        $("#proName").select2({
            placeholder: "可以模糊查询",
            allowClear: true,
            data:<%=projectInfos%>
        });

        $("#gczt").select2({
            placeholder: "可以模糊查询",
            allowClear: true
        });
    });

</script>


</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<!-- 条件查询 -->
		<div data-options="region:'north',title:'查询条件',border:false"
			style="height: 75px; overflow: hidden;">
			<form id="searchForm">
				<table class="table table-hover table-condensed"
					style="display: none;">
					<tr>
						<td>项目名称:&nbsp;
                            <%--<input name="proName" placeholder="可以模糊查询" class="span2" style="width: 200px" />--%>
                            <select  style="width: 136px" name="proName" id="proName">
                                <option ></option>
                            </select>
                        </td>
						<td >施工项目经理:&nbsp;
                            <input style="width: 136px" name="manager" placeholder="可以模糊查询" class="span2" />
                        </td>
						<td valign="middle">工程状态:&nbsp;
                            <select style="width: 136px" name="gczt" id="gczt">
								<option></option>
								<option>在建工程</option>
								<option>未开工工程</option>
								<option>分包工程</option>
								<option>已完成未验收工程</option>
								<option>已验收工程</option>
								<option>审计完工程</option>
								<option>纯养护工程</option>
								<option>养护结束工程</option>
								<option>尾款已付清工程</option>
								<option>其他工程</option>
						    </select>
                        </td>
						<td>开工起止日期:
                            <input class="span2" name="kgrq" id="kgrq" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly" value="${first }" />&nbsp;&nbsp; —
							&nbsp;&nbsp;
                            <input class="span2" name="jgrq" id="jgrq" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" readonly="readonly" value="${last }" />
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
	<div id="toolbar" >
        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'search_new',plain:true" onclick="searchFun();">条件查询</a>
		<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'zhongzhiguolvtiaojian_new',plain:true" onclick="cleanFun();">清空条件</a>
	</div>
</body>
</html>