package com.baba.portal.controller;

import com.baba.pojo.Cart;
import com.baba.pojo.Item;
import com.baba.pojo.SuperPojo;
import com.baba.service.CartService;
import com.baba.service.SessionService;
import com.baba.service.SkuService;
import com.baba.utils.SessionTool;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.log4j.xml.SAXErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 购物车控制中心
 * @author Jamayette
 *         Created on  2017/8/16
 */
@Controller
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private SessionService sessionService;

    // 显示购物车页面
    @RequestMapping(value="/cart")
    public String showCart(Model model, HttpServletRequest request, HttpServletResponse response) {

        Cart finalCart;
        Cart redisCart;

        // 从cookie中取出购物车对象
        Cart cookieCart = this.getCartFromCookies(request);

        //判断用户是否登陆
        String username = sessionService.getUsernameFromRedis(SessionTool.getSessionID(request, response));

        if (username != null){
            // 从redis中取出购物车
            redisCart = cartService.getCartFormRedis(username);
            //将两车合并
            finalCart = mergeCart(cookieCart,redisCart);
            // 清除cookie中的购物车
            this.delCartFormCookies(request, response);
            //刷新redis里的购物车
            cartService.addCartToRedis(username, finalCart);
        }else {
            finalCart = cookieCart;
        }

        // 填充cart中的items中的skus信息
        finalCart = cartService.completeItemsWithSkuInformation(finalCart);

        // 将此cart发送到页面
        model.addAttribute("finalCart", finalCart);

        return "cart";
    }

    private Cart mergeCart(Cart cookieCart, Cart redisCart) {
        if (cookieCart == null) {
            return redisCart;
        } else if (redisCart == null) {
            return cookieCart;
        } else {
            // 则合并购物车
            List<Item> items = cookieCart.getItems();
            for (Item item : items) {
                redisCart.addItem(item);
            }
            return redisCart;
        }
    }

    private void delCartFormCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("cart".equals(cookie.getName())) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                break;
            }
        }

    }

    // 添加到购物车
    @RequestMapping(value="/addToCart")
    public String addToCart(Long skuId,Integer amount,
                           HttpServletRequest request, HttpServletResponse response) {
        Cart cart;

        //判断用户是否登陆
        String username = sessionService.getUsernameFromRedis(SessionTool.getSessionID(request, response));

        if (username!=null){
            //已登陆从redis中取出购物车
            cart = cartService.getCartFormRedis(username);
        }else {
            // 未登录从cookie中取出购物车对象
            cart = this.getCartFromCookies(request);
        }

        // 用户之前没有将商品加入过购物车
        if (cart == null) {
            cart = new Cart();
        }

        // 将相应的库存商品信息，添加到购物车对象中
        Item item = new Item();
        item.setAmount(amount);
        item.setSkuId(skuId);
        cart.addItem(item);

        if (username!=null){
            // 将购物车保存到redis
            cartService.addCartToRedis(username, cart);
        }else {
            // 将购物车保存到cookie中
            this.addCartToCookies(response, cart);
        }

        return "redirect:/cart";
    }

    private void addCartToCookies(HttpServletResponse response, Cart cart) {

        //转json字符串
        ObjectMapper om = new ObjectMapper();
        StringWriter w = new StringWriter();

        try {
            om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            om.writeValue(w, cart);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Cookie cookie = new Cookie("cart", w.toString());
        cookie.setMaxAge(60 * 60 * 24);// 设置保存一天
        cookie.setPath("/");// 设置路径
        response.addCookie(cookie);

    }

    /**
     * 从cookie中取出购物车
     */
    private Cart getCartFromCookies(HttpServletRequest request) {

        Cart cart =null;

        // 从cookie中取出购物车信息
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("cart".equals(cookie.getName())){
                // 将购物车的json字符串转成购物车对象
                String value = cookie.getValue();
                ObjectMapper om = new ObjectMapper();
                try {
                    cart = om.readValue(value, Cart.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return cart;
    }

    @RequestMapping(value = "buyer/checkOut")
    public String trueBuy(Long[] skuIds, Model model,
                          HttpServletRequest request,HttpServletResponse response) {

        String username = sessionService.getUsernameFromRedis(SessionTool.getSessionID(request, response));

        if (username==null){
            return "/cart";
        }

        //无货标识
        Boolean flag = true;

        //判断redis里的购物车不能为空
        Cart cart = cartService.getCartFormRedis(username);

        if (cart!=null && cart.getItems().size()>0){

            List<Item> items = cart.getItems();

            //将未提交的item移除
            List<Item> newItems = new ArrayList<>();
            for (Item item : items) {
                if (Arrays.asList(skuIds).contains(item.getSkuId())){
                    newItems.add(item);
                }
            }
            cart.setItems(newItems);

            // 填充购物车 复合信息（具体库存相关信息）
            cart = cartService.completeItemsWithSkuInformation(cart);
            // 判断库存一定够
            for (Item item : cart.getItems()) {
                if (item.getAmount()>Integer.parseInt(item.getSku().get("stock").toString())){
                    flag = false;
                }
            }
            //至少有一款商品无货
            if(!flag){
                return "/cart";
            }

        }else {
            // 回到空的购物车页面
            return "redirect:/cart";
        }

        model.addAttribute("cart", cart);

        //进入订单页面
        return "order";
    }

}
