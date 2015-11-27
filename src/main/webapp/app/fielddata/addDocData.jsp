<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- 引入jQuery -->
<script src="${pageContext.request.contextPath}/jslib/jquery-1.8.3.js"
	type="text/javascript" charset="utf-8"></script>
<!-- 引入EasyUI -->
<!-- <link rel="stylesheet" href="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.3/themes/icon.css" type="text/css"> -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.3/jquery.easyui.min.js"
	charset="utf-8"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"
	charset="utf-8"></script>
<!-- 修复EasyUI1.3.3中layout组件的BUG -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/jslib/jquery-easyui-1.3.3/plugins/jquery.layout.js"
	charset="utf-8"></script>

<!-- 引入EasyUI Portal插件 -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/jslib/jquery-easyui-portal/jquery.portal.js"
	charset="utf-8"></script>

<!-- 扩展EasyUI -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/jslib/extEasyUI.js?v=201305241044"
	charset="utf-8"></script>

<!-- 扩展jQuery -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/jslib/extJquery.js?v=201305301341"
	charset="utf-8"></script>
<link
	href="${pageContext.request.contextPath }/jslib/upload/ajaxfileupload.css"
	type="text/css" rel="stylesheet">
<script type="text/javascript"
	src="${pageContext.request.contextPath }/jslib/upload/ajaxfileupload.js"></script>
<script type="text/javascript">
	var flag = 0;

	//上传文件
	function uploadfile(filed, mid) {
		parent.$.messager.progress({
			title : '提示',
			text : '文件正在上传中，请不要离开此页面，请稍后....'
		});
		$
				.ajaxFileUpload({
					url : '${pageContext.request.contextPath }/fieldDataController/upload',
					secureuri : false,
					fileElementId : filed,
					dataType : 'json',
					data : {
						id : mid,
						name : filed,
						fileds : filed
					},
					success : function(data) {
						parent.$.messager.progress('close');
						if (data.success) {
							alert(data.msg);
						} else {
							alert(data.msg);
						}
					},
					error : function(data, status, e) {
						alert(data.msg);
						parent.$.messager.progress('close');
					}
				});
	}

	function selectp() {
		parent.$
				.modalDialog({
					title : '选择项目工程',
					width : 550,
					height : 550,
					href : '${pageContext.request.contextPath }/fieldDataController/securi_selectp',
					buttons : [ {
						text : '确认',
						handler : function() {
							var id = parent.$.modalDialog.handler.find(
									"#proidh").val();
							var name = parent.$.modalDialog.handler.find(
									"#proNameh").val();
							document.getElementById("projectName").value = id;
							document.getElementById("pName").value = name;
							parent.$.modalDialog.handler.dialog('close');
						}
					} ]
				});
	}

	function selectc() {
		parent.$
				.modalDialog({
					title : '选择费用类型',
					width : 450,
					height : 500,
					href : '${pageContext.request.contextPath }/fieldDataController/securi_selectcc',
					buttons : [ {
						text : '确认',
						handler : function() {
							var id = parent.$.modalDialog.handler.find(
									"#proidh").val();
							var name = parent.$.modalDialog.handler.find(
									"#proNameh").val();
							var itemCode = parent.$.modalDialog.handler.find(
									"#code").val();
							document.getElementById("costType").value = id;
							document.getElementById("costTypeName").value = name;
							document.getElementById("itemCode").value = itemCode;
							parent.$.modalDialog.handler.dialog('close');
//							if (name == '纯附件') {
								$('.will_hide').hide();
//							} else {
//								$('.will_hide').show();
//							}
						}
					} ]
				});
	}

	var cfg = {
		url : '${pageContext.request.contextPath}/fieldDataController/savefieldData',
		type : 'post',
		dataType : 'json',
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		success : function(data) {
			alert(data.msg);
			if (data.success) {
				field_id = data.obj;
				if (document.getElementById('file1').value != '') {
					uploadfile('file1', field_id);
				}
				if (document.getElementById('file2').value != '') {
					uploadfile('file2', field_id);
				}
				if (document.getElementById('file3').value != '') {
					uploadfile('file3', field_id);
				}
				if (document.getElementById('file4').value != '') {
					uploadfile('file4', field_id);
				}
			}

            // add by heyh
            $("input[type=reset]").trigger("click");

			flag = 1;
		}
	};

	function aaa() {

		if (flag == 1) {
			var a = confirm("检测到该数据可能已经添加，确定要重复添加吗");
			if (!a) {
				return;
			}
		}
		var projectName = document.getElementById("projectName").value;
		var costType = document.getElementById("costType").value;
		var dataName = document.getElementById("dataName").value;
		var price = document.getElementById("price").value;
		price = (price * 1).toFixed(2);
		var count = document.getElementById("count").value;
		var specifications = document.getElementById("specifications").value;
		var remark = document.getElementById("remark").value;
		var unit = document.getElementById("unit").value;
		var itemCode = document.getElementById("itemCode").value;

		if (projectName == '') {
			alert("项目名称不能为空");
			return;
		}
		if (costType == '') {
			alert("费用类型不能为空");
			return;
		}
		if (dataName == '') {
			alert("现场数据名称不能为空");
			return;
		}
//		if (price == ''
//				&& document.getElementById("costTypeName").value != '纯附件') {
//			alert("价格不能为空");
//			return;
//		}
//		if (count == ''
//				&& document.getElementById("costTypeName").value != '纯附件') {
//			alert("数量不能为空");
//			return;
//		}

		cfg.data = {
			'projectName' : projectName,
			'costType' : costType,
			'dataName' : dataName,
			'price' : price,
			'count' : count,
			'specifications' : specifications,
			'remark' : remark,
			'unit' : unit,
			'itemCode' : itemCode
		}

		$.ajax(cfg);

	}

	function cal() {
		var price = document.getElementById("price").value;
		var count = document.getElementById("count").value;
		if (price != '' && count != '') {
			document.getElementById("sumprice").value = (price * count)
					.toFixed(2);
		}
	}

	window.onload = function() {
//		if (document.getElementById("costTypeName").value == '纯附件') {
			$('.will_hide').hide();
//		}
		$('#form input').click(function() {
			flag = 0;
		})
	}
