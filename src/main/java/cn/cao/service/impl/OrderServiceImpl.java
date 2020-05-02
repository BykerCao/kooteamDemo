package cn.cao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cao.mapper.OrderDetailMapper;
import cn.cao.mapper.OrderMapper;
import cn.cao.pojo.Order;
import cn.cao.pojo.OrderDetail;
import cn.cao.pojo.OrderDetailExample;
import cn.cao.pojo.OrderDetailExample.Criteria;
import cn.cao.pojo.OrderExample;
import cn.cao.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderMapper OrderMapper;
	
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	
	@Override
	public int deleteByPrimaryKey(Long orderId) {
		return OrderMapper.deleteByPrimaryKey(orderId);
	}

	@Override
	public int insert(Order record) {
		return OrderMapper.insert(record);
	}

	@Override
	public List<Order> selectByExample(OrderExample example) {
		return OrderMapper.selectByExample(example);
	}

	@Override
	public Order selectByPrimaryKey(Long orderId) {
		return OrderMapper.selectByPrimaryKey(orderId);
	}

	@Override
	public int updateByPrimaryKeySelective(Order record) {
		return OrderMapper.updateByPrimaryKeySelective(record);
	}
	
	/**
	 * 点击+ 展开子表信息
	 * 通过订单表order 的订单id 查找属于它的所有详情信息order_detail。
	 */
	@Override
	public List<OrderDetail> selectDetailsByOrderId(Long orderId) {
		OrderDetailExample orderDetailExample = new OrderDetailExample();
		Criteria createCriteria = orderDetailExample.createCriteria();
		createCriteria.andOrderIdEqualTo(orderId);
		
		List<OrderDetail> orderDetails = orderDetailMapper.selectByExample(orderDetailExample);
		
		return orderDetails;
	}

}
