package com.heima.activemq;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Component;

import com.aliyuncs.sendsms.SmsSystem;

@Component("smsSender")
public class SmsSender implements MessageListener{

	public void onMessage(Message message) {
		
		MapMessage mapMessage = (MapMessage) message;
		// 调用SMS服务发送短信   SmsSystem阿发送短信给客户手机实现类
		try {
			// 发送短信 Map 来自ActiveMQ 生成者
			SmsSystem.sendMessage(mapMessage.getString("msg"), mapMessage.getString("telephone")); 			
			System.out.println( "-----发送消息成功...");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
