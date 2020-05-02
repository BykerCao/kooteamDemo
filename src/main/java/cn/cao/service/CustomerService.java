package cn.cao.service;

import java.util.List;

import cn.cao.pojo.Customer;
import cn.cao.pojo.CustomerExample;


public interface CustomerService {
	int deleteByPrimaryKey(Long customerId);

	int insert(Customer record);

	List<Customer> selectByExample(CustomerExample example);

	Customer selectByPrimaryKey(Long customerId);

	int updateByPrimaryKeySelective(Customer record);

}
