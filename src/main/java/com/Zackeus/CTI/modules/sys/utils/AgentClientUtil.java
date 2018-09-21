package com.Zackeus.CTI.modules.sys.utils;

import javax.annotation.PostConstruct;

import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Zackeus.CTI.common.entity.HttpClientResult;
import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.common.utils.WebUtils;
import com.Zackeus.CTI.common.utils.httpClient.HttpClientUtil;
import com.Zackeus.CTI.common.utils.httpClient.HttpStatus;
import com.Zackeus.CTI.modules.sys.entity.User;
import com.Zackeus.CTI.modules.sys.utils.AgentQueue;

import net.sf.json.JSONObject;

/**
 * 
 * @Title:AgentClientUtil
 * @Description:TODO(坐席请求工具类)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月20日 下午3:03:29
 */
@Component
public class AgentClientUtil extends HttpClientUtil {
	
	@Autowired
	private AgentQueue agentQueue;
	
	public static AgentClientUtil agentClientUtil;
	
	private static final String GUID = "guid";
	private static final String COOKIE = "Cookie";
	private static final String SET_GUID = "Set-GUID";
	private static final String SET_COOKIE = "Set-Cookie";
	private static final String JSESSIONID = "JSESSIONID=";
	
	@PostConstruct
	public void init() {
		agentClientUtil = this;
	}
	
	/**
	 * 
	 * @Title：put
	 * @Description: TODO(PUT请求)
	 * @see：
	 * @param user
	 * @param url
	 * @param entityParams
	 * @return
	 * @throws Exception
	 */
	public static HttpClientResult put(User user, String url, Object entityParams) throws Exception {
		CloseableHttpClient httpClient = getHttpClient(url);
		HttpPut httpPut = new HttpPut(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT)
				.setSocketTimeout(SOCKET_TIMEOUT).build();
		httpPut.setConfig(requestConfig);
		packageAgentHeade(user, httpPut);
		packageParam(JSONObject.fromObject(entityParams), httpPut, PARAM_JSON);
		CloseableHttpResponse httpResponse = null;
		try {
			return getAgentClientResult(user, httpResponse, httpClient, httpPut);
		} finally {
			release(httpResponse, httpClient);
		}
	}
	
	/**
	 * 
	 * @Title：packageAgentHeade
	 * @Description: TODO(封装请求头)
	 * @see：
	 * @param httpMethod
	 */
	private static void packageAgentHeade(User user, HttpRequestBase httpMethod) {
		// GUID 座席的鉴权信息
		if (StringUtils.isNotBlank(user.getId()) && agentClientUtil.agentQueue.guidMap.containsKey(user.getId())) {
			httpMethod.setHeader(GUID, agentClientUtil.agentQueue.guidMap.get(user.getId()));
		}
		
		// COOKIE
		if (StringUtils.isNotBlank(user.getId()) && agentClientUtil.agentQueue.elbSessionMap.containsKey(user.getId())) {
			httpMethod.setHeader(COOKIE, agentClientUtil.agentQueue.elbSessionMap.get(user.getId()));
		}
	}
	
	/**
	 * 
	 * @Title：getAgentClientResult
	 * @Description: TODO(处理响应结果)
	 * @see：
	 * @param user
	 * @param httpResponse
	 * @param httpClient
	 * @param httpMethod
	 * @return
	 * @throws Exception
	 */
	private static HttpClientResult getAgentClientResult(User user, CloseableHttpResponse httpResponse,
			CloseableHttpClient httpClient, HttpRequestBase httpMethod) throws Exception {
		// 执行请求
		httpResponse = httpClient.execute(httpMethod);
		// 请求成功，且是 PUT 请求，更新坐席代理事件队列
    	if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK && 
    			StringUtils.equals(HttpPut.METHOD_NAME, httpMethod.getMethod())) {
    		dealGuid(user, httpResponse);
    	}
		// 获取返回结果
		if (ObjectUtils.isNotEmpty(httpResponse) && ObjectUtils.isNotEmpty(httpResponse.getStatusLine())) {
			String content = StringUtils.EMPTY;
			if (ObjectUtils.isNotEmpty(httpResponse.getEntity())) {
				content = EntityUtils.toString(httpResponse.getEntity(), WebUtils.UTF_ENCODING);
			}
			return new HttpClientResult(httpResponse.getStatusLine().getStatusCode(), content);
		}
		return new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * 
	 * @Title：dealGuid
	 * @Description: TODO(分配鉴权信息)
	 * @see：
	 * @param user
	 * @param response
	 */
	private static void dealGuid(User user, CloseableHttpResponse response) {
		Header[] allHeaders = response.getAllHeaders();
		if (ObjectUtils.isNotEmpty(allHeaders) && allHeaders.length > 0) {
		    for (Header header : allHeaders) {
		        if (StringUtils.equals(SET_GUID, header.getName())) {
		            String setGuid = header.getValue();
		            if (StringUtils.isNotBlank(setGuid)) {
		            	agentClientUtil.agentQueue.guidMap.put(user.getId(), setGuid.replace(JSESSIONID, StringUtils.EMPTY));
		            }
		            break;
		        } 
		        if (StringUtils.equals(SET_COOKIE, header.getName())) {
		        	String setCookie = header.getValue();
		        	setCookie = setCookie.substring(0, setCookie.indexOf(StringUtils.SEPARATOR_SECOND));
		        	agentClientUtil.agentQueue.elbSessionMap.put(user.getId(), setCookie);
		        }
		    }
		}
	}
}
