package cn.cao.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.cao.pojo.BaseData;
import cn.cao.pojo.Customer;
import cn.cao.pojo.CustomerExample;
import cn.cao.pojo.CustomerExample.Criteria;
import cn.cao.pojo.User;
import cn.cao.pojo.UserExample;
import cn.cao.service.BaseDataService;
import cn.cao.service.CustomerService;
import cn.cao.service.UserService;
import cn.cao.vo.MessageVO;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BaseDataService baseDataService;

	@RequestMapping("/customerPage")
	public String adminPage() {
		return "customerPage";// 管理员信息界面，再从jsp发送ajax到/list
	}

	@RequestMapping("/list")
	@ResponseBody
	public PageInfo<Customer> list(@RequestParam(defaultValue = "1") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize, String searchVal) {

		PageHelper.startPage(pageNum, pageSize);
		
		CustomerExample customerExample = new CustomerExample();
		Criteria createCriteria = customerExample.createCriteria();
		
		//获取shiro中的主体对象,并获得令牌
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		//如果当前登录用户为业务员，则只能看自己的客户
		if("业务员".equals(user.getRolename())) {
			createCriteria.andUserIdEqualTo(user.getUserId());
		}
		
		// 当搜索框有值
		if (StringUtils.isNotBlank(searchVal)) { // org.apache.commons.lang3.StringUtils
			createCriteria.andCustomerNameLike("%" + searchVal + "%");
		}

		List<Customer> customers = customerService.selectByExample(customerExample);
		PageInfo<Customer> pageInfo = new PageInfo<Customer>(customers);

		return pageInfo;// 返回的是json类型数据
	}
	
	/**
	 * 删除数据
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public MessageVO delete(Long customerId) {
		MessageVO mo = MessageVO.CreateMessageVO(0, "删除数据失败");
		
		int resoult = customerService.deleteByPrimaryKey(customerId);
		if (resoult > 0) {
			mo = MessageVO.CreateMessageVO(1, "删除数据成功");
		}
		
		
		return mo;
	}
	
	
	@RequestMapping("/edit") // 点击添加、修改，弹出ifram页面
	public String toEdit(Model m,Long customerId) {
		
		if(customerId != null) {//此时是编辑修改，不是添加
			Customer customer = customerService.selectByPrimaryKey(customerId);
			m.addAttribute("beforeEditCustomer", customer);
		}
		//查询所有的业务员，（先查找所有管理员，在设置条件）  返回给添加选
		UserExample example = new UserExample();
		cn.cao.pojo.UserExample.Criteria createCriteria = example.createCriteria();
		createCriteria.andRolenameEqualTo("业务员");
		List<User> users = userService.selectByExample(example);
		m.addAttribute("users", users);
		//查询所有区间
		 List<BaseData> baseDataList=baseDataService.selectByBaseDataParentName("区间管理");
		m.addAttribute("baseDataList", baseDataList);
		 
		 
		return "customer_edit";
	}

	/**
	 * 	添加客户
	 * @param customer
	 * @return
	 */
	@RequestMapping("/addCustomer")
	@ResponseBody
	public MessageVO addCustomer(Customer customer) {
		MessageVO mo = MessageVO.CreateMessageVO(0, "添加数据失败");
		int resoult = customerService.insert(customer);
		if (resoult == 1) {
			mo = MessageVO.CreateMessageVO(1, "添加数据成功");
		}
		return mo;
	}
	
	/**
	 * 验证客户名称是否唯一
	 * @param name
	 * @return
	 */
	@RequestMapping("/checkCustomerName")
	@ResponseBody
	public boolean checkCustomerName(String name) {
		CustomerExample customerExample = new CustomerExample();
		Criteria createCriteria = customerExample.createCriteria();
		createCriteria.andCustomerNameEqualTo(name);
		
		List<Customer> selectByExample = customerService.selectByExample(customerExample);
		
		return selectByExample.size() == 0 ? true : false;
	}
	
	
	@RequestMapping("/afterEditCustomer")
	@ResponseBody
	public MessageVO afterEditCustomer(Customer customer) {
		MessageVO mo = MessageVO.CreateMessageVO(0, "修改数据失败");
		int resoult = customerService.updateByPrimaryKeySelective(customer);
		
		if (resoult == 1) {
			mo = MessageVO.CreateMessageVO(1, "修改数据成功");
		}
		
		return mo;
	}
	
	
}
