package com.Zackeus.CTI.modules.agent.entity;

import java.util.Date;

import javax.validation.GroupSequence;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.Zackeus.CTI.common.annotation.excel.ExcelField;
import com.Zackeus.CTI.common.entity.DataEntity;
import com.Zackeus.CTI.common.service.valid.First;
import com.Zackeus.CTI.common.service.valid.Second;
import com.Zackeus.CTI.common.service.valid.Third;
import com.Zackeus.CTI.modules.sys.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @Title:AgentCallData
 * @Description:TODO(坐席通话实体)
 * @Company:
 * @author zhou.zhang
 * @date 2018年9月27日 下午4:15:30
 */
@GroupSequence({AgentCallData.class, First.class, Second.class, Third.class})
public class AgentCallData extends DataEntity<AgentCallData> {

	private static final long serialVersionUID = 1L;

	@ExcelField(title = "呼叫流水号")
	private String callid; 						// 呼叫流水号
	@ExcelField(title = "员工号")
	private String userId; 						// 员工号
	@ExcelField(title = "受理人")
	private String userName;					// 当前受理人
	@ExcelField(title = "坐席号")
	private String workNo; 						// 坐席号
	@ExcelField(title = "对方坐席号")
	private String otherPhoneWorkno; 			// 对方座席号
	@ExcelField(title = "座机号")
	private String phoneNumber; 				// 座机号
	@ExcelField(title = "对方电话")
	private String otherPhone; 					// 对方电话号码
	@ExcelField(title = "呼叫类型")
	private String type; 						// 呼叫类型(main：去电；called：来电)
	@ExcelField(title = "结果")
	private boolean result = Boolean.FALSE; 	// 结果(未通话false 0,通话 true 1)
	
	@ExcelField(title = "发生时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createDate;					// 发生时间
	
	@ExcelField(title = "振铃结束")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date riningDate;					// 振铃结束时间
	
	@ExcelField(title = "通话时长(秒)")
	private Integer talkTime;					// 通话时长
	
	@Valid
	@NotNull(message = "{agentCallData.agentRecord.NotNull}")
	private AgentRecord agentRecord;			// 录音
	
	private Integer count;						// mybatis判断数据是否存在

	public AgentCallData() {
		super();
	}

	public AgentCallData(String callid) {
		super();
		this.callid = callid;
	}
	
	public AgentCallData(AgentAlerting agentAlerting) {
		super();
		this.callid = agentAlerting.getCallid();
		this.otherPhone = agentAlerting.getCaller();
		this.type = agentAlerting.getType();
	}
	
	public AgentCallData(AgentRecord agentRecord) {
		super();
		this.agentRecord = agentRecord;
	}
	
	public AgentCallData(String callid, String userId, String userName, String workNo, String otherPhoneWorkno,
			String phoneNumber, String otherPhone, String type, boolean result, Date createDate, Date riningDate,
			AgentRecord agentRecord) {
		super();
		this.callid = callid;
		this.userId = userId;
		this.userName = userName;
		this.workNo = workNo;
		this.otherPhoneWorkno = otherPhoneWorkno;
		this.phoneNumber = phoneNumber;
		this.otherPhone = otherPhone;
		this.type = type;
		this.result = result;
		this.createDate = createDate;
		this.riningDate = riningDate;
		this.agentRecord = agentRecord;
	}

	public String getCallid() {
		return callid;
	}

	public void setCallid(String callid) {
		this.callid = callid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getWorkNo() {
		return workNo;
	}

	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}

	public String getOtherPhoneWorkno() {
		return otherPhoneWorkno;
	}

	public void setOtherPhoneWorkno(String otherPhoneWorkno) {
		this.otherPhoneWorkno = otherPhoneWorkno;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getOtherPhone() {
		return otherPhone;
	}

	public void setOtherPhone(String otherPhone) {
		this.otherPhone = otherPhone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public Date getRiningDate() {
		return riningDate;
	}

	public void setRiningDate(Date riningDate) {
		this.riningDate = riningDate;
	}

	public Integer getTalkTime() {
		return talkTime;
	}

	public void setTalkTime(Integer talkTime) {
		this.talkTime = talkTime;
	}

	public AgentRecord getAgentRecord() {
		return agentRecord;
	}

	public void setAgentRecord(AgentRecord agentRecord) {
		this.agentRecord = agentRecord;
	}
	
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public void setUser(User user) {
		this.userId = user.getId();
		this.workNo = user.getAgentUser().getWorkno();
		this.phoneNumber = user.getAgentUser().getPhonenumber();
	}

}
