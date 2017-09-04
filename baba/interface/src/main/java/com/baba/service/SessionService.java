package com.baba.service;

import org.springframework.stereotype.Service;

/**
 * @author Jamayette
 *         Created on  2017/8/20
 */
@Service
public interface SessionService {

    /**
     * 保存用户名到redis中
     *
     */
    void addUsernameToRedis(String key, String value);

    /**
     * 从redis中取出用户名
     *
     */
    String getUsernameFromRedis(String key);

}
