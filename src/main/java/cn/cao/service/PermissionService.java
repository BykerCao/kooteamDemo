package cn.cao.service;

import java.util.List;

import cn.cao.pojo.Permission;
import cn.cao.pojo.PermissionExample;


public interface PermissionService {
	int deleteByPrimaryKey(Long permissionId);

	int insert(Permission record);

	List<Permission> selectByExample(PermissionExample example);

	Permission selectByPrimaryKey(Long permissionId);

	int updateByPrimaryKeySelective(Permission record);

	List<String> selectPressionsByIds(List<Long> permissionIdsList);

}
