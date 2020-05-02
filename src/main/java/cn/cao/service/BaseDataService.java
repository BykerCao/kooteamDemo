package cn.cao.service;

import java.util.List;

import cn.cao.pojo.BaseData;
import cn.cao.pojo.BaseDataExample;


public interface BaseDataService {
	int deleteByPrimaryKey(Long baseId);

	int insert(BaseData record);

	List<BaseData> selectByExample(BaseDataExample example);

	BaseData selectByPrimaryKey(Long baseId);

	int updateByPrimaryKeySelective(BaseData record);

	List<BaseData> selectByBaseDataParentName(String baseDateParentName);

}
