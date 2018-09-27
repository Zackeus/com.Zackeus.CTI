package com.Zackeus.CTI.modules.agent.config;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.Zackeus.CTI.common.utils.StringUtils;

/**
 * 
 * @Title:LoginConfig
 * @Description:TODO(坐席登录参数)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月21日 上午10:06:43
 */
@Component
public class LoginParam {
	
	private String password = StringUtils.EMPTY;	// 坐席密码
	
	private String phonenum;						// 坐席电话
	
	private Boolean autoanswer = Boolean.TRUE;		// 是否自动应答
	
	private Boolean autoenteridle = Boolean.TRUE;	// 是否自动进入空闲态
	
	@Value("${agentConfig.LoginParam.status}")
	private Integer status;							// 签入后的状态
	
	private Boolean releasephone = Boolean.TRUE;	// 座席挂机后是否进入非长通态 
	
	@Value("${agentConfig.LoginParam.ip}")
	private String ip;								// 坐席IP
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhonenum() {
		return phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

	public Boolean getAutoanswer() {
		return autoanswer;
	}

	public void setAutoanswer(Boolean autoanswer) {
		this.autoanswer = autoanswer;
	}

	public Boolean getAutoenteridle() {
		return autoenteridle;
	}

	public void setAutoenteridle(Boolean autoenteridle) {
		this.autoenteridle = autoenteridle;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getReleasephone() {
		return releasephone;
	}

	public void setReleasephone(Boolean releasephone) {
		this.releasephone = releasephone;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
