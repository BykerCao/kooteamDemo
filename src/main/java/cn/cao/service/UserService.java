package cn.cao.service;

import java.util.List;

import cn.cao.pojo.User;
import cn.cao.pojo.UserExample;


public interface UserService {
	int deleteByPrimaryKey(Long userId);
	int deleteByPrimarys(Long []userIds);

	int insert(User record);

	List<User> selectByExample(UserExample example);

	User selectByPrimaryKey(Long userId);

	int updateByPrimaryKeySelective(User record);

}
