package com.bite.travel.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class JedisUtil {
    //redis连接池
    private static JedisPool jedisPool;

    static {
        //读取配置文件
        InputStream is = JedisPool.class.getClassLoader()
                .getResourceAsStream("jedis.properties");
        //创建Properties 对象
        Properties properties = new Properties();
        //关联文件
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取数据,设置到JedisPoolConfig中
        //JedisConfig: jedis的配置对象(配置最大连接数/ zuida1kongxianshu1)
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.parseInt(properties.getProperty("maxTotal")));
        config.setMaxIdle(Integer.parseInt(properties.getProperty("maxIdle")));

        //初始化JedisPool
        jedisPool = new JedisPool(config, properties.getProperty("host"), Integer.parseInt(properties.getProperty("port")));
    }

    /*
    * 获取连接的方法
    * Jedis操作底层redis客户端对象
    * */
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }

    /**
     * 关闭Jedis
     */
    public static void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
   }

}
