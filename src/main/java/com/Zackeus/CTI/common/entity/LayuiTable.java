package com.Zackeus.CTI.common.entity;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 
 * @Title:LayuiTable
 * @Description:TODO(前端table渲染实体)
 * @Company: 
 * @author zhou.zhang
 * @param <T>
 * @date 2018年8月20日 下午4:57:25
 */
public class LayuiTable<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer code; 			// 数据状态的字段名称 陈功默认为0
	private String msg; 			// 状态信息的字段名称
	private Integer count;			// 总条数
	private List<T> data;			// 分页实体数据
	
	public LayuiTable() {
		super();
	}
	
	public LayuiTable(List<T> data) {
		this(0, "", data.size(), data);
	}
	
	public LayuiTable(Integer code, String msg, Integer count, List<T> data) {
		super();
		this.code = code;
		this.msg = msg;
		this.count = count;
		this.data = data;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
	
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
