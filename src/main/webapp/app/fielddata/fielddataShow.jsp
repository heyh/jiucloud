<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String userId = null;
    String underlingUsers = null;
    String projectInfos = null;
    List<Map<String, Object>> dataCostInfos = new ArrayList<Map<String, Object>>();
    
    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        userId = sessionInfo.getId();
        underlingUsers = sessionInfo.getUnderlingUsers();
        projectInfos = sessionInfo.getProjectInfos();
        dataCostInfos = sessionInfo.getCostTypeInfos().get("dataCostInfos");
    }

%>

<!DOCTYPE html>
<html>
<head>
<title>服务号管理</title>
<jsp:include page="../../inc.jsp"></jsp:include>
    <link href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/css/select2.min.css" rel="stylesheet" />
    <script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/js/select2.min.js"></script>
<script type="text/javascript">
	var dataGrid;
	$(function() {
		dataGrid = $('#dataGrid')
				.datagrid(
						{
							url : '${pageContext.request.contextPath}/fieldDataController/dataGrid?source=data',
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
							columns : [ [
									{
										field : 'id',
										title : '多选框',
										width : 100,
										checkbox : true
									},
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
										field : 'unit',
										title : '单位',
										width : 100
									},
									{
										field : 'price',
										title : '单价',
										width : 100
									},
									{
										field : 'count',
										title : '数量',
										width : 100
									},
									{
										field : 'moeny',
										title : '金额',
										width : 100,
										formatter : function(value, row, index) {
											return (row.count * row.price)
													.toFixed(2);
										}

									},
									{
										field : 'specifications',
										title : '规格类型',
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
                                        field : 'uid',
                                        title : '用户ID',
                                        width : 100,
                                        hidden: true
                                    },
                                    {
                                        field : 'isLock',
                                        title : '工程锁定标志',
                                        width : 100,
                                        hidden: true
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
                                            } else if ('8' == value) {
                                                str = '<span style="color: #ff0000">' + '审批中' + '</span>';
                                            } else if ('9' == value) {
                                                str = '<span style="color: #ff0000">' + '审批未通过' + '</span>';
                                            }
                                            return str;
                                        }
                                    },
                                    {
                                        field : 'currentApprovedUser',
                                        title : '当前审批人',
                                        width : 100
                                    },
									{
										field : 'action',
										title : '操作',
										width : 100,
										formatter : function(value, row, index) {
											var str = '';
                                            // modify by heyh 当数据填报之后，在当日内23:59分内均可以修改自己填报数据
                                            var userId = <%= userId%>;
                                            if(compareDate(getCurrentDate(), row.creatTime.substring(0, 10)) == 0 && userId == row.uid) {
                                                if ('0' == row.isLock && '2' != row.needApproved) {
                                                    str += $
                                                            .formatString(
                                                            '<img onclick="editFun(\'{0}\');" src="{1}" title="编辑" />',
                                                            row.id,
                                                            '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/modify-blue.png');
                                                    str += '&nbsp;';
                                                    str += $
                                                            .formatString(
                                                            '<img onclick="deleteFun(\'{0}\');" src="{1}" title="删除"/>',
                                                            row.id,
                                                            '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/delete-blue.png');
                                                }
                                            }
											str += $
													.formatString(
															' <img onclick="FileFun(\'{0}\');" src="{1}" title="附件管理"/>',
															row.id,
															'${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/fujianguanli-blue.png');
											return str;
										}
									} ] ],
							toolbar : '#toolbar',
							onLoadSuccess : function() {
								$('#searchForm table').show();
								parent.$.messager.progress('close');
								$(this).datagrid('tooltip');
							}
                        });
    });

	//删除
	function deleteFun(id) {
		if (id == undefined) {//点击右键菜单才会触发这个
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		} else {//点击操作里面的删除图标会触发这个
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.messager
				.confirm(
						'询问',
						'您是否要删除当前配置？',
						function(b) {
							if (b) {
								parent.$.messager.progress({
									title : '提示',
									text : '数据处理中，请稍后....'
								});
								$
										.ajax({
											type : "post",
											url : '${pageContext.request.contextPath}/fieldDataController/delfieldData',
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
	}

	function batchDeleteFun() {
		var rows = dataGrid.datagrid('getChecked');
		var ids = [];
		if (rows.length > 0) {
            var userId = <%= userId%>;
            for(var i=0; i<rows.length; i++) {
                if(compareDate(getCurrentDate(), rows[i].creatTime.substring(0, 10)) != 0) {
                    alert("当前选中的数据存在不是今天保存的记录，不能删除！");
                    return;
                }
                if(userId != rows[i].uid) {
                    alert("当前选中的数据存在不是您本人保存的记录，不能删除！");
                    return;
                }
            }

			parent.$.messager
					.confirm(
							'确认',
							'您是否要删除当前选中的数据？',
							function(r) {
								if (r) {
									parent.$.messager.progress({
										title : '提示',
										text : '数据处理中，请稍后....'
									});
									for (var i = 0; i < rows.length; i++) {
										ids.push(rows[i].id);
									}
									$
											.getJSON(
													'${pageContext.request.contextPath}/fieldDataController/securi_bunchdelete?',
													{
														ids : ids.join(','),
														price_id : $(
																'#price_id')
																.val()
													},
													function(result) {
														if (result.success) {
															dataGrid
																	.datagrid('load');
															dataGrid
																	.datagrid(
																			'uncheckAll')
																	.datagrid(
																			'unselectAll')
																	.datagrid(
																			'clearSelections');
														}
														parent.$.messager
																.alert(
																		'提示',
																		result.msg,
																		'info');
														parent.$.messager
																.progress('close');
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

	//编辑
	function editFun(id) {
		parent.$
				.modalDialog({
					title : '编辑',
					width : 420,
					height : 460,
					href : '${pageContext.request.contextPath}/fieldDataController/upfieldData?id='
							+ id,
					buttons : [ {
						text : '下一步',
						handler : function() {
							parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
							var f = parent.$.modalDialog.handler.find('#form');
							f.submit();
						}
					} ]
				});
	}

	function addFun() {
		var url = '${pageContext.request.contextPath}/fieldDataController/addfieldData';
		var text = "添加费用数据";
		var params = {
			url : url,
			title : text,
			iconCls : 'wrench'
		};
		window.parent.ac(params);
		//parent.$.modalDialog.handler.dialog('close');
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

	function exportFun(objTab) {
		var str = '';
		str += '&uname=' + $('#uname').val();
		str += '&projectName=' + $('#projectName').val();
		str += '&costType=' + $('#costType').val();
		str += '&startTime=' + $('#startTime').val();
		str += '&endTime=' + $('#endTime').val();
		var url = "${pageContext.request.contextPath}/fieldDataController/securi_execl?a=1&source=data"
				+ str;
		window.open(url);
	}

	//过滤条件查询
	function searchFun() {
//		$('#startTime').val($('#startTime').val() + ' 00:00:00');
//		$('#endTime').val($('#endTime').val() + ' 00:00:00');
        if($('#startTime').val() != '' && $('#endTime').val() != '') {
            $('#startTime').val($('#startTime').val().substring(0, 10) + ' 00:00:00');
            $('#endTime').val($('#endTime').val().substring(0, 10) + ' 23:59:59');
        }

		dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
	}
	//清除条件
	function cleanFun() {
		$('#searchForm input').val('');
		dataGrid.datagrid('load', {});
	}

    // 获取当前日期
    function getCurrentDate() {
        var date = new Date();
        var seperator = "-";
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = year + seperator + month + seperator + strDate;
        return currentdate;
    }

    // yyyy-MM-dd 日期比较
    function compareDate(dateA, dateB) {
        return new Date(dateA.replace(/-/g, "/")) - new Date(dateB.replace(/-/g, "/"));
    }

    $(document).ready(function() {
        $("#uname").select2({
            placeholder: "可以模糊查询",
            allowClear: true,
            data:<%=underlingUsers%>
        });
        $("#projectName").select2({
            placeholder: "可以模糊查询",
            allowClear: true,
            data:<%=projectInfos%>
        });
        $("#costType").select2({
            tags: "true",
            placeholder: "可以模糊查询",
            allowClear: true,
            <%--data:<%=costTypeInfos%>--%>
        });
    });

</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<div data-options="region:'north',title:'查询条件',border:false"
			style="height: 75px; overflow: hidden;">
			<form id="searchForm">
				<table class="table table-hover table-condensed"
					style="display: none;">
					<tr>
						<td>操作人:&nbsp;
                            <%--<input name="uname" id='uname' placeholder="可以模糊查询" class="span2" />--%>
                            <select  style="width: 136px" name="uname" id="uname">
                                <option ></option>

                            </select>
                        </td>
						<td>工程名称:&nbsp;
                            <%--<input name="projectName" id="projectName" placeholder="可以模糊查询" class="span2" />--%>
                            <select  style="width: 136px" name="projectName" id="projectName">
                                <option ></option>

                            </select>
                        </td>
						<td>费用类型:&nbsp;
                            <%--<input name="costType" id='costType' placeholder="可以模糊查询" class="span2" />--%>
                            <select style="width: 136px" name="costType" id="costType">
                                <option></option>
                                <c:forEach var="costTypeInfo" items="<%= dataCostInfos %>" varStatus="index">
                                    <%--<c:if test="${costTypeInfo.isSend == '0'}">--%>
                                        <%--<optgroup label="${costTypeInfo.costType}"> " " </optgroup>--%>
                                    <%--</c:if>--%>
                                    <c:if test="${costTypeInfo.isSend == '0'}">
                                        <option value="${costTypeInfo.costType}">${costTypeInfo.costType}</option>
                                    </c:if>
                                    <c:if test="${costTypeInfo.isSend == '1'}">
                                        <option value="${costTypeInfo.costType}">&nbsp;&nbsp;&nbsp;&nbsp;${costTypeInfo.costType}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </td>
						<td>起止时间:&nbsp;<input class="span2" name="startTime"
							id='startTime' placeholder="点击选择时间"
							onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
							readonly="readonly" value='${first }' /> - <input class="span2"
							name="endTime" id='endTime' placeholder="点击选择时间"
							onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
							readonly="readonly" value='${last }' /></td>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="dataGrid"></table>
		</div>
	</div>
	<div id="toolbar" style="display: none;">
		<a onclick="addFun();" href="javascript:void(0);"
			class="easyui-linkbutton"
			data-options="plain:true,iconCls:'add_new'">添加</a>
        <%--modify by heyh --%>
        <%--<a onclick="batchDeleteFun();" href="javascript:void(0);"--%>
			<%--class="easyui-linkbutton" data-options="plain:true,iconCls:'delete'">批量删除</a>--%>
		<a href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'out_new',plain:true"
			onclick="exportFun();">execl导出</a><a href="javascript:void(0);"
			class="easyui-linkbutton"
			data-options="iconCls:'search_new',plain:true" onclick="searchFun();">过滤条件</a><a
			href="javascript:void(0);" class="easyui-linkbutton"
			data-options="iconCls:'zhongzhiguolvtiaojian_new',plain:true"
			onclick="cleanFun();">清空条件</a>
	</div>

	<div id="menu" class="easyui-menu" style="width: 120px; display: none;">
		<div onclick="addFun();" data-options="iconCls:'pencil_add'">增加</div>
		<div onclick="deleteFun();" data-options="iconCls:'pencil_delete'">删除</div>
		<div onclick="editFun();" data-options="iconCls:'pencil'">编辑</div>
	</div>
</body>
</html>