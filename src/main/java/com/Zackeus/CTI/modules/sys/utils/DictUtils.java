package com.Zackeus.CTI.modules.sys.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Zackeus.CTI.common.utils.JsonMapper;
import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.modules.sys.entity.Dict;
import com.Zackeus.CTI.modules.sys.service.DictService;

/**
 * 
 * @Title:DictUtils
 * @Description:TODO(字典工具类)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年8月22日 下午3:13:18
 */
@Component
public class DictUtils {
	
	@Autowired
	private DictService dictService;
	
	public static DictUtils dictUtils;
	
	@PostConstruct
	public void init() {
		dictUtils = this;
	}
	
	/**
	 * 
	 * @Title：getDictList
	 * @Description: TODO(根据字典类型获取数据字典列表)
	 * @see：
	 * @param type
	 * @return
	 */
	public static List<Dict> getDictList(String type) {
		List<Dict> dictList = new ArrayList<>();
		for (Dict dict : dictUtils.dictService.findListByType(type)) {
			if (dictList != null){
				dictList.add(dict);
			}
		}
		return dictList;
	}
	
	/**
	 * 
	 * @Title：getDictLabel
	 * @Description: TODO(根据类型和键值获取字典标签)
	 * @see：
	 * @param value
	 * @param type
	 * @param defaultValue
	 * @return
	 */
	public static String getDictLabel(String type, String value, String defaultValue) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)) {
			String label = dictUtils.dictService.getDictLabel(new Dict(type, value));
			return StringUtils.isBlank(label) ? defaultValue : label;
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @Title：getDictListJson
	 * @Description: TODO(返回字典列表（JSON）)
	 * @see：
	 * @param type
	 * @return
	 */
	public static String getDictListJson(String type){
		return JsonMapper.toJsonString(getDictList(type));
	}

}
