package cn.cao.controller;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.cao.pojo.Role;
import cn.cao.pojo.RoleExample;
import cn.cao.pojo.User;
import cn.cao.pojo.UserExample;
import cn.cao.pojo.UserExample.Criteria;
import cn.cao.service.RoleService;
import cn.cao.service.UserService;
import cn.cao.vo.MessageVO;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@RequiresPermissions("admin:adminPage")
	@RequestMapping("/adminPage")
	public String adminPage() {
		return "adminPage";// 管理员信息界面，再从jsp发送ajax到/list
	}
	
	@RequiresPermissions("admin:list")
	@RequestMapping("/list")
	@ResponseBody
	public PageInfo<User> list(@RequestParam(defaultValue = "1") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize, String searchVal) {

		PageHelper.startPage(pageNum, pageSize);

		UserExample userExample = new UserExample();
		// 当搜索框有值
		if (StringUtils.isNotBlank(searchVal)) { // org.apache.commons.lang3.StringUtils
			Criteria createCriteria = userExample.createCriteria();
			createCriteria.andRealnameLike("%" + searchVal + "%");
			Criteria createCriteria2 = userExample.createCriteria();
			createCriteria2.andUsernameLike("%" + searchVal + "%");
			userExample.or(createCriteria2);
		}

		List<User> users = userService.selectByExample(userExample);
		PageInfo<User> pageInfo = new PageInfo<User>(users);

		return pageInfo;// 返回的是json类型数据
	}

	/**
	 * 删除数据
	 * 
	 * @param userId
	 * @return
	 */
	@RequiresPermissions("admin:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public MessageVO delete(Long userId) {
		MessageVO mo = MessageVO.CreateMessageVO(0, "删除数据失败");
		int resoult = userService.deleteByPrimaryKey(userId);
		if (resoult > 0) {
			mo = MessageVO.CreateMessageVO(1, "删除数据成功");
		}
		return mo;
	}
	
	@RequiresPermissions("admin:delete")
	@RequestMapping("/deleteMore")
	@ResponseBody
	public MessageVO deleteMore(Long []ids) {
		MessageVO mo = MessageVO.CreateMessageVO(0, "批量删除数据失败");
		System.out.println("wo="+ids);
		int deletes = userService.deleteByPrimarys(ids);
		if(deletes>0) {
			 mo = MessageVO.CreateMessageVO(1, "批量删除数据成功");
		}
		return mo;
	}
	
	@RequiresPermissions("admin:update")
	@RequestMapping("/edit") // 点击添加、修改，弹出ifram页面
	public String toEdit(Model m,Long userId) {
		if(userId != null) {//此时是编辑修改，不是添加
			//查找对应ID的数据返回到页面
			User user = userService.selectByPrimaryKey(userId);
			m.addAttribute("beforeEditUser", user);
		}
		
		//查找角色 供选择器选择
		RoleExample roleExample = new RoleExample();
		List<Role> roles = roleService.selectByExample(roleExample);
		m.addAttribute("roles", roles);
		return "admin_edit";
	}
	
	
	/**
	 * 验证账号唯一性
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping("/checkUsername")
	@ResponseBody
	public boolean checkUsername(String username) {
		UserExample userExample = new UserExample();
		Criteria createCriteria = userExample.createCriteria();
		createCriteria.andUsernameEqualTo(username);
		List<User> users = userService.selectByExample(userExample);

		return users.size() == 0 ? true : false;
	}

	/**
	 * 	添加账号
	 * @param user
	 * @return
	 */
	@RequiresPermissions("admin:insert")
	@RequestMapping("/addUser")
	@ResponseBody
	public MessageVO addUser(User user) {
		 //密码加密：1.生成随机盐2.密码+盐+散列3次
		String salt = UUID.randomUUID().toString().substring(0, 4);
		user.setSalt(salt);
		Md5Hash md5HashPassword = new Md5Hash(user.getPassword(), salt, 3);
		user.setPassword(md5HashPassword.toString());
		
		int resoult = userService.insert(user);
		MessageVO mo = MessageVO.CreateMessageVO(0, "添加数据失败");
		if (resoult == 1) {
			mo = MessageVO.CreateMessageVO(1, "添加数据成功");
		}
		return mo;
	}
	
	@RequestMapping("/afterEditUser")
	@ResponseBody
	public MessageVO afterEditUser(User user) {
		 //密码加密：1.生成随机盐2.密码+盐+散列3次
		String salt = UUID.randomUUID().toString().substring(0, 4);
		user.setSalt(salt);
		Md5Hash md5HashPassword = new Md5Hash(user.getPassword(), salt, 3);
		user.setPassword(md5HashPassword.toString());
		
		MessageVO mo = MessageVO.CreateMessageVO(0, "修改数据失败");
		int resoult = userService.updateByPrimaryKeySelective(user);
		if (resoult == 1) {
			mo = MessageVO.CreateMessageVO(1, "修改数据成功");
		}
		
		return mo;
	}
	
	
}
