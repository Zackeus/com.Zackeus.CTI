package com.Zackeus.CTI.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.Zackeus.CTI.common.web.BaseController;
import com.Zackeus.CTI.modules.sys.entity.Office;
import com.Zackeus.CTI.modules.sys.service.OfficeService;

/**
 * 
 * @Title:OfficeController
 * @Description:TODO(机构Controller)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月17日 下午1:29:16
 */
@Controller
@RequestMapping(value = "/sys/office")
public class OfficeController extends BaseController {
	
	@Autowired
	private OfficeService officeService;
	
	/**
	 * 
	 * @Title：officeMange
	 * @Description: TODO(组织架构管理)
	 * @see：
	 * @param request
	 * @param response
	 */
	@RequiresRoles(value = { "admin" })
	@RequestMapping(value = "/mange", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void officeMange(HttpServletRequest request, HttpServletResponse response) {
		renderString(response, officeService.getAllOffuceList(new Office()));
	}

}
