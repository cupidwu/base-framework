package com.github.dactiv.showcase.web.account;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.dactiv.orm.core.Page;
import com.github.dactiv.orm.core.PageRequest;
import com.github.dactiv.orm.core.PageRequest.Sort;
import com.github.dactiv.orm.core.PropertyFilter;
import com.github.dactiv.orm.core.PropertyFilters;
import com.github.dactiv.showcase.common.SystemVariableUtils;
import com.github.dactiv.showcase.common.annotation.OperatingAudit;
import com.github.dactiv.showcase.common.enumeration.entity.ResourceType;
import com.github.dactiv.showcase.entity.account.Resource;
import com.github.dactiv.showcase.service.account.AccountManager;

/**
 * 资源管理Controller
 * 
 * @author maurice
 *
 */
@Controller
@OperatingAudit("资源管理")
@RequestMapping("/account/resource")
public class ResourceController {

	@Autowired
	private AccountManager accountManager;
	
	/**
	 * 获取资源列表,返回account/resource/view.html页面
	 * 
	 * @param pageRequest 分页实体信息
	 * @param request HttpServlet请求
	 * 
	 * @return {@link Page}
	 */
	@RequestMapping("view")
	public Page<Resource> view(PageRequest pageRequest,HttpServletRequest request) {
		
		List<PropertyFilter> filters = PropertyFilters.build(request,true);
		
		request.setAttribute("resourceType", SystemVariableUtils.getVariables(ResourceType.class));
		request.setAttribute("resourcesList", accountManager.getResources());
		
		if (!pageRequest.isOrderBySetted()) {
			pageRequest.setOrderBy("sort");
			pageRequest.setOrderDir(Sort.DESC);
		}
		
		return accountManager.searchResourcePage(pageRequest, filters);
	}
	
	/**
	 * 
	 * 保存或更新资源,保存成功后重定向到:account/resource/view
	 * 
	 * @param entity 实体信息
	 * @param parentId 所对应的父类id
	 * @param redirectAttributes spring mvc 重定向属性
	 * 
	 * @return String
	 */
	@RequestMapping("save")
	@OperatingAudit(function="保存或更新资源")
	public String save(@ModelAttribute("entity") Resource entity,
					   String parentId,
					   RedirectAttributes redirectAttributes) {
		
		if (StringUtils.isEmpty(parentId)) {
			entity.setParent(null);
		} else {
			Resource parent = accountManager.getResource(parentId);
			entity.setParent(parent);
		}
		
		accountManager.saveResource(entity);
		redirectAttributes.addFlashAttribute("success", "保存成功");
		return "redirect:/account/resource/view";
	}
	
	/**
	 * 
	 * 读取资源信息,返回account/resource/read.html页面
	 * 
	 * @param id 主键id
	 * @param model Spring mvc的Model接口，主要是将model的属性返回到页面中
	 * 
	 * @return {@link Resource}
	 */
	@RequestMapping("read")
	public void read(String id, Model model) {
		model.addAttribute("resourceType", SystemVariableUtils.getVariables(ResourceType.class));
		model.addAttribute("resourcesList", accountManager.getResources(id));
	}
	
	/**
	 * 通过主键id集合删除资源,删除成功后重定向到:account/resource/view
	 * 
	 * @param ids 主键id集合
	 * @param redirectAttributes spring mvc 重定向属性
	 * 
	 * @return String
	 */
	@RequestMapping("delete")
	@OperatingAudit(function="删除资源")
	public String delete(@RequestParam("ids")List<String> ids,RedirectAttributes redirectAttributes) {
		accountManager.deleteResources(ids);
		redirectAttributes.addFlashAttribute("success", "删除" + ids.size() + "条信息成功");
		return "redirect:/account/resource/view";
	}
	
	/**
	 * 绑定实体数据，如果存在id时获取后从数据库获取记录，进入到相对的C后在将数据库获取的记录填充到相应的参数中
	 * 
	 * @param id 主键ID
	 * 
	 */
	@ModelAttribute("entity")
	public Resource bindingModel(String id) {

		Resource resource = new Resource();
		if (StringUtils.isNotEmpty(id)) {
			resource = accountManager.getResource(id);
		} else {
			resource.setSort(accountManager.getResourceCount() + 1);
		}

		return resource;
	}
	
}
