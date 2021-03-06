package com.Zackeus.CTI.modules.agent.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 
 * @Title:Result
 * @Description:TODO(结果信息的对象)
 * @Company:
 * @author zhou.zhang
 * @date 2018年9月21日 上午11:51:55
 */
public class Result implements Serializable {

	private static final long serialVersionUID = 1L;

	private String workno; 		// 坐席工号
	private String vdnid; 		// 虚拟呼叫中心ID
	private String sipport; 	// 未知
	private String sipip; 		// 未知
	private String mediatype; 	// 所签入的媒体服务器，如TTF
	
	/*坐席状态响应消息*/
	private Integer agentState;			// 座席状态
	private Boolean isWorking;			// false表示没有正在通话。true表示正在通话
	private String agentStateText;		// 状态字段
	private String agentStateColor;		// 字段颜色

	public String getWorkno() {
		return workno;
	}

	public void setWorkno(String workno) {
		this.workno = workno;
	}

	public String getVdnid() {
		return vdnid;
	}

	public void setVdnid(String vdnid) {
		this.vdnid = vdnid;
	}

	public String getSipport() {
		return sipport;
	}

	public void setSipport(String sipport) {
		this.sipport = sipport;
	}

	public String getSipip() {
		return sipip;
	}

	public void setSipip(String sipip) {
		this.sipip = sipip;
	}

	public String getMediatype() {
		return mediatype;
	}

	public void setMediatype(String mediatype) {
		this.mediatype = mediatype;
	}
	
	public Integer getAgentState() {
		return agentState;
	}

	public void setAgentState(Integer agentState) {
		this.agentState = agentState;
	}

	public Boolean getIsWorking() {
		return isWorking;
	}

	public void setIsWorking(Boolean isWorking) {
		this.isWorking = isWorking;
	}
	
	public String getAgentStateText() {
		return agentStateText;
	}

	public void setAgentStateText(String agentStateText) {
		this.agentStateText = agentStateText;
	}

	public String getAgentStateColor() {
		return agentStateColor;
	}

	public void setAgentStateColor(String agentStateColor) {
		this.agentStateColor = agentStateColor;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
