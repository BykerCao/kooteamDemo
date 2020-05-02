<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">

<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<link rel="Bookmark" href="/favicon.ico" >
<link rel="Shortcut Icon" href="/favicon.ico" />
<link rel="stylesheet" type="text/css" href="static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css" href="lib/Hui-iconfont/1.0.8/iconfont.css" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/skin/default/skin.css" id="skin" />
<link rel="stylesheet" type="text/css" href="static/h-ui.admin/css/style.css" />
<link rel="stylesheet" type="text/css" href="lib/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="lib/bootstrap-table/bootstrap-table.min.css" />
</head>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 角色管理 <span class="c-gray en">&gt;</span> 角色列表 </nav>
<div class="page-container">

	<span class="l" id="toolbar">
		<a href="javascript:;" onclick="datadel()" class="btn btn-danger radius"><i class="Hui-iconfont">&#xe6e2;</i> 批量删除</a> 
		<a href="javascript:;" onclick="role_add()" class="btn btn-primary radius"><i class="Hui-iconfont">&#xe600;</i> 添加角色</a>
	</span>  
	
	<table id="infoTable"></table>
	
</div>
<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="lib/jquery/1.11.3/jquery.min.js"></script> 
<script type="text/javascript" src="lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="static/h-ui/js/H-ui.min.js"></script> 
<script type="text/javascript" src="static/h-ui.admin/js/H-ui.admin.js"></script> <!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="lib/My97DatePicker/4.8/WdatePicker.js"></script> 
<script type="text/javascript" src="lib/datatables/1.10.0/jquery.dataTables.min.js"></script> 
<script type="text/javascript" src="lib/laypage/1.2/laypage.js"></script>
<script type="text/javascript" src="lib/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="lib/bootstrap-table/bootstrap-table.min.js"></script>
<script type="text/javascript" src="lib/bootstrap-table/bootstrap-table-zh-CN.min.js"></script>

<script type="text/javascript">
	$(function(){
		//初始化表格
		$('#infoTable').bootstrapTable({
			url: 'role/list.do',
			method:'get',
			
			responseHandler: function(res) { //响应头设置,返回list、total，但是bootstrapTable分页只有rows、total
				var data =  {rows: res.list,total: res.total};
				return data;
			},
			pagination: true,
			toolbar: "#toolbar",//添加和批量删除
			contentType: 'application/x-www-form-urlencoded',//条件搜索的时候ajax请求给后台数据的数据类型（条件搜索post提交必须设置）
			search: true,//显示搜索框
			pageNumber: 1,
			pageSize: 10,
			pageList:[10,18,25,50,100],//每页容量
			sidePagination: "server",//是否是服务器分页，每次请求都是对应的pageSize条数据，下一页发送ajax请求
			paginationHAlign: 'right', //底部分页条
			showRefresh: true, //显示刷新按钮
			
			columns: [
				{checkbox: true},//	显示复选框
				{field: 'roleId',title: '角色编号'}, 
				{field: 'rolename',title: '角色名称'}, 
				{field: 'remark',title: '描述'},
				{field: 'permissionIds',title: '拥有的权限'},
				{
					field:'roleId',title:'操作',align:'center',
					formatter:operationFormatter  /* formatter: 格式化这一行，回调一个函数		 修改+删除方法 */
				} 
			],
			queryParams:function(params){//console.log(params);		//limit：每页条数	offset：每页的结束位置	search:搜索框对应的值
				
				var paramsData = {
						pageNum: params.offset / params.limit + 1,
						pageSize: params.limit,
						searchVal: params.search 
					};
				return paramsData;//此方法   分页 或 搜索的时    自动发送ajax请求    调用，并把对应的参数传递给后台
			}
		})
	});
	
	/* 	格式化行的函数，可以在此函数使用html标签组装一个删除和修改按钮，运行过程中会自动渲染的到浏览器
		value ：当前行的 值  field:'userId'		row ：当前行对应的json对象 		index ：索引 ,从0开始	*/
	function operationFormatter(value,row,index){
		var html = "<span onclick='role_del("+value+")' style='color:red;cursor: pointer;' class='glyphicon glyphicon-trash'></span> &nbsp;&nbsp;";		
		 html += "<span onclick='role_edit("+value+")' style='color:blue;cursor: pointer;' class='glyphicon glyphicon-pencil'></span>";		
		return html;
	}
	
</script>
<script type="text/javascript">
function role_del(roleId) {//删除方法
	layer.confirm('确认要删除吗？',function(index){
		//		url					 data 			callback
		$.get("role/delete.do",{roleId:roleId},function(data){
			layer.msg(data.msgData, {time: 2000, icon:data.msgId});
			if(data.msgId == 1){
				refreshTable();//刷新页面
			}
		});
	});
}
function refreshTable() {
	$("#infoTable").bootstrapTable("refresh");
}
</script>
<script type="text/javascript">
	//'添加角色','admin-add.html','800','500'
	function role_add(){
		layer_show("添加角色","role/edit.do");
	}
	/*修改-编辑*/
	function role_edit(roleId){
		layer_show("修改角色","role/edit.do?roleId="+roleId);
	}
	
</script>

</body>
</html>