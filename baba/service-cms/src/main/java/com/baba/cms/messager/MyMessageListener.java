package com.baba.cms.messager;

import com.baba.pojo.SuperPojo;
import com.baba.service.ProductService;
import com.baba.service.StaticPageService;
import freemarker.template.TemplateException;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 消息回掉处理类
 * @author Jamayette
 *         Created on  2017/8/20
 */
public class MyMessageListener implements MessageListener {

    @Autowired
    private StaticPageService staticPageService;

    @Autowired
    private ProductService productService;

    /**
     * 当监听到消息后，会自动调用此方法
     */
    @Override
    public void onMessage(Message message) {
        //消息转化
        ActiveMQTextMessage amessage = (ActiveMQTextMessage) message;

        try {
            String ids = amessage.getText();

            // 从ids中遍历出id
            String[] split = ids.split(",");

            for (String id : split) {

                Long productId = Long.parseLong(id);
                SuperPojo superPojo = productService.findProductById(productId);

                List skus = (List) superPojo.get("skus");

                // 去除颜色重复 将原有的map变成能够支持Freemarker
                Set<SuperPojo> colors = new HashSet<>();

                for (Object o : skus) {
                    SuperPojo sku = (SuperPojo) o;

                    // 定义颜色对象
                    SuperPojo color = new SuperPojo();
                    color.setProperty("id", sku.get("color_id"));
                    color.setProperty("name", sku.get("name"));

                    // 将颜色对象添加到hm集合中，利用hm集合来去除重复
                    colors.add(color);
                }

                // 将非重复的颜色对象也通过superPojo传递到页面
                superPojo.setProperty("colors",colors);

                HashMap hashMap = new HashMap();
                hashMap.put("superPojo", superPojo);

                // 开始静态化
                staticPageService.staticProductPage(hashMap, id);
            }

        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
