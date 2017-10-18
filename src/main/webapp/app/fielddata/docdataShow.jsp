<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="net.sf.json.JSONArray" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
    String userId = null;
//    String underlingUsers = null;
    String projectInfos = null;
//    List<Map<String, Object>> docCostInfos = new ArrayList<Map<String, Object>>();
	JSONArray docCostTree = new JSONArray();
    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
    if (sessionInfo == null) {
        response.sendRedirect(request.getContextPath());
    } else {
        userId = sessionInfo.getId();
//        underlingUsers = sessionInfo.getUnderlingUsers();
        projectInfos = sessionInfo.getProjectInfos();
//        docCostInfos = sessionInfo.getCostTypeInfos().get("docCostInfos");
		docCostTree = sessionInfo.getDocCostTree();
    }

%>

<!DOCTYPE html>
<html>
<head>
<title>服务号管理</title>
<jsp:include page="../../inc.jsp"></jsp:include>
    <link href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/css/select2.min.css" rel="stylesheet" />
    <script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1-rc.1/js/select2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/jslib/layer-v3.0.3/layer/layer.js"></script>
	<style>
		.table_style {
			width: 100%;
			margin-bottom: 20px;
			border:1px solid #EDEDED

		}
		.table_style th{height:34px; background:#76b3ff; color:#fff; font-weight: normal}
		.table_style td{height:32px; color:#0e0e0e;padding-left:10px;color:#666}
		.table_style tbody tr{background:#eee; cursor: default}
		.table_style tbody tr.hover td{background:#e4fbfb}
		.td_title{font-weight:bold;color:#333}

		.subtotal { font-weight: bold; }/*合计单元格样式*/
	</style>
<script type="text/javascript">
	var dataGrid;
	$(function() {
		dataGrid = $('#dataGrid')
				.datagrid(
						{
							url : '${pageContext.request.contextPath}/fieldDataController/dataGrid?source=doc',
							fit : true,
                            fitColumns : false,
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
                            striped:true,
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
										field : 'sectionName',
										title : '标段',
										width : 100
									},
									{
										field : 'costType',
										title : '资料类型',
										width : 100
									},
									{
										field : 'dataName',
										title : '名称',
										width : 350
									},
									/*{
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
									},*/
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
										field : 'approvedOption',
										title : '审批意见',
										width : 100,
										formatter: function (value, row, index) {
                                            var str = '';
                                            if (row.approvedOption != '' && row.approvedOption != undefined && row.approvedOption != 'undefined') {
                                                str += $.formatString('<a onclick="viewApproveDetailsFun(\' {0} \');" href="javascript:void(0);" class="easyui-linkbutton">' +
                                                    '<img src="${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/viewDetails.png">' +
                                                    '审批详情</a>',
                                                    row.approvedOption);
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
										field : 'remark',
										title : '备注',
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
                                            if(compareDate(getCurrentDate(), row.creatTime.substring(0, 10)) == 0
                                                    && userId == row.uid && '0' == row.isLock && '2' != row.needApproved) {
//                                                if ('0' == row.isLock && '2' != row.needApproved) {
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
//                                                }
                                            } else {
                                                str += $
                                                        .formatString(
                                                        '<img onclick="previewFun(\'{0}\');" src="{1}" title="预览" />',
                                                        row.id,
                                                        '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/preview-blue.png');
                                                str += '&nbsp;';
                                            }
											str += $
													.formatString(
															' <img onclick="FileFun(\'{0}\');" src="{1}" title="附件管理"/>',
															row.id,
															'${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/fujianguanli-blue.png');

                                            str += '&nbsp;';
                                            str += $
                                                .formatString(
                                                    '<img onclick="discussFun(\'{0}\');" src="{1}" title="交流"/>',
                                                    row.id,
                                                    '${pageContext.request.contextPath}/style/images/extjs_icons/icon-new/discuss-blue.png');

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

    function viewApproveDetailsFun(approvedOption) {
        var approvedOptions = approvedOption.split('|');

        var approvedOptionsHtml =
            '<table class="table_style" style="font-size: 12px;" cellpadding="0" cellspacing="0">' +
            '<tr>' +
            '<td align="center">时间</td>' +
            '<td align="center">审核人</td>' +
            '<td align="center">审核意见</td>' +
            '</tr>';

        for (var i=0; i<approvedOptions.length; i++) {
            approvedOptionsHtml += '<tr>';
            var approvedOptionInfos = approvedOptions[i].split('::');
            for (var j=0; j<approvedOptionInfos.length; j++) {
                approvedOptionsHtml += '<td>' + approvedOptionInfos[j] + '</td>';
            }
            approvedOptionsHtml += '</tr>'
        }
        approvedOptionsHtml += '</table>';

		layer.open({
            type: 1,
            title: '审批详情',
            closeBtn: 2,
            shadeClose: true,
            maxmin: true, //开启最大化最小化按钮
            area: ['400px', '300px'],
            content: approvedOptionsHtml
        });
    }

	// 交流
    function discussFun(id) {

        if (id == undefined) {
            var rows = dataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }

        var url = '${pageContext.request.contextPath}/discussController/securi_discussShow?discussId=' + id + '&discussType=0';
        var text = "交流讨论区";
        var params = {
            url : url,
            title : text,
            iconCls : 'wrench'
        };
        window.parent.ac(params);
    }

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

    //预览
    function previewFun(id) {
        parent.$
                .modalDialog({
                    title : '预览',
                    width : 420,
                    height : 460,
                    href :
                    '${pageContext.request.contextPath}/fieldDataController/upfieldData?id=' +
                    id + '&preview=' + true ,
                    buttons : [ {
                        text : '关闭',
                        handler : function() {
                            parent.$.modalDialog.handler.dialog('destroy');
                            parent.$.modalDialog.handler = undefined;
                        }
                    } ]
                });
    }

	function addFun() {
		var url = '${pageContext.request.contextPath}/fieldDataController/securi_addPage?source=doc';
		var text = "添加资料数据";
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
//		str += '&uname=' + $('#uname').val();
        str += '&keyword=' + $('#keyword').val();
		str += '&projectName=' + $('#projectName').val();
		str += '&itemCode=' + $('#itemCode').val();
		str += '&startTime=' + $('#startTime').val();
		str += '&endTime=' + $('#endTime').val();
		var url = "${pageContext.request.contextPath}/fieldDataController/securi_execl?a=1&source=doc"
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
        $('#costType').val($('.combo-text').val());
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
        <%--$("#uname").select2({--%>
            <%--placeholder: "可以模糊查询",--%>
            <%--allowClear: true,--%>
            <%--data:<%=underlingUsers%>--%>
        <%--});--%>
        $("#projectName").select2({
            placeholder: "可以模糊查询",
            allowClear: true,
            data:<%=projectInfos%>
        });
        <%--$("#costType").select2({--%>
            <%--tags: "true",--%>
            <%--placeholder: "可以模糊查询",--%>
            <%--allowClear: true,--%>
            <%--&lt;%&ndash;data:<%=costTypeInfos%>&ndash;%&gt;--%>
        <%--});--%>

        $('#costTypeRef').combotree({
            data: <%= docCostTree %>,
            lines: true,
            editable:true,
            onLoadSuccess: function () {
                $('#costTypeRef').combotree('tree').tree("collapseAll");
            },
            //选择树节点触发事件
//			onSelect : function(node) {
//				debugger;
//				//返回树对象
//				var tree = $(this).tree;
//				//选中的节点是否为叶子节点,如果不是叶子节点,清除选中
//				var isLeaf = tree('isLeaf', node.target);
//				if (!isLeaf) {
//					//清除选中
//					$('.easyui-combotree').treegrid("unselect");
//				}
//			}
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
		<div data-options="region:'north',title:'查询条件',border:false"
			style="height: 75px; overflow: hidden;">
			<form id="searchForm">
				<table class="table table-hover table-condensed"
					style="display: none;">
					<tr>
						<td>关键字搜索:&nbsp;
                            <%--<input name="uname" id='uname' placeholder="可以模糊查询" class="span2" />--%>
                            <%--<select  style="width: 136px" name="uname" id="uname">--%>
                                <%--<option ></option>--%>
                            <%--</select>--%>
                            <input name="keyword" id="keyword" placeholder="可以模糊查询" class="span2" />
                        </td>
						<td>工程名称:&nbsp;
                            <%--<input name="projectName" id="projectName" placeholder="可以模糊查询" class="span2" />--%>
                            <select  style="width: 136px" name="projectName" id="projectName">
                                <option ></option>
                            </select>
                        </td>
						<td>资料类型:&nbsp;
							<input class="easyui-combotree" name="costTypeRef" id="costTypeRef" style="width:180px;" placeholder="请选择">
							<input type="hidden" name="costType" id="costType">
						</td>
						<td>起止时间:&nbsp;<input class="Wdate span2" name="startTime"
							id='startTime' placeholder="点击选择时间"
							onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
							readonly="readonly" value='${first }' /> - <input class="Wdate span2"
							name="endTime" id='endTime' placeholder="点击选择时间"
							onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
							readonly="readonly" value='${last }' /></td>
					</tr>
				</table>
			</form>
		</div>
		<div style="overflow-x:auto; " data-options="region:'center',border:false">
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
		<%--<a href="javascript:void(0);" class="easyui-linkbutton"--%>
			<%--data-options="iconCls:'building_go',plain:true"--%>
			<%--onclick="exportFun();">execl导出</a>--%>
        <a href="javascript:void(0);"
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