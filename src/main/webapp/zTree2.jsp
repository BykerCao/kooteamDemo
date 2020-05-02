<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
/* http://localhost:8080/logistics/ */
%>
<!DOCTYPE HTML>
<html>
<head>
<!-- 设置页面的 基本路径，页面所有资源引入和页面的跳转全部基于 base路径 -->
<base href="<%=basePath%>">
<link rel="stylesheet" href="lib/zTree/v3/css/zTreeStyle/zTreeStyle.css" type="text/css">

<meta charset="UTF-8">
<title>zTree演示</title>
</head>
<body>
	<button onclick="getzTreeCheckedData()">获取zTree选中数据</button>
	<ul id="permissionTree" class="ztree"></ul>
	
<script type="text/javascript" src="lib/jquery/1.11.3/jquery.min.js"></script>
<script type="text/javascript" src="lib/zTree/v3/js/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript">
		var setting = {
				check: {enable: true},
				data: {
					simpleData: {enable: true}
				},
				async: {
					enable: true,
					url:"permission/selectAllPermission.do",
					dataFilter: filter
				},
				callback: {
					onAsyncSuccess: zTreeOnAsyncSuccess
				}
		};
		function filter(treeId, parentNode, childNodes) {
			
			for(var i=0,h=childNodes.length;i<h;i++){
				var child = childNodes[i];
				//console.log(child)
				child.id = child.permissionId;
				child.pId = child.parentId;
				child.open = true; 
				console.log(child);
			}  
			return childNodes;
		}
		function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
			
			var permissionIds= "23,24";
			var permissionIdsArr = permissionIds.split(",");
			
			var treeObj = $.fn.zTree.getZTreeObj("permissionTree");
			for(var i=0;i<permissionIdsArr.length;i++){
				var permissionId = permissionIdsArr[i];
				var node = treeObj.getNodeByParam("id",permissionId, null);
				
				if(node!=null){//让其选中
					treeObj.checkNode(node, true, true);
				}
			}
			
			
			
		}
		//获取选中结点
		function getzTreeCheckedData() {
			var treeObj = $.fn.zTree.getZTreeObj("permissionTree");
			var nodes = treeObj.getCheckedNodes(true);
			
			//创建数组保存权限id
			var permissionIdsArr = [];
			for(var i=0;i<nodes.length;i++){
				var node = nodes[i];//获取节点数据
				var permissionId = node.permissionId;
				permissionIdsArr.push(permissionId);
			}
			console.log(permissionIdsArr);
			var permissionStr = permissionIdsArr.toString();
			//var permissionStr = permissionIdsArr.join(",");
			
		}
		$(document).ready(function(){
			$.fn.zTree.init($("#permissionTree"), setting);
		});
	
</script>
</body>
</html>