package cn.cao.mapper;

import cn.cao.pojo.Permission;
import cn.cao.pojo.PermissionExample;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface PermissionMapper {
    int deleteByPrimaryKey(Long permissionId);

    int insert(Permission record);

    int insertSelective(Permission record);

    List<Permission> selectByExample(PermissionExample example);

    Permission selectByPrimaryKey(Long permissionId);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

	List<String> selectPressionsByIds(@Param("permissionIdsList") List<Long> permissionIdsList);
}