</script>

<style type="text/css">
.basic-grey {
	margin-left: auto;
	margin-right: auto;
	max-width: 1002px;
	background: #F7F7F7;
	padding: 25px 15px 25px 10px;
	font: 12px Georgia, "Times New Roman", Times, serif;
	color: #888;
	text-shadow: 1px 1px 1px #FFF;
	border: 1px solid #E4E4E4;
}

.basic-grey h1 {
	font-size: 25px;
	padding: 0px 0px 10px 40px;
	display: block;
	border-bottom: 1px solid #E4E4E4;
	margin: -10px -15px 30px -10px;;
	color: #888;
}

.basic-grey h1>span {
	display: block;
	font-size: 11px;
}

.basic-grey label {
	display: block;
	margin: 0px;
}

.basic-grey label>span {
	float: left;
	width: 20%;
	text-align: right;
	padding-right: 10px;
	margin-top: 10px;
	color: #888;
}

.basic-grey input[type="text"], .basic-grey input[type="email"],
	.basic-grey textarea, .basic-grey select {
	border: 1px solid #DADADA;
	color: #888;
	height: 30px;
	margin-bottom: 16px;
	margin-right: 6px;
	margin-top: 2px;
	outline: 0 none;
	padding: 3px 3px 3px 5px;
	width: 70%;
	font-size: 12px;
	line-height: 15px;
	box-shadow: inset 0px 1px 4px #ECECEC;
	-moz-box-shadow: inset 0px 1px 4px #ECECEC;
	-webkit-box-shadow: inset 0px 1px 4px #ECECEC;
}

.basic-grey textarea {
	padding: 5px 3px 3px 5px;
}

.basic-grey select {
	appearance: none;
	-webkit-appearance: none;
	-moz-appearance: none;
	text-indent: 0.01px;
	text-overflow: '';
	width: 70%;
	height: 35px;
	line-height: 25px;
}

.basic-grey textarea {
	height: 100px;
}

.basic-grey .button {
	background: #E27575;
	border: none;
	padding: 10px 25px 10px 25px;
	color: #FFF;
	box-shadow: 1px 1px 5px #B6B6B6;
	border-radius: 3px;
	text-shadow: 1px 1px 1px #9E3F3F;
	cursor: pointer;
}

.basic-grey .button:hover {
	background: #CF7A7A
}

.clear {
	clear: both;
	font-size: 0;
	height: 0;
	overflow: hidden;
}

.clearfix {
	zoom: 1;
}

.clearfix:after {
	content: '*';
	height: 0;
	display: block;
	visibility: hidden;
	clear: both;
}

.ty-newsfileds .table-condensed {
	box-shadow: 0 1px 4px #ececec inset;
	background: #fff;
	border-radius: 1px;
	width: 760px;
}

.ty-newsfileds label {
	float: left;
	display: block;
	width: 50%;
}

.ty-newsfileds .ty-addbtn {
	width: 100%;
	padding-top: 20px;
}

.ty-newsfileds .ty-addbtn input {
	margin: auto;
	display: block;
}

.ty-newsfileds .ty-summary {
	width: 100%;
}

.ty-newsfileds .ty-summary span {
	width: 10%;
}

.ty-newsfileds .ty-summary #remark {
	width: 751px;
	height: 80px;
}
</style>

