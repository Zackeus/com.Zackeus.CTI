package com.Zackeus.CTI.modules.agent.demo;

import com.Zackeus.CTI.common.entity.HttpClientResult;
import com.Zackeus.CTI.common.utils.httpClient.HttpClientUtil;
import com.Zackeus.CTI.modules.agent.config.CallParam;

import net.sf.json.JSONObject;

public class HttpTest {
	
	public static void main(String[] args) throws Exception {
		String url = "http://10.5.133.244:8008/com.Zackeus.CTI/sys/httpAgent/voiceCallOut?userId=1&postUrl=http://10.5.133.244:8008/com.Zackeus.CTI/sys/demo/receive";
		CallParam callParam = new CallParam();
		callParam.setCalled("015058041631");
		HttpClientResult  httpClientResult = HttpClientUtil.doPostJson(url, JSONObject.fromObject(callParam));
		System.out.println("结果：" + httpClientResult);
	}

}
