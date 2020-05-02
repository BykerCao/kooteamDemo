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
</head>
<body>
<article class="page-container">
	<form class="form form-horizontal" id="form-admin-edit" action="${beforeEditUser == null?'admin/addUser.do':'admin/afterEditUser.do' } ">
		<!-- 修改时要用到的beforeEditUser的id -->
		<input type="hidden" name="userId" value="${beforeEditUser.userId }">
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>账	号：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" ${beforeEditUser.userId != null? "disabled":"" } value="${beforeEditUser.username }" placeholder="" id="username" name="username">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>真实姓名:</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" value="${beforeEditUser.realname }" placeholder="" id="realname" name="realname">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>初始密码：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="password" class="input-text" autocomplete="off" value="" placeholder="密码" id="password" name="password">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>确认密码：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="password" class="input-text" autocomplete="off"  placeholder="确认新密码" id="password2" name="password2">
			</div>
		</div>
		
		<!-- <div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>性别：</label>
			<div class="formControls col-xs-8 col-sm-9 skin-minimal">
				<div class="radio-box">
					<input name="sex" type="radio" id="sex-1" checked>
					<label for="sex-1">男</label>
				</div>
				<div class="radio-box">
					<input type="radio" id="sex-2" name="sex">
					<label for="sex-2">女</label>
				</div>
			</div>
		</div> -->
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">角色：</label>
			<div class="formControls col-xs-8 col-sm-9"> <span class="select-box" style="width:150px;">
				<select class="select" name="roleId" size="1">
					<option value="0">请选择</option>
					<c:forEach items="${roles }" var="role">
						<option ${beforeEditUser.roleId eq role.roleId? 'selected':'' } value="${role.roleId }">${role.rolename }</option>
					</c:forEach>
					
				</select>
				</span> </div>
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
<script type="text/javascript">
$(function(){
	$("#form-admin-edit").validate({
		rules:{//对应的name
			username:{
					required:true,
					remote: {//验证账户唯一性
					    url: "admin/checkUsername.do",     //后台处理程序
					    type: "post",               //数据发送方式
					    dataType: "json",           //接受数据格式   
					    data: {                     //要传递的数据
					        username: function() {
					            return $("#username").val();
					        }
					    }
					}
				},
			realname:{required:true, minlength: 2,isChinese:true},
			password:"required",
			password2:{required: true,equalTo: "#password"},
			roleId:{min:1}
		},
		messages: {//提示的校验信息
			username:{required:"账号不能空",remote:"账号已存在"},
			realname:{required:"请填写真实姓名", minlength: "最短两位字符",isChinese:"只能是中文"},
			password:"密码不能为空",
			password2:{required: "请输入确认密码",equalTo: "两次密码不一致"},
			roleId:{min:"请选择角色"}
		},
		submitHandler:function(fromtable){//fromtable ：原生js的DOM对象;	校验全部成功返回当前表单
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
</script> 

</body>
</html>