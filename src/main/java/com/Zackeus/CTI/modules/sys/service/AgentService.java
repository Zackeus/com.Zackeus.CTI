package com.Zackeus.CTI.modules.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.Zackeus.CTI.common.utils.Logs;
import com.Zackeus.CTI.common.utils.StringUtils;

@Service
public class AgentService {
	
	 @Autowired
	 private TaskExecutor taskExecutor;
	 
	 public void test(String id, String name) {
		 taskExecutor.execute(new TestThread(id, name));;
	 }
	 
	 private class TestThread implements Runnable {
		
		 private String id;
		 private String name;
		 private String pushUrl;	// 推送地址
		 
		private TestThread(String id, String name) {
	        super();
	        this.id = id;
	        this.name = name;
		}

		@Override
		public void run() {
			while(Boolean.TRUE) {
				if (StringUtils.isBlank(pushUrl)) {
					Logs.info("推送地址为空");
				}
				
				Logs.info(id + " : " + name);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	 }
}
