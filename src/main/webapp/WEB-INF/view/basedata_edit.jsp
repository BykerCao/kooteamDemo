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
	<form class="form form-horizontal" id="form-admin-edit" action="${beforeEditBaseData == null?'basedata/addBaseData.do':'basedata/afterEditBaseData.do' } ">
		<!-- 修改时要用到的beforeEditUser的id -->
		<input type="hidden" name="baseId" value="${beforeEditBaseData.baseId }">
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>基础信息名称:</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" ${beforeEditBaseData != null? "disabled":"" } value="${beforeEditBaseData.baseName }" placeholder="" id="nameid" name="baseName">
			</div>
		</div>
		
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>基础信息描述:</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" value="${beforeEditBaseData.baseDesc }" placeholder="" id="baseDesc" name="baseDesc">
			</div>
		</div>
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">父基础信息：</label>
			<div class="formControls col-xs-8 col-sm-9"> 
				<span class="select-box" style="width:150px;">
				<select class="select" name="parentId" size="1">
					<option value="0">请选择</option>
					<c:forEach items="${basedatas }" var="p">
						<option ${beforeEditBaseData.parentId eq p.baseId? 'selected':'' } value="${p.baseId}">${p.baseName }</option>
					</c:forEach>
					
				</select>
				</span>
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
<script type="text/javascript">
$(function(){
	$("#form-admin-edit").validate({
		rules:{//对应的name
			/* baseName:{
					required:true,
					isChinese:true,
					remote: {//验证基础信息名称唯一性
					    url: "basedata/checkBaseDataName.do",     //后台处理程序
					    type: "post",               //数据发送方式
					    dataType: "json",           //接受数据格式   
					    data: {                     //要传递的数据
					        name: function() {
					            return $("#nameid").val();
					        }
					    }
					}
				} */
			},
		messages: {//提示的校验信息
			//baseName:{required:"基础信息名不能空",isChinese:"必须中文",remote:"基础信息名已存在"},
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