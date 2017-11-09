package com.cetcme.springBootDemo.controller;

import com.cetcme.springBootDemo.domain.DeviceExtend;
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
        return "OK";
    }

    @RequestMapping(value = "/getRedis", method = RequestMethod.GET)
    public DeviceExtend getRedis(String deviceNo) {
        return redissonUtil.getDeviceInfo(deviceNo);
    }

}