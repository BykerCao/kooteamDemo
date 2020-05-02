package cn.cao.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.cao.pojo.User;
import cn.cao.pojo.UserExample;
import cn.cao.pojo.UserExample.Criteria;
import cn.cao.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class UserServiceTest {
	
	@Autowired
	private UserService userService;
	
	
	@Test
	public void testDeleteByPrimaryKeyString() {
		/*
		 * Integer[] a= {103,104}; int deleteByPrimarys =
		 * userService.deleteByPrimarys(a); System.out.println("删除="+deleteByPrimarys);
		 */
	}
	
	@Test
	public void testSelectByPrimaryKey() {
		
		int pageNum = 1;
		int pageSize = 10;
		PageHelper.startPage(pageNum, pageSize);
		
		String seachVal = "小";
		UserExample userExample = new UserExample();
		Criteria createCriteria = userExample.createCriteria();
		createCriteria.andRealnameLike("%"+seachVal+"%");
		
		List<User> users = userService.selectByExample(userExample);
		PageInfo<User> pageInfo = new PageInfo<User>(users);
		
	}

	

}
