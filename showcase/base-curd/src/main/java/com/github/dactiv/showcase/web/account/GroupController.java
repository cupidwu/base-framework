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

import com.github.dactiv.common.utils.ServletUtils;
import com.github.dactiv.orm.core.Page;
import com.github.dactiv.orm.core.PageRequest;
import com.github.dactiv.orm.core.PageRequest.Sort;
import com.github.dactiv.orm.core.PropertyFilter;
import com.github.dactiv.orm.core.PropertyFilters;
import com.github.dactiv.showcase.common.SystemVariableUtils;
import com.github.dactiv.showcase.common.annotation.OperatingAudit;
import com.github.dactiv.showcase.common.enumeration.entity.GroupType;
import com.github.dactiv.showcase.common.enumeration.entity.State;
import com.github.dactiv.showcase.entity.account.Group;
import com.github.dactiv.showcase.service.account.AccountManager;

/**
 * 组管理Controller
 * 
 * @author maurice
 *
 */
@Controller
@OperatingAudit("组管理")
@RequestMapping("/account/group")
public class GroupController {
	
	@Autowired
	private AccountManager accountManager;
	
	/**
	 * 获取组列表,返回account/group/view.html页面
	 * 
	 * @param pageRequest 分页实体信息
	 * @param request HttpServlet请求
	 * 
	 * @return {@link Page}
	 */
	@RequestMapping("view")
	public Page<Group> view(PageRequest pageRequest,HttpServletRequest request) {
		
		List<PropertyFilter> filters = PropertyFilters.build(request,true);
		
		request.setAttribute("states", SystemVariableUtils.getVariables(State.class,3));
		request.setAttribute("groupsList", accountManager.getGroup(GroupType.RoleGorup));
		
		if (!pageRequest.isOrderBySetted()) {
			pageRequest.setOrderBy("id");
			pageRequest.setOrderDir(Sort.DESC);
		}
		
		filters.add(PropertyFilters.build("EQS_type", GroupType.RoleGorup.getValue()));
		
		return accountManager.searchGroupPage(pageRequest, filters);
	}
	
	/**
	 * 
	 * 保存或更新组,保存成功后重定向到:account/group/view
	 * 
	 * @param entity 实体信息
	 * @param request HttpServletRequest
	 * @param redirectAttributes spring mvc 重定向属性
	 * 
	 * @return String
	 */
	@RequestMapping("save")
	@OperatingAudit(function="保存或更新组")
	public String save(@ModelAttribute("entity") Group entity,
					   HttpServletRequest request,
					   RedirectAttributes redirectAttributes) {
		
		String parentId = request.getParameter("parentId");
		
		if (StringUtils.isEmpty(parentId)) {
			entity.setParent(null);
		} else {
			entity.setParent(accountManager.getGroup(parentId));
		}
		
		List<String> resourceIds = ServletUtils.getParameterValues(request, "resourceId");
		
		entity.setResourcesList(accountManager.getResources(resourceIds));
		
		accountManager.saveGroup(entity);
		redirectAttributes.addFlashAttribute("success", "保存成功");
		
		return "redirect:/account/group/view";
	}
	
	/**
	 * 
	 * 读取组信息,返回account/group/read.html页面
	 * 
	 * @param id 主键id
	 * @param model Spring mvc的Model接口，主要是将model的属性返回到页面中
	 * 
	 * @return {@link Group}
	 */
	@RequestMapping("read")
	public void read(String id, Model model) {
		
		model.addAttribute("resourcesList", accountManager.getResources());
		model.addAttribute("states", SystemVariableUtils.getVariables(State.class,3));
		model.addAttribute("groupsList", accountManager.getGroup(GroupType.RoleGorup, id));
	}
	
	/**
	 * 通过主键id集合删除组,删除成功后重定向到:account/group/view
	 * 
	 * @param ids 主键id集合
	 * @param redirectAttributes spring mvc 重定向属性
	 * 
	 * @return String
	 */
	@RequestMapping("delete")
	@OperatingAudit(function="删除组")
	public String delete(@RequestParam("ids")List<String> ids,RedirectAttributes redirectAttributes) {
		accountManager.deleteGroups(ids);
		redirectAttributes.addFlashAttribute("success", "删除" + ids.size() + "条信息成功");
		return "redirect:/account/group/view";
	}
	
	/**
	 * 绑定实体数据，如果存在id时获取后从数据库获取记录，进入到相对的C后在将数据库获取的记录填充到相应的参数中
	 * 
	 * @param id 主键ID
	 * 
	 */
	@ModelAttribute("entity")
	public Group bindingModel(String id) {

		Group group = new Group();

		if (StringUtils.isNotEmpty(id)) {
			group = accountManager.getGroup(id);
		}

		return group;
	}
	
}
