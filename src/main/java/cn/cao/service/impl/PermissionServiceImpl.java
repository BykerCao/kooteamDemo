package cn.cao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cao.mapper.PermissionMapper;
import cn.cao.pojo.Permission;
import cn.cao.pojo.PermissionExample;
import cn.cao.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService{
	
	@Autowired
	private PermissionMapper PermissionMapper;
	
	@Override
	public int deleteByPrimaryKey(Long permissionId) {
		return PermissionMapper.deleteByPrimaryKey(permissionId);
	}

	@Override
	public int insert(Permission record) {
		return PermissionMapper.insert(record);
	}

	@Override
	public List<Permission> selectByExample(PermissionExample example) {
		return PermissionMapper.selectByExample(example);
	}

	@Override
	public Permission selectByPrimaryKey(Long permissionId) {
		return PermissionMapper.selectByPrimaryKey(permissionId);
	}

	@Override
	public int updateByPrimaryKeySelective(Permission record) {
		return PermissionMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<String> selectPressionsByIds(List<Long> permissionIdsList) {
		return PermissionMapper.selectPressionsByIds(permissionIdsList);
	}

}
