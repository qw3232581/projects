package com.baba.service;

import com.baba.pojo.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

/**
 * @author Jamayette
 *         Created on  2017/8/23
 */
@Service
public interface OrderService {

    Order addOrderAndDetail(Order order, String username);

}
