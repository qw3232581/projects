package com.baba.cob.service.impl;

import com.baba.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * @author Jamayette
 *         Created on  2017/8/20
 */
@Service("sessionService")
public class SessionServiceImpl implements SessionService{

    @Autowired
    private Jedis jedis;

    @Override
    public void addUsernameToRedis(String key, String username) {
        // key是uuid（babasessionid） value是用户名
        // 因为考虑到以后可能还有验证码，所以在key后面加个username再保存到redis中
        jedis.set(key + ":username",username);
        //设置失效时间
        jedis.expire(key + ":username",1800);

    }

    @Override
    public String getUsernameFromRedis(String key) {

        String value = jedis.get(key + ":username");
        if (value !=null){
            // 失效时间应该从最后一次访问开始算起，所以让value有值时，则延长续命时间
            // 设置失效时间 单位秒
            jedis.expire(key + ":username", 1800);
        }
        return value;
    }
}
