<%@ page import="sy.pageModel.SessionInfo" %>
<%@ page import="sy.util.ConfigUtil" %>
<%@ page import="sy.model.Param" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	boolean hasOnlyReadRight = false;
	boolean hasReadEditRight = false;
	SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
	if (sessionInfo == null) {
		response.sendRedirect(request.getContextPath());
	} else {
		hasOnlyReadRight = sessionInfo.getRightList().contains("16") && 0 != sessionInfo.getParentId();
		hasReadEditRight = sessionInfo.getRightList().contains("15") || 0 == sessionInfo.getParentId();
	}

%>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$('#form')
				.form(
						{
							url : '${pageContext.request.contextPath}/fieldDataController/updatefieldData',

							onSubmit : function() {
								var dataName = document
										.getElementById("dataName").value;
								if (dataName == '') {
									alert('请输入名称');
									return false;
								}
								return true;
							},
							success : function(result) {
								parent.$.messager.progress('close');
								result = $.parseJSON(result);
								if (result.success) {
									alert("修改成功!");
									parent.$.modalDialog.openner_dataGrid
											.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
									parent.$.modalDialog.handler
											.dialog('close');
								} else {
									parent.$.messager.alert('错误', result.msg,
											'error');
								}
							}
						});

	});
	//获得上传文件信息
	function getFileInfo(pic, icon) {
		$("#" + pic).change(function() {
			var size = document.getElementById(pic).files[0].size;
			var pname = document.getElementById(pic).files[0].name;
			size = formatFileSize(size);
			$("#" + icon).val(pname);
			$("#appSize").val(size);
			alert(size + "," + pname);
		});
	}

    $(function() {
        if($('.priview').val() == 'true'){
            debugger;
            $('.easyui-validatebox').attr('disabled', true);
        } else {
            $('.easyui-validatebox').removeAttr('disabled');
        }
    });

    var sectionId = document.getElementById('sectionId').value;
    for (var i = 0; i < document.getElementById('section').options.length; i++) {
        if (document.getElementById('section').options[i].value == sectionId) {
            document.getElementById('section').options[i].selected = true;
            break;
        }
    }

    var unitRef = document.getElementById('unitRef').value;
    for (var i = 0; i < document.getElementById('unit').options.length; i++) {
        if (document.getElementById('unit').options[i].value == unitRef) {
            document.getElementById('unit').options[i].selected = true;
            break;
        }
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title=""
		style="overflow: hidden;">
        <input class="priview" value="${preview}" style="display: none;"/>
		<form id="form" method="post" enctype="multipart/form-data">
			<table class="table table-hover table-condensed">
				<tr>
					<td style="width: 80px;">工程名称</td>
					<td><input type="hidden" id="id" name="id" value="${tfielddata.id}" />
                        <input type="hidden" id="itemCode" name="itemCode" value="${tfielddata.itemCode}" />
                        <input type="hidden" id="creatTime" name="creatTime" value="${tfielddata.creatTime}" />
						<input type="hidden" id="cid" name="cid" value="${tfielddata.cid}" />
						<input type="hidden" id="uid" name="uid" value="${tfielddata.uid}" />
						<input type="hidden" id="uname" name="uname" value="${tfielddata.uname}" />
                        <input type="hidden" id="company" name="company" value="${tfielddata.company}" />
                        <input type="hidden" id="costType" name="costType" value="${tfielddata.costType}" />
                        <input type="hidden" id="projectName" name="projectName" value="${tfielddata.projectName}" />
						<input type="hidden" id="unitRef" name="unitRef" value="${tfielddata.unit}" />
						<input type="hidden" id="sectionId" value="${tfielddata.section}"/>

						<input type="hidden" id="needApproved" name="needApproved" value="${tfielddata.needApproved}"/>
						<input type="hidden" id="approvedUser" name="approvedUser" value="${tfielddata.approvedUser}"/>
						<input type="hidden" id="currentApprovedUser" name="currentApprovedUser" value="${tfielddata.currentApprovedUser}"/>
						<input type="hidden" id="approvedOption" name="approvedOption" value="${tfielddata.approvedOption}"/>
						<input type="hidden" id="relId" name="relId" value="${tfielddata.relId}"/>
						<input type="hidden" id="price_sj" name="price_sj" value="${tfielddata.price_sj}"/>
						<input type="hidden" id="price_ys" name="price_ys" value="${tfielddata.price_ys}"/>

                        <input type="text" placeholder="工程名称" style="width: 250px;"
                               class="easyui-validatebox span2" data-options="required:true"
                               value="${project.proName}" readonly='readonly'></td>
				</tr>
				<tr>
					<td style="width: 80px;">费用类型</td>
					<td><input type="text" style="width: 250px;"
						placeholder="费用类型" class="easyui-validatebox span2"
						data-options="required:true" value="${cost.costType}"
						readonly='readonly'></td>
				</tr>
				<tr>
					<td style="width: 80px;">设施名称</td>
					<td><input style="width: 250px;" name="specifications"
							   type="text" placeholder="设施名称" class="easyui-validatebox span2"
							   value="${tfielddata.specifications}">
				</tr>

				<tr>
					<td style="width: 80px;">名称</td>
					<td><input name="dataName" id="dataName" type="text"
							   style="width: 250px;" placeholder="名称"
							   class="easyui-validatebox span2" data-options="required:true"
							   value="${tfielddata.dataName}"></td>
				</tr>
				<tr>
					<td style="width: 80px;">单位</td>
					<td>
						<select id="unit" name="unit" style="width: 264px;">
							<c:forEach items="${unitParams}" var="unitParam" varStatus="index">
								<c:if test="${unitParam.parentCode == ''}">
									<optgroup label="${unitParam.paramValue}"></optgroup>
								</c:if>
								<c:if test="${unitParam.parentCode != ''}">
									<option value="${unitParam.paramValue}">&nbsp;&nbsp;&nbsp;&nbsp;${unitParam.paramValue}</option>
								</c:if>
							</c:forEach>
						</select>
					</td>
				</tr>
				<%--<tr>--%>
					<%--<td style="width: 80px;">单价</td>--%>
					<%--<td>--%>
						<%--<c:choose>--%>
							<%--<c:when test="${tfielddata.itemCode.substring(0, 3) != '700' && tfielddata.itemCode.substring(0, 3) != '800'}">--%>
								<%--<input name="price" style="width: 250px;" type="text" placeholder="单价" class="easyui-validatebox span2" data-options="required:true" value="${tfielddata.price}">--%>
							<%--</c:when>--%>
							<%--<c:when test="<%= hasReadEditRight %>">--%>
								<%--<input name="price" style="width: 250px;" type="text" placeholder="单价" class="easyui-validatebox span2" data-options="required:true" value="${tfielddata.price}">--%>
							<%--</c:when>--%>
							<%--<c:when test="<%= hasOnlyReadRight && !hasReadEditRight%>">--%>
								<%--<input readonly='readonly' name="price" style="width: 250px;" type="text" placeholder="单价" class="easyui-validatebox span2" data-options="required:true" value="${tfielddata.price}">--%>
							<%--</c:when>--%>
							<%--<c:otherwise>--%>
								<%--<input readonly='readonly'  style="width: 250px;" type="text" placeholder="单价" class="easyui-validatebox span2" data-options="required:true" value="***">--%>
								<%--<input name="price" style="display: none" type="text" placeholder="单价" class="easyui-validatebox span2" data-options="required:true" value="${tfielddata.price}">--%>
							<%--</c:otherwise>--%>
						<%--</c:choose>--%>
					<%--</td>--%>
				<%--</tr>--%>
				<tr>
					<td style="width: 80px;">数量</td>
					<td><input name="count" style="width: 250px;" type="text"
						placeholder="数量" class="easyui-validatebox span2"
						data-options="required:true,precision:4" value="${tfielddata.count}"></td>
				</tr>
				<%--<tr>--%>
					<%--<td style="width: 80px;">实付金额</td>--%>
					<%--<td><input name="payAmount" style="width: 250px;" type="text"--%>
							   <%--placeholder="实付金额" class="easyui-validatebox span2"--%>
							   <%--data-options="required:true,precision:4" value="${tfielddata.payAmount}"></td>--%>
				<%--</tr>--%>
				<tr>
					<td style="width: 80px;">项目特征</td>
					<td><input name="remark" style="width: 250px;" type="text"
							   placeholder="项目特征" class="easyui-validatebox span2"
							   value="${tfielddata.remark}"></td>
				</tr>
				<%--<tr>--%>
					<%--<td style="width: 80px;">供应商</td>--%>
					<%--<td><input style="width: 250px;" name="supplier"--%>
							   <%--type="text" placeholder="供应商" class="easyui-validatebox span2"--%>
							   <%--data-options="required:true" value="${tfielddata.supplier}">--%>
				</tr>
				<tr>
					<td style="width: 80px;">工程属性</td>
					<td>
						<select id="section" name="section" style="width: 264px;">
							<c:forEach items="${selectItems}" var="tem">
								<option value="${tem.id}">${tem.text}</option>
							</c:forEach>
						</select>
					</td>

				</tr>
			</table>
		</form>
	</div>
</div>