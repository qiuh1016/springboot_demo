package com.cetcme.springBootDemo.task;


import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qiuhong on 02/11/2017.
 */
public class RefreshCacheTask {

    Logger logger = LoggerFactory.getLogger(RefreshCacheTask.class);

    Timer timer;

    public RefreshCacheTask() {
        this.timer = new Timer();
    }

    public void start() {
        int period = 3 * 1000;
        timer.schedule(new RefreshCache(), 0, period);
    }

    public void stop() {
        timer.cancel();
    }

    class RefreshCache extends TimerTask {

        @Override
        public void run() {
            logger.info("RefreshCache");
        }
    }

}
