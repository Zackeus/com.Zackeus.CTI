package com.Zackeus.CTI.modules.agent.config;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 * @Title:RecordPlayParam
 * @Description:TODO(录音播放参数)
 * @Company:
 * @author zhou.zhang
 * @date 2018年10月24日 上午11:57:28
 */
@Component
public class RecordPlayParam {

	private String voicepath; 		// 文件路径。最大长度100个字符
	
	@Value("${agentConfig.RecordPlayParam.startpostion}")
	private Long startpostion; 		// 播放文件起始位置
	
	@Value("${agentConfig.RecordPlayParam.volumechange}")
	private String volumechange; 	// 放音音量，默认"50"。0-5位数字
	
	@Value("${agentConfig.RecordPlayParam.speedchange}")
	private String speedchange; 	// 放音音速，默认"50"。0-5位数字
	
	@Value("${agentConfig.RecordPlayParam.times}")
	private Integer times; 			// 播放次数，默认为1。
	
	@Value("${agentConfig.RecordPlayParam.codeformat}")
	private Integer codeformat; 	// 文件编码格式，目前暂时只支持0：默认为0。

	public RecordPlayParam() {
		super();
	}

	public RecordPlayParam(String voicepath, Long startpostion, String volumechange, String speedchange, Integer times,
			Integer codeformat) {
		super();
		this.voicepath = voicepath;
		this.startpostion = startpostion;
		this.volumechange = volumechange;
		this.speedchange = speedchange;
		this.times = times;
		this.codeformat = codeformat;
	}

	public String getVoicepath() {
		return voicepath;
	}

	public void setVoicepath(String voicepath) {
		this.voicepath = voicepath;
	}

	public Long getStartpostion() {
		return startpostion;
	}

	public void setStartpostion(Long startpostion) {
		this.startpostion = startpostion;
	}

	public String getVolumechange() {
		return volumechange;
	}

	public void setVolumechange(String volumechange) {
		this.volumechange = volumechange;
	}

	public String getSpeedchange() {
		return speedchange;
	}

	public void setSpeedchange(String speedchange) {
		this.speedchange = speedchange;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Integer getCodeformat() {
		return codeformat;
	}

	public void setCodeformat(Integer codeformat) {
		this.codeformat = codeformat;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
