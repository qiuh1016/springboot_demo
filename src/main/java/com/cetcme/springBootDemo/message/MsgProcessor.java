package com.cetcme.springBootDemo.message;

import com.cetcme.springBootDemo.App;
import com.cetcme.springBootDemo.domain.AcqData;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiuhong on 22/11/2017.
 */
public class MsgProcessor {

    Logger logger = LoggerFactory.getLogger(App.class);

    public MsgProcessor(String msgStr) {

        JSONArray msgArr = JSONArray.fromObject(msgStr);
        for (Object msg: msgArr) {
            JSONObject msgDetailJson = JSONObject.fromObject(msg);

            JSONArray _$01Arr = msgDetailJson.getJSONArray("$01");
            if (!_$01Arr.isEmpty()) {
                List<AcqData> list = (List<AcqData>) JSONArray.toList(_$01Arr, new AcqData(), new JsonConfig());
                new RealTimeMsgProcessor().process(list);
            }

//            JSONArray _$02Arr = msgDetailJson.getJSONArray("$02");
//            if (!_$02Arr.isEmpty()) {
//                List<AcqData> list = (List<AcqData>) JSONArray.toList(_$02Arr, new AcqData(), new JsonConfig());
////                new PunchMsgProcessor().process(list);
//            }
//
//            JSONArray _$03Arr = msgDetailJson.getJSONArray("$03");
//            if (!_$03Arr.isEmpty()) {
//                List<AcqData> list = (List<AcqData>) JSONArray.toList(_$03Arr, new AcqData(), new JsonConfig());
////                new DeviceMsgProcessor().process(list);
//            }

        }

    }
}
