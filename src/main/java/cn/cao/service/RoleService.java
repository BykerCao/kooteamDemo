package cn.cao.service;

import java.util.List;

import cn.cao.pojo.Role;
import cn.cao.pojo.RoleExample;


public interface RoleService {
	int deleteByPrimaryKey(Long roleId);

	int insert(Role record);

	List<Role> selectByExample(RoleExample example);

	Role selectByPrimaryKey(Long roleId);

	int updateByPrimaryKeySelective(Role record);

}
