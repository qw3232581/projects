package com.baba.cob.service.impl;

import com.baba.dao.BuyerDAO;
import com.baba.pojo.Buyer;
import com.baba.pojo.Cart;
import com.baba.pojo.Item;
import com.baba.pojo.SuperPojo;
import com.baba.service.BuyerService;
import com.baba.service.CartService;
import com.baba.service.SkuService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jamayette
 *         Created on  2017/8/20
 */
@Service("cartService")
public class CartServiceImpl implements CartService {

    @Autowired
    private Jedis jedis;
    @Autowired
    private SkuService skuService;

    @Override
    public Cart getCartFormRedis(String username) {
        Cart cart = null;

        // 从redis中取出cart的json字符串
        String str = jedis.get("cart:" + username);

        if (str ==null){
            return null;
        }

        // 将cart的json字符串转成cart对象
        ObjectMapper om = new ObjectMapper();
        try {
            cart = om.readValue(str, Cart.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cart;
    }

    @Override
    public void addCartToRedis(String username, Cart cart) {
        //将cart对象变成json字符串
        ObjectMapper om = new ObjectMapper();
        StringWriter w = new StringWriter();
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            om.writeValue(w, cart);
        } catch (IOException e) {
            e.printStackTrace();
        }

        jedis.set("cart:"+username,w.toString());

    }

    public Cart completeItemsWithSkuInformation(Cart finalCart) {

        List<Item> detailedItems = new ArrayList<>();

        if (finalCart!=null){
            //获取不完整的items对象
            List<Item> items = finalCart.getItems();

            for (Item item : items) {
                SuperPojo sku = skuService.findSkuAndProductBySkuId(item.getSkuId());
                // 将sku添加到item中
                item.setSku(sku);
                // 将完整的item添加到集合中
                detailedItems.add(item);
            }

            // 将含有完整item信息的arrayList覆盖到cart中，（用完整信息取代残缺信息）
            finalCart.setItems(detailedItems);
        }

        return finalCart;
    }
}
