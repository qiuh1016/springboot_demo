package com.cetcme.springBootDemo;

import com.cetcme.springBootDemo.service.CacheService;
import com.cetcme.springBootDemo.task.RefreshCacheTask;
import com.cetcme.springBootDemo.utils.CacheUtil;
import com.cetcme.springBootDemo.utils.RedissonUtil;
import org.redisson.api.RKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

	public static Logger logger = LoggerFactory.getLogger(App.class);
	public static RedissonUtil redissonUtil = new RedissonUtil();

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);

        CacheService cacheService = new CacheService();
        cacheService.loadCache();

        new RefreshCacheTask().start();
	}
}
