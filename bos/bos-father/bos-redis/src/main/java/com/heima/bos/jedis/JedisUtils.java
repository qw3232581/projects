package com.heima.bos.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ResourceBundle;

public class JedisUtils {

    private static JedisPoolConfig poolConfig = null;
    private static JedisPool pool = null;
    // 定义一些静态常量信息
    private static int maxTotal = 0;
    private static long maxWaitMillis = 0;
    private static String host = null;
    private static int port = 0;

    static {
        ResourceBundle rb = ResourceBundle.getBundle("redis");
        maxTotal = Integer.parseInt(rb.getString("maxTotal"));
        maxWaitMillis = Long.parseLong(rb.getString("maxWaitMillis"));
        host = rb.getString("host");
        port = Integer.parseInt(rb.getString("port"));

        poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        pool = new JedisPool(poolConfig, host, port);
    }

    // 获得会话资源的方法
    public static Jedis getJedis() {
        return pool.getResource();
    }

    // 释放资源
    public static void close(Jedis j) {
        if (j != null) {
            j.close();
        }
    }

}
