<%@ page import="net.sf.json.JSONArray" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="net.sf.json.JSONObject" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	JSONArray billCostTree = new JSONArray();
	List<Map<String, Object>> billCostInfos = new ArrayList<Map<String, Object>>();
	JSONArray jsonArray = new JSONArray();

	String cid = "";
	String uid = "";
	SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
	if (sessionInfo == null) {
		response.sendRedirect(request.getContextPath());
	} else {
		billCostTree = sessionInfo.getBillCostTree();
		billCostInfos = sessionInfo.getCostTypeInfos().get("billCostInfos");
		for (Map<String, Object> nodeMap : billCostInfos) {
			JSONObject nodeJson = JSONObject.fromObject(nodeMap);
			jsonArray.add(nodeJson);
		}

		cid = sessionInfo.getCompid();
		uid = sessionInfo.getId();
	}

%>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$('#form')
				.form(
						{
							url : '${pageContext.request.contextPath}/featureController/securi_add',

							onSubmit : function() {
							    if($("#itemCode").val() == '') {
							        alert("请选择费用类型");
							        return false;
                                }

                                if($("#mc").val() == '') {
                                    alert("请输入名称");
                                    return false;
                                }

                                if($("#count").val() == '') {
                                    alert("请输入数量");
                                    return false;
                                }

                                if($("#dw").val() == '') {
                                    alert("请输入单位");
                                    return false;
                                }

								return true;
							},
							success : function(result) {
								parent.$.messager.progress('close');
								result = $.parseJSON(result);
								if (result.success) {
									alert("添加成功!");
									parent.$.modalDialog.openner_dataGrid
											.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
									parent.$.modalDialog.handler
											.dialog('close');
								} else {
									parent.$.messager.alert('错误', result.msg, 'error');
								}
							}
						});

	});


    $('#costTypeRef').combotree({
        data: <%= billCostTree %>,
        lines: true,
        editable:true,
        onLoadSuccess: function () {
            $('#costTypeRef').combotree('tree').tree("collapseAll");
        },
        //选择树节点触发事件
        onSelect : function(node) {
            var _jsonArray = <%= jsonArray %>;
            for (var i=0; i<_jsonArray.length; i++) {
                if (_jsonArray[i].nid == node.id) {
                    $('#itemCode').val(_jsonArray[i].itemCode);
                    break;
                }
            }
        }
    });

</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form class="form-horizontal" name="form" id="form" method="post" role="form">

			<div class="control-group" style="padding-top: 20px; padding-right: 50px">
				<label class="control-label" for="costTypeRef">费用类型</label>
				<div class="controls">
					<input class="easyui-combotree" name="costTypeRef" id="costTypeRef" style="width: 220px" placeholder="请选择">
					<input type="hidden" name="costType" id="costType">
					<input type="hidden" name="itemCode" id="itemCode">
				</div>
			</div>

			<div class="control-group" style="padding-top: 20px; padding-right: 50px">
				<label class="control-label" for="mc">名称</label>
				<div class="controls">
					<input type="text" name="mc" id="mc">
				</div>
			</div>

			<div class="control-group" style="padding-top: 20px; padding-right: 50px">
				<label class="control-label" for="count">数量</label>
				<div class="controls">
					<input type="text" name="count" id="count">
				</div>
			</div>

			<div class="control-group" style="padding-top: 20px; padding-right: 50px">
				<label class="control-label" for="dw">单位</label>
				<div class="controls">
					<input type="text" name="dw" id="dw">
				</div>
			</div>
		</form>
	</div>
</div>