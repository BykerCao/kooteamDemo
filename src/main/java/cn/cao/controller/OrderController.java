package cn.cao.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.cao.pojo.BaseData;
import cn.cao.pojo.BaseDataExample;
import cn.cao.pojo.Customer;
import cn.cao.pojo.CustomerExample;
import cn.cao.pojo.Order;
import cn.cao.pojo.OrderDetail;
import cn.cao.pojo.OrderExample;
import cn.cao.pojo.OrderExample.Criteria;
import cn.cao.pojo.User;
import cn.cao.pojo.UserExample;
import cn.cao.service.BaseDataService;
import cn.cao.service.CustomerService;
import cn.cao.service.OrderService;
import cn.cao.service.UserService;
import cn.cao.utils.Constant;
import cn.cao.vo.MessageVO;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BaseDataService baseDataService;
	
	@Autowired 
	private CustomerService customerService;

	@RequestMapping("/orderPage")
	public String adminPage() {
		return "orderPage";// 管理员信息界面，再从jsp发送ajax到/list
	}

	@RequestMapping("/list")
	@ResponseBody
	public PageInfo<Order> list(@RequestParam(defaultValue = "1") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize, String searchVal) {

		PageHelper.startPage(pageNum, pageSize);
		
		OrderExample orderExample = new OrderExample();
		// 当搜索框有值
		if (StringUtils.isNotBlank(searchVal)) { // org.apache.commons.lang3.StringUtils
			Criteria createCriteria = orderExample.createCriteria();
			createCriteria.andShippingNameLike("%" + searchVal + "%");
			
			Criteria createCriteria2 = orderExample.createCriteria();
			createCriteria2.andTakeNameLike("%" + searchVal + "%");
			
			orderExample.or(createCriteria2);
		}
		

		List<Order> orders = orderService.selectByExample(orderExample);
		PageInfo<Order> pageInfo = new PageInfo<Order>(orders);

		return pageInfo;// 返回的是json类型数据
	}
	
	/**
	 * 删除数据
	 * 
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public MessageVO delete(Long orderId) {
		MessageVO mo = MessageVO.CreateMessageVO(0, "删除数据失败");
		
		int resoult = orderService.deleteByPrimaryKey(orderId);
		if (resoult > 0) {
			mo = MessageVO.CreateMessageVO(1, "删除数据成功");
		}
		
		
		return mo;
	}
	
	
	@RequestMapping("/edit") // 点击添加、修改，弹出ifram页面
	public String toEdit(Model m,Long orderId) {
		
		if(orderId != null) {//此时是编辑修改，不是添加
			Order order = orderService.selectByPrimaryKey(orderId);
			m.addAttribute("beforeEditOrder", order);
		}
		
		//1，所有业务员
		UserExample example = new UserExample();
		cn.cao.pojo.UserExample.Criteria createCriteria = example.createCriteria();
		createCriteria.andRolenameEqualTo(Constant.ROLE_SALESMAN);//查出所有业务员
		List<User> users = userService.selectByExample(example);
		m.addAttribute("users", users);
		//2，所有客户
		CustomerExample customerExample = new CustomerExample();
		List<Customer> customers = customerService.selectByExample(customerExample);
		m.addAttribute("customers", customers);
		//3，区间管理
		List<BaseData> intervals = baseDataService.selectByBaseDataParentName(Constant.BASIC_COMMON_INTERVAL);
		m.addAttribute("intervals", intervals);
		//4，付款方式
		List<BaseData> payments = baseDataService.selectByBaseDataParentName(Constant.BASIC_PAYMENT_TYPE);
		m.addAttribute("payments", payments);
		//5，货运方式
		List<BaseData> freights = baseDataService.selectByBaseDataParentName(Constant.BASIC_FREIGHT_TYPE);
		m.addAttribute("freights", freights);
		//6，取件方式
		List<BaseData> fetchTypes = baseDataService.selectByBaseDataParentName(Constant.BASIC_FETCH_TYPE);
		m.addAttribute("fetchTypes", fetchTypes);
		//7，单位
		List<BaseData> units = baseDataService.selectByBaseDataParentName(Constant.BASIC_UNIT); 
		m.addAttribute("units", units);
		
		return "order_edit";
	}

	/**
	 * 	添加订单
	 * @param order
	 * @return
	 */
	@RequestMapping("/addOrder")
	@ResponseBody
	public MessageVO addOrder(Order order) {
		MessageVO mo = MessageVO.CreateMessageVO(0, "添加数据失败");
		int resoult = orderService.insert(order);
		if (resoult == 1) {
			mo = MessageVO.CreateMessageVO(1, "添加数据成功");
		}
		return mo;
	}
	
	
	@RequestMapping("/afterEditOrder")
	@ResponseBody
	public MessageVO afterEditOrder(Order order) {
		MessageVO mo = MessageVO.CreateMessageVO(0, "修改数据失败");
		int resoult = orderService.updateByPrimaryKeySelective(order);
		
		if (resoult == 1) {
			mo = MessageVO.CreateMessageVO(1, "修改数据成功");
		}
		return mo;
	}
	
	
	@RequestMapping("detail")
	@ResponseBody
	public List<OrderDetail> detail(Long orderId){
		List<OrderDetail> orderDetails = orderService.selectDetailsByOrderId(orderId);
		return orderDetails;
	}
	
}
