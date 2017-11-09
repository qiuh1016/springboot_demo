package com.cetcme.springBootDemo.controller;

import com.cetcme.springBootDemo.message.DeviceMsgProcessor;
import com.cetcme.springBootDemo.message.PunchMsgProcessor;
import com.cetcme.springBootDemo.message.RealTimeMsgProcessor;
import com.mongodb.util.JSON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by qiuhong on 08/11/2017.
 */
@RestController
@RequestMapping(value = "/message")
public class MessageController {

    @RequestMapping(value = "/realTime", method = RequestMethod.POST)
    public String realTime(JSON json) {
        new RealTimeMsgProcessor().process(true, "123123123");
        return "ok";
    }

    @RequestMapping(value = "/punch", method = RequestMethod.POST)
    public String punch(JSON json) {
        new PunchMsgProcessor().process("");
        return "ok";
    }

    @RequestMapping(value = "/device", method = RequestMethod.POST)
    public String device(JSON json) {
        new DeviceMsgProcessor().process();
        return "ok";
    }
}
