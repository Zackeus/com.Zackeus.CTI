package com.Zackeus.CTI.modules.agent.entity;

import java.util.Date;

import javax.validation.GroupSequence;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.Zackeus.CTI.common.entity.DataEntity;
import com.Zackeus.CTI.common.service.valid.First;
import com.Zackeus.CTI.common.service.valid.Second;
import com.Zackeus.CTI.common.service.valid.Third;
import com.Zackeus.CTI.modules.sys.entity.User;

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

	private String callid; 						// 呼叫流水号
	private String userId; 						// 员工号
	private String userName;					// 当前受理人
	private String workNo; 						// 坐席号
	private String otherPhoneWorkno; 			// 对方座席号
	private String phoneNumber; 				// 座机号
	private String otherPhone; 					// 对方电话号码
	private String type; 						// 呼叫类型(main：去电；called：来电)
	private Date createDate;					// 发生时间
	private boolean result = Boolean.FALSE; 	// 结果(未通话false 0,通话 true 1)

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
	
	public AgentCallData(String callid, String userId, String workNo, String otherPhoneWorkno, String phoneNumber,
			String otherPhone, String type, boolean result, AgentRecord agentRecord) {
		super();
		this.callid = callid;
		this.userId = userId;
		this.workNo = workNo;
		this.otherPhoneWorkno = otherPhoneWorkno;
		this.phoneNumber = phoneNumber;
		this.otherPhone = otherPhone;
		this.type = type;
		this.result = result;
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
