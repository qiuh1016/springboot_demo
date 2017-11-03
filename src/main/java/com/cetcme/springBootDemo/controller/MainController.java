package com.cetcme.springBootDemo.controller;

import com.cetcme.springBootDemo.message.RealTimeMsg;
import com.cetcme.springBootDemo.utils.RedissonUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.cetcme.springBootDemo.App.redissonUtil;


@RestController
public class MainController {

    public static Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ApiOperation(nickname = "get", value = "get", notes = "根据ID获取渔船")
    public String get(@ApiParam(value = "渔船ID") @RequestParam Long id) {
        return RealTimeMsg.processSingleFrame(id);
    }

    @RequestMapping(value = "/getRedis", method = RequestMethod.GET)
    public Object getRedis(String shipNo) {
        return redissonUtil.getDeviceList(shipNo).get("cfsEndDate");
    }
}