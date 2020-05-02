package cn.cao.mapper;

import cn.cao.pojo.User;
import cn.cao.pojo.UserExample;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Long userId);
    int deleteByPrimarys(@Param("userIds")Long[] userIds);
    
    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}