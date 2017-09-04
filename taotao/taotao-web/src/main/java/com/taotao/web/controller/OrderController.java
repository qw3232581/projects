package com.taotao.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.manage.pojo.Item;
import com.taotao.sso.bean.User;
import com.taotao.web.bean.Cart;
import com.taotao.web.bean.Order;
import com.taotao.web.service.CartService;
import com.taotao.web.service.ItemService;
import com.taotao.web.service.OrderService;
import com.taotao.web.service.UserService;
import com.taotao.web.threadlocal.UserThreadLocal;

@RequestMapping("order")
@Controller
public class OrderController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    /**
     * 去订单确认页
     * 
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ModelAndView toOrder(@PathVariable("itemId") Long itemId) {
        ModelAndView mv = new ModelAndView("order");
        Item item = this.itemService.queryByItemId(itemId);
        mv.addObject("item", item);
        return mv;
    }

    /**
     * 去购物车的订单页
     * 
     * @return
     */
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView toCartOrder() {
        ModelAndView mv = new ModelAndView("order-cart-old");
        User user = UserThreadLocal.get();
        List<Cart> carts = this.cartService.queryCartListByUserId(user.getId());
        mv.addObject("carts", carts);
        return mv;
    }

    /**
     * 提交订单
     * 
     * @param order
     * @param token
     * @return
     */
    @RequestMapping(value = "submit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> submit(Order order) {
        Map<String, Object> result = new HashMap<String, Object>();

        String orderId = this.orderService.submit(order);
        if (StringUtils.isEmpty(orderId)) {
            // 提交订单失败
            result.put("status", 300);
        } else {
            // 提交订单成功
            result.put("status", 200);
            result.put("data", orderId);
        }

        return result;
    }

    /**
     * 成功页
     * 
     * @param orderId
     * @return
     */
    @RequestMapping(value = "success", method = RequestMethod.GET)
    public ModelAndView success(@RequestParam("id") String orderId) {
        ModelAndView mv = new ModelAndView("success");

        // 订单对象
        mv.addObject("order", this.orderService.queryOrderById(orderId));

        // 预计送货时间，当前的时间加两天，格式化：12月13日
        mv.addObject("date", new DateTime().plusDays(2).toString("MM月dd日"));

        return mv;
    }

}
