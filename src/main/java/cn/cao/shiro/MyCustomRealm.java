package cn.cao.shiro;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import cn.cao.pojo.User;
import cn.cao.pojo.UserExample;
import cn.cao.pojo.UserExample.Criteria;
import cn.cao.service.PermissionService;
import cn.cao.service.UserService;

public class MyCustomRealm extends AuthorizingRealm {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PermissionService permissionService;
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// 验证身份
		System.out.println("验证身份====");
		//1.获取Token中的身份
		String principal = (String)token.getPrincipal();
		System.out.println("获取身份："+principal);
		//2, 调用UserService中的根据账号查询用户信息的方法
		UserExample userExample = new UserExample();
		
		Criteria createCriteria = userExample.createCriteria();
		createCriteria.andUsernameEqualTo(principal);
		List<User> users = null;
		try {
			users = userService.selectByExample(userExample);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		if(users.size()==0) {//数据库没有这个账号
			return null;
		}
		User user = users.get(0);
		//获取user的密码
		String password = user.getPassword();
		//获取盐
		String salt = user.getSalt();
		//创建认证信息对象SimpleAuthenticationInfo
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, password, ByteSource.Util.bytes(salt), this.getName());	
		//SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, password, this.getName());
		return authenticationInfo;
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		 //1.获取当前认证通过的身份信息对象 ，就是 user对象
		User user = (User) principals.getPrimaryPrincipal();
		 //  user对象包含当前身份的所有信息 
		 //  	角色id，角色对应的权限的id集合 ： 1,20,18,15,30
		 // 2,将 当前用户对应的角色所有的权限 的id 使用逗号切割成数组，变成一个个权限的id值
		String permissionIds = user.getPermissionIds();
		System.out.println("user:"+permissionIds);
		String[] permissionIdsArr = permissionIds.split(",");
		
		List<Long> permissionIdsList = new ArrayList<>();	//创建集合，将字符串的数组的值转换成Long类型
		for (String permissionId : permissionIdsArr) {
			permissionIdsList.add(Long.valueOf(permissionId));
		}
		 // 3，根据一个个权限的id去权限表中t_permission 查询出对应的权限id的权限表达式
		List<String>permissions = permissionService.selectPressionsByIds(permissionIdsList);
		 //  List<String> permissions = permissionService.selectExpressionsByIds(permissionIds)
		 // 	查询出一个权限表达式集合
		 // 		user:insert
		 // 		user:update
		 // 		user:delete
		 // 		teacher:list
		 // 		student:studentPage
		 // 4,创建 授权信息对象AuthorizationInfo
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo(); 
		 // 5,将第三步查询的所有权限表达式添加的授权信息对象中AuthorizationInfo
		authorizationInfo.addStringPermissions(permissions);
		
		return authorizationInfo;
	}

}
