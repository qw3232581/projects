package com.baba.portal.controller;

import com.baba.pojo.Order;
import com.baba.service.OrderService;
import com.baba.service.SessionService;
import com.baba.utils.SessionTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 订单控制器
 * @author Jamayette
 *         Created on  2017/8/23
 */
@Controller
public class OrderController {

    @Autowired
    SessionService sessionService;
    @Autowired
    OrderService orderService;

    //提交订单
    @RequestMapping(value = "/buyer/submitOrder")
    public String submitOrder(Order order, Model model,
                            HttpServletRequest request, HttpServletResponse response){
        //判断用户是否登陆
        String username = sessionService.getUsernameFromRedis(SessionTool.getSessionID(request, response));

        order = orderService.addOrderAndDetail(order, username);

        model.addAttribute("orderPrice",order.getOrderPrice());
        model.addAttribute("orderId",order.getId());

        return "success";

    }



}
