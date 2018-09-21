package com.Zackeus.CTI.modules.sys.entity.agent;

import com.Zackeus.CTI.common.entity.DataEntity;

/**
 * 
 * @Title:Agent
 * @Description:TODO(坐席用户实体类)
 * @Company:
 * @author zhou.zhang
 * @date 2018年9月12日 下午5:05:28
 */
public class AgentUser extends DataEntity<AgentUser> {

	private static final long serialVersionUID = 1L;

	private String userNo; 					// 员工号
	private String workno; 					// 坐席工号
	private String name; 					// 坐席名称
	private String phonenumber; 			// 座席电话号码
	private Integer status; 				// 坐席状态
	private Integer citStatus; 				// 座席平台状态
	private Integer groupid; 				// 座席班组ID
	private String groupname; 				// 坐席班组名称
	private String mediatype; 				// 座席媒体类型
	private Integer vdnid; 					// 所属VDN ID
	private Long currentstatetime; 			// 当前状态时长
	private Long logindate; 				// 签入时间
	private Integer inMultimediaConf; 		// 是否在多媒体会议中。0表示不在多媒体会议中。1表示在多媒体会议中。
	private Integer currentStateReason; 	// 当前状态原因码

	public AgentUser() {
		super();
	}

	public AgentUser(String id) {
		super(id);
	}

	public AgentUser(String userNo, String workno, String name, String phonenumber, Integer status, Integer citStatus,
			Integer groupid, String groupname, String mediatype, Integer vdnid, Long currentstatetime, Long logindate,
			Integer inMultimediaConf, Integer currentStateReason) {
		super();
		this.userNo = userNo;
		this.workno = workno;
		this.name = name;
		this.phonenumber = phonenumber;
		this.status = status;
		this.citStatus = citStatus;
		this.groupid = groupid;
		this.groupname = groupname;
		this.mediatype = mediatype;
		this.vdnid = vdnid;
		this.currentstatetime = currentstatetime;
		this.logindate = logindate;
		this.inMultimediaConf = inMultimediaConf;
		this.currentStateReason = currentStateReason;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getWorkno() {
		return workno;
	}

	public void setWorkno(String workno) {
		this.workno = workno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCitStatus() {
		return citStatus;
	}

	public void setCitStatus(Integer citStatus) {
		this.citStatus = citStatus;
	}

	public Integer getGroupid() {
		return groupid;
	}

	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getMediatype() {
		return mediatype;
	}

	public void setMediatype(String mediatype) {
		this.mediatype = mediatype;
	}

	public Integer getVdnid() {
		return vdnid;
	}

	public void setVdnid(Integer vdnid) {
		this.vdnid = vdnid;
	}

	public Long getCurrentstatetime() {
		return currentstatetime;
	}

	public void setCurrentstatetime(Long currentstatetime) {
		this.currentstatetime = currentstatetime;
	}

	public Long getLogindate() {
		return logindate;
	}

	public void setLogindate(Long logindate) {
		this.logindate = logindate;
	}

	public Integer getInMultimediaConf() {
		return inMultimediaConf;
	}

	public void setInMultimediaConf(Integer inMultimediaConf) {
		this.inMultimediaConf = inMultimediaConf;
	}

	public Integer getCurrentStateReason() {
		return currentStateReason;
	}

	public void setCurrentStateReason(Integer currentStateReason) {
		this.currentStateReason = currentStateReason;
	}

}
