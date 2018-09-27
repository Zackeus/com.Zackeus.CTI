package com.Zackeus.CTI.modules.agent.entity;

import com.Zackeus.CTI.common.entity.DataEntity;

/**
 * 
 * @Title:AgentRecord
 * @Description:TODO(坐席录音)
 * @Company:
 * @author zhou.zhang
 * @date 2018年9月27日 下午5:16:34
 */
public class AgentRecord extends DataEntity<AgentRecord> {

	private static final long serialVersionUID = 1L;

	private String recordID; 		// 录音流水号
	private String callid; 			// 呼叫流水号
	private String recordTitle; 	// 录音标题
	private String fileName; 		// 录音文件地址
	private String locationId; 		// 录音对应的中心节点ID

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

}
