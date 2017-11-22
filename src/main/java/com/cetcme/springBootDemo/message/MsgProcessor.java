package com.cetcme.springBootDemo.message;

import com.cetcme.springBootDemo.App;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qiuhong on 22/11/2017.
 */
public class MsgProcessor {

    Logger logger = LoggerFactory.getLogger(App.class);

    public MsgProcessor(String msg) {
        logger.info("MsgProcessor: " + msg);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "$01");
        jsonObject.put("data", "123123123123");

        switch (jsonObject.get("type").toString()) {
            case "$01":
                new RealTimeMsgProcessor().process(jsonObject.getJSONArray("data"));
                break;
            case "$02":
                new PunchMsgProcessor().process(jsonObject.getJSONObject("data"));
                break;
            case "$03":
                new DeviceMsgProcessor().process(jsonObject.getJSONObject("data"));
                break;
            default:
                break;
        }

    }
}
