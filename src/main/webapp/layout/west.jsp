<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	var layout_west_tree;
	var layout_west_tree_url = '';
	var sessionInfo_userId = '${sessionInfo.id}';
	/* if (true) {
		layout_west_tree_url = '${pageContext.request.contextPath}/resourceController/tree';
	} */
	$(function() {
		layout_west_tree = $('#layout_west_tree').tree(
				{
					url : layout_west_tree_url,
					parentField : 'pid',
					//lines : true,
					onClick : function(node) {
						if (node.attributes && node.attributes.url) {
							var url;
							if (node.attributes.url.indexOf('/') == 0) {/*如果url第一位字符是"/"，那么代表打开的是本地的资源*/
								url = '${pageContext.request.contextPath}'
										+ node.attributes.url;
								if (url.indexOf('/druidController') == -1) {/*如果不是druid相关的控制器连接，那么进行遮罩层屏蔽*/
									parent.$.messager.progress({
										title : '提示',
										text : '数据处理中，请稍后....'
									});
								}
							} else {/*打开跨域资源*/
								url = node.attributes.url;
							}
							addTab({
								url : url,
								title : node.text,
								iconCls : node.iconCls
							});
						}
					},
					onBeforeLoad : function(node, param) {
						if (layout_west_tree_url) {//只有刷新页面才会执行这个方法
							parent.$.messager.progress({
								title : '提示',
								text : '数据处理中，请稍后....'
							});
						}
					},
					onLoadSuccess : function(node, data) {
						parent.$.messager.progress('close');
					}
				});
		if (!sessionInfo_userId) {
			loginFun();
		}
	});

	function addTab(params) {
		var iframe = '<iframe src="'
				+ params.url
				+ '" frameborder="0" style="border:0;width:100%;height:98%;"></iframe>';
		var t = $('#index_tabs');
		var opts = {
			title : params.title,
			closable : true,
			iconCls : params.iconCls,
			content : iframe,
			border : false,
			fit : true
		};
		if (t.tabs('exists', opts.title)) {
			t.tabs('select', opts.title);
			parent.$.messager.progress('close');
		} else {
			t.tabs('add', opts);
		}
	}
</script>
<div style="background-color: #f5f5f5;" class="easyui-accordion"
	data-options="fit:true,border:false">
	<div title="系统菜单" style="background-color: #f5f5f5; padding: 5px;">
		<div class="well well-small" style="background-color: #f5f5f5;">
			<ul id="layout_west_tree" style="border: 0px;"></ul>
		</div>
	</div>
	<!-- <div title="其他示例" data-options="border:false,iconCls:'anchor'">
		<ul>
			<li>菜单</li>
			<li>菜单</li>
			<li>菜单</li>
		</ul> 
	</div> -->
</div>