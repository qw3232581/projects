package com.taotao.sso.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.service.RedisService;
import com.taotao.sso.bean.User;
import com.taotao.sso.service.UserQueryService;

@Service
public class UserQueryServiceImpl implements UserQueryService {

    @Autowired
    private RedisService redisService;

    @Override
    public User queryUserByToken(String token) {
        try {
            String key = "TOKEN_" + token;
            User user = this.redisService.get(key, User.class);
            if (user == null) {
                // 登录超时
                return null;
            }
            this.redisService.expire(key, 1800);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
