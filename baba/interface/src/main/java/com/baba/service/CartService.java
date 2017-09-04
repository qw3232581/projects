package com.baba.service;

import com.baba.pojo.Cart;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : Jamayette
 * Created on 2017/8/16
 */
@Service
public interface CartService {

    /**
     * 从redis中取得购物车信息
     */
    Cart getCartFormRedis(String username);

    /**
     * 将购物车信息添加到Redis中
     */
    void addCartToRedis(String username, Cart cart);



    Cart completeItemsWithSkuInformation(Cart finalCart);


}
