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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class App {

	public static Logger logger = LoggerFactory.getLogger(App.class);
	public static RedissonUtil redissonUtil = new RedissonUtil();

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);

        CacheService cacheService = new CacheService();
        cacheService.loadCache();

		otherTask();

	}

	public static void otherTask() {
		// 线程池
		@SuppressWarnings("unused")
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		int cnt = Runtime.getRuntime().availableProcessors();
		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(cnt + 1);

		// 每隔2分钟更新一下缓存(注意：缓存要在下一次设备发射前更新完成，否则缓存是旧的，导致数据出错)
		scheduledThreadPool.scheduleAtFixedRate(new RefreshCacheTask(), 100, 120, TimeUnit.SECONDS);
	}
}
