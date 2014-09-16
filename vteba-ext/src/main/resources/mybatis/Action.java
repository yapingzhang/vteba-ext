package ${packages}.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ${packages}.model.${className};
import ${packages}.service.spi.${className}Service;

import com.vteba.web.action.GenericAction;

/**
 * ${tableName}控制器
 * @author yinlei
 * @date ${currentDate}
 */
@Controller
@RequestMapping("/${smallClassName}")
public class ${className}Action extends GenericAction<${className}> {
	@Inject
	private ${className}Service ${smallClassName}ServiceImpl;
	
	/**
	 * 获得${tableName}List，Json格式。
	 * @param model 参数
	 * @return ${tableName}List
	 */
	@ResponseBody
	@RequestMapping("/list")
	public List<${className}> list(${className} model) {
		List<${className}> list = ${smallClassName}ServiceImpl.pagedForList(model);
		return list;
	}
	
	/**
     * 跳转到新增页面
     * @return 新增页面逻辑视图
     */
    @RequestMapping("/add")
    public String add() {
        return "${smallClassName}/add";
    }
    
    /**
     * 执行实际的新增操作
     * @param model 要新增的数据
     * @return 新增页面逻辑视图
     */
    @RequestMapping("/doAdd")
    public String doAdd(${className} model) {
        ${smallClassName}ServiceImpl.save(model);
        return "${smallClassName}/add";
    }
    
    /**
     * 查看${tableName}详情页。
     * @param model 查询参数，携带ID
     * @return ${tableName}详情页
     */
    @RequestMapping("/detail")
    public String detail(${className} model, Map<String, Object> maps) {
        model = ${smallClassName}ServiceImpl.get(model.getId());
        maps.put("model", model);//将model放入视图中，供页面视图使用
        return "${smallClassName}/detail";
    }
    
    /**
     * 跳转到编辑信息页面
     * @param model 查询参数，携带ID
     * @return 编辑页面
     */
    @RequestMapping("/edit")
    public String edit(${className} model, Map<String, Object> maps) {
        model = ${smallClassName}ServiceImpl.get(model.getId());
        maps.put("model", model);
        return "${smallClassName}/edit";
    }
    
    /**
     * 更新${tableName}信息
     * @param model 要更新的${tableName}信息，含有ID
     * @return 列表页面
     */
    @RequestMapping("/update")
    public String update(${className} model, Map<String, Object> maps) {
        ${smallClassName}ServiceImpl.updateById(model);
        maps.put("model", model);
        return "${smallClassName}/list";
    }
    
    /**
     * Ajax删除${tableName}信息
     * @param model 要删除的${tableName}，ID不为空
     */
    @ResponseBody
    @RequestMapping("/delete")
    public void delete(${className} model) {
        ${smallClassName}ServiceImpl.deleteById(model.getId());
        renderText("success");
    }
}
