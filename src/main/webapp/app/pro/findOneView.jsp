<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
	});
	//添加
	function addFun() {
		var url = '${pageContext.request.contextPath}/fieldDataController/addfieldData?proid=${pro.id}';
		var text = "添加现场数据";
		var params = {
			url : url,
			title : text,
			iconCls : 'wrench'
		}
		window.parent.ac(params);
		parent.$.modalDialog.handler.dialog('close');
	}
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title=""
		style="overflow: hidden;">
		<form id="form" method="post">
			<input name="id" type="hidden" class="span2" value="${pro.id}"
				readonly="readonly">
			<table class="table table-hover table-condensed"
				style="font-size: 12px;">
				<tr>
					<td>项目编号</td>
					<td>${pro.projectId}</td>
					<td>项目名称</td>
					<td>${pro.proName}</td>
				</tr>
				<tr>
					<td>工程简称</td>
					<td>${pro.shortname}</td>
					<td>中标通知书日期</td>
					<td>${pro.zbtzsrq}</td>
				</tr>
				<tr>
					<td>工程合同价</td>
					<td>${pro.gchtj}</td>
					<td>建筑面积或规模值</td>
					<td>${pro.jzmjorgm}</td>
				</tr>
				<tr>
					<td>省</td>
					<td>${pro.provice}</td>
					<td>市</td>
					<td>${pro.city}</td>
				</tr>
				<tr>
					<td>投标项目经理</td>
					<td>${pro.manager2}</td>
					<td>工程类型</td>
					<td>${pro.gclx}</td>
				</tr>
				<tr>
					<td>开工日期</td>
					<td>${pro.kgrq}</td>
					<td>竣工日期</td>
					<td>${pro.jgrq}</td>
				</tr>
				<tr>
					<td>造价类型</td>
					<td>${pro.zjlx}</td>
					<td>工程状态</td>
					<td>${pro.gczt}</td>
				</tr>
				<tr>
					<td>工程质保约定</td>
					<td>${pro.gczbyd}</td>
					<td>工程付款约定</td>
					<td>${pro.gcfkyd}</td>
				</tr>

				<tr>
					<td>施工项目经理</td>
					<td>${pro.manager}</td>
					<td>工程款支付状态</td>
					<td>${pro.money_state}</td>
				</tr>
				<tr>
					<td>工程正式开工日期</td>
					<td>${pro.gckgrq}</td>
					<td>工程正式竣工日期</td>
					<td>${pro.gcjgrq}</td>
				</tr>
				<tr>
					<td>工程到期移交情况</td>
					<td>${pro.gcdqyjqk}</td>
					<td>工程质量获奖情况</td>
					<td>${pro.gczlhjqk}</td>
				</tr>
				<tr>
					<td>竣工结算书</td>
					<td>${pro.jgjss}</td>
					<td>工程安全文明情况</td>
					<td>${pro.gcaqwmqk}</td>
				</tr>
				<tr>
					<td>竣工报告</td>
					<td>${pro.jgbb}</td>
					<td>竣工资料</td>
					<td>${pro.jgzl}</td>
				</tr>
				<tr>
					<td>造价员</td>
					<td>${pro.zjy}</td>
					<td>资料员</td>
					<td>${pro.zly}</td>
				</tr>

				<tr>
					<td>养护级别</td>
					<td>${pro.yhjb}</td>
					<td>养护承包人</td>
					<td>${pro.yhcbr}</td>
				</tr>
				<tr>
					<td>内部维(养)护开始时间</td>
					<td>${pro.whkssj}</td>
					<td>内部维(养)护结束时间</td>
					<td>${pro.whjssj}</td>
				</tr>
				<tr>
					<td>合同维(养)护截止日</td>
					<td>${pro.htwhjzr}</td>
					<td>工程移交日期</td>
					<td>${pro.gcyjrq}</td>
				</tr>
				<tr>
					<td>工程维(养)护期</td>
					<td>${pro.gcwhq}</td>
				</tr>

				<tr>
					<td>操作</td>
					<td><input type="button" value="添加现场数据" onclick="addFun()"
						style="color: red"></td>
				</tr>
			</table>
		</form>
	</div>
</div>