<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title=""
		style="overflow: hidden; margin-top: 20px;">
		<form id="form" method="post" enctype="multipart/form-data"
			action="http://180.96.11.6:8080/jiucloud/fieldDataController/savefieldData"
			class="basic-grey ty-newsfileds">
			<h1>
				<span>添加数据</span>
			</h1>
			<label> <span>工程名称:</span> <input type="hidden"
				id="projectName" name="projectName" value='' /> <input type="text"
				style="width: 250px;" id="pName" name="pName" placeholder="工程名称"
				class="easyui-validatebox span2" data-options="required:true"
				value="" readonly>&nbsp;&nbsp;&nbsp;&nbsp;<img alt="选择工程"
                <%--src="${pageContext.request.contextPath}/style/images/extjs_icons/pencil.png"--%>
				src="http://180.96.11.6:8080/jiucloud/style/images/extjs_icons/pencil.png"
				style="cursor: pointer;" onclick="selectp()">
			</label> <label> <span>费用类型:</span> <input type="hidden"
				id="costType" name="costType" value="" /> <input type="hidden"
				id="itemCode" name="itemCode" value="" /><input type="text"
				style="width: 250px;" id="costTypeName" placeholder="费用类型"
				class="easyui-validatebox span2" data-options="required:true"
				value="" readonly="readonly">&nbsp;&nbsp;&nbsp;&nbsp;<img
				alt="选择费用"
				src="http://180.96.11.6:8080/jiucloud/style/images/extjs_icons/pencil.png"
				style="cursor: pointer;" onclick="selectc()">
			</label> <label> <span>名称:</span> <input name="dataName"
				id="dataName" type="text" style="width: 250px;" placeholder="名称"
				class="easyui-validatebox span2" data-options="required:true"
				value="">
			</label>
            <label class="will_hide">
                <span>单位:</span>
                <input name="unit" id="unit" type="text" style="width: 250px;" placeholder="单位" class="easyui-validatebox span2">
			</label>
            <label class="will_hide">
                <span>单价:</span>
                <input name="price" id="price" type="text" style="width: 250px;" placeholder="单价" class="easyui-validatebox span2" onblur="cal()" value="">
			</label>
            <label class="will_hide">
                <span>数量:</span>
                <input name="count" id="count" type="text" style="width: 250px;" placeholder="数量" class="easyui-validatebox span2" onblur="cal()" value="">
			</label>
            <label class="will_hide">
                <span>金额:</span>
                <input id="sumprice" type="text" style="width: 250px;" placeholder="0" class="easyui-validatebox span2"  value="" readonly>
			</label>
            <label class="will_hide">
                <span>规格型号:</span>
                <input name="specifications" id="specifications" type="text" style="width: 250px;" placeholder="规格型号" class="easyui-validatebox span2"  value="">
			</label>
            <label class="ty-summary">
                <span>备注说明 :</span>
                <textarea id="remark" name="remark" placeholder="请在这里填写备注信息"></textarea>
			</label>
            <div class="ty-summary">
                <div style="width:10%;float:left;padding-right:10px;text-align:right">上传附件 :</div>
                <div style="float:left;">
				<table class="table table-hover table-condensed"
					style="font-size: 12px; border: 1px solid #B4B4B4; line-height: 40px;width:751px ">
					<tr>
						<td style="width: 80px;">文档附件</td>
						<td style="text-align: center;"><input
							style="width: 100px; font-size: 12px;" type="file" name="file1"
							id="file1" /></td>
						<td style="text-align: center;"><span style="width: 80px;">视频附件</span></td>
						<td style="text-align: center;"><input
							style="width: 100px; font-size: 12px;" type="file" name="file2"
							id="file2" /></td>
					</tr>
					<tr>
						<td style="width: 80px;">音频附件</td>
						<td style="text-align: center;"><input
							style="width: 100px; font-size: 12px;" type="file" name="file3"
							id="file3" /></td>
						<td style="text-align: center;"><span style="width: 80px;">图片附件</span></td>
						<td style="text-align: center;"><input
							style="width: 100px; font-size: 12px;" type="file" name="file4"
							id="file4" /></td>
					</tr>
				</table>
                </div>
			</div>
            <%--<label class="ty-addbtn">--%>
                <%--<input type="button" class="button" value="添加" onclick="aaa()" style="width: 250px; height: 40px;" />--%>
            <%--</label>--%>
            <div style="text-align:center">
                <input type="button" class="button" value="添加" onclick="aaa()" style="width: 250px; margin-top:20px;height: 40px;" />
            </div>


			<div class="clear">
                // add by heyh
                <input type="reset" name="reset" style="display: none;" />
            </div>
		</form>
	</div>


</div>
