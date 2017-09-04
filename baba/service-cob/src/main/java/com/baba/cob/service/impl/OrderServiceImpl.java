package com.baba.cob.service.impl;

import com.baba.dao.DetailDAO;
import com.baba.dao.OrderDAO;
import com.baba.pojo.Cart;
import com.baba.pojo.Detail;
import com.baba.pojo.Item;
import com.baba.pojo.Order;
import com.baba.service.CartService;
import com.baba.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.List;

/**
 * Order服务类
 * @author Jamayette
 *         Created on  2017/8/23
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private Jedis jedis;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private DetailDAO detailDAO;


    @Override
    public Order addOrderAndDetail(Order order, String username) {

        //生成订单id
        Long id = jedis.incr("oid");
        order.setId(id);

        //从redis中取出对应购物车
        Cart cart = cartService.getCartFormRedis(username);

        // 填充购物车
        cart = cartService.completeItemsWithSkuInformation(cart);

        // 从购物车中取出相关信息放入订单对象中
        order.setDeliverFee(cart.getFee());
        order.setTotalPrice(cart.getTotalPrice());
        order.setOrderPrice(cart.getProductPrice());

        // 设置订单的支付状态
        // 支付状态 :0到付1待付款,2已付款,3待退款,4退款成功,5退款失败
        if (order.getPaymentWay()==1){
            order.setIsPaiy(0);
        }else {
            order.setIsPaiy(1);
        }

        // 设置订单状态
        // 订单状态 0:提交订单 1:仓库配货 2:商品出库 3:等待收货 4:完成 5待退货 6已退货
        order.setOrderState(0);

        // 设置时间
        order.setCreateDate(new Date());

        // 设置用户id
        // 前台注册的时候可以将用户名和用户id保存到redis中，key：用户名
        // value：用户id，方便根据用户名获得用户id
        Long buyerId = Long.parseLong(jedis.get(username));
        order.setBuyerId(buyerId);

        orderDAO.insert(order);

        // 保存订单详情
        // 遍历购物项，来保存订单详情
        List<Item> items = cart.getItems();
        for (Item item : items) {
            Detail detail = new Detail();
            detail.setOrderId(id);
            detail.setProductId(Long.parseLong(item.getSku().get("product_id").toString()));
            detail.setProductName(item.getSku().get("productName").toString());
            detail.setColor(item.getSku().get("colorName").toString());
            detail.setSize(item.getSku().get("size").toString());
            detail.setPrice(
                    Float.parseFloat(item.getSku().get("price").toString()));
            detail.setAmount(item.getAmount());

            detailDAO.insert(detail);
        }

        // 清空购物车
        jedis.del("cart:" + username);

        return order;

    }
}
