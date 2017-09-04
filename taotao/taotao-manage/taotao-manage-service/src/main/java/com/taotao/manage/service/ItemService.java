package com.taotao.manage.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.common.service.ApiService;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;

@Service
public class ItemService extends BaseService<Item> {

    @Autowired
    private ItemDescService itemDescService;

    @Autowired
    private ItemParamItemService itemParamItemService;

    @Autowired
    private ApiService apiService;

    @Value("${TAOTAO_WEB_URL}")
    private String TAOTAO_WEB_URL;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public void saveItem(Item item, String desc, String itemParams) {
        item.setStatus(1);// 默认状态为正常
        item.setId(null); // 强制设置为null，处于安全的考虑
        // 保存商品数据
        super.save(item);

        // 保存商品描述数据
        ItemDesc record = new ItemDesc();
        record.setItemId(item.getId());
        record.setItemDesc(desc);
        this.itemDescService.save(record);

        // 保存规格参数数据
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(itemParams);
        this.itemParamItemService.save(itemParamItem);

        sendMsg(item.getId(), "insert");
    }

    public EasyUIResult queryPageItemList(Integer page, Integer rows) {
        PageInfo<Item> pageInfo = super.queryPageListOrderBy(page, rows, "updated DESC", Item.class);
        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }

    public void updateItem(Item item, String desc, String itemParams) {
        // 强制设置不能更新的字段为null
        item.setStatus(null);
        super.updateSelective(item);

        // 更新商品描述数据
        ItemDesc record = new ItemDesc();
        record.setItemId(item.getId());
        record.setItemDesc(desc);
        this.itemDescService.updateSelective(record);

        // 修改规格参数数据
        this.itemParamItemService.updateItemParamItem(item.getId(), itemParams);

        // 通知其他系统该商品已经更新
        // try {
        // String url = TAOTAO_WEB_URL + "/item/cache/" + item.getId() + ".html";
        // this.apiService.doPost(url);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        sendMsg(item.getId(), "update");
    }

    private void sendMsg(Long itemId, String type) {
        try {
            // 发送消息通知其他系统
            Map<String, Object> msg = new HashMap<String, Object>();
            msg.put("itemId", itemId);
            msg.put("type", type);
            msg.put("date", System.currentTimeMillis());
            this.rabbitTemplate.convertAndSend("item." + type, MAPPER.writeValueAsString(msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
