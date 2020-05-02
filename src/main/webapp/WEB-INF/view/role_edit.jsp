<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">

<meta charset="utf-8">
<link rel="Bookmark" href="/favicon.ico" >
<link rel="Shortcut Icon" href="/favicon.ico" />
<link rel="stylesheet" type="text/css" href="static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css" href="lib/Hui-iconfont/1.0.8/iconfont.css" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/skin/default/skin.css" id="skin" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/css/style.css" />
<link rel="stylesheet" type="text/css" href="lib/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="lib/bootstrap-table/bootstrap-table.min.css" />
<link rel="stylesheet" href="lib/zTree/v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
</head>
<body>
<article class="page-container">
	<form class="form form-horizontal" id="form-admin-edit" action="${beforeEditRole == null?'role/addRole.do':'role/afterEditRole.do' } ">
		<!-- 修改时要用到的beforeEditRole的id -->
		<input type="hidden" name="roleId" value="${beforeEditRole.roleId }">
		<input type="hidden" id="permissionIds" name="permissionIds">
		
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>角色名称:</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text"  value="${beforeEditRole.rolename }" placeholder="" id="rolenameid" name="rolename">
			</div>
		</div>
		
		 <div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">角色描述：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<textarea name="remark" cols="" rows="" class="textarea" 
				  placeholder="说点什么...最少输入10个字符" >${beforeEditRole.remark}</textarea>
			</div>
		</div>
		
		<!-- 用zTree显示     拥有的权限/可选择的权限  -->
		 <div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">权限选择：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<ul id="permissionTree" class="ztree"></ul>
			</div>
		</div>
	
		<div class="row cl">
			<div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
				<input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
			</div>
		</div>
	</form>
</article>

<!--_footer 作为公共模版分离出去--> 
<script type="text/javascript" src="lib/jquery/1.9.1/jquery.min.js"></script> 
<script type="text/javascript" src="lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="static/h-ui/js/H-ui.min.js"></script> 
<script type="text/javascript" src="static/h-ui.admin/js/H-ui.admin.js"></script> <!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="lib/jquery.validation/1.14.0/jquery.validate.js"></script> 
<script type="text/javascript" src="lib/jquery.validation/1.14.0/validate-methods.js"></script> 
<script type="text/javascript" src="lib/jquery.validation/1.14.0/messages_zh.js"></script> 
<script type="text/javascript" src="lib/zTree/v3/js/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript">
$(function(){
	$("#form-admin-edit").validate({
		rules:{//对应的name
			rolename:{
					required:true,
					isChinese:true/* ,
					remote: {//验证角色名称唯一性
					    url: "role/checkRoleName.do",     //后台处理程序
					    type: "post",               //数据发送方式
					    dataType: "json",           //接受数据格式   
					    data: {                     //要传递的数据
					        name: function() {
					            return $("#rolenameid").val();
					        }
					    }
					} */
				},
			remark: "required"
			},
		messages: {//提示的校验信息
			rolename:{required:"角色名不能空",isChinese:"必须中文"},
			remark: {required:"描述一下角色"}
		},
		submitHandler:function(fromtable){//fromtable ：原生js的DOM对象;	校验全部成功返回当前表单
			getzTreeCheckedData();//获取选中的permissionIds的值
			if($("#permissionIds").val()==""){
				layer.msg("请给角色分配权限",{icon:0});
				return false;
			}
			var jqFromTable = $(fromtable);
				jqFromTable.ajaxSubmit(function(data){
					if(data.msgId == 1){
						layer.msg(data.msgData,{icon:1,time:2000},function(){
							window.parent.refreshTable();
							window.parent.layer.closeAll();
						})
					}
						
				})
		}
		
	});
});
	/* zTree */
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
	//将拼接的选中id 设置到隐藏域
	$("#permissionIds").val(permissionStr);
}
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
				onAsyncSuccess: zTreeOnAsyncSuccess,
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
		
		var permissionIds= "${beforeEditRole.permissionIds}";
		var permissionIdsArr = permissionIds.split(",");
		
		var treeObj = $.fn.zTree.getZTreeObj("permissionTree");
		for(var i=0;i<permissionIdsArr.length;i++){
			var permissionId = permissionIdsArr[i];
			var node = treeObj.getNodeByParam("id",permissionId, null);
			
			if(node!=null){//让其选中
				treeObj.checkNode(node, true, false);
			}
		}
	}
	
	$(document).ready(function(){
		$.fn.zTree.init($("#permissionTree"), setting);
	});
	
</script>
</body>
</html>