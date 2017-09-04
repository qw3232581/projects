package com.taotao.cart.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.bean.Item;
import com.taotao.cart.pojo.Cart;
import com.taotao.common.utils.CookieUtils;

@Service
public class CartCookieService {

    public static final String COOKIE_NAME = "TT_CART";

    public static final Integer COOKIE_TIME = 60 * 60 * 24 * 365;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private ItemService itemService;

    public void addItemToCart(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        List<Cart> carts = this.queryCartList(request);
        Cart cart = null;
        for (Cart c : carts) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                cart = c;
                break;
            }
        }

        if (null == cart) {
            // 不存在
            cart = new Cart();
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            cart.setItemId(itemId);
            cart.setNum(1); // TODO 暂时默认为1

            Item item = this.itemService.queryItemById(itemId);
            cart.setItemTitle(item.getTitle());
            cart.setItemPrice(item.getPrice());
            String[] images = StringUtils.split(item.getImage(), ',');
            if (images != null && images.length > 0) {
                cart.setItemImage(images[0]);
            }
            carts.add(cart);
        } else {
            // 存在
            cart.setNum(cart.getNum() + 1); // TODO 先默认为1，后续实现
            cart.setUpdated(new Date());
        }

        // 将购物车的数据写入到cookie中
        try {
            CookieUtils.setCookie(request, response, COOKIE_NAME, MAPPER.writeValueAsString(carts),
                    COOKIE_TIME, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Cart> queryCartList(HttpServletRequest request) {
        String jsonData = CookieUtils.getCookieValue(request, COOKIE_NAME, true);
        if (StringUtils.isBlank(jsonData)) {
            return new ArrayList<Cart>(0);
        }
        try {
            return MAPPER.readValue(jsonData,
                    MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Cart>(0);
    }

    public void updateNum(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        List<Cart> carts = this.queryCartList(request);
        Cart cart = null;
        for (Cart c : carts) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                cart = c;
                break;
            }
        }

        if (null == cart) {
            return;
        }

        cart.setNum(num);
        cart.setUpdated(new Date());

        // 将购物车的数据写入到cookie中
        try {
            CookieUtils.setCookie(request, response, COOKIE_NAME, MAPPER.writeValueAsString(carts),
                    COOKIE_TIME, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteItem(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        List<Cart> carts = this.queryCartList(request);
        for (Cart c : carts) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                carts.remove(c);
                break;
            }
        }
        // 将购物车的数据写入到cookie中
        try {
            CookieUtils.setCookie(request, response, COOKIE_NAME, MAPPER.writeValueAsString(carts),
                    COOKIE_TIME, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
