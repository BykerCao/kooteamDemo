package cn.cao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cao.mapper.BaseDataMapper;
import cn.cao.pojo.BaseData;
import cn.cao.pojo.BaseDataExample;
import cn.cao.service.BaseDataService;

@Service
public class BaseDataServiceImpl implements BaseDataService{
	
	@Autowired
	private BaseDataMapper BaseDataMapper;
	
	@Override
	public int deleteByPrimaryKey(Long baseDataId) {
		return BaseDataMapper.deleteByPrimaryKey(baseDataId);
	}

	@Override
	public int insert(BaseData record) {
		return BaseDataMapper.insert(record);
	}

	@Override
	public List<BaseData> selectByExample(BaseDataExample example) {
		return BaseDataMapper.selectByExample(example);
	}

	@Override
	public BaseData selectByPrimaryKey(Long baseDataId) {
		return BaseDataMapper.selectByPrimaryKey(baseDataId);
	}

	@Override
	public int updateByPrimaryKeySelective(BaseData record) {
		return BaseDataMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<BaseData> selectByBaseDataParentName(String baseDateParentName) {
		return BaseDataMapper.selectByBaseDataParentName(baseDateParentName);
	}

}
