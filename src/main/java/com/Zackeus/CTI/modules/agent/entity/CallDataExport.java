package com.Zackeus.CTI.modules.agent.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.Zackeus.CTI.common.annotation.validator.CallDataExportValidator;
import com.Zackeus.CTI.common.service.valid.First;
import com.Zackeus.CTI.common.service.valid.Second;
import com.Zackeus.CTI.common.service.valid.Third;


/**
 * 
 * @Title:CallDataExport
 * @Description:TODO(通话记录导出参数类)
 * @Company:
 * @author zhou.zhang
 * @date 2019年1月1日 下午1:34:15
 */
@GroupSequence({CallDataExport.class, First.class, Second.class, Third.class})
@CallDataExportValidator(groups = { Third.class })
public class CallDataExport implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "{callDataExport.startDate.NotNull}", groups = {First.class})
	private Date startDate; 	// 导出起始时间
	
	@NotNull(message = "{callDataExport.endDate.NotNull}", groups = {First.class})
	private Date endDate; 		// 导出结束时间

	public CallDataExport() {
		super();
	}

	public CallDataExport(Date startDate, Date endDate) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
