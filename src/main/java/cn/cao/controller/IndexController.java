package cn.cao.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	//显示首页
	@RequestMapping("/index")
	public String index(HttpSession session) {
		Subject subject = SecurityUtils.getSubject();
		Object user = subject.getPrincipal();
		session.setAttribute("user", user);
		
		
		return "index";
	}
	
	//首页显示欢迎内容
	@RequestMapping("/welcome")
	public String welcome() {
		
		return "index_welcome/welcome";
	}
	
	@RequestMapping("/login.do")
	public String Login(HttpServletRequest req,Model m) {
		//获取身份验证错误信息
		String shiroLoginFailure = (String) req.getAttribute("shiroLoginFailure");
		System.out.println(shiroLoginFailure);
		//org.apache.shiro.authc.IncorrectCredentialsException
		//org.apache.shiro.authc.UnknownAccountException
		if(UnknownAccountException.class.getName().equals(shiroLoginFailure)) {
			m.addAttribute("errorMsg", "账号不存在");
		}else if(IncorrectCredentialsException.class.getName().equals(shiroLoginFailure)) {
			m.addAttribute("errorMsg", "密码错误");
		}
		return "forward:/login.jsp";
	}
	
	
}
