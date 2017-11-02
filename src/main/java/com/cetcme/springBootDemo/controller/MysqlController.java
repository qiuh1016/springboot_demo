package com.cetcme.springBootDemo.controller;
import com.cetcme.springBootDemo.dao.AcqDataDao;
import com.cetcme.springBootDemo.dao.FenceDao;
import com.cetcme.springBootDemo.domain.AcqData;
import com.cetcme.springBootDemo.domain.FenceExtend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


/**
 * Created by qiuhong on 01/11/2017.
 */
@RestController
@RequestMapping("/mysql")
public class MysqlController {

//    @Autowired
////    private AcqDataDao acqDataDao;
//
    public static Logger logger = LoggerFactory.getLogger(MysqlController.class);
//
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public String getAll() {
        FenceDao dao = new FenceDao();
        List<FenceExtend> fenceList = dao.getAll();
        logger.info("getAll");
        if (fenceList == null || fenceList.size() == 0) {
            return "null";
        }

        return fenceList.get(0).toString();
    }

//    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
//    public UserEntity getUser(Long id) {
//        return userMapper.getOne(id);
//    }
//
//    @RequestMapping(value = "/add", method = RequestMethod.GET)
//    public String add(String userName, String pwd) {
//        UserEntity user = new UserEntity(userName, pwd, UserSexEnum.MAN);
//        userMapper.insert(user);
//        return "add";
//    }
//
//    @RequestMapping(value="update", method = RequestMethod.GET)
//    public void update(UserEntity user) {
//        userMapper.update(user);
//    }
//
//    @RequestMapping(value="/delete/{id}", method = RequestMethod.GET)
//    public void delete(@PathVariable("id") Long id) {
//        userMapper.delete(id);
//    }
}
