<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



<script type="text/javascript">
	parent.$.messager.progress('close');

	var zjl = document.getElementById('zjl').value;
	for (var i = 0; i < document.getElementById('zjlx').options.length; i++) {
		if (document.getElementById('zjlx').options[i].value == zjl) {
			document.getElementById('zjlx').options[i].selected = true;
			break;
		}
	}
	var gcl = document.getElementById('gcl').value;
	for (var i = 0; i < document.getElementById('gclx').options.length; i++) {
		if (document.getElementById('gclx').options[i].value == gcl) {
			document.getElementById('gclx').options[i].selected = true;
			break;
		}
	}
	var gcy = document.getElementById('gcz').value;
	for (var i = 0; i < document.getElementById('gczt').options.length; i++) {
		if (document.getElementById('gczt').options[i].value == gcy) {
			document.getElementById('gczt').options[i].selected = true;
			break;
		}
	}

	var provicehid = document.getElementById('provicehid').value;
	for (var i = 0; i < document.getElementById('provice').options.length; i++) {
		if (document.getElementById('provice').options[i].value == provicehid) {
			document.getElementById('provice').options[i].selected = true;
			break;
		}
	}
	var cityhid = document.getElementById('cityhid').value;
	for (var i = 0; i < document.getElementById('city').options.length; i++) {
		if (document.getElementById('city').options[i].value == cityhid) {
			document.getElementById('city').options[i].selected = true;
			break;
		}
	}
	$(function() {
		parent.$.messager.progress('close');
		$('#form')
				.form(
						{
							url : '${pageContext.request.contextPath}/projectController/update',
							onSubmit : function() {
								parent.$.messager.progress({
									title : '提示',
									text : '数据处理中，请稍后....'
								});
								var isValid = $(this).form('validate');
								if (!isValid) {
									parent.$.messager.progress('close');
								}
								for (var i = 1; i < 11; i++) {
									if ($('#data' + i).val() == '') {
										continue;
									}
									$('#data' + i).val(
											$('#data' + i).val() + ' 00:00:00');
								}
								return isValid;
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
									//parent.$.modalDialog.openner_dataGrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
								} else {
									parent.$.messager.alert('错误', result.msg,
											'error');
								}
							}
						});
	});

	var cfg = {
		url : '${pageContext.request.contextPath}/projectController/getCities',
		type : 'GET',
		dataType : 'json',
		success : function(dataObj) {
			$("#city").empty();
			$.each(dataObj, function(idx, item) {
				$(
						"<option value='" + item.cityname + "'>"
								+ item.cityname + "</option>").appendTo(
						$("#city"));
			});
		}
	};

	function getCity() {
		//获取表单值，并以json的数据形式保存到data中  
		cfg.data = {
			provincename : $("#provice").val()
		}
		$.ajax(cfg);
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
					<td><input type="text" name="projectId"
						value='${pro.projectId }' /></td>
					<td>工程名称</td>
					<td><input type="text" name="proName" value='${pro.proName }' /></td>
					<td>工程简称</td>
					<td><input type="text" name="shortname"
						value='${pro.shortname }' /></td>
				</tr>
				<tr>
					<td>建筑面积或规模值</td>
					<td><input type="text" name="jzmjorgm"
						value='${pro.jzmjorgm }' /></td>
					<td>工程合同价</td>
					<td><input type="text" name="gchtj" value='${pro.gchtj }' /></td>
					<td>投标项目经理</td>
					<td><input type="text" name="manager2"
						value='${pro.manager2 }' /></td>
				</tr>
				<tr>
					<td>合同签订日期</td>
					<td><input class="span2" name="htqdrq" id="data1"
						placeholder="点击选择时间"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
						readonly="readonly" value='${pro.htqdrq }' /></td>
					<td>合同开工日期</td>
					<td><input class="span2" name="kgrq" id="data2"
						placeholder="点击选择时间"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
						readonly="readonly" value='${pro.kgrq }' /></td>
					<td>合同竣工日期</td>
					<td><input class="span2" name="jgrq" id="data3"
						placeholder="点击选择时间"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
						readonly="readonly" value='${pro.jgrq }' /></td>
				</tr>
				<tr>
					<td>工程质保约定</td>
					<td><input type="text" name="gczbyd" value='${pro.gczbyd }' /></td>
					<td>工程付款约定</td>
					<td><input type="text" name="gcfkyd" value='${pro.gcfkyd }' /></td>
					<td>中标通知书日期</td>
					<td><input class="span2" name="zbtzsrq" id="data4"
						placeholder="点击选择时间"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
						readonly="readonly" value='${pro.zbtzsrq }' /></td>
				</tr>
				<tr>
					<td>工程状态</td>
					<td><select name="gczt" id="gczt">
							<option>在建工程</option>
							<option>未开工工程</option>
							<option>分包工程</option>
							<option>已完成未验收工程</option>
							<option>已验收工程</option>
							<option>审计完工程</option>
							<option>纯维(养)护工程</option>
							<option>维(养)护结束工程</option>
							<option>尾款已付清工程</option>
							<option>其他工程</option>
					</select><input type="hidden" id="gcz" value='${pro.gczt }'></td>
					<td>工程类型</td>
					<td><select name="gclx" id="gclx">
							<option>建筑工程</option>
							<option>市政工程</option>
							<option>安装工程</option>
							<option>装饰工程</option>
							<option>仿古园林</option>
							<option>绿化工程</option>
							<option>纯维(养)护工程</option>
							<option>修缮工程</option>
							<option>人防工程</option>
							<option>电力工程</option>
							<option>水利工程</option>
							<option>交通工程</option>
							<option>冶金工程</option>
							<option>内河航运</option>
							<option>沿海港口</option>
							<option>铁路工程</option>
							<option>通讯工程</option>
							<option>设计工程</option>
							<option>其他</option>
					</select><input type="hidden" id="gcl" value='${pro.gclx }'></td>
					<td>造价类型</td>
					<td><select name="zjlx" id="zjlx">
							<option>工程预算</option>
							<option>工程结算</option>
							<option>工程报价</option>
							<option>工程标底</option>
							<option>设计概算</option>
							<option>投资估算</option>
					</select><input type="hidden" id="zjl" value='${pro.zjlx }'></td>
				</tr>
				<tr>
					<td>项目所在地</td>
					<td><select id="provice" name="provice" onchange="getCity()"
						style="width: 85px">
							<c:forEach items="${provinces}" var="tem">
								<option value="${tem.provincename}">${tem.provincename}</option>
							</c:forEach>
					</select> <select id="city" name="city" style="width: 85px"><c:forEach
								items="${cities}" var="tem">
								<option value="${tem.cityname}">${tem.cityname}</option>
							</c:forEach>
					</select><input type="hidden" id="provicehid" value='${pro.provice }'><input
						type="hidden" id="cityhid" value='${pro.city }'></td>
					<td>工程款支付状态</td>
					<td><input type="text" name="money_state"
						value='${pro.money_state }' /></td>
				</tr>

				<tr>
					<td>施工项目经理</td>
					<td><input type="text" name="manager" value='${pro.manager }' /></td>
					<td>工程到期移交情况</td>
					<td><input type="text" name="gcdqyjqk"
						value='${pro.gcdqyjqk }' /></td>
					<td>工程质量获奖情况</td>
					<td><input type="text" name="gczlhjqk"
						value='${pro.gczlhjqk }' /></td>
				</tr>
				<tr>
					<td>竣工结算书</td>
					<td><input type="text" name="jgjss" value='${pro.jgjss }' /></td>
					<td>工程安全文明情况</td>
					<td><input type="text" name="gcaqwmqk"
						value='${pro.gcaqwmqk }' /></td>
					<td>竣工报告</td>
					<td><input type="text" name="jgbb" value='${pro.jgbb }' /></td>
				</tr>
				<tr>
					<td>正式开工日期</td>
					<td><input class="span2" name="gckgrq" id="data5"
						placeholder="点击选择时间"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
						readonly="readonly" value='${pro.gckgrq }' /></td>
					<td>正式竣工日期</td>
					<td><input class="span2" name="gcjgrq" id="data6"
						placeholder="点击选择时间"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
						readonly="readonly" value='${pro.gcjgrq }' /></td>
					<td>工程移交日期</td>
					<td><input class="span2" name="gcyjrq" id="data10"
						placeholder="点击选择时间"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
						readonly="readonly" value='${pro.gcyjrq }' /></td>
				</tr>
				<tr>
					<td>竣工资料</td>
					<td><input type="text" name="jgzl" value='${pro.jgzl }' /></td>
					<td>资料员</td>
					<td><input type="text" name="zly" value='${pro.zly }' /></td>
					<td>造价员</td>
					<td><input type="text" name="zjy" value='${pro.zjy }' /></td>
				</tr>
				<tr>
					<td>维(养)护开始时间</td>
					<td><input class="span2" name="whkssj" id="data7"
						placeholder="点击选择时间"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
						readonly="readonly" value='${pro.whkssj }' /></td>
					<td>维(养)护结束时间</td>
					<td><input class="span2" name="whjssj" id="data8"
						placeholder="点击选择时间"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
						readonly="readonly" value='${pro.whjssj }' /></td>
					<td>合同维(养)护截止日</td>
					<td><input class="span2" name="htwhjzr" id="data9"
						placeholder="点击选择时间"
						onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
						readonly="readonly" value='${pro.htwhjzr }' /></td>
				</tr>
				<tr>
					<td>工程维(养)护期</td>
					<td><input type="text" name="gcwhq" value='${pro.gcwhq }' /></td>
					<td>维(养)护级别</td>
					<td><input type="text" name="yhjb" value='${pro.yhjb }' /></td>
					<td>维(养)护承包人</td>
					<td><input type="text" name="yhcbr" value='${pro.yhcbr }' /></td>
				</tr>
			</table>
		</form>
	</div>
</div>