<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title>服务号菜单管理</title>
<jsp:include page="../inc.jsp"></jsp:include>
<link rel='stylesheet' href='${pageContext.request.contextPath}/style/blocksit/css/style.css' media='screen' />
<%-- <link rel='stylesheet' href='${pageContext.request.contextPath}/style/css/info.css' media='screen' /> --%>
<!--[if lt IE 9]>
<script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<style type="text/css">
	ul,ol,li{list-style:none}
</style>
<script type="text/javascript">
	var dataGrid;
	var ck;
	$(function() {
		conn = new Easemob.im.Connection();//获得连接
		$('#win').window('close');//关闭
		
		dataGrid = $('#dataGrid').datagrid({
			url : '${pageContext.request.contextPath}/api/button/fwMenuGrid',
			fit : true,
			fitColumns : true,
			border : false,
			pagination : true,
			idField : 'id',
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
			sortName : 'id',
			sortOrder : 'asc',
			checkOnSelect : false,
			selectOnCheck : false,
			nowrap : false,
			frozenColumns : [ [ {
				field : 'id',
				title : '编号',
				width : 150,
				checkbox : true
			}, {
				field : 'firstName',
				title : '第1个菜单名',
				width : 150
			},{
				field:'secondName',
				title:'第2个菜单名',
				width: 150
			} ,{
				field:'thirdName',
				title:'第3个菜单名',
				width: 150
			} ,{
				field:'fwName',
				title:'所属服务号',
				width: 150
			} ,{
				field:'createTime',
				title:'创建时间',
				width: 150
			} ] ],
			columns : [ [{
				field : 'action',
				title : '操作',
				width : 100,
				formatter : function(value, row, index) {
					var str = '';
					str += $.formatString('<img onclick="showMenu(\'{0}\');" src="{1}" title="显示菜单"/>', row.fwName, '${pageContext.request.contextPath}/style/images/extjs_icons/key.png');
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

	//显示菜单
	function showMenu(id){
		back();//选择页面
		$("#showMenu").html('');
		$('#win').window({
    		title:'菜单信息'
    	});
		$.post(
			"${pageContext.request.contextPath}/api/button/menuLode",
			{"fwName":id},
			function(data,statu){
				if(data.success){
					var m = data.obj.buttion;
					var str = "";
					for(var i = 0; i < m.length; i++) {
						var a = m[i];
						var chilId = "chil"+parseInt(100*Math.random());
						str += '<ul><li><h5 onclick="addEvent(this)"  class="parNode" title="parNode" id="'+a.id+'">'+a.name+'</h5><ul id="'+chilId+'">';
						for (var j = 0; j < a.sub_button.length; j++) {
							str += '<li  style="padding-left: 30px;" onclick="addEvent(this)"  class="chilNode" id="'+a.sub_button[j].id+'">'+a.sub_button[j].name+'</li>';
						}
						str += '</ul></ul></li>';
					}
					$("#customMenuId").val(data.obj.id);
					$("#showMenu").append(str);
				}
			},"json"
		);
		
	}
	
	//返回
	function back(){
		var str = '<div  style="color: gray;text-align: center;" id="sentMsg"><h5>请选择订阅者点击菜单后,公众号做出的相应动作</h5>';
		str += '<img src="${pageContext.request.contextPath}/images/sentText.png" onclick="sentText()"/>';
		str += '<img src="${pageContext.request.contextPath}/images/sentSrc.png" onclick="sentSrc()"/>';
		str += "</div>";
		$("#addEvent").html(str);
	}
	
	//添加动作
	function addEvent(node){
		$("#nodeId").val('');
		$(".parNode").removeClass("add");
		$(".chilNode").removeClass("add");
		var title = $(node)[0].title;
		if(title!=''){
			if($.trim($(node).parent().find('ul')[0].textContent)!=''){
				return;
			}
		}
		$("#msg").hide();
		$(node).addClass("add");
		$.post(//判断节点是否有动作
			"${pageContext.request.contextPath}/api/button/menuInfo",
			{"nodeId":node.id},
			function(data,statu){
				if(data.success){
					if(data.obj.type==1){
						getCk("1");
						ck.setData(data.obj.key);
					}else if(data.obj.type==2){
						findInfoById(data.obj.informId);
					}else if(data.obj.type==3){
						getSrc();
						$("#src").val(data.obj.url);
					}
				}else{
					back();//选择页面
				}
			},"json"
		);
		
		$("#nodeId").val(node.id);
		
	}
	//获得ck
	function getCk(data){
		$("#isText").val('');
		var str = '<div  style="height: 550px;color: gray;padding-left:5%;" id="sentMsg">';
		str += '<h5>订阅者点击该菜单会收到以下消息&nbsp;&nbsp;';
		if(data == "1"){
			str += '<input type="radio" name="type" id="imgType" value="1" checked="checked"/>&nbsp;文本信息&nbsp;&nbsp;';
			str += '<input type="radio" name="type" id="txtType" value="2"/>&nbsp;图文信息';
		}
		if(data == "2"){
			str += '<input type="radio" name="type" id="imgType" value="1"/>&nbsp;文本信息&nbsp;&nbsp;';
// 			str += '<input type="radio" name="type" id="txtType" value="2" checked="checked"/>&nbsp;图文信息';
		}
		str += '</h5>';
		str += '<div class="inform"><textarea name="content" id="content" style="width:300px;height:130px;"></textarea></div>';
		str += '<p  class="btno" style="text-align: center;padding-top: 8px;">';
		str += '<input onclick="back()" type="button" value="返回" style="background-image: url(${pageContext.request.contextPath}//images/back.png);width: 106px;height: 30px;border: 0px;"></p>';
		str += "</div>";
		$("#addEvent").html(str);
		var val=$('input:radio[name="type"]:checked').val();
		if(val=="1"){
			$("#isText").val('1');//文本
			//ck
			ck = CKEDITOR.replace( 'content',    
				{        
					width: 600,
					height: 290 ,
					toolbar :        
					[            
					 ['Bold', 'Italic', '-', 'NumberedList', 'BulletedList', 'insert','-'],
					 [ 'Format']
					]    
				});
		}else{
			$("#isText").val('2');//图文
			$('#win2').window({
	    		title:'图文消息'
	    	});
			showInform("");
			$(".inform").html('');
		}
		$("input:radio").click(function () {
// 			ck.setData("");//清空
			if (CKEDITOR.instances['content']) {//如果有销毁
				CKEDITOR.instances['content'].destroy();
				//CKEDITOR.remove(CKEDITOR.instances['content']);
// 				delete CKEDITOR.instances['content']; 
			}
			if($(this).val()=="1"){
				$("#isText").val('1');//普通文本
				$(".inform").html('<textarea name="content" id="content" style="width:300px;height:130px;"></textarea>');
				//ck
				ck = CKEDITOR.replace( 'content',    
					{        
						width: 600,
						height: 290 ,
						toolbar :        
						[            
						 ['Bold', 'Italic', '-', 'NumberedList', 'BulletedList', 'insert','-'],
						 [ 'Format']
						]    
					});
			}else{
				$("#isText").val('2');//图文
				$('#win2').window({
		    		title:'图文消息'
		    	});
				showInform("");
				$(".inform").html('');
			}
		//alert($("#isText").val());
        });
	}
	//获得src
	function getSrc(){
		$("#isText").val('');
		$("#isText").val('3');
		var str = '<div  style="height: 550px;color: gray;text-align: center;padding-top: 3%;" id="sentMsg"><h5>订阅者点击该菜单会跳转以下链接</h5>';
		str += '<input type="text" id="src" name="" value="" style="width: 60%;" /><br/>';
		str += '<p  style="text-align: center;padding-top: 8px;" class="btno"><input onclick="back()" type="button" value="返回" class="btn"></p>';
		str += "</div>";
		$("#addEvent").html(str);
	}
	//按id显示Inform信息
	function findInfoById(informId){
		$.post(
				"${pageContext.request.contextPath}/informController/findInfoById",
				{"informId":informId},
				function(data,statu){
					if(data.success){
						var a = data.obj;
						var str = '<div  style="height: 550px;color: gray;text-align: center;padding-top: 3%;" id="sentMsg"><h5>订阅者点击该菜单会收到以下消息</h5>';
						str += '<div style="margin-left: 32%;" class="grid2" id="'+a.id+'"><p style="width: 175px;height: 20px;text-overflow: ellipsis; white-space:nowrap;overflow:hidden;">'+a.title+'</p>';
						str += '<div class="imgholder"><img style="width: 175px;height: 131px;" src="'+a.picPath+'" /></div>';
						str += '<p style="width: 175px;height: 20px;text-overflow: ellipsis; white-space:nowrap;overflow:hidden;">'+a.simpleCon+'</p></div>';
						str += '<input onclick="back()" type="button" value="返回" style="background-image: url(${pageContext.request.contextPath}//images/back.png);width: 106px;height: 30px;border: 0px;"></div>';
						$("#addEvent").html(str);
						$('#win2').window('close');//关闭
					}
				},"json"
			);
	}
	function searchFun() {
		dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
	}
	function cleanFun() {
		$('#searchForm input').val('');
		dataGrid.datagrid('load', {});
	}
</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<div data-options="region:'north',title:'查询条件',border:false" style="height: 70px; overflow: hidden;">
			<form id="searchForm">
				<table class="table table-hover table-condensed" style="display: none;">
					<tr>
						<th style="width: 100px;text-align: right;">服务号</th>
						<td><input name="appcomp" placeholder="可以模糊查询服务号" class="span2" style="width: 250px;" /></td>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="dataGrid"></table>
		</div>
	</div>
	<div id="toolbar" style="display: none;">
		<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'brick_add',plain:true" onclick="searchFun();">过滤条件</a>
		<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'brick_delete',plain:true" onclick="cleanFun();">清空条件</a>
	</div>

	
	<%-- 窗口 modal:true为模式窗口 
		--%>
		<div id="win" class="easyui-window" title="My Window"
			style="width: 1150px; height: 700px" data-options="modal:true,collapsible:false,maximizable:false,minimizable:false,draggable:false,resizable:false"
			>
			<input type="hidden" value="" id="customMenuId"/>
			<input type="hidden" value="" id="nodeId"/>
			<input type="hidden" value="" id="parNodeId"/>
			<input type="hidden" value="" id="isText"/>
			<input type="hidden" value="" id="chilId"/>
			<div class="easyui-layout" data-options="fit : true,border : false">
				<div data-options="region:'center',border:false">
					<div class='rbox'>
						<div>
							<h5 style="color: gray;padding-left:40px;">可创建最多3个一级菜单,每个一级菜单下最多创建5个二级菜单</h5>
						</div>
						<div style='margin-left:25px;padding-bottom: 10px;'>
							<table cellpadding='0' cellspacing='0' border='1' bordercolor='#c3c3c3' style="width:90%; ">
								<thead>
									<tr style="background-color: #F4F5F9;font:bold 14px 微软雅黑;">
										<td style="width: 30%;height: 40px;">
											&nbsp;&nbsp;菜单管理
											<div style="float: right;">
												<img alt="添加" src="${pageContext.request.contextPath}/images/jia.png" style="width: 20px;height: 20px;" id="addMenu"/>&nbsp;&nbsp;&nbsp;&nbsp;
											</div>
										</td>
										<td>
											&nbsp;&nbsp;设置动作
										</td>
									</tr>
								</thead >
								<tbody  style="height: 360px; ">
									<tr>
										<td>
											<div id="showMenu" style="color: gray;height: 550px;text-align: center;">
												
												
											</div>
										</td>
										<td id="addEvent">
											<h5 style="color: gray;line-height:550px;text-align: center;" id="msg">你可以先添加一个菜单，然后开始为其设置响应动作</h5>
										</td>
									</tr>
								</tbody>
							</table>
							
						</div>
					</div>
				</div>
			</div>
			
		</div>
</body>
</html>