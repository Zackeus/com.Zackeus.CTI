package com.Zackeus.CTI.modules.agent.config;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.Zackeus.CTI.common.annotation.validator.CallNum;
import com.Zackeus.CTI.common.utils.StringUtils;

/**
 * 
 * @Title:CallParam
 * @Description:TODO(坐席外呼参数)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月26日 上午8:58:32
 */
@Component
public class CallParam {
	
	private String caller = StringUtils.EMPTY;		// (可选)主叫号码。内容可为空，为空时为平台默认主叫号码，0-24位数字
	
	@CallNum
	private String called;							// (必选)被叫号码。内容不可为空，1-24位数字或*或#
	private Integer skillid = null;					// (可选)技能ID。（数字类型，内容可为空，为空时为平台配置默认的技能队列ID
	private String callappdata = StringUtils.EMPTY;	// (可选)随路数据信息。可为空，最大长度为1024
	private Long callcontrolid = null;				// (可选)手工预览呼叫使用。手工预览呼出时必须带，来自预览发起事件附带信息中的controlid
	
	@Value("${agentConfig.CallParam.mediaability}")
	private Integer mediaability;					// (可选)媒体能力，默认为0。0：音频，1：视频
	
	public CallParam() {
		super();
	}
	
	public CallParam(String called) {
		super();
		this.called = called;
	}

	public CallParam(String caller, String called, Integer skillid, String callappdata, Long callcontrolid,
			Integer mediaability) {
		super();
		this.caller = caller;
		this.called = called;
		this.skillid = skillid;
		this.callappdata = callappdata;
		this.callcontrolid = callcontrolid;
		this.mediaability = mediaability;
	}

	public String getCaller() {
		return caller;
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}

	public String getCalled() {
		return called;
	}

	public void setCalled(String called) {
		this.called = called;
	}

	public Integer getSkillid() {
		return skillid;
	}

	public void setSkillid(Integer skillid) {
		this.skillid = skillid;
	}

	public String getCallappdata() {
		return callappdata;
	}

	public void setCallappdata(String callappdata) {
		this.callappdata = callappdata;
	}

	public Long getCallcontrolid() {
		return callcontrolid;
	}

	public void setCallcontrolid(Long callcontrolid) {
		this.callcontrolid = callcontrolid;
	}

	public Integer getMediaability() {
		return mediaability;
	}

	public void setMediaability(Integer mediaability) {
		this.mediaability = mediaability;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
