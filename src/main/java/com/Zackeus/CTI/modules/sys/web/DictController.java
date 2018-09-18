package com.Zackeus.CTI.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.Zackeus.CTI.common.annotation.argumentResolver.PageRequestBody;
import com.Zackeus.CTI.common.entity.AjaxResult;
import com.Zackeus.CTI.common.entity.Page;
import com.Zackeus.CTI.common.utils.HttpStatus;
import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.common.web.BaseController;
import com.Zackeus.CTI.modules.sys.entity.Dict;
import com.Zackeus.CTI.modules.sys.service.DictService;

/**
 * 
 * @Title:DictController
 * @Description:TODO(字典Controller)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月5日 下午1:49:04
 */
@Controller
@RequestMapping("/sys/dict")
public class DictController extends BaseController {
	
	@Autowired
	private DictService dictService;
	
	/**
	 * 
	 * @Title：dictManage
	 * @Description: TODO(字典管理)
	 * @see：
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresRoles(value = { "admin" })
	@RequestMapping(value = "/manage", method = RequestMethod.GET)
	public String dictManage(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("typeList", dictService.findTypeList());
		return "modules/sys/dict/dictManage";
	}
	
	/**
	 * 
	 * @Title：dictList
	 * @Description: TODO(字典分页条件查询)
	 * @see：
	 * @param request
	 * @param response
	 */
	@RequiresRoles(value = { "admin" })
	@RequestMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE, 
		produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void dictList(@PageRequestBody Dict dict, HttpServletRequest request, HttpServletResponse response) {
		renderString(response, dictService.findPage(new Page<>(request), dict));
	}
	
	/**
	 * 
	 * @Title：addDict
	 * @Description: TODO(添加字典页面)
	 * @see：
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresRoles(value = { "admin" })
	@RequestMapping(value = {"/add", "/add/{id}"}, method = RequestMethod.GET)
	public String addDictPage(@PathVariable(value = "id", required = false) String id, 
			HttpServletRequest request, HttpServletResponse response, Model model) {
		if (StringUtils.isBlank(id)) {
			model.addAttribute("dict", new Dict());
		} else {
			Dict dict = dictService.get(id);
			dict.setSort(ObjectUtils.isEmpty(dict.getSort()) ? 10 : dict.getSort() + 10);
			model.addAttribute("dict", dict);
		}
		return "modules/sys/dict/addDict";
	}
	
	/**
	 * 
	 * @Title：addDict
	 * @Description: TODO(添加字典)
	 * @see：
	 * @param dict
	 * @param request
	 * @param response
	 */
	@RequiresRoles(value = { "admin" })
	@RequestMapping(value = {"/add"}, consumes = MediaType.APPLICATION_JSON_VALUE, 
		produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void addDict(@RequestBody Dict dict, HttpServletRequest request, HttpServletResponse response) {
		dictService.save(dict);
		renderString(response, new AjaxResult(HttpStatus.SC_SUCCESS, "添加字典成功"));
	}
	
	/**
	 * 
	 * @Title：editDictPage
	 * @Description: TODO(编辑字典页面)
	 * @see：
	 * @param id
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresRoles(value = { "admin" })
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editDictPage(@PathVariable("id") String id, HttpServletRequest request, 
			HttpServletResponse response, Model model) {
		model.addAttribute("dict", dictService.get(id));
		return "modules/sys/dict/editDict";
	}
	
	/**
	 * 
	 * @Title：editDict
	 * @Description: TODO(更新字典)
	 * @see：
	 * @param menu
	 * @param request
	 * @param response
	 */
	@RequiresRoles(value = { "admin" })
	@RequestMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void editDict(@RequestBody Dict dict, HttpServletRequest request, HttpServletResponse response) {
		dictService.save(dict);
		renderString(response, new AjaxResult(HttpStatus.SC_SUCCESS, "更新字典成功"));
	}
	
	/**
	 * 
	 * @Title：delDict
	 * @Description: TODO(删除字典)
	 * @see：
	 * @param dict
	 * @param request
	 * @param response
	 */
	@RequiresRoles(value = { "admin" })
	@RequestMapping(value = "/del", consumes = MediaType.APPLICATION_JSON_VALUE, 
		produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void delDict(@RequestBody Dict dict, HttpServletRequest request, HttpServletResponse response) {
		dictService.delete(dict);
		renderString(response, new AjaxResult(HttpStatus.SC_SUCCESS, "删除字典成功"));
	}

}
