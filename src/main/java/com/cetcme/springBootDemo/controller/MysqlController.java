package com.cetcme.springBootDemo.controller;

import com.cetcme.springBootDemo.dao.AcqDataDao;
import com.cetcme.springBootDemo.dao.FenceDao;
import com.cetcme.springBootDemo.dao.ShipDao;
import com.cetcme.springBootDemo.domain.AcqData;
import com.cetcme.springBootDemo.domain.FenceExtend;
import com.cetcme.springBootDemo.domain.Ship;
import com.cetcme.springBootDemo.task.RefreshCacheTask;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * Created by qiuhong on 01/11/2017.
 */
@RestController
@RequestMapping("/mysql")
public class MysqlController {

    public static Logger logger = LoggerFactory.getLogger(MysqlController.class);

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ApiOperation(nickname = "get", value = "get", notes = "根据ID获取渔船")
    public String get(@ApiParam(value = "渔船ID") @RequestParam Long id) {
        ShipDao dao = new ShipDao();
        Ship ship = dao.getByShipId(id);
        if (ship == null) {
            return "null";
        }
        return ship.toString();
    }
}
