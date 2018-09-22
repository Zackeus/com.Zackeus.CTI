package com.Zackeus.CTI.modules.sys.entity.agent;


/**
 * 
 * @Title:AgentHttpResult
 * @Description:TODO(坐席接口返回参数)
 * @Company:
 * @author zhou.zhang
 * @date 2018年9月21日 上午11:47:45
 */
public class AgentHttpResult extends BasicHttpResult {

	private static final long serialVersionUID = 1L;

	private Result result; 		// 查询成功后，返回结果信息的对象

	public AgentHttpResult() {
		super();
	}

	public AgentHttpResult(String message, String retcode) {
		super(message, retcode);
	}

	public AgentHttpResult(Result result) {
		super();
		this.result = result;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}
	
}
