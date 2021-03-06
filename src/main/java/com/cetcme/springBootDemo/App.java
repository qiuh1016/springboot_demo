package com.cetcme.springBootDemo;

import com.cetcme.springBootDemo.message.DeviceStatusProcessor;
import com.cetcme.springBootDemo.message.MsgProcessor;
import com.cetcme.springBootDemo.netty.TcpClient;
import com.cetcme.springBootDemo.netty.TcpClientHandler;
import com.cetcme.springBootDemo.service.CacheService;
import com.cetcme.springBootDemo.task.DataInsertTask;
import com.cetcme.springBootDemo.task.RefreshCacheTask;
import com.cetcme.springBootDemo.task.TcpSendTask;
import com.cetcme.springBootDemo.utils.RedissonUtil;
import org.redisson.Redisson;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.config.Config;
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
	private static CacheService cacheService = new CacheService();

	public static String messageListenerKey = "testTopic";
	public static String messageListenerCountDownKey = "testCountDownLatch";

    private static int cnt = Runtime.getRuntime().availableProcessors();
    public static ExecutorService threadPool = Executors.newFixedThreadPool(cnt + 1);

	public static void main(String[] args) throws Exception {

		SpringApplication.run(App.class, args);

		otherTask();
		messageListener();
		connectTcp();
	}

	private static void otherTask() {

		// 线程池
		int cnt = Runtime.getRuntime().availableProcessors();
		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(cnt + 1);

		// 每隔5秒钟将实时数据缓存中的实时数据插入实时数据库，并清空实时数据缓存
		scheduledThreadPool.scheduleAtFixedRate(new DataInsertTask(), 0, 5, TimeUnit.SECONDS);

		// 先更新一下缓存
		cacheService.loadCache();

		// 每隔2分钟更新一下缓存(注意：缓存要在下一次设备发射前更新完成，否则缓存是旧的，导致数据出错)
		scheduledThreadPool.scheduleAtFixedRate(new RefreshCacheTask(), 100, 120, TimeUnit.SECONDS);

		new DeviceStatusProcessor(1);
	}

	private static void connectTcp() throws Exception {
		String host = "61.164.208.174";
		int port = 3349;
		new TcpClient().connect(host, port);
	}

	public static int total = 0;

	private static void messageListener() {
		new Thread(() -> {
            Config config = new Config();
            config
                .useSingleServer()
                .setAddress("redis://61.164.208.174:3350")
                .setPassword("foobared")
                .setDatabase(0);

            RedissonClient topcRedisson = Redisson.create(config);

            logger.info("订阅");
            RTopic<String> rTopic = topcRedisson.getTopic(messageListenerKey);
            rTopic.addListener((s, s2) -> {
//                logger.info("我接收的是： ");
                new MsgProcessor(s2);
            });
            RCountDownLatch rCountDownLatch = topcRedisson.getCountDownLatch(messageListenerCountDownKey);
            rCountDownLatch.trySetCount(1);
            try {
                rCountDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

	}

}
