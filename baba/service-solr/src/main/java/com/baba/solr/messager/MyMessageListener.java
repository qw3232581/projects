package com.baba.solr.messager;

import com.baba.service.SolrService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.io.IOException;

/**
 * @author Jamayette
 *         Created on  2017/8/19
 */
public class MyMessageListener implements MessageListener {

    @Autowired
    private SolrService solrService;


    @Override
    public void onMessage(Message message) {

        ActiveMQTextMessage amessage = (ActiveMQTextMessage) message;

        try {
            String ids = amessage.getText();
            System.out.println("消费方接收到的消息：" + ids);
            // 添加商品信息到solr服务器
            solrService.addProduct(ids);
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
