package com.vteba.${packages}.action;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vteba.${packages}.model.${className};
import com.vteba.${packages}.service.spi.${className}Service;
import com.vteba.tm.generic.Page;
import com.vteba.web.action.BaseAction;

/**
 * ${tableName}控制器
 * @author yinlei
 * @date ${currentDate}
 */
@Controller
@RequestMapping("/${smallClassName}")
public class ${className}Action extends BaseAction {
	@Inject
	private ${className}Service ${smallClassName}ServiceImpl;
	
	/**
	 * 获得${tableName}List
	 * @param model 参数
	 * @return ${tableName}List JSON字符串
	 * @author yinlei
	 * @date ${currentDate}
	 */
	@ResponseBody
	@RequestMapping("/list")
	public List<${className}> list(${className} model) {
		String hql = "select g from ${className} g ";
		Page<${className}> page = new Page<${className}>();
		page.setPageSize(10);
		List<${className}> list = ${smallClassName}ServiceImpl.pagedQueryByHql(page, hql);
		return list;
	}
}
