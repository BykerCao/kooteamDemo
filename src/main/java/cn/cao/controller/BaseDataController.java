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
import cn.cao.pojo.BaseDataExample.Criteria;
import cn.cao.service.BaseDataService;
import cn.cao.vo.MessageVO;

@Controller
@RequestMapping("/basedata")
public class BaseDataController {

	@Autowired
	private BaseDataService basedataService;
	
	@RequiresPermissions("basicData:basicDatapage")
	@RequestMapping("/basedataPage")
	public String adminPage() {
		return "basedataPage";// 管理员信息界面，再从jsp发送ajax到/list
	}
	
	@RequiresPermissions("basicData:list")
	@RequestMapping("/list")
	@ResponseBody
	public PageInfo<BaseData> list(@RequestParam(defaultValue = "1") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize, String searchVal) {

		PageHelper.startPage(pageNum, pageSize);

		BaseDataExample basedataExample = new BaseDataExample();
		// 当搜索框有值,	自关联，andNameLike、andExpressionLike里面的字段要改
		if (StringUtils.isNotBlank(searchVal)) { // org.apache.commons.lang3.StringUtils
			Criteria createCriteria = basedataExample.createCriteria();
			createCriteria.andBaseNameLike("%" + searchVal + "%");
		}

		List<BaseData> basedatas = basedataService.selectByExample(basedataExample);
		PageInfo<BaseData> pageInfo = new PageInfo<BaseData>(basedatas);

		return pageInfo;// 返回的是json类型数据
	}
	
	/**
	 * 删除数据
	 * @param basedataId
	 * @return
	 */
	@RequiresPermissions("basicData:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public MessageVO delete(Long baseId) {
		//根据传进来的id 查找当前基础名是否有
		BaseDataExample basedataExample = new BaseDataExample();
		Criteria createCriteria = basedataExample.createCriteria();
		createCriteria.andParentIdEqualTo(baseId);
		List<BaseData> selectByExample = basedataService.selectByExample(basedataExample);
		
		MessageVO mo = MessageVO.CreateMessageVO(0, "删除数据失败");
		if(selectByExample.size()==0) {
			int resoult = basedataService.deleteByPrimaryKey(baseId);
			if (resoult > 0) {
				mo = MessageVO.CreateMessageVO(1, "删除数据成功");
			}
		}else {
			mo = MessageVO.CreateMessageVO(0, "该基础名含有子基础名不能删除");
		}
		
		return mo;
	}
	
	
	@RequestMapping("/edit") // 点击添加、修改，弹出ifram页面
	public String toEdit(Model m,Long baseId) {
		
		if(baseId != null) {//此时是编辑修改，不是添加
			BaseData basedata = basedataService.selectByPrimaryKey(baseId);
			//System.out.println("beforeEditBaseData="+basedata);
			m.addAttribute("beforeEditBaseData", basedata);
		}
		
		//查询所有的父基础名共享给编辑页面，让其进行父基础名选择
		BaseDataExample basedataExample = new BaseDataExample();
		Criteria createCriteria = basedataExample.createCriteria();
		createCriteria.andParentIdIsNull();
		
		List<BaseData> basedatas = basedataService.selectByExample(basedataExample);
		m.addAttribute("basedatas", basedatas);
		return "basedata_edit";
	}

	/**
	 * 	添加基础名
	 * @param basedata
	 * @return
	 */
	@RequestMapping("/addBaseData")
	@ResponseBody
	public MessageVO addBaseData(BaseData basedata) {
		System.out.println("BaseData basedata添加");
		MessageVO mo = MessageVO.CreateMessageVO(0, "添加数据失败");
		int resoult = basedataService.insert(basedata);
		if (resoult == 1) {
			mo = MessageVO.CreateMessageVO(1, "添加数据成功");
		}
		return mo;
	}
	
	/**
	 * 验证基础名名称是否唯一
	 * @param name
	 * @return
	 */
	@RequestMapping("/checkBaseDataName")
	@ResponseBody
	public boolean checkBaseDataName(String name) {
		BaseDataExample basedataExample = new BaseDataExample();
		Criteria createCriteria = basedataExample.createCriteria();
		createCriteria.andBaseNameEqualTo(name);
		
		List<BaseData> selectByExample = basedataService.selectByExample(basedataExample);
		
		return selectByExample.size() == 0 ? true : false;
	}
	
	
	@RequestMapping("/afterEditBaseData")
	@ResponseBody
	public MessageVO afterEditBaseData(BaseData basedata) {
		System.out.println("BaseData basedata修改");
		MessageVO mo = MessageVO.CreateMessageVO(0, "修改数据失败");
		int resoult = basedataService.updateByPrimaryKeySelective(basedata);
		
		if (resoult == 1) {
			mo = MessageVO.CreateMessageVO(1, "修改数据成功");
		}
		
		return mo;
	}
	
	
}
