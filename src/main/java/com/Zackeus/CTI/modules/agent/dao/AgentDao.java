package com.Zackeus.CTI.modules.agent.dao;

import java.util.List;

import com.Zackeus.CTI.common.annotation.MyBatisDao;
import com.Zackeus.CTI.common.dao.CrudDao;
import com.Zackeus.CTI.modules.agent.entity.AgentCallData;
import com.Zackeus.CTI.modules.agent.entity.AgentRecord;
import com.Zackeus.CTI.modules.agent.entity.CallDataExport;

/**
 * 
 * @Title:AgentDao
 * @Description:TODO(坐席Dao接口)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月27日 下午4:40:45
 */
@MyBatisDao
public interface AgentDao extends CrudDao<AgentCallData> {
	
	public void insertRecord(AgentCallData agentCallData);
	
	public void updateRecordEndDate(AgentCallData agentCallData);
	
	public AgentRecord getRecordByCallId(String callId);
	
	public AgentRecord getRecordByRecordID(String recordID);
	
	public List<AgentCallData> findCallRecordList(AgentCallData agentCallData);
	
	public List<AgentCallData> getCallDataByExport(CallDataExport callDataExport);
	

}
