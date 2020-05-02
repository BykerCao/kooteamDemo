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

import cn.cao.pojo.Permission;
import cn.cao.pojo.PermissionExample;
import cn.cao.pojo.PermissionExample.Criteria;
import cn.cao.service.PermissionService;
import cn.cao.vo.MessageVO;

@Controller
@RequestMapping("/permission")
public class PermissionController {

	@Autowired
	private PermissionService permissionService;

	@RequestMapping("/permissionPage")
	public String adminPage() {
		return "permissionPage";// 管理员信息界面，再从jsp发送ajax到/list
	}

	@RequestMapping("/list")
	@ResponseBody
	public PageInfo<Permission> list(@RequestParam(defaultValue = "1") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize, String searchVal) {

		PageHelper.startPage(pageNum, pageSize);

		PermissionExample permissionExample = new PermissionExample();
		// 当搜索框有值,	自关联，andNameLike、andExpressionLike里面的字段要改
		if (StringUtils.isNotBlank(searchVal)) { // org.apache.commons.lang3.StringUtils
			Criteria createCriteria = permissionExample.createCriteria();
			createCriteria.andNameLike("%" + searchVal + "%");

			Criteria createCriteria2 = permissionExample.createCriteria();
			createCriteria2.andExpressionLike("%" + searchVal + "%");

			permissionExample.or(createCriteria2);
		}

		List<Permission> permissions = permissionService.selectByExample(permissionExample);
		PageInfo<Permission> pageInfo = new PageInfo<Permission>(permissions);

		return pageInfo;// 返回的是json类型数据
	}
	
	/**
	 * 删除数据
	 * 
	 * @param permissionId
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public MessageVO delete(Long permissionId) {
		//根据传进来的id 查找当前权限是否有子权限，如果有则不能删除type=menu则有
		PermissionExample permissionExample = new PermissionExample();
		Criteria createCriteria = permissionExample.createCriteria();
		createCriteria.andParentIdEqualTo(permissionId);
		List<Permission> selectByExample = permissionService.selectByExample(permissionExample);
		
		MessageVO mo = MessageVO.CreateMessageVO(0, "删除数据失败");
		if(selectByExample.size()==0) {
			int resoult = permissionService.deleteByPrimaryKey(permissionId);
			if (resoult > 0) {
				mo = MessageVO.CreateMessageVO(1, "删除数据成功");
			}
		}else {
			mo = MessageVO.CreateMessageVO(0, "该权限含有子权限不能删除");
		}
		
		return mo;
	}
	
	
	@RequestMapping("/edit") // 点击添加、修改，弹出ifram页面
	public String toEdit(Model m,Long permissionId) {
		
		if(permissionId != null) {//此时是编辑修改，不是添加
			Permission permission = permissionService.selectByPrimaryKey(permissionId);
			m.addAttribute("beforeEditPermission", permission);
		}
		
		//查询所有的父权限（type=menu）的权限共享给编辑页面，让其进行父权限选择
		PermissionExample permissionExample = new PermissionExample();
		Criteria createCriteria = permissionExample.createCriteria();
		createCriteria.andTypeEqualTo("menu");
		
		List<Permission> permissions = permissionService.selectByExample(permissionExample);
		m.addAttribute("permissions", permissions);
		
		return "permission_edit";
	}

	/**
	 * 	添加权限
	 * @param permission
	 * @return
	 */
	@RequestMapping("/addPermission")
	@ResponseBody
	public MessageVO addPermission(Permission permission) {
		MessageVO mo = MessageVO.CreateMessageVO(0, "添加数据失败");
		int resoult = permissionService.insert(permission);
		if (resoult == 1) {
			mo = MessageVO.CreateMessageVO(1, "添加数据成功");
		}
		return mo;
	}
	
	/**
	 * 验证权限名称是否唯一
	 * @param name
	 * @return
	 */
	@RequestMapping("/checkPermissionName")
	@ResponseBody
	public boolean checkPermissionName(String name) {
		PermissionExample permissionExample = new PermissionExample();
		Criteria createCriteria = permissionExample.createCriteria();
		createCriteria.andNameEqualTo(name);
		
		List<Permission> selectByExample = permissionService.selectByExample(permissionExample);
		
		return selectByExample.size() == 0 ? true : false;
	}
	
	
	@RequestMapping("/afterEditPermission")
	@ResponseBody
	public MessageVO afterEditPermission(Permission permission) {
		MessageVO mo = MessageVO.CreateMessageVO(0, "修改数据失败");
		int resoult = permissionService.updateByPrimaryKeySelective(permission);
		
		if (resoult == 1) {
			mo = MessageVO.CreateMessageVO(1, "修改数据成功");
		}
		
		return mo;
	}
	
	//———————————————————————查询所有权力，供给角色选择—————————————————————————————————————
	@RequestMapping("/selectAllPermission")
	@ResponseBody
	public List<Permission>  selectAllPermission() {

		PermissionExample permissionExample = new PermissionExample();

		List<Permission> permissions = permissionService.selectByExample(permissionExample);

		return permissions;// 返回的是json类型数据
	}
	
}
