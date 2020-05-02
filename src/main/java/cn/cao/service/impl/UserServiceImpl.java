package cn.cao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cao.mapper.UserMapper;
import cn.cao.pojo.User;
import cn.cao.pojo.UserExample;
import cn.cao.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserMapper UserMapper;
	
	@Override
	public int deleteByPrimaryKey(Long userId) {
		return UserMapper.deleteByPrimaryKey(userId);
	}
	
	@Override
	public int deleteByPrimarys(Long[] userIds) {
		return UserMapper.deleteByPrimarys(userIds);
	}
	
	@Override
	public int insert(User record) {
		return UserMapper.insert(record);
	}

	@Override
	public List<User> selectByExample(UserExample example) {
		return UserMapper.selectByExample(example);
	}

	@Override
	public User selectByPrimaryKey(Long userId) {
		return UserMapper.selectByPrimaryKey(userId);
	}

	@Override
	public int updateByPrimaryKeySelective(User record) {
		return UserMapper.updateByPrimaryKeySelective(record);
	}

}
