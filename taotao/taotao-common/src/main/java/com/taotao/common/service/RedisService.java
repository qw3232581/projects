package com.taotao.common.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RedisService {

    @Autowired(required = false) // 这个注入不是必须的，如果Spring容器中有就注入，没有就不注入
    private ShardedJedisPool shardedJedisPool;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private <T> T execute(Function<T, ShardedJedis> fun) {
        ShardedJedis shardedJedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis = shardedJedisPool.getResource();
            return fun.callback(shardedJedis);
        } finally {
            if (null != shardedJedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }
    }

    /**
     * 执行SET操作
     * 
     * @param key
     * @param value
     * @return
     */
    public String set(final String key, final String value) {
        return this.execute(new Function<String, ShardedJedis>() {
            @Override
            public String callback(ShardedJedis e) {
                return e.set(key, value);
            }
        });
    }

    /**
     * 执行GET操作
     * 
     * @param key
     * @return
     */
    public String get(final String key) {
        return this.execute(new Function<String, ShardedJedis>() {
            @Override
            public String callback(ShardedJedis e) {
                return e.get(key);
            }
        });
    }

    /**
     *  从缓存中命中数据，并且反序列化为指定的对象，如果没有命中返回null
     *  
     * @param key
     * @param clazz
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public <T> T get(final String key, Class<T> clazz) throws JsonParseException, JsonMappingException,
            IOException {
        String cacheData = this.get(key);
        if (StringUtils.isNotEmpty(cacheData)) {
            // 命中
            return MAPPER.readValue(cacheData, clazz);
        }
        return null;
    }

    /**
     * 执行DEL操作
     * 
     * @param key
     * @return
     */
    public Long del(final String key) {
        return this.execute(new Function<Long, ShardedJedis>() {
            @Override
            public Long callback(ShardedJedis e) {
                return e.del(key);
            }
        });
    }

    /**
     * 设置生存时间，单位为：秒
     * 
     * @param key
     * @return
     */
    public Long expire(final String key, final Integer seconds) {
        return this.execute(new Function<Long, ShardedJedis>() {
            @Override
            public Long callback(ShardedJedis e) {
                return e.expire(key, seconds);
            }
        });
    }

    /**
     * 执行SET操作，并且设置生存时间，单位为：秒
     * 
     * @param key 键
     * @param value 值
     * @param seconds 生存时间，单位为：秒
     * @return 状态回复
     */
    public String set(final String key, final String value, final Integer seconds) {
        return this.execute(new Function<String, ShardedJedis>() {
            @Override
            public String callback(ShardedJedis e) {
                String str = e.set(key, value);
                e.expire(key, seconds);
                return str;
            }
        });
    }

    /**
     * 执行SET操作，并且设置生存时间，单位为：秒
     * 
     * @param key 键
     * @param obj 对象，该方法会将该对象序列化为json字符串
     * @param seconds 生存时间，单位为：秒
     * @return 状态回复
     * @throws JsonProcessingException
     */
    public String set(final String key, final Object obj, final Integer seconds)
            throws JsonProcessingException {
        String value = MAPPER.writeValueAsString(obj);
        return this.set(key, value, seconds);
    }

}
