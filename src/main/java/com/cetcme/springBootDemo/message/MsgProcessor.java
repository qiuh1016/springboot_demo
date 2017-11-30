package com.cetcme.springBootDemo.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cetcme.springBootDemo.App;
import com.cetcme.springBootDemo.domain.AcqData;
import com.cetcme.springBootDemo.task.RealTimeProcessTask;
import net.sf.ezmorph.object.DateMorpher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by qiuhong on 22/11/2017.
 */
public class MsgProcessor {

    Logger logger = LoggerFactory.getLogger(App.class);

    public MsgProcessor(String msgStr) {


        JSONArray msgArr = JSONArray.parseArray(msgStr);
        for (Object msg: msgArr) {
            JSONObject msgDetailJson = JSONObject.parseObject(msg.toString());

            String _$01Str = msgDetailJson.getString("$01");
            if (!_$01Str.isEmpty()) {
                List<AcqData>list = JSONObject.parseArray(_$01Str, AcqData.class);
                App.total += list.size();
                logger.info("收到数据： " + list.size() + "， 共接收： " + App.total);
                for (AcqData acqData : list) {
                    RealTimeProcessTask realTimeProcessTask = new RealTimeProcessTask(acqData);
                    App.threadPool.execute(realTimeProcessTask);
                }
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
