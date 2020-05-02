package cn.cao.service;

import java.util.List;

import cn.cao.pojo.Order;
import cn.cao.pojo.OrderDetail;
import cn.cao.pojo.OrderExample;


public interface OrderService {
	int deleteByPrimaryKey(Long orderId);

	int insert(Order record);

	List<Order> selectByExample(OrderExample example);

	Order selectByPrimaryKey(Long orderId);

	int updateByPrimaryKeySelective(Order record);

	List<OrderDetail> selectDetailsByOrderId(Long orderId);

}
