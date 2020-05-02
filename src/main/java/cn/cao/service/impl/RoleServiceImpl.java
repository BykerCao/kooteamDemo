package cn.cao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cao.mapper.RoleMapper;
import cn.cao.pojo.Role;
import cn.cao.pojo.RoleExample;
import cn.cao.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{
	
	@Autowired
	private RoleMapper RoleMapper;
	
	@Override
	public int deleteByPrimaryKey(Long roleId) {
		return RoleMapper.deleteByPrimaryKey(roleId);
	}

	@Override
	public int insert(Role record) {
		return RoleMapper.insert(record);
	}

	@Override
	public List<Role> selectByExample(RoleExample example) {
		return RoleMapper.selectByExample(example);
	}

	@Override
	public Role selectByPrimaryKey(Long roleId) {
		return RoleMapper.selectByPrimaryKey(roleId);
	}

	@Override
	public int updateByPrimaryKeySelective(Role record) {
		return RoleMapper.updateByPrimaryKeySelective(record);
	}

}
