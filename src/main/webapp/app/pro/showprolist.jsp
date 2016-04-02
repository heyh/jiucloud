<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
    Integer parentId = 1;
    String projectInfos = null;
    List<String> rightList = null;
    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        parentId = sessionInfo.getParentId();
        projectInfos = sessionInfo.getProjectInfos();
        rightList = sessionInfo.getRightList();
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
							fitColumns : true,
							border : false,
							pagination : true,
							idField : 'id',
							pageSize : 10,
							pageList : [ 10, 20, 30, 40, 50 ],
							sortName : 'name',
							sortOrder : 'asc',
							checkOnSelect : false,
							selectOnCheck : false,
							nowrap : false,
							frozenColumns : [ [ {
								field : 'id',
								title : '多选框',
								width : 100,
								checkbox : true
							}, {
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
										width : 150,
										formatter : function(value, row, index) {
                                            var str = value;
                                            if(row.city != undefined && row.city != '') {
                                                str = str + '-' + row.city;
                                            }
                                            if(row.area != undefined && row.area != '') {
                                                str = str + '-' + row.area;
                                            }
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
//									{
//										field : 'gczt',
//										title : '工程状态',
//										width : 100
//									},
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
															'<img onclick="viewFun(\'{0}\');" src="{1}" title="预览"/>',
															row.id,
															'${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/preview-blue.png');
											str += '&nbsp;';
                                            if ( <%= parentId == 0 || rightList.contains("3") || rightList.contains("4") || rightList.contains("5") || rightList.contains("6")%> ) {
                                                if ('1' == row.isLock) {
                                                    str += '&nbsp;';
                                                    str += $
                                                            .formatString(
                                                            ' <img onclick="unLockFun(\'{0}\');" src="{1}" title="解锁"/>',
                                                            row.id,
                                                            '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/unlock-blue.png');
                                                } else {
                                                    str += $
                                                            .formatString(
                                                            ' <img onclick="eidtFun(\'{0}\');" src="{1}" title="修改"/>',
                                                            row.id,
                                                            '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/modify-blue.png');
                                                    str += '&nbsp;';
                                                    str += $
                                                            .formatString(
                                                            '<img onclick="deleteFun(\'{0}\');" src="{1}" title="删除"/>',
                                                            row.id,
                                                            '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/delete-blue.png');

                                                    str += '&nbsp;';
                                                    str += $
                                                            .formatString(
                                                            ' <img onclick="lockFun(\'{0}\');" src="{1}" title="锁定"/>',
                                                            row.id,
                                                            '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/lock-blue.png');
                                                }
                                            }
											return str;
										}
									} ] ],
							toolbar : '#toolbar',
							onLoadSuccess : function() {
								$('#searchForm table').show();
								parent.$.messager.progress('close');
								$(this).datagrid('tooltip');
							},
							onRowContextMenu : function(e, rowIndex, rowData) {
								e.preventDefault();
								$(this).datagrid('unselectAll').datagrid(
										'uncheckAll');
								$(this).datagrid('selectRow', rowIndex);
							}
						});
	});

	//删除
	function deleteFun(id) {
        // add by heyh
        if (<%= parentId != 0 %>) {
            return;
        }
		if (id == undefined) {//点击右键菜单才会触发这个
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		} else {//点击操作里面的删除图标会触发这个
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.messager
				.confirm(
						'询问',
						'您是否要删除当前项目？',
						function(b) {
							if (b) {
								parent.$.messager.progress({
									title : '提示',
									text : '数据处理中，请稍后....'
								});
								$
										.ajax({
											type : "post",
											url : '${pageContext.request.contextPath}/projectController/deleteProject',
											data : {
												id : id
											},
											dataType : "json",
											success : function(data) {
												if (data.success == true) {
													searchFun();
												}
											}
										});
							}
						});
	};

	//批量删除
	function batchDeleteFun() {
		var rows = dataGrid.datagrid('getChecked');
		var ids = [];
		if (rows.length > 0) {
			parent.$.messager.confirm('确认', '您是否要删除当前选中的数据？', function(r) {
				if (r) {
					parent.$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍后....'
					});
					for (var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
					var getTimestamp = new Date().getTime();
					$.getJSON(
							'${pageContext.request.contextPath}/projectController/batchDeleteProject?t='
									+ getTimestamp, {
								ids : ids.join(',')
							}, function(result) {
								if (result.success) {
									dataGrid.datagrid('load');
									dataGrid.datagrid('uncheckAll').datagrid(
											'unselectAll').datagrid(
											'clearSelections');
								}
								parent.$.messager.alert('提示', result.msg,
										'info');
								parent.$.messager.progress('close');
							});
				}
			});
		} else {
			parent.$.messager.show({
				title : '提示',
				msg : '请勾选要删除的记录！'
			});
		}
	}

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
		parent.$
				.modalDialog({
					title : '项目详情',
					width : 750,
					height : 550,
					href : '${pageContext.request.contextPath}/projectController/findOneView?proId='
							+ id

				});
	}

	//编辑
	function eidtFun(id) {

        // add by heyh
        if (<%= parentId != 0 && !rightList.contains("3") && !rightList.contains("4") && !rightList.contains("5") && !rightList.contains("6")%>) {
            return;
        }
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$
				.modalDialog({
					title : '修改工程',
					width : 1050,
					height : 600,
					href : '${pageContext.request.contextPath}/projectController/edit?proId='
							+ id,
					buttons : [ {
						text : '修改',
						handler : function() {
							parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
							var f = parent.$.modalDialog.handler.find('#form');
							f.submit();
						}
					} ]
				});

	}

	//添加
	function addFun() {
		parent.$
				.modalDialog({
					title : '新增工程',
					width : 1050,
					height : 600,
					href : '${pageContext.request.contextPath}/projectController/toAddPage',
					buttons : [ {
						text : '添加',
						handler : function() {
							parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
							var f = parent.$.modalDialog.handler.find('#form');
							f.submit();
						}
					} ]
				});
            <%--var url = '${pageContext.request.contextPath}/projectController/toAddPage';--%>
            <%--var text = "新增工程";--%>
            <%--var params = {--%>
                <%--url : url,--%>
                <%--title : text,--%>
                <%--iconCls : 'wrench'--%>
            <%--};--%>
            <%--window.parent.ac(params);--%>
            //parent.$.modalDialog.handler.dialog('close');
	};

    // 锁定工程
    function lockFun(id) {

        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = dataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager
                .confirm(
                '询问',
                '您是否要锁定当前项目？',
                function(b) {
                    if (b) {
                        parent.$.messager.progress({
                            title : '提示',
                            text : '数据处理中，请稍后....'
                        });
                        $
                                .ajax({
                                    type : "post",
                                    url : '${pageContext.request.contextPath}/projectController/securi_lockProject',
                                    data : {
                                        id : id
                                    },
                                    dataType : "json",
                                    success : function(data) {
                                        if (data.success == true) {
                                            searchAllProject();
                                        }
                                    }
                                });
                    }
                });
    };

    // 解锁工程
    function unLockFun(id) {

        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = dataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager
                .confirm(
                '询问',
                '您是否要解锁当前项目？',
                function(b) {
                    if (b) {
                        parent.$.messager.progress({
                            title : '提示',
                            text : '数据处理中，请稍后....'
                        });
                        $
                                .ajax({
                                    type : "post",
                                    url : '${pageContext.request.contextPath}/projectController/securi_unLockProject',
                                    data : {
                                        id : id
                                    },
                                    dataType : "json",
                                    success : function(data) {
                                        if (data.success == true) {
                                            searchAllProject();
                                        }
                                    }
                                });
                    }
                });
    };

    function searchAllProject() {
        var o = {};
        o['keyword'] = null;
        o['gczt'] = null;
        o['proName'] = null;
        o['projetcId'] = null;
        o['kgrq'] = null;
        o['jgrq'] = null;
        dataGrid.datagrid('load',o);
    }
	//过滤条件查询
	function searchFun() {
		if ($('#kgrq').val() != '') {
//			$('#kgrq').val($('#kgrq').val() + ' 00:00:00');
			$('#kgrq').val($('#kgrq').val().substring(0, 10) + ' 00:00:00');
		}
		if ($('#jgrq').val() != '') {
//			$('#jgrq').val($('#jgrq').val() + ' 00:00:00');
			$('#jgrq').val($('#jgrq').val().substring(0, 10) + ' 23:59:59');
		}
		dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
	};

	//清除条件
	function cleanFun() {
		$('#searchForm input').val('');
		dataGrid.datagrid('load', {});
	};

    $(document).ready(function() {
        if ( <%= parentId == 0 || rightList.contains("4") %> ) {
            $('#dataGrid').datagrid('showColumn', 'gchtj');
            $('#dataGrid').datagrid('showColumn', 'coll');
        } else {
            $('#dataGrid').datagrid('hideColumn', 'gchtj');
            $('#dataGrid').datagrid('hideColumn', 'coll');
        }

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
                        <td >搜索关键字:&nbsp;
                            <input name="keyword" id="keyword" style="width: 136px" placeholder="可以模糊查询" class="span2" />
                        </td>
						<td>项目名称:&nbsp;
                            <%--<input name="proName" placeholder="可以模糊查询" class="span2" style="width: 200px" />--%>
                            <select  style="width: 136px" name="proName" id="proName">
                                <option ></option>
                            </select>
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
	<div id="toolbar" style="display: none;">
        <a onclick="addFun();" href="javascript:void(0);"
            class="easyui-linkbutton"
            data-options="plain:true,iconCls:'add_new'"
                <%= parentId == 0 || rightList.contains("3") ? "" : "disabled"%> >添加</a>
        <a onclick="batchDeleteFun();" href="javascript:void(0);"
            class="easyui-linkbutton"
            data-options="plain:true,iconCls:'batdelete_new'"
                <%= parentId == 0 ? "" : "disabled"%> >批量删除</a>
        <a href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'search_new',plain:true" onclick="searchFun();">条件查询</a>
		<a href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'zhongzhiguolvtiaojian_new',plain:true"
			onclick="cleanFun();">清空条件</a>
	</div>
</body>
</html>