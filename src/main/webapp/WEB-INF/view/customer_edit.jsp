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
	<form class="form form-horizontal" id="form-admin-edit" action="${beforeEditCustomer == null?'customer/addCustomer.do':'customer/afterEditCustomer.do' } ">
		<!-- 修改时要用到的beforeEditCustomer的id -->
		<input type="hidden" name="customerId" value="${beforeEditCustomer.customerId }">
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>客户名称:</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" ${beforeEditCustomer != null? "disabled":"" } value="${beforeEditCustomer.customerName }" id="nameid" name="customerName">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>客户电话：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text"    value="${beforeEditCustomer.phone}" placeholder="" id="phone" name="phone">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>客户邮箱：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text"    value="${beforeEditCustomer.email}" placeholder="" id="email" name="email">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>客户地址：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text"    value="${beforeEditCustomer.address}" placeholder="" id="address" name="address">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>客户身份证：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text"    value="${beforeEditCustomer.idCard}" placeholder="" id="idCard" name="idCard">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>性别：</label>
			<div class="formControls col-xs-8 col-sm-9 skin-minimal">
				<div class="radio-box">
					<input name="gender" type="radio" id="sex-1" ${beforeEditCustomer.gender eq 1 ?'checked':''} value="1">
					<label for="sex-1">男</label>
				</div>
				<div class="radio-box">
					<input type="radio" id="sex-2" name="gender" ${beforeEditCustomer.gender eq 2 ?'checked':''} value="2">
					<label for="sex-2">女</label>
				</div>
			</div>
		</div>
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">业务员：</label>
			<div class="formControls col-xs-8 col-sm-9"> 
				<span class="select-box" style="width:150px;">
				
			<c:if test="${user.rolename eq '业务员'}">				
				<input type="hidden" name="userId" value="${user.userId}">
				${user.realname}
			</c:if>
			<!-- 不是业务员 -->
			<c:if test="${user.rolename != '业务员'}">	
				<select class="select" name="userId" size="1">
					<option value="0">请选择</option>
					<c:forEach items="${users }" var="p">
						<option ${beforeEditCustomer.userId eq p.userId? 'selected':'' } value="${p.userId}">${p.realname }</option>
					</c:forEach>
				</select>
			</c:if>	
				</span>
			</div>
		</div>
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">客户区间：</label>
			<div class="formControls col-xs-8 col-sm-9"> 
				<span class="select-box" style="width:150px;">
					
					<select class="select" name="baseId" size="1">
						<option value="">请选择</option>
						<c:forEach items="${baseDataList}" var="p">
							<option ${beforeEditCustomer.baseId eq p.baseId ?'selected':''} value="${p.baseId}">${p.baseName}</option>
						</c:forEach>
					</select>
				</span> 
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>客户描述：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<textarea name="remark" class="textarea"   placeholder="说点什么...最少输入10个字符" >${beforeEditCustomer.remark}</textarea>
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
					remote: {//验证客户名称唯一性
					    url: "customer/checkCustomerName.do",     //后台处理程序
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
			//baseName:{required:"客户名不能空",isChinese:"必须中文",remote:"客户名已存在"},
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