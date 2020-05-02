package cn.cao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cao.mapper.CustomerMapper;
import cn.cao.pojo.Customer;
import cn.cao.pojo.CustomerExample;
import cn.cao.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService{
	
	@Autowired
	private CustomerMapper CustomerMapper;
	
	@Override
	public int deleteByPrimaryKey(Long customerId) {
		return CustomerMapper.deleteByPrimaryKey(customerId);
	}
	
	@Override
	public int insert(Customer record) {
		return CustomerMapper.insert(record);
	}

	@Override
	public List<Customer> selectByExample(CustomerExample example) {
		return CustomerMapper.selectByExample(example);
	}

	@Override
	public Customer selectByPrimaryKey(Long customerId) {
		return CustomerMapper.selectByPrimaryKey(customerId);
	}

	@Override
	public int updateByPrimaryKeySelective(Customer record) {
		return CustomerMapper.updateByPrimaryKeySelective(record);
	}

}
