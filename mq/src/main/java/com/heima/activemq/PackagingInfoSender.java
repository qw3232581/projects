package com.heima.activemq;

import org.springframework.stereotype.Component;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@Component("packagingInfoSender")
public class PackagingInfoSender implements MessageListener {

    public void onMessage(Message message) {

        MapMessage mapMessage = (MapMessage) message;
        // 调用SMS服务发送短信   SmsSystem阿发送短信给客户手机实现类
        try {
            // 发送短信 Map 来自ActiveMQ 生成者
            SendPackagingInfo.sendMessage(mapMessage.getString("customername")
                    , mapMessage.getString("customertel"), mapMessage.getString("customeraddr"),
                    mapMessage.getString("stafftelephone"));
            System.out.println("-----发送消息成功...");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
