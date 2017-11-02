package com.cetcme.springBootDemo.controller;

import com.cetcme.springBootDemo.entity.UserEntity;
import com.cetcme.springBootDemo.enums.UserSexEnum;
import com.cetcme.springBootDemo.mapper.UserMapper;
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

    @Autowired
    private UserMapper userMapper;

    public static Logger logger = LoggerFactory.getLogger(MysqlController.class);

    @RequestMapping(value = "/getUsers", method = RequestMethod.GET)
    public List<UserEntity> getUsers() {
        logger.info("getUsers");
        return userMapper.getAll();
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public UserEntity getUser(Long id) {
        return userMapper.getOne(id);
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(String userName, String pwd) {
        UserEntity user = new UserEntity(userName, pwd, UserSexEnum.MAN);
        userMapper.insert(user);
        return "add";
    }

    @RequestMapping(value="update", method = RequestMethod.GET)
    public void update(UserEntity user) {
        userMapper.update(user);
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.GET)
    public void delete(@PathVariable("id") Long id) {
        userMapper.delete(id);
    }
}
