package com.cetcme.springBootDemo.controller;

import com.cetcme.springBootDemo.App;
import com.cetcme.springBootDemo.domain.DeviceExtend;
import com.cetcme.springBootDemo.netty.TcpClient;
import com.cetcme.springBootDemo.netty.TcpClientHandler;
import com.cetcme.springBootDemo.task.TcpSendTask;
import com.cetcme.springBootDemo.utils.RedissonUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.redisson.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.cetcme.springBootDemo.App.redissonUtil;


@RestController
public class MainController {

    public static Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ApiOperation(nickname = "get", value = "get", notes = "根据ID获取渔船")
    public String get(@ApiParam(value = "渔船ID") @RequestParam Long id) {
        return "OK";
    }




    @RequestMapping(value = "/countDown", method = RequestMethod.GET)
    public String countDown(String message) {
        RTopic<String> rTopic = RedissonUtil.redisson.getTopic(App.messageListenerKey);
        rTopic.publish(message);
        RCountDownLatch rCountDownLatch = RedissonUtil.redisson.getCountDownLatch(App.messageListenerCountDownKey);
        rCountDownLatch.countDown();
        return message;
    }

    @RequestMapping(value = "/tcpSend", method = RequestMethod.GET)
    public String tcpSend() {
        String msg = "24 30 31 2C 31 37 30 39 37 37 37 32 2C 02 2C 12 33 44 40 13 11 56 60 17 10 18 15 57 39 00 15 11 32 F4 11 CD 00 20 40 2C 12 33 44 80 13 11 56 49 17 10 18 15 59 38 00 17 00 00 F4 11 CD 00 20 60 85";
        TcpClient.tcpSend(msg);
        return "ok";
    }

    @RequestMapping(value = "/setRedis", method = RequestMethod.GET)
    public String setRedis() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", "a");
        map.put("2", "b");

        RedissonClient redisson = RedissonUtil.redisson;
        RBucket rBucket = redisson.getBucket("123");
        rBucket.set(map);
        return "ok";
    }

    @RequestMapping(value = "/getRedis", method = RequestMethod.GET)
    public String getRedis(String deviceNo) {
        RedissonClient redisson = RedissonUtil.redisson;
        RBucket rBucket = redisson.getBucket("123");
        Map<String, Object> map = (Map<String, Object>) rBucket.get();
        return map.get("1").toString();
    }

    @RequestMapping(value = "/redisMap", method = RequestMethod.GET)
    public String redisMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", "a");
        map.put("2", "b");

        RedissonUtil.redisson.getMap("123").putAll(map);


        Map<String, Object> map1 = new HashMap<>();
        map1.put("2", "c");
        map1.put("3", "d");

        RedissonUtil.redisson.getMap("123").putAll(map1);

        return "OK";
    }

    @RequestMapping(value = "/redisBucket", method = RequestMethod.GET)
    public String redisBucket() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", "a");
        map.put("2", "b");

        RedissonUtil.redisson.getBucket("123b").set(map);


        Map<String, Object> map1 = new HashMap<>();
        map1.put("2", "c");
        map1.put("3", "d");

        RedissonUtil.redisson.getBucket("123b").set(map1);

        return "OK";
    }
}