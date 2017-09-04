package com.taotao.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.httpclient.HttpResult;
import com.taotao.common.service.ApiService;
import com.taotao.sso.bean.User;
import com.taotao.web.bean.Order;
import com.taotao.web.threadlocal.UserThreadLocal;

@Service
public class OrderService {

    @Autowired
    private ApiService apiService;

    @Value("${TAOTAO_ORDER_URL}")
    private String TAOTAO_ORDER_URL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String submit(Order order) {
        try {
            User user = UserThreadLocal.get();
            order.setUserId(user.getId());
            order.setBuyerNick(user.getUsername());

            String url = TAOTAO_ORDER_URL + "/order/create";
            HttpResult httpResult = this.apiService.doPostJson(url, MAPPER.writeValueAsString(order));
            if (httpResult.getCode().intValue() == 200) {
                JsonNode jsonNode = MAPPER.readTree(httpResult.getBody());
                if (jsonNode.get("status").intValue() == 200) {
                    // 订单提交成功
                    return jsonNode.get("data").asText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Order queryOrderById(String orderId) {
        String url = TAOTAO_ORDER_URL + "/order/query/" + orderId;
        try {
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isNotEmpty(jsonData)) {
                return MAPPER.readValue(jsonData, Order.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
