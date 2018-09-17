package com.Zackeus.CTI.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Zackeus.CTI.common.service.CrudService;
import com.Zackeus.CTI.modules.sys.dao.OfficeDao;
import com.Zackeus.CTI.modules.sys.entity.Office;

/**
 * 
 * @Title:OfficeService
 * @Description:TODO(机构Service)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月17日 上午11:11:23
 */
@Service("officeService")
public class OfficeService extends CrudService<OfficeDao, Office> {
	
	/**
	 * 
	 * @Title：getAllOffuceList
	 * @Description: TODO(获取全部组织架构列表列表)
	 * @see：
	 * @param office
	 * @return
	 */
	public List<Office> getAllOffuceList(Office office) {
		return dao.getAllOffuceList(office);
	}
	
}
