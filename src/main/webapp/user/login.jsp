<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" charset="utf-8">
	var loginDialog;
	var defaultUserInfoDialog;
	var loginTabs;
	$(function() {
		loginFun();
		/* loginDialog = $('#loginDialog').show().dialog({
			modal : true,
			closable : false,
			buttons : [ {
				text : '登录',
				handler : function() {
					loginFun();
				}
			} ] 
		});*/

		//defaultUserInfoDialog = $('#defaultUserInfoDialog').show().dialog({
		//	top : 0,
		//left : 200
		//});

		var sessionInfo_userId = '${sessionInfo.id}';
		//if (true) {/*目的是，如果已经登陆过了，那么刷新页面后也不需要弹出登录窗体*/
		//	loginDialog.dialog('close');
			//defaultUserInfoDialog.dialog('close');
		//}

		$('#loginDialog input').keyup(function(event) {
			if (event.keyCode == '13') {
				loginFun();
			}
		});

	});

	function loginFun() {
		var uid =<%=request.getParameter("uid")%>;
		var cid =<%=request.getParameter("cid")%>;
		//if (layout_west_tree) {//当west功能菜单树加载成功后再执行登录
			loginTabs = $('#loginTabs');//.tabs('getSelected');//当前选中的tab
			var form = loginTabs.find('form');//选中的tab里面的form
			$
					.post(
							'${pageContext.request.contextPath}/userController/login?id=' + uid + '&cid=' + cid,
							form.serialize(),
							function(result) {
								if (result.success) {
									if (!layout_west_tree_url) {
										layout_west_tree
												.tree({
													url : '${pageContext.request.contextPath}/resourceController/tree',
													onBeforeLoad : function(
															node, param) {
														parent.$.messager
																.progress({
																	title : '提示',
																	text : '数据处理中，请稍后....'
																});
													}
												});
									}
									var url = '${pageContext.request.contextPath}/fieldDataController/fieldDataShow';
//									var text = "现场数据管理";
                                    var text = "项目数据管理";
									var params = {
										url : url,
										title : text,
										iconCls : 'prodatamng'
									}
									window.parent.ac(params);
									parent.$.modalDialog.handler
											.dialog('close');
									$('#loginDialog').dialog('close');
									$('#sessionInfoDiv')
											.html(
													$
															.formatString(
																	'[<strong>{0}:{1}</strong>]，欢迎你！',
																	result.obj.compName,
																	result.obj.name));
								} else {
									layout_west_tree = null;
							        $.messager.alert('错误',result.msg,null,function(){
							        	window.location.href="http://www.9393915.com/department"; 
							        });
									//$.messager.alert('错误', result.msg, 'error');
									//window.location.href="http://www.9393915.com/department";
								}
								parent.$.messager.progress('close');
							}, "JSON");
		//}
	}
</script>
<div id="loginDialog" title="系统登录"
	style="width: 330px; height: 220px; overflow: hidden; display: none;">
	<div id="loginTabs" class="easyui-tabs"
		data-options="fit:true,border:false">
		<form method="post">
			<table class="table table-hover table-condensed">
				<tr>
					<th>登录名</th>
					<td><input name="name" type="text" placeholder="请输入登录名"
						class="easyui-validatebox" data-options="required:true" value=""></td>
				</tr>
				<tr>
					<th>密码</th>
					<td><input name="pwd" type="password" placeholder="请输入密码"
						class="easyui-validatebox" data-options="required:true" value=""></td>
				</tr>
			</table>
		</form>
	</div>

</div>
