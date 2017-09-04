package com.baba.redis.test;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author Jamayette
 *         Created on  2017/8/16
 */
public class RedisTest {

    @Test
    public void testRedis() {
        // 创建redis客户端对象并指定服务器地址 端口默认为6379
        Jedis jedis = new Jedis("192.168.57.103", 6379);
        // 使redis中的pno key值加1
        Long incr = jedis.incr("pno");
        System.out.println(incr);
    }

}
