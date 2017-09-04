package com.taotao.sso.service;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.service.RedisService;
import com.taotao.sso.mapper.UserMapper;
import com.taotao.sso.pojo.User;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    public Boolean checkParam(String param, Integer type) {
        User record = new User();

        switch (type) {
        case 1:
            record.setUsername(param);
            break;
        case 2:
            record.setPhone(param);
            break;
        case 3:
            record.setEmail(param);
            break;
        default:
            return null;
        }

        return this.userMapper.selectOne(record) == null;
    }

    public Boolean doRegister(User user) {
        user.setId(null);
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());
        // 需要MD5加密处理
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        return this.userMapper.insert(user) == 1;
    }

    public String doLogin(String username, String password) throws Exception {
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        if (null == user) {
            // 用户名不存在
            return null;
        }
        if (!StringUtils.equals(user.getPassword(), DigestUtils.md5Hex(password))) {
            // 密码错误
            return null;
        }
        // 登录成功
        String token = DigestUtils.md5Hex(System.currentTimeMillis() + username);
        this.redisService.set("TOKEN_" + token, user, 1800);

        return token;
    }

    public User queryUserByToken(String token) throws Exception {
        String key = "TOKEN_" + token;
        User user = this.redisService.get(key, User.class);
        if (user == null) {
            // 登录超时
            return null;
        }
        this.redisService.expire(key, 1800);
        return user;
    }

}
