package cn.cao.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import cn.cao.pojo.UserExample;
import cn.cao.pojo.RoleExample.Criteria;
import cn.cao.pojo.User;
import cn.cao.service.RoleService;
import cn.cao.service.UserService;
import cn.cao.vo.MessageVO;

@Controller
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;

	@RequestMapping("/rolePage")
	public String adminPage() {
		return "rolePage";// 管理员信息界面，再从jsp发送ajax到/list
	}

	@RequestMapping("/list")
	@ResponseBody
	public PageInfo<Role> list(@RequestParam(defaultValue = "1") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize, String searchVal) {

		PageHelper.startPage(pageNum, pageSize);

		RoleExample roleExample = new RoleExample();
		// 当搜索框有值,	自关联，andNameLike、andExpressionLike里面的字段要改
		if (StringUtils.isNotBlank(searchVal)) { // org.apache.commons.lang3.StringUtils
			Criteria createCriteria = roleExample.createCriteria();
			createCriteria.andRolenameLike("%" + searchVal + "%");
		}

		List<Role> roles = roleService.selectByExample(roleExample);
		PageInfo<Role> pageInfo = new PageInfo<Role>(roles);

		return pageInfo;// 返回的是json类型数据
	}

	/**
	 * 删除数据
	 * 
	 * @param roleId
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public MessageVO delete(Long roleId) {
		//根据传进来的id 查找当前角色是否有用户，如果有则不能删除
		
		UserExample userExample = new UserExample();
		cn.cao.pojo.UserExample.Criteria createCriteria = userExample.createCriteria();
		createCriteria.andRoleIdEqualTo(roleId);
		
		List<User> selectByExample = userService.selectByExample(userExample);
		
		MessageVO mo = MessageVO.CreateMessageVO(0, "删除数据失败");
		if(selectByExample.size()==0) {
			int resoult = roleService.deleteByPrimaryKey(roleId);
			if (resoult > 0) {
				mo = MessageVO.CreateMessageVO(1, "删除数据成功");
			}
		}else {
			mo = MessageVO.CreateMessageVO(0, "该角色含有用户不能删除");
		}
		
		return mo;
	}
	
	
	@RequestMapping("/edit") // 点击添加、修改，弹出ifram页面
	public String toEdit(Model m,Long roleId) {
		
		if(roleId != null) {//此时是编辑修改，不是添加
			Role role = roleService.selectByPrimaryKey(roleId);
			m.addAttribute("beforeEditRole", role);
		}
		
		return "role_edit";
	}

	/**
	 * 	添加角色
	 * @param role
	 * @return
	 */
	@RequestMapping("/addRole")
	@ResponseBody
	public MessageVO addRole(Role role) {
		MessageVO mo = MessageVO.CreateMessageVO(0, "添加数据失败");
		int resoult = roleService.insert(role);
		if (resoult == 1) {
			mo = MessageVO.CreateMessageVO(1, "添加数据成功");
		}
		return mo;
	}
	
	/**
	 * 验证角色名称是否唯一
	 * @param name
	 * @return
	 */
	@RequestMapping("/checkRoleName")
	@ResponseBody
	public boolean checkRoleName(String name) {
		RoleExample roleExample = new RoleExample();
		Criteria createCriteria = roleExample.createCriteria();
		createCriteria.andRolenameEqualTo(name);
		
		List<Role> selectByExample = roleService.selectByExample(roleExample);
		
		return selectByExample.size() == 0 ? true : false;
	}
	
	
	@RequestMapping("/afterEditRole")
	@ResponseBody
	public MessageVO afterEditRole(Role role) {
		MessageVO mo = MessageVO.CreateMessageVO(0, "修改数据失败");
		int resoult = roleService.updateByPrimaryKeySelective(role);
		
		if (resoult == 1) {
			mo = MessageVO.CreateMessageVO(1, "修改数据成功");
		}
		
		return mo;
	}
	
	
}
