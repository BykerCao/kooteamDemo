package cn.cao.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import cn.cao.pojo.User;

public class MyFormAuthenticationFiter extends FormAuthenticationFilter{
	//最先执行的类,true执行shiro框架false拒绝访问
	/*
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest req = (HttpServletRequest)request;
		//1.获取用户表单提交的验证码
		String verifyCode = req.getParameter("verifyCode");
		//2获取Session中的随机码
		String code = (String) req.getSession().getAttribute("code");
		System.out.println("verifyCode="+verifyCode+",code="+code);
		if(!code.equalsIgnoreCase(verifyCode)) {
			req.setAttribute("errorMsg", "亲，验证码不正确");
			req.getRequestDispatcher("/login.jsp").forward(req, response);
			return false;
		}
		return super.onAccessDenied(request, response);
	}
	*/
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		//获取主体
		Subject subject = getSubject(request, response);
		//从主体获取shiro框架的Session
		Session session = subject.getSession();
		//如果主体没有认证（Session中认证）并且 主体已经设置记住我了
		if(!subject.isAuthenticated() && subject.isRemembered()) {
			//（从记住我的Cookie中获取主体身份）
			User user = (User) subject.getPrincipal();
			//将身份认证信息共享到 Session中
			session.setAttribute("principal", user);
		}
		return subject.isAuthenticated() || subject.isRemembered();
	}
	
	
}
