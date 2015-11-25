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
<script src="${pageContext.request.contextPath}/style/blocksit/js/blocksit.min.js"></script>
<script>
$(document).ready(function() {
	
	//blocksit define
	$(window).load( function() {
		$('#container').BlocksIt({
			numOfCol: 3,
			offsetX: 8,
			offsetY: 8,
			blockElement: '.grid'
		});
	});
	
	//window resize
	var currentWidth = 1100;
	$(window).resize(function() {
		var winWidth = $(window).width();
		var conWidth;
		if(winWidth < 660) {
			conWidth = 440;
			col = 2
		} else if(winWidth < 880) {
			conWidth = 660;
			col = 3
		} else if(winWidth < 1100) {
			conWidth = 880;
			col = 4;
		} else {
			conWidth = 1100;
			col = 5;
		}
		
		if(conWidth != currentWidth) {
			currentWidth = conWidth;
			$('#container').width(conWidth);
			$('#container').BlocksIt({
				numOfCol: col,
				offsetX: 8,
				offsetY: 8
			});
		}
	});
});
</script>
<style type="text/css">
	body{background-color:#f2f2f2;color:#686868; font-family:'微软雅黑',Arial, Helvetica, sans-serif;}
	ul,ol,li{list-style:none}
	.rbox{ border:1px solid #dde1e; border-radius:1px; box-shadow:0 0 3px #dde1e0; background:#fff;width: 90%;margin: 0px auto; margin-top:14px;}
	.rbox .tit,.rbox .rtit{ font-size:14px; padding-left:40px; line-height:48px; height:48px;}
	.rbox .rtit{ border-bottom:1px solid #bfbfbf;overflow:hidden;}
	ul li{width: 90%;line-height: 25px;}
	.hover{background: #F4F5F9;} 
 	.add{background: #F4F5F9;}
 	.grid:hover{background:gray; }
 	.gray{background:gray; }
 		.selected{ border:3px solid #0081C2;}
	.selected i{ display:block;}
	i{position:absolute; width:10px; height:10px; font-size:0; line-height:0; right:2px; bottom:2px; background:url(${pageContext.request.contextPath}/images/sys_item_selected.gif) no-repeat right bottom; z-index:99;display:none;}
</style>
<script type="text/javascript">
	var ck ;
	$(function() {
		parent.$.messager.progress('close');
		$('#win').window('close');//关闭
		$('#win2').window('close');//关闭
		showInform();
		//取消
		$("#res").click(function(){
			closeWin();
		});
		//提交
		$("#sub").click(function(){
			//菜单名称名字不大于4个汉字或8个字母
			var menuName = $("#menuName").val();
			if(lenReg(menuName)>8){
				alert("菜单名称名字不大于4个汉字或8个字母");
				return;
			}
			var chilId = $("#chilId").val();
			var parNode = $("#parNodeId").val();
			if(chilId!=''){
				if(chilId.indexOf("chil")>-1){
					$.post(
						"${pageContext.request.contextPath}/api/button/addPrimaryMenu",//添加二级菜单
						{parNode:parNode,menuName:menuName},
						function(data,statu){
							if(data.success){
								var	id = data.obj;	
								$("#"+chilId).append('<li style="padding-left: 30px;" onclick="addEvent(this)" onmouseenter="mouseOver(this)" onmouseleave="mouseOut(this)" class="chilNode" id="'+id+'">'+menuName+'</li>');
							}
						},"json"
					);
				}else{//编辑
					var nodeId = $("#nodeId").val();
					$.post(
							"${pageContext.request.contextPath}/api/button/addPrimaryMenu",//编辑菜单
							{nodeId:nodeId,menuName:menuName},
							function(data,statu){
								if(data.success){
									$("#"+nodeId).html(menuName);
								}
							},"json"
						);
				}
			}else{
				$.post(
					"${pageContext.request.contextPath}/api/button/addPrimaryMenu",//添加一级菜单
					{menuName:menuName},
					function(data,statu){
						if(data.success){
							var	id = data.obj;	
							var chilId = "chil"+parseInt(100*Math.random());
							$("#showMenu").append('<ul><li><h5 onclick="addEvent(this)" onmouseenter="mouseOver(this)" onmouseleave="mouseOut(this)" class="parNode" title="parNode" id="'+id+'">'+menuName+'</h5><ul id="'+chilId+'"></ul></ul></li>');
						}
					},"json"
				);
			}
			$("#nodeId").val('');
			closeWin();
		});
		//添加主菜单
		$("#addMenu").click(function(){
			var pnames = $('.parNode');
			if(pnames.length==3){
				alert("可创建最多3个一级菜单");	
				return;
			}
			$("#nodeId").val('');
			$("#chilId").val('');
			$("#parNodeId").val('');
			$('#win').window({
	    		title:'添加主菜单'
	    	});
		});
		
		menuLode();//加载菜单信息
		//查询图文模板消息
		$("#search").click(function(){
			var infoTitle = $("#infoTitle").val();
			alert(infoTitle);
			showInform(infoTitle);
		});
		//提交图文信息
		$("#subInfoImg").click(function(){
			var informId = $("#informId").val();
			if(informId==""){
				alert("请选择一个模板消息...");
				return;
			}
			subEvent();//提交动作
			findInfoById(informId);//按id显示Inform信息
		});
		
		
	});
	//按id显示Inform信息
	function findInfoById(informId){
		$.post(
				"${pageContext.request.contextPath}/informController/findInfoById",
				{"informId":informId},
				function(data,statu){
					if(data.success){
						var a = data.obj;
						var str = '<div  style="height: 550px;color: gray;text-align: center;padding-top: 3%;" id="sentMsg"><h5>订阅者点击该菜单会收到以下消息</h5>';
						str += '<div style="margin-left: 32%;" class="grid2"><p  style="width: 175px;height: 20px;text-overflow: ellipsis; white-space:nowrap;overflow:hidden;">'+a.title+'</p>';
						str += '<div class="imgholder"><img style="width: 175px;height: 131px;" src="'+a.picPath+'" /></div>';
						str += '<p style="width: 175px;height: 20px;text-overflow: ellipsis; white-space:nowrap;overflow:hidden;">'+a.simpleCon+'</p></div>';
						str += '<input onclick="toInfo()" type="button" value="修改" style="background-image: url(${pageContext.request.contextPath}//images/btnBg.png);width: 106px;height: 30px;border: 0px;color: white;">&nbsp;&nbsp;';
						str += '<input onclick="back()" type="button" value="返回" style="background-image: url(${pageContext.request.contextPath}//images/back.png);width: 106px;height: 30px;border: 0px;"></div>';
						$("#addEvent").html(str);
						$('#win2').window('close');//关闭
					}
				},"json"
			);
	}
	//修改显示所有图文消息
	function toInfo(){
		$('#win2').window({
    		title:'图文消息'
    	});
		showInform("");
	}
	//编辑节点
	function editNode(node){
		$("#parNode").val('');
		$("#chilId").val('');
		$('#win').window({
    		title:'编辑菜单'
    	});
		var nodeId = $(node).parent().parent()[0].id;
		$("#chilId").val(nodeId);
		$("#menuName").val($.trim($(node).parent().parent()[0].textContent));//显示节点
	}
	//删除节点
	function deleteNode(node){
		if($(node).parent().parent()[0].title=="parNode"){
			var nodeId = $(node).parent().parent()[0].id;
			var customMenuId = $("#customMenuId").val();
			$.post(
				"${pageContext.request.contextPath}/api/button/delPrimaryMenu",//删除一级菜单
				{"nodeId":nodeId,"customMenuId":customMenuId},
				function(data,statu){
					if(data.success){
						$(node).parent().parent().parent().remove();//移除一行
					}
				},"json"
			);
// 			$(node).parent().parent().parent().remove();//移除一行
		}
		else{
			var nodeId = $(node).parent().parent()[0].id;
			var parId =  $(node).parent().parent().parent().parent().find('h5')[0].id;
			$.post(
				"${pageContext.request.contextPath}/api/button/delsubMenu",//删除二级菜单
				{"parId":parId,"nodeId":nodeId},
				function(data,statu){
					if(data.success){
						$(node).parent().parent().remove();//移除一行
					}
				},"json"
			);
// 			$(node).parent().parent().remove();//移除一行
		}
	}
	
	//关闭
	function closeWin(){
		$("#menuName").val('');
		$('#win').window('close');//关闭
	}
	//显示菜单 添加子菜单
	function showWin(node){
		$("#menuName").val('');
		$("#nodeId").val('');
		$("#parNode").val('');
		$("#chilId").val('');
		var chilId = $(node).parent().parent().next()[0].id;
		$("#chilId").val(chilId);
		var parNodeId = $(node).parent().parent()[0].id;
		$("#parNodeId").val(parNodeId);
		var pnames = $("#"+chilId+" .chilNode");
		if(pnames.length==5){
			alert("每个一级菜单下最多创建5个二级菜单");	
			return;
		}
		$.post(
			"${pageContext.request.contextPath}/api/button/delEvent",//删除动作
			{"nodeId":parNodeId},
			function(data,statu){
				if(data.success){
					$('#win').window({
			    		title:'添加菜单'
			    	});
				}
			},"json"
		);
	}
	
	//父节点
	function mouseOver(node){
		var title = $(node)[0].title;
			var img = '<div style="float: right;" >';
			if(title!='')
				img +='<img alt="添加" src="${pageContext.request.contextPath}/images/jia.png" style="width: 20px;height: 20px;" onclick="showWin(this)"/>&nbsp;&nbsp;';
			img +='<img alt="编辑" src="${pageContext.request.contextPath}/images/edit.png" onclick="editNode(this)" />';
			img +='<img alt="删除" src="${pageContext.request.contextPath}/images/delete.png" style="width: 20px;height: 20px;" onclick="deleteNode(this)"/></div>';
			$(node).addClass("hover");
		    $(node).append(img);
	}
	function mouseOut(node){
		$(node).removeClass("hover");
		$(node).html($.trim($(node)[0].textContent));
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
		var str = '<div  style="height: 550px;color: gray;padding-left:100px;" id="sentMsg">';
		str += '<h5>订阅者点击该菜单会收到以下消息&nbsp;&nbsp;';
		if(data == "1"){
			str += '<input type="radio" name="type" id="imgType" value="1" checked="checked"/>&nbsp;文本信息&nbsp;&nbsp;';
			str += '<input type="radio" name="type" id="txtType" value="2"/>&nbsp;图文信息';
		}
		if(data == "2"){
			str += '<input type="radio" name="type" id="imgType" value="1"/>&nbsp;文本信息&nbsp;&nbsp;';
			str += '<input type="radio" name="type" id="txtType" value="2" checked="checked"/>&nbsp;图文信息';
		}
		str += '</h5>';
		str += '<div class="inform"><textarea name="content" id="content" style="width:300px;height:130px;"></textarea></div>';
		str += '<p  class="btno" style="text-align: center;padding-top: 8px;"><input onclick="subEvent()" type="button" value="保存" style="background-image: url(${pageContext.request.contextPath}//images/btnBg.png);width: 106px;height: 30px;border: 0px;color: white;">&nbsp;&nbsp;';
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
		str += '<p  style="text-align: center;padding-top: 8px;" class="btno"><input onclick="subEvent()" type="button" value="保存" style="background-image: url(${pageContext.request.contextPath}//images/btnBg.png);width: 106px;height: 30px;border: 0px;color: white;">&nbsp;&nbsp;';
		str += '<input onclick="back()" type="button" value="返回" style="background-image: url(${pageContext.request.contextPath}//images/back.png);width: 106px;height: 30px;border: 0px;"></p>';
		str += "</div>";
		$("#addEvent").html(str);
	}
	//发送消息
	function sentText(){
		getCk("1");
	}
	
	//发送链接
	function sentSrc(){
		getSrc();
	}
	//返回
	function back(){
		var str = '<div  style="color: gray;text-align: center;" id="sentMsg"><h5>请选择订阅者点击菜单后,公众号做出的相应动作</h5>';
		str += '<img src="${pageContext.request.contextPath}/images/sentText.png" onclick="sentText()"/>';
		str += '<img src="${pageContext.request.contextPath}/images/sentSrc.png" onclick="sentSrc()"/>';
		str += "</div>";
		$("#addEvent").html(str);
	}
	//保存节点操作事件
	function subEvent(){
		//选中的节点
		var nodeId = $("#nodeId").val()
		//事件内容
		var srcInfo = $("#src").val();
		//是否是链接
		var isText = $("#isText").val();
		//ck中文本 带标签的
// 		var context = ck.getData();
		//图文信息informId
		var informId = $("#informId").val();
		var txt = "";
		if(isText=="1")
			txt =  ck.document.getBody().getText();//ck 不带标签的
		else if(isText=="2")
			txt = informId;
		else if(isText=="3")
			txt = srcInfo;
		$.post(
			"${pageContext.request.contextPath}/api/button/addEvent",//添加动作
			{nodeId:nodeId,"type":isText,txt:txt},
			function(data,statu){
				if(data.success){
					$(".btno").html('<input onclick="editEvent('+nodeId+')" type="button" value="修改" style="background-image: url(${pageContext.request.contextPath}//images/btnBg.png);width: 106px;height: 30px;border: 0px;color: white;">&nbsp;&nbsp;<input onclick="back()" type="button" value="返回" style="background-image: url(${pageContext.request.contextPath}//images/back.png);width: 106px;height: 30px;border: 0px;">');
				}
			},"json"
		);
	}
	//修改
	function editEvent(nodeId){
		var srcInfo = $("#src").val();
		//是否是链接
		var isText = $("#isText").val();
		//ck中文本 带标签的
// 		var context = ck.getData();
		//informId
		var informId = $("#informId").val();
		var txt = "";
		if(isText=="1")
			txt =  ck.document.getBody().getText();//ck 不带标签的
		else if(isText=="2")
			txt = context
		else if(isText=="3")
			txt = srcInfo;
		$.post(
			"${pageContext.request.contextPath}/api/button/addEvent",//添加动作
			{nodeId:nodeId,"type":isText,txt:txt},
			function(data,statu){
				if(data.success){
					alert(data.msg);
				}
			},"json"
		);
	}
	//发布
	function release(){
		//一级菜单
    	var pnames = $('.parNode');
		var pname = [];			//保存id				
		if (pnames.length > 0) {
			for ( var i = 0; i < pnames.length; i++) {
				pname.push(pnames[i].id);	
			}
		}	 
		var customMenuId = $("#customMenuId").val();
		$.post(
			"${pageContext.request.contextPath}/api/button/saveMenu",
			{"menuId":pname.join(','),"customMenuId":customMenuId},
			function(data,statu){
				if(data.success){
					alert(data.msg);
				}
			},"json"
		);
	}
	//初始加载菜单
	function menuLode(){
		$.post(
			"${pageContext.request.contextPath}/api/button/menuLode",
			{},
			function(data,statu){
				if(data.success){
					var m = data.obj.buttion;
					var str = "";
					for(var i = 0; i < m.length; i++) {
						var a = m[i];
						var chilId = "chil"+parseInt(100*Math.random());
						str += '<ul><li><h5 onclick="addEvent(this)" onmouseenter="mouseOver(this)" onmouseleave="mouseOut(this)" class="parNode" title="parNode" id="'+a.id+'">'+a.name+'</h5><ul id="'+chilId+'">';
						for (var j = 0; j < a.sub_button.length; j++) {
							str += '<li style="padding-left: 30px;" onclick="addEvent(this)" onmouseenter="mouseOver(this)" onmouseleave="mouseOut(this)" class="chilNode" id="'+a.sub_button[j].id+'">'+a.sub_button[j].name+'</li>';
						}
						str += '</ul></ul></li>';
					}
					$("#customMenuId").val(data.obj.id);
					$("#showMenu").append(str);
				}
			},"json"
		);
	}
	//判断输入字节长度
	function lenReg(str){
	    return str.replace(/[^\x00-\xFF]/g,'**').length;
	};
	
	//显示当前服务号下的推送消息
	function showInform(infoTitle){
		$("#container").html('');//搜索时清空原有的
		$.post(
			"${pageContext.request.contextPath}/informController/getAllInfo",
			{"infoTitle":infoTitle},
			function(data,statu){
				if(data.success){
					var str = "";
					var a = data.obj;
					for(var i = 0; i < a.length; i++) {
						str += '<div style="position:relative;"" onclick="findInform('+a[i].id+')" class="grid" id="'+a[i].id+'"><p style="width: 175px;height: 18px;text-overflow: ellipsis; white-space:nowrap;overflow:hidden;">'+a[i].title+'</p>';
						str += '<div class="imgholder"><img style="width: 175px;height: 129px;" src="'+a[i].picPath+'" /></div>';
						str += '<p style="width: 175px;height: 18px;text-overflow: ellipsis; white-space:nowrap;overflow:hidden;">'+a[i].simpleCon+'</p><i></i></div>';
					}
					$("#container").append(str);
				}
			},"json"
		);
	}
	//选择推送图文消息
	function findInform(informId){
		$(".grid").removeClass("selected");
		$("#"+informId).addClass("selected");
		$("#informId").val(informId);
		$("#isText").val("2");
	}
</script>
</head>
<body>
	<input type="hidden" value="" id="informId" />
	<input type="hidden" value="" id="customMenuId" />
	<input type="hidden" value="" id="nodeId"/>
	<input type="hidden" value="" id="parNodeId"/>
	<input type="hidden" value="" id="isText"/>
	<input type="hidden" value="" id="chilId"/>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<div data-options="region:'center',border:false">
			<div class='rbox'>
				<div class='rtit'>
					<h4>自定义菜单</h4>
				</div>
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
						<tbody  style="height: 500px; ">
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
					
					<p style="text-align: center;padding-top: 8px;">
						<a id="" href="#" class="easyui-linkbutton" style="width: 160px;" onclick="release()" >发布</a>&nbsp;&nbsp;
<!-- 						<a id="" href="#" class="easyui-linkbutton" style="width: 160px;" >预览</a>  -->
					</p>
				</div>
			</div>
		</div>
	</div>

	
	<%-- 窗口 modal:true为模式窗口 
		--%>
	 <div id="win" class="easyui-window" title="My Window"
			style="width: 500px; height: 300px;overflow:hidden;" data-options="modal:true,collapsible:false,maximizable:false,minimizable:false,draggable:false,resizable:false"
			>
			<div id="info"
						style="background: #fafafa;height: 300px;padding-top: 30px;">
				<form action="#" method="post">
					<h5 style="color: gray;padding-left: 50px;">菜单名称名字不大于4个汉字或8个字母</h5>
					<p style="padding-left: 50px;">
						<input name="menuName" id="menuName" style="width: 350px;" type="text" placeholder="请输入名称" class="easyui-validatebox span2" data-options="required:true" value="">
					</p>
					<p style="text-align: center;">
						<a id="sub" href="#" class="easyui-linkbutton" >提交</a>&nbsp;&nbsp;
						<a id="res" href="#" class="easyui-linkbutton" >取消</a> 
					</p> 
				</form>
			</div>
		</div> 
		
		
		 <div id="win2" class="easyui-window" title="My Window"
			style="width: 900px; height: 610px;" data-options="modal:true,collapsible:false,maximizable:false,minimizable:false,draggable:false,resizable:false"
			>
			<div class="easyui-panel" 
							style=" background: #fafafa;">
				<!-- <div id="header" class="header">
				  <header id="defaultHeader">
				  	<a class="button" id="backButton" onClick="closePage();">Back</a>
				    <h1 id="pageTitle">Welcome</h1>
				    <a class="menuButton" id="menubadge"></a>
				  </header>
				</div> -->
				<div style="position:fixed; height:35px; width:850px; background:rgba(0,0,0,0.25); z-index:1; color:#fff;padding-left: 20px;padding-top: 10px;">
					<input type="text" value="" id="infoTitle" />&nbsp;&nbsp;<a id="search" href="#" class="easyui-linkbutton" >搜索</a>
					<span style="padding-left: 460px;"><a id="subInfoImg" href="#" class="easyui-linkbutton" >提交</a></span>
				</div>
				<div id="container" style="margin-top: 50px;">
					
				</div>
			</div>
		</div> 
</body>
</html>