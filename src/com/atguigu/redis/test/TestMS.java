package com.atguigu.redis.test;

import redis.clients.jedis.Jedis;
/*
 * redis 主从模式
 *
 * Windows版配置
 * 1 复制一份redis目录
 * 2 配置redis.windows.conf
 *      slaveof 127.0.0.1 6379
 *      #masterauth 123456  配置密码
 *      #slave-read-only no 配置非只读(不影响装载rdb文件)，若yes则不能执行set等写命令，
 *      否则报错JedisDataException: READONLY You can't write against a read only slave.
 *      修改配置需重启服务
 * 3 redis-server --service-install redis.windows.conf  --service-name Redis6380（安装服务并取名再启动）
 *
 * 同步过程
 *  1、Slave服务启动，主动连接Master，并发送SYNC命令，请求初始化同步

    2、Master收到SYNC后，执行BGSAVE命令生成RDB文件，并缓存该时间段内的写命令

    3、Master完成RDB文件后，将其发送给所有Slave服务器

    4、Slave服务器接收到RDB文件后，删除内存中旧的缓存数据，并装载RDB文件

    5、Master在发送完RDB后，即刻向所有Slave服务器发送缓存中的写命令

    6、至此初始化完成，后续进行增量同步
 */

public class TestMS {
	public static void main(String[] args) {
		Jedis master = new Jedis("127.0.0.1",6379);

		Jedis slave = new Jedis("127.0.0.1",6380);
		//master.auth("123456"); 设置密码
		//master.select(6); 选择数据库

		slave.slaveof("127.0.0.1",6379);

		master.set("class","1122V2");
		slave.set("test","11");
		String result = slave.get("class");
		System.out.println(result);
	}
}
