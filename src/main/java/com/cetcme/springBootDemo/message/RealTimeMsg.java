package com.cetcme.springBootDemo.message;

import com.cetcme.springBootDemo.dao.ShipDao;
import com.cetcme.springBootDemo.domain.Ship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qiuhong on 02/11/2017.
 */
public class RealTimeMsg {

    public static Logger logger = LoggerFactory.getLogger(RealTimeMsg.class);

    public static String processSingleFrame(Long shipId) {
        ShipDao dao = new ShipDao();
        Ship ship = dao.getByShipId(shipId);
        if (ship == null) {
            return "null";
        }
        logger.warn(ship.toString());
        return ship.getShipName();
    }

}
