package com.Zackeus.CTI.modules.agent.entity;

import java.util.Date;

import javax.validation.GroupSequence;

import org.hibernate.validator.constraints.NotBlank;

import com.Zackeus.CTI.common.annotation.validator.AgentRecordValidator;
import com.Zackeus.CTI.common.entity.DataEntity;
import com.Zackeus.CTI.common.service.valid.First;
import com.Zackeus.CTI.common.service.valid.Second;
import com.Zackeus.CTI.common.service.valid.Third;

/**
 * 
 * @Title:AgentRecord
 * @Description:TODO(坐席录音)
 * @Company:
 * @author zhou.zhang
 * @date 2018年9月27日 下午5:16:34
 */
@GroupSequence({AgentRecord.class, First.class, Second.class, Third.class})
@AgentRecordValidator(groups = { Third.class })
public class AgentRecord extends DataEntity<AgentRecord> {

	private static final long serialVersionUID = 1L;

	private String recordID; 		// 录音流水号
	private String callid; 			// 呼叫流水号
	private String recordTitle; 	// 录音标题
	private String fileName; 		// 录音文件地址
	private String locationId; 		// 录音对应的中心节点ID
	private Date startDate;			// 录音起始时间
	private Date endDate;			// 录音结束时间
	
	@NotBlank(message = "{agentRecord.controlSign.NotBlank}")
	private String controlSign; 	// 控制标识
	private Long fastTime;			// 偏移时间(单位：s)

	public AgentRecord() {
		super();
	}

	public AgentRecord(String id) {
		super(id);
	}

	public AgentRecord(String recordID, String callid, String recordTitle, String fileName, String locationId) {
		super();
		this.recordID = recordID;
		this.callid = callid;
		this.recordTitle = recordTitle;
		this.fileName = fileName;
		this.locationId = locationId;
	}

	public String getRecordID() {
		return recordID;
	}

	public void setRecordID(String recordID) {
		this.recordID = recordID;
	}

	public String getCallid() {
		return callid;
	}

	public void setCallid(String callid) {
		this.callid = callid;
	}

	public String getRecordTitle() {
		return recordTitle;
	}

	public void setRecordTitle(String recordTitle) {
		this.recordTitle = recordTitle;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
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

	public String getControlSign() {
		return controlSign;
	}

	public void setControlSign(String controlSign) {
		this.controlSign = controlSign;
	}

	public Long getFastTime() {
		return fastTime;
	}

	public void setFastTime(Long fastTime) {
		this.fastTime = fastTime;
	}
	
}
