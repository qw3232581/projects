package com.heima.activemq;

import org.springframework.stereotype.Component;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;


@Component("validationCodeSender")
public class ValidationCodeSender implements MessageListener {

    public void onMessage(Message message) {

        MapMessage mapMessage = (MapMessage) message;
        // 调用SMS服务发送短信   SmsSystem阿发送短信给客户手机实现类
        try {
            // 发送短信 Map 来自ActiveMQ 生成者
            SendValidationCode.sendMessage(mapMessage.getString("msg"), mapMessage.getString("telephone"));
            System.out.println("-----发送消息成功...");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
