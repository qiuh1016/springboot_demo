package com.cetcme.springBootDemo.task;

import com.cetcme.springBootDemo.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qiuhong on 02/11/2017.
 */
public class RefreshCacheTask implements Runnable{

    Logger logger = LoggerFactory.getLogger(RefreshCacheTask.class);
    private CacheService cacheService = new CacheService();

    @Override
    public void run() {
        cacheService.reloadCache();
    }

